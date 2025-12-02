import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal del juego Bad Ice Cream.
 * Maneja el estado global del juego, niveles, temporizadores,
 * jugadores, enemigos y frutas. Independiente de GUI.
 */
public class BadIceCream implements Serializable {

    // -------------------------------
    // ATRIBUTOS GENERALES DEL JUEGO
    // -------------------------------
    private List<Nivel> niveles;
    private int indiceNivel;

    private ArrayList<Jugador> jugadores;
    private boolean juegoTerminado;
    private boolean pausado;

    // Tiempo
    private long tiempoInicioNivel;
    private final long TIEMPO_MAXIMO = 180_000; // 3 minutos por nivel

    // Persistencia temporal
    private long ultimoMovimientoHelado; // Para activar movimiento de piñas
    private long ultimoCrecimientoCactus; // Para púas

    // -------------------------------
    // CONSTRUCTOR
    // -------------------------------
    public BadIceCream(List<Nivel> niveles) throws BadDopoException {

        if (niveles == null || niveles.isEmpty())
            throw new BadDopoException("El juego debe tener al menos un nivel.");

        this.niveles = niveles;
        this.indiceNivel = 0;

        this.jugadores = new ArrayList<>();
        this.juegoTerminado = false;
        this.pausado = false;
    }

    // -------------------------------
    // MÉTODOS PARA CONFIGURAR JUGADORES
    // -------------------------------
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

    // -------------------------------
    // INICIO DEL JUEGO
    // -------------------------------
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

    // -------------------------------
    // PAUSA / REANUDAR
    // -------------------------------
    public void pausar() { pausado = true; }
    public void reanudar() { pausado = false; }

    // -------------------------------
    // GAME LOOP PRINCIPAL
    // -------------------------------
    public void actualizar() throws BadDopoException {

        if (juegoTerminado || pausado) return;

        Nivel nivel = getNivelActual();

        long ahora = System.currentTimeMillis();

        // --- Expiración de tiempo ---
        if (ahora - tiempoInicioNivel >= TIEMPO_MAXIMO) {
            reiniciarNivel();
            return;
        }

        // ---------------------------
        // MOVIMIENTO DE JUGADORES
        //----------------------------
        // Nota: Jugador.realizarMovimiento(Nivel) retorna void en tu diseño,
        // por eso NO podemos usarlo como boolean. Para detectar si al menos un helado
        // se movió (necesario para mover piñas) guardamos posiciones antes y después.
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

        // ---------------------------
        // DETECCIÓN DE COLISIONES
        // ---------------------------
        // usamos el método del nivel (lo añadiremos en Nivel)
        nivel.verificarColisiones();

        // ---------------------------
        // FIN DEL NIVEL
        // ---------------------------
        if (nivel.estaCompletado()) {
            avanzarNivel();
        }
    }

    // -------------------------------------------------
    //      LÓGICA DE FRUTAS (PIÑA, CEREZA, CACTUS)
    // -------------------------------------------------
    // ahora recibe heladoSeMovio para decidir si mover piñas
    private void actualizarFrutas(Nivel nivel, long ahora, boolean heladoSeMovio) throws BadDopoException {

        // iteramos sobre copia para evitar ConcurrentModification si se recolectan
        List<Fruta> copia = new ArrayList<>(nivel.getFrutas());

        for (Fruta f : copia) {

            // Piñas -> se mueven SOLO cuando se mueve un helado
            if (f instanceof Piña) {
                if (heladoSeMovio) {
                    ((Piña) f).mover(nivel);
                }
            }

            // Cereza -> teletransporte cada 20s
            if (f instanceof Cereza) {
                // Añadimos el método wrapper actualizarTeleportacion(long, Nivel)
                ((Cereza) f).actualizarTeleportacion(ahora, nivel);
            }

            // Cactus -> alterna púas cada 30s
            if (f instanceof Cactus) {
                // Añadimos la sobrecarga actualizarEstado(long, Nivel)
                ((Cactus) f).actualizarEstado(ahora, nivel);
            }
        }
    }

    // -------------------------------------------------
    //      PASAR NIVEL, REINICIAR, TERMINAR JUEGO
    // -------------------------------------------------
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

    // -------------------------------------------------
    //                   PERSISTENCIA
    // -------------------------------------------------
    public void guardar(String archivo) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo));
        oos.writeObject(this);
        oos.close();
    }

    public static BadIceCream cargar(String archivo)
            throws IOException, ClassNotFoundException {

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo));
        BadIceCream juego = (BadIceCream) ois.readObject();
        ois.close();
        return juego;
    }

}
