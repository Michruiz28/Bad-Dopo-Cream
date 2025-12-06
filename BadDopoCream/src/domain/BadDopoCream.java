package domain;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal del juego Bad Ice Cream.
 * Maneja el estado global del juego, niveles, temporizadores,
 * jugadores, enemigos y frutas. Independiente de GUI.
 */
public class BadDopoCream implements Serializable {

    private List<Nivel> niveles;
    private int indiceNivel;

    private boolean juegoTerminado;
    private boolean pausado;

    // Tiempo
    private long tiempoInicioNivel;
    private final long TIEMPO_MAXIMO = 180_000; // 3 minutos por nivel

    // Persistencia temporal
    private long ultimoMovimientoHelado; // Para activar movimiento de Pinas
    private long ultimoCrecimientoCactus; // Para púas
    
    private CreadorElemento creador;
    private ArrayList<Helado> jugadores;
    private List<Enemigo> enemigos;

    public BadDopoCream(String modo, String sabor1, String sabor2, String perfil1, String perfil2) {
        // Seleccionar factory según el modo
        creador = CreadorElemento(modo, perfil1, perfil2);
        enemigos = new java.util.ArrayList<>();
    }

    /**
     * Crea la factory apropiada según el modo de juego
     */
    private CreadorElemento crearFactory(String modo, String perfil1, String perfil2) {
        switch (modo) {
            case "Un jugador":
                return new UnJugador();

            case "Jugador vs Jugador":
                return new JugadorVsJugador();

            case "Jugador vs Máquina":
                return new JugadorVsMaquina(perfil2);

            case "Máquina vs Máquina":
                return new MaquinaVsMaquina(perfil1, perfil2);

            default:
                return new UnJugador();
        }
    }

    public void agregarJugadorHumano(String nombre, Helado helado)
            throws BadDopoException {

        JugadorHumano j = new JugadorHumano(nombre, helado);
        jugadores.add(j);
    }

    public void agregarJugadorMaquina(String nombre, Helado helado, EstrategiaMovimiento estrategia)
            throws BadDopoException {

        JugadorMaquina j = new JugadorMaquina(nombre, helado, estrategia);
        jugadores.add(j);
    }

    public List<Jugador> getJugadores() { return jugadores; }

    public void iniciarJuego() throws BadDopoException {
        cargarNivel(0);
        tiempoInicioNivel = System.currentTimeMillis();
    }

    private void cargarNivel(int index) throws BadDopoException {

        if (index < 0 || index >= niveles.size())
            throw new BadDopoException("Índice de nivel inválido.");

        indiceNivel = index;

        Nivel nivel = niveles.get(indiceNivel);
        nivel.inicializar(); // Generación de frutas, enemigos y obstáculos

        // Inicializar helados con tablero y grafo (cada helado obtiene el grafo del nivel)
        for (Jugador j : jugadores) {
            Helado h = j.getHelado();
            h.setTablero(nivel.getTablero());
            h.setGrafo(new GrafoTablero(nivel.getTablero()));
        }

        ultimoMovimientoHelado = System.currentTimeMillis();
        ultimoCrecimientoCactus = System.currentTimeMillis();
    }

    public Nivel getNivelActual() {
        return niveles.get(indiceNivel);
    }

    public int getNumeroNivel() {
        return indiceNivel + 1;
    }


    public void pausar() { pausado = true; }
    public void reanudar() { pausado = false; }


    public void actualizar() throws BadDopoException {

        if (juegoTerminado || pausado) return;

        Nivel nivel = getNivelActual();

        long ahora = System.currentTimeMillis();

        // --- Expiración de tiempo ---
        if (ahora - tiempoInicioNivel >= TIEMPO_MAXIMO) {
            reiniciarNivel();
            return;
        }


        boolean heladoSeMovio = false;
        List<int[]> posicionesAntes = new ArrayList<>(jugadores.size());
        for (Jugador j : jugadores) {
            Helado h = j.getHelado();
            posicionesAntes.add(new int[]{h.getFila(), h.getColumna()});
        }

        // Ejecutar movimientos (humano o máquina)
        for (Jugador j : jugadores) {
            j.realizarMovimiento(nivel);
        }

        // Comparar posiciones
        for (int i = 0; i < jugadores.size(); i++) {
            Helado h = jugadores.get(i).getHelado();
            int[] antes = posicionesAntes.get(i);
            if (h.getFila() != antes[0] || h.getColumna() != antes[1]) {
                heladoSeMovio = true;
                break;
            }
        }

        // Registrar tiempo de movimiento del helado
        if (heladoSeMovio) ultimoMovimientoHelado = ahora;

        // ---------------------------
        // MOVIMIENTO DE ENEMIGOS
        // ---------------------------
        for (Enemigo e : nivel.getEnemigos()) {
            e.realizarMovimiento(nivel);
        }

        // ---------------------------
        // ACTUALIZACIÓN DE FRUTAS
        // ---------------------------
        actualizarFrutas(nivel, ahora, heladoSeMovio);

        // usamos el método del nivel (lo añadiremos en Nivel)
        nivel.verificarColisiones();

        if (nivel.estaCompletado()) {
            avanzarNivel();
        }
    }

    private void actualizarFrutas(Nivel nivel, long ahora, boolean heladoSeMovio) throws BadDopoException {


        List<Fruta> copia = new ArrayList<>(nivel.getFrutas());

        for (Fruta f : copia) {

            if (f instanceof Pina) {
                if (heladoSeMovio) {
                    ((Pina) f).mover(nivel);
                }
            }


            if (f instanceof Cereza) {
                // Añadimos el método wrapper actualizarTeleportacion(long, Nivel)
                ((Cereza) f).actualizarTeleportacion(ahora, nivel);
            }


            if (f instanceof Cactus) {

                ((Cactus) f).actualizarEstado(ahora, nivel);
            }
        }
    }

    private void reiniciarNivel() throws BadDopoException {
        cargarNivel(indiceNivel);
        tiempoInicioNivel = System.currentTimeMillis();
    }

    private void avanzarNivel() throws BadDopoException {

        indiceNivel++;

        if (indiceNivel >= niveles.size()) {
            juegoTerminado = true;
            return;
        }

        cargarNivel(indiceNivel);
        tiempoInicioNivel = System.currentTimeMillis();
    }

    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }


    public void guardar(String archivo) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo));
        oos.writeObject(this);
        oos.close();
    }

    public static BadDopoCream cargar(String archivo)
            throws IOException, ClassNotFoundException {

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo));
        BadDopoCream juego = (BadDopoCream) ois.readObject();
        ois.close();
        return juego;
    }

}
