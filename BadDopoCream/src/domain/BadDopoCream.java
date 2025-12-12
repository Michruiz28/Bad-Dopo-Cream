package domain;

import java.io.*;
import java.util.*;

/**
 * Clase principal del juego Bad DOPO Cream.
 * Fachada entre la capa de presentación (GUI) y el dominio del juego.
 * Maneja el ciclo completo del juego: niveles, tiempo, puntuaciones, victoria/derrota.
 */
public class BadDopoCream implements Serializable {

    // Atributos principales
    private Tablero tablero;
    private GrafoTablero grafo;
    private String modo;
    private int nivelActual;
    private ArrayList<Integer> nivelesCompletados;

    // Jugadores
    private Helado helado1;
    private Helado helado2; // null si es un solo jugador
    private String sabor1;
    private String sabor2;
    private String nombreJugador1;
    private String nombreJugador2;
    private String perfil1; // Para máquinas: "hungry", "fearful", "expert"
    private String perfil2;

    // Elementos del juego
    private HashMap<String, Elemento> elementos; // key: "fila-col"
    private ArrayList<Fruta> frutasEnJuego;
    private ArrayList<Enemigo> enemigosEnJuego;
    private ArrayList<Obstaculo> obstaculosEnJuego;

    // Configuración del nivel
    private String[][] infoNivel;
    private HashMap<String, Integer> frutasRequeridas;
    private HashMap<String, Integer> frutasRecolectadas;

    // Factory para modo de juego
    private CreadorElemento modoJuego;

    private CreadorElemento creador;

    // Estado del juego
    private boolean juegoIniciado;
    private boolean juegoTerminado;
    private boolean pausado;
    private boolean nivelCompletado;
    private boolean victoria;
    private String mensajeEstado;

    // Puntuación
    private int puntajeJugador1;
    private int puntajeJugador2;

    // Tiempo
    private long tiempoInicioNivel;
    private long tiempoPausado;
    private long tiempoTotalPausado;
    private final long TIEMPO_MAXIMO = 180_000; // 3 minutos en milisegundos

    // Temporizadores para frutas especiales
    private long ultimoMovimientoPina;
    private long ultimoTeletransporteCereza;
    private long ultimoCrecimientoCactus;
    private final long INTERVALO_CEREZA = 20_000; // 20 segundos
    private final long INTERVALO_CACTUS = 30_000; // 30 segundos
    private final long INTERVALO_PINA = 500; // 0.5 segundos

    private static final int MAX_NIVELES = 3;


    /**
     * Constructor
     */
    public BadDopoCream(int nivel, String modo, String sabor1, String sabor2,
                        String nombre1, String nombre2, String perfil1, String perfil2)
            throws BadDopoException {

        if (nivel < 1 || nivel > MAX_NIVELES) {
            throw new BadDopoException(BadDopoException.NIVEL_INVALIDO);
        }

        this.nivelActual = nivel;
        this.modo = modo;
        this.sabor1 = sabor1;
        this.sabor2 = sabor2;
        this.nombreJugador1 = nombre1;
        this.nombreJugador2 = nombre2;
        this.perfil1 = perfil1;
        this.perfil2 = perfil2;

        this.nivelesCompletados = new ArrayList<>();
        this.elementos = new HashMap<>();
        this.frutasEnJuego = new ArrayList<>();
        this.enemigosEnJuego = new ArrayList<>();
        this.obstaculosEnJuego = new ArrayList<>();
        this.frutasRequeridas = new HashMap<>();
        this.frutasRecolectadas = new HashMap<>();

        this.juegoIniciado = false;
        this.juegoTerminado = false;
        this.pausado = false;
        this.nivelCompletado = false;
        this.victoria = false;

        this.puntajeJugador1 = 0;
        this.puntajeJugador2 = 0;

        this.modoJuego = crearFactory(modo, perfil1, perfil2);
        this.creador = this.modoJuego;
    }

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

    public void iniciarJuego() throws BadDopoException {
        cargarNivel(nivelActual);
        inicializarHelados();
        juegoIniciado = true;
        tiempoInicioNivel = System.currentTimeMillis();
        tiempoTotalPausado = 0;
        mensajeEstado = "¡Juego iniciado! Recolecta todas las frutas.";
    }

    private void cargarNivel(int nivel) throws BadDopoException {
        this.infoNivel = InfoNivel.getNivel(nivel);
        String[][] mapaNivel = InfoNivel.getNivel(nivel);

        if (mapaNivel == null) {
            throw new BadDopoException(BadDopoException.NIVEL_INVALIDO);
        }

        int filas =   mapaNivel.length;
        int columnas =  mapaNivel[0].length;

        this.grafo = new GrafoTablero(filas, columnas, mapaNivel, creador);
        this.tablero = new Tablero(mapaNivel, creador);

        sincronizarElementosDesdeTablero();

        configurarFrutasRequeridas(nivel);

        ultimoMovimientoPina = System.currentTimeMillis();
        ultimoTeletransporteCereza = System.currentTimeMillis();
        ultimoCrecimientoCactus = System.currentTimeMillis();

        nivelCompletado = false;
    }


    private void sincronizarElementosDesdeTablero() {
        elementos.clear();
        frutasEnJuego.clear();
        enemigosEnJuego.clear();
        obstaculosEnJuego.clear();

        frutasEnJuego.addAll(tablero.getFrutas());
        enemigosEnJuego.addAll(tablero.getEnemigos());
        obstaculosEnJuego.addAll(tablero.getObstaculos());

    }

    public HashMap<String, int[]> getPosicionesHelados() throws BadDopoException {
        HashMap<String, int[]> posicionesHelados = new HashMap<>();
        int[][] pos = InfoNivel.getPosicionesHelados(nivelActual);
        int[] posicionHelado1 = new int[2];
        posicionHelado1[0] = pos[0][0];
        posicionHelado1[1] = pos[0][1];
        int[] posicionHelado2 = new int[2];
        posicionHelado2[0] = pos[1][0];
        posicionHelado2[1] = pos[1][1];
        posicionesHelados.put("helado1", null);
        posicionesHelados.put("helado2", null);
        return posicionesHelados;
    }

    private void inicializarHelados() throws BadDopoException {
        int[][] pos = InfoNivel.getPosicionesHelados(nivelActual);

        if (esJugadorHumano1()) {
            helado1 = new JugadorHumano(pos[0][0], pos[0][1], sabor1);
        } else {
            helado1 = new JugadorMaquina(pos[0][0], pos[0][1], sabor1, perfil1);
        }
        tablero.agregarHelado(helado1);

        if (requiereSegundoJugador()) {
            if (esJugadorHumano2()) {
                helado2 = new JugadorHumano(pos[1][0], pos[1][1], sabor2);
            } else {
                helado2 = new JugadorMaquina(pos[1][0], pos[1][1], sabor2, perfil2);
            }
            tablero.agregarHelado(helado2);
        }
    }

    private void configurarFrutasRequeridas(int nivel) {
        frutasRequeridas.clear();
        frutasRecolectadas.clear();

        HashMap<String, Integer> frutas = InfoNivel.getFrutasNivel(nivel);
        for (Map.Entry<String, Integer> entry : frutas.entrySet()) {
            frutasRequeridas.put(entry.getKey(), entry.getValue());
            frutasRecolectadas.put(entry.getKey(), 0);
        }
    }


    //    private void moverEnemigos() throws BadDopoException {
    //        for (Enemigo enemigo : enemigosEnJuego) {
    //                String direccion = enemigo.calcularMovimiento(tablero, helado1, helado2);
    //        }
    //    }


    public void moverHelado(Helado helado, String direccion) throws BadDopoException {
        if (!juegoIniciado || pausado || juegoTerminado) {
            throw new BadDopoException(BadDopoException.JUEGO_NO_INICIADO);
        }

        int[] posActual = tablero.getPosicionHelado(helado);
        if (posActual == null) {
            throw new BadDopoException("Helado no encontrado en el tablero");
        }

        tablero.solicitarMovimiento(posActual[0], posActual[1], direccion);

        verificarRecoleccionFruta(posActual[0], posActual[1], helado);

        moverPinas();
    }


    public void moverHelado1(String direccion) throws BadDopoException {

    }


    public void moverHelado2(String direccion) throws BadDopoException {
        if (helado2 != null) {
            moverHelado(helado2, direccion);
        }
    }


    public void realizarAccion(int fila, int columna, String ultimaDireccion) throws BadDopoException {
        tablero.realizarAccion(fila, columna, ultimaDireccion);
    }


    private void moverPinas() throws BadDopoException {
        long tiempoActual = System.currentTimeMillis();
        if (tiempoActual - ultimoMovimientoPina < INTERVALO_PINA) {
            return;
        }

        for (Fruta fruta : frutasEnJuego) {
            if (fruta instanceof Pina) {
                ((Pina) fruta).mover();
            }
        }
        ultimoMovimientoPina = tiempoActual;
    }


    private void verificarColisiones() throws BadDopoException {
        if (helado1 != null && colisionaConEnemigo(helado1)) {
            heladoEliminado(helado1);
        }
        if (helado2 != null && colisionaConEnemigo(helado2)) {
            heladoEliminado(helado2);
        }
    }

    private boolean colisionaConEnemigo(Helado helado) {
        int[] posHelado = tablero.getPosicionHelado(helado);
        if (posHelado == null) return false;

        for (Enemigo enemigo : enemigosEnJuego) {
            int[] posEnemigo = tablero.getPosicionEnemigo(enemigo);
            if (posEnemigo != null &&
                    posHelado[0] == posEnemigo[0] &&
                    posHelado[1] == posEnemigo[1]) {
                return true;
            }
        }
        return false;
    }


    private void heladoEliminado(Helado helado) throws BadDopoException {
        mensajeEstado = "¡Helado eliminado! Reiniciando nivel...";
        reiniciarNivel();
    }

    private void verificarRecoleccionFruta(int fila, int col, Helado helado) {
        Fruta frutaEnPosicion = null;

        for (Fruta fruta : frutasEnJuego) {
            int[] posFruta = tablero.getPosicionFruta(fruta);
            if (posFruta != null && posFruta[0] == fila && posFruta[1] == col) {
                frutaEnPosicion = fruta;
                break;
            }
        }

        if (frutaEnPosicion != null) {
            recolectarFruta(frutaEnPosicion, helado);
        }
    }


    private void recolectarFruta(Fruta fruta, Helado helado) {
        int puntos = fruta.getGanancia();


        if (helado == helado1) {
            puntajeJugador1 += puntos;
        } else if (helado == helado2) {
            puntajeJugador2 += puntos;
        }

        frutasEnJuego.remove(fruta);
        tablero.removerFruta(fruta);

        mensajeEstado = "¡+" + puntos + " puntos!";
    }

    public void reiniciarNivel() throws BadDopoException {
        cargarNivel(nivelActual);
        inicializarHelados();
        tiempoInicioNivel = System.currentTimeMillis();
        tiempoTotalPausado = 0;
        mensajeEstado = "Nivel reiniciado";
    }


    private void completarNivel() throws BadDopoException {
        nivelesCompletados.add(nivelActual);
        nivelCompletado = true;

        if (nivelActual >= MAX_NIVELES) {
            // Juego completado
            juegoTerminado = true;
            victoria = true;
            mensajeEstado = "¡Felicidades! Has completado todos los niveles.";
        } else {
            mensajeEstado = "¡Nivel completado! Presiona continuar para el siguiente nivel.";
        }
    }


    public void avanzarNivel() throws BadDopoException {
        if (!nivelCompletado) {
            throw new BadDopoException("No puedes avanzar sin completar el nivel actual");
        }

        nivelActual++;
        if (nivelActual > MAX_NIVELES) {
            juegoTerminado = true;
            victoria = true;
            return;
        }

        cargarNivel(nivelActual);
        inicializarHelados();
        tiempoInicioNivel = System.currentTimeMillis();
        tiempoTotalPausado = 0;
        nivelCompletado = false;
    }


    public long getTiempoRestante() {
        if (!juegoIniciado || juegoTerminado) {
            return TIEMPO_MAXIMO;
        }

        long tiempoTranscurrido = System.currentTimeMillis() - tiempoInicioNivel - tiempoTotalPausado;
        long tiempoRestante = TIEMPO_MAXIMO - tiempoTranscurrido;
        return Math.max(0, tiempoRestante);
    }

    public String getTiempoRestanteFormato() {
        long milisegundos = getTiempoRestante();
        long segundos = milisegundos / 1000;
        long minutos = segundos / 60;
        segundos = segundos % 60;
        return String.format("%02d:%02d", minutos, segundos);
    }

    private boolean tiempoExcedido() {
        return getTiempoRestante() == 0;
    }

    public void pausar() {
        if (juegoIniciado && !pausado && !juegoTerminado) {
            pausado = true;
            tiempoPausado = System.currentTimeMillis();
        }
    }

    public void reanudar() {
        if (pausado) {
            pausado = false;
            tiempoTotalPausado += System.currentTimeMillis() - tiempoPausado;
        }
    }

    private boolean todasLasFrutasRecolectadas() {
        for (Map.Entry<String, Integer> entry : frutasRequeridas.entrySet()) {
            String tipo = entry.getKey();
            int requeridas = entry.getValue();
            int recolectadas = frutasRecolectadas.getOrDefault(tipo, 0);

            if (recolectadas < requeridas) {
                return false;
            }
        }
        return true;
    }

    private boolean requiereSegundoJugador() {
        return modo.equals("Jugador vs Jugador") ||
                modo.equals("Jugador vs Máquina") ||
                modo.equals("Máquina vs Máquina");
    }

    private boolean esJugadorHumano1() {
        return modo.equals("Un jugador") ||
                modo.equals("Jugador vs Jugador") ||
                modo.equals("Jugador vs Máquina");
    }


    private boolean esJugadorHumano2() {
        return modo.equals("Jugador vs Jugador");
    }


    public Tablero getTablero() { return tablero; }

    public int getNivelActual() { return nivelActual; }

    public String getModo() { return modo; }

    public boolean isJuegoIniciado() { return juegoIniciado; }

    public boolean isJuegoTerminado() { return juegoTerminado; }

    public boolean isPausado() { return pausado; }

    public boolean isNivelCompletado() { return nivelCompletado; }

    public boolean isVictoria() { return victoria; }

    public String getMensajeEstado() { return mensajeEstado; }

    public int getPuntajeJugador1() { return puntajeJugador1; }

    public int getPuntajeJugador2() { return puntajeJugador2; }

    public Helado getHelado1() { return helado1; }

    public Helado getHelado2() { return helado2; }

    public ArrayList<Fruta> getFrutasEnJuego() { return new ArrayList<>(frutasEnJuego); }

    public ArrayList<Enemigo> getEnemigosEnJuego() { return new ArrayList<>(enemigosEnJuego); }

    public HashMap<String, Integer> getFrutasRequeridas() { return new HashMap<>(frutasRequeridas); }

    public HashMap<String, Integer> getFrutasRecolectadas() { return new HashMap<>(frutasRecolectadas); }

    public int getCantidadNivelesCompletados() { return nivelesCompletados.size(); }

    public String getProgresoFrutas(String tipoFruta) {
        int recolectadas = frutasRecolectadas.getOrDefault(tipoFruta, 0);
        int requeridas = frutasRequeridas.getOrDefault(tipoFruta, 0);
        return recolectadas + "/" + requeridas;
    }

    public String getGanador() {
        if (!requiereSegundoJugador()) {
            return nombreJugador1;
        }

        if (puntajeJugador1 > puntajeJugador2) {
            return nombreJugador1;
        } else if (puntajeJugador2 > puntajeJugador1) {
            return nombreJugador2;
        } else {
            return "Empate";
        }
    }

    public void guardar(String archivo) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(this);
        }
    }

    public static BadDopoCream cargar(String archivo) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            BadDopoCream juego = (BadDopoCream) ois.readObject();

            if (juego.pausado) {
                juego.tiempoPausado = System.currentTimeMillis();
            }
            return juego;
        }
    }

    public void terminarJuego() {
        juegoTerminado = true;
        pausado = false;
        mensajeEstado = "Juego terminado por el usuario";
    }

    public void reiniciarJuego() throws BadDopoException {
        nivelActual = 1;
        nivelesCompletados.clear();
        puntajeJugador1 = 0;
        puntajeJugador2 = 0;
        juegoTerminado = false;
        victoria = false;
        iniciarJuego();
    }

    public String getResumenFinal() {
        StringBuilder resumen = new StringBuilder();
        resumen.append("Modo: ").append(modo).append("\n");
        resumen.append("Niveles completados: ").append(nivelesCompletados.size()).append("/").append(MAX_NIVELES).append("\n");
        resumen.append("\nPUNTUACIÓN\n");
        resumen.append(nombreJugador1).append(": ").append(puntajeJugador1).append(" puntos\n");

        if (requiereSegundoJugador()) {
            resumen.append(nombreJugador2).append(": ").append(puntajeJugador2).append(" puntos\n");
            resumen.append("Ganador: ").append(getGanador()).append("\n");
        }

        if (victoria) {
            resumen.append("\nHas completado todos los niveles.\n");
        } else {
            resumen.append("\nJuego terminado en el nivel ").append(nivelActual).append("\n");
        }

        return resumen.toString();
    }


    public HashMap<String, Object> getEstadisticas() {
        HashMap<String, Object> stats = new HashMap<>();
        stats.put("modo", modo);
        stats.put("nivelActual", nivelActual);
        stats.put("nivelesCompletados", nivelesCompletados.size());
        stats.put("puntajeJugador1", puntajeJugador1);
        stats.put("puntajeJugador2", puntajeJugador2);
        stats.put("victoria", victoria);
        stats.put("juegoTerminado", juegoTerminado);
        stats.put("tiempoTranscurrido", TIEMPO_MAXIMO - getTiempoRestante());
        stats.put("frutasRecolectadas", new HashMap<>(frutasRecolectadas));
        stats.put("ganador", getGanador());
        return stats;
    }

    public int[] getDimensionesTablero(){
        int[] dimensiones =  tablero.getDimensiones();
        return dimensiones;
    }

    public String[][] getRepresentacionTablero(){
        return this.infoNivel;
    }

    public HashMap<String, Fruta> getPosicionesFrutas() {
        return tablero.getPosicionesFrutas();
    }

    public HashMap<String, Enemigo> getPosicionesEnemigos() {
        return tablero.getPosicionesEnemigos();
    }

    public HashMap<String, Obstaculo> getPosicionesObstaculos() {
        return tablero.getPosicionesObstaculos();
    }

    public void actualizar() throws BadDopoException {
        if (!juegoIniciado || pausado || juegoTerminado) {
            return;
        }

        long tiempoActual = System.currentTimeMillis();

        // Actualizar frutas y elementos que dependan del tiempo
        // Iteramos sobre una copia para evitar ConcurrentModification
        ArrayList<Fruta> copiaFrutas = new ArrayList<>(frutasEnJuego);
        for (Fruta f : copiaFrutas) {
            f.actualizar(tiempoActual);
        }

        // Mover pinas y otras actualizaciones periódicas
        moverPinas();

        // Verificar colisiones entre helados y enemigos
        verificarColisiones();

        // Verificar si se agotó el tiempo
        if (tiempoExcedido()) {
            juegoTerminado = true;
            mensajeEstado = "Tiempo agotado";
        }

        // Si todas las frutas fueron recolectadas, completar el nivel
        if (todasLasFrutasRecolectadas()) {
            completarNivel();
        }
    }

    public String getSabor2() {
        return sabor2;
    }

    public String getSabor1() {
        return sabor1;
    }
}