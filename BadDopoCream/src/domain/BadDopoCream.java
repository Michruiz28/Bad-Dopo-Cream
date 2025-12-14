package domain;

import java.io.*;
import java.util.*;

/**
 * Clase principal con soporte para múltiples fases por nivel
 */
public class BadDopoCream implements Serializable {

    // Atributos principales
    private Tablero tablero;
    private GrafoTablero grafo;
    private String modo;
    private int nivelActual;
    private ArrayList<Integer> nivelesCompletados;

    // Control de fases
    private int faseActual; // Índice de la fase actual (0, 1, 2...)
    private ArrayList<InfoNivel.FaseNivel> fasesDelNivel; // Todas las fases del nivel
    private boolean faseCompletada; // Flag para detectar cuando se completó una fase
    
    // Jugadores
    private Helado helado1;
    private Helado helado2;
    private String sabor1;
    private String sabor2;
    private String nombreJugador1;
    private String nombreJugador2;
    private String perfil1;
    private String perfil2;

    // Elementos del juego
    private HashMap<String, Elemento> elementos;
    private ArrayList<Fruta> frutasEnJuego;
    private ArrayList<Enemigo> enemigosEnJuego;
    private ArrayList<Obstaculo> obstaculosEnJuego;

    // Configuración del nivel
    private String[][] mapaBase; 
    private HashMap<String, Integer> frutasRequeridas;
    private HashMap<String, Integer> frutasRecolectadas;

    // Factory
    private CreadorElemento modoJuego;
    private CreadorElemento creador;

    // Estado del juego
    private boolean juegoIniciado;
    private boolean juegoTerminado;
    private boolean pausado;
    private boolean nivelCompletado;
    private boolean victoria;
    private String mensajeEstado;
    private boolean esperandoDecisionNivel;

    // Puntuación
    private int puntajeJugador1;
    private int puntajeJugador2;

    // Tiempo
    private long tiempoInicioNivel;
    private long tiempoPausado;
    private long tiempoTotalPausado;
    private final long TIEMPO_MAXIMO = 180_000;

    // Temporizadores
    private long ultimoMovimientoPina;
    private long ultimoTeletransporteCereza;
    private long ultimoCrecimientoCactus;
    private final long INTERVALO_CEREZA = 20_000;
    private final long INTERVALO_CACTUS = 30_000;
    private final long INTERVALO_PINA = 500;

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

        this.nivelActual = nivel - 1;
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

        // Inicializar control de fases
        this.faseActual = 0;
        this.faseCompletada = false;

        this.juegoIniciado = false;
        this.juegoTerminado = false;
        this.pausado = false;
        this.nivelCompletado = false;
        this.victoria = false;
        this.esperandoDecisionNivel = false;

        this.puntajeJugador1 = 0;
        this.puntajeJugador2 = 0;

        this.modoJuego = crearFactory(modo, perfil1, perfil2);
        this.creador = this.modoJuego;
    }

    private CreadorElemento crearFactory(String modo, String perfil1, String perfil2) {
        switch (modo) {
            case "Un jugador":
            case "Un solo jugador":
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
        // Cargar mapa base (sin frutas)
        this.mapaBase = InfoNivel.getNivelBase(nivel);
        
        if (mapaBase == null) {
            throw new BadDopoException(BadDopoException.NIVEL_INVALIDO);
        }

        // Cargar todas las fases del nivel
        this.fasesDelNivel = InfoNivel.getFasesNivel(nivel);
        if (fasesDelNivel == null || fasesDelNivel.isEmpty()) {
            throw new BadDopoException("Nivel sin fases configuradas");
        }

        // Iniciar en la fase 0
        this.faseActual = 0;
        this.faseCompletada = false;

        int filas = mapaBase.length;
        int columnas = mapaBase[0].length;

        this.tablero = new Tablero(mapaBase, creador);

        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {
                String tipo = mapaBase[fila][col];
                tablero.setElementoEnGrafo(fila, col, tipo);
            }
        }

        sincronizarElementosDesdeTablero();

        // Cargar primera fase
        cargarFase(faseActual);

        ultimoMovimientoPina = System.currentTimeMillis();
        ultimoTeletransporteCereza = System.currentTimeMillis();
        ultimoCrecimientoCactus = System.currentTimeMillis();

        nivelCompletado = false;
    }
    public void setNivelCompletado(boolean nivelCompletado) {
        this.nivelCompletado = nivelCompletado;
    }

    /**
     * Carga una fase específica: agrega las frutas en sus posiciones
     * SIN importar el estado actual del tablero
     */
    private void cargarFase(int indiceFase) throws BadDopoException {
        System.out.println("\n[BADDOPO] ========== CARGANDO FASE " + indiceFase + " ==========");
        
        if (indiceFase >= fasesDelNivel.size()) {
            throw new BadDopoException("Fase inválida");
        }

        InfoNivel.FaseNivel fase = fasesDelNivel.get(indiceFase);
        
        // IMPORTANTE: Limpiar frutas de la fase anterior
        tablero.limpiarFrutas();
        frutasEnJuego.clear();
        
        // Configurar frutas requeridas para esta fase
        frutasRequeridas.clear();
        frutasRecolectadas.clear();
        frutasRequeridas.putAll(fase.getFrutasRequeridas());
        
        for (String tipo : frutasRequeridas.keySet()) {
            frutasRecolectadas.put(tipo, 0);
        }
        
        System.out.println("[BADDOPO] Frutas requeridas en esta fase:");
        for (Map.Entry<String, Integer> entry : frutasRequeridas.entrySet()) {
            System.out.println("  - " + entry.getKey() + ": " + entry.getValue());
        }

        // Agregar frutas en las posiciones especificadas
        int frutasCreadas = 0;
        for (InfoNivel.PosicionFruta pf : fase.getPosicionesFrutas()) {
            try {
                Fruta fruta = creador.crearFruta(pf.fila, pf.columna, pf.tipo);
                
                if (fruta != null) {
                    tablero.agregarFrutaEnPosicion(fruta, pf.fila, pf.columna);
                    frutasEnJuego.add(fruta);
                    frutasCreadas++;
                } else {
                    System.err.println("[BADDOPO] FALLO al crear fruta: " + pf.tipo);
                }
            } catch (BadDopoException e) {
                System.err.println("[BADDOPO] Error creando fruta: " + e.getMessage());
            }
        }
        
        System.out.println("[BADDOPO] Total frutas creadas: " + frutasCreadas);
        System.out.println("[BADDOPO] Total frutas en juego: " + frutasEnJuego.size());
        System.out.println("[BADDOPO] ===========================================\n");

        mensajeEstado = "Fase " + (indiceFase + 1) + " de " + fasesDelNivel.size();
    }

    /**
     * Agrega una fruta en una posición específica del tablero
     */
    private void agregarFrutaEnPosicion(String tipo, int fila, int col) throws BadDopoException {
        Fruta fruta = creador.crearFruta(fila, col, tipo);
        
        if (fruta != null) {
            tablero.agregarFrutaEnPosicion(fruta, fila, col);
            frutasEnJuego.add(fruta);
        } else {
            System.err.println("[BADDOPO] No se pudo crear fruta tipo: " + tipo);
        }
    }

    private void sincronizarElementosDesdeTablero() {
        elementos.clear();
        frutasEnJuego.clear();
        enemigosEnJuego.clear();
        obstaculosEnJuego.clear();

        ArrayList<Fruta> frutas = tablero.getListaFrutas();
        if (frutas != null) {
            frutasEnJuego.addAll(frutas);
        }
        
        ArrayList<Enemigo> enemigos = tablero.getEnemigos();
        if (enemigos != null) {
            enemigosEnJuego.addAll(enemigos);
        }
        
        ArrayList<Obstaculo> obstaculos = tablero.getObstaculos();
        if (obstaculos != null) {
            obstaculosEnJuego.addAll(obstaculos);
        }
    }

    public HashMap<String, int[]> getPosicionesHelados() throws BadDopoException {
        HashMap<String, int[]> posicionesHelados = new HashMap<>();
        if (helado1 != null) {
            int[] p1 = tablero.getPosicionHelado(helado1);
            posicionesHelados.put("helado1", p1);
        }
        if (helado2 != null) {
            int[] p2 = tablero.getPosicionHelado(helado2);
            posicionesHelados.put("helado2", p2);
        }
        return posicionesHelados;
    }

    private void verificarFinDeFase() throws BadDopoException {
        if (faseActual >= fasesDelNivel.size() - 1) {
            nivelCompletado = true;
        } else {
            faseActual++;
            cargarFase(faseActual);
        }
    }

    public static boolean existeNivel(int nivelIndex) {
        try {
            return nivelIndex >= 0 && nivelIndex < MAX_NIVELES;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
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

    public void moverHelado(Helado helado, String direccion) throws BadDopoException {
        System.out.println("\n[BADDOPO] INICIO MOVIMIENTO");
        
        if (!juegoIniciado || pausado || juegoTerminado) {
            System.out.println("[BADDOPO] ERROR: No se puede mover");
            throw new BadDopoException(BadDopoException.JUEGO_NO_INICIADO);
        }

        int filaActual = helado.getFila();
        int colActual = helado.getColumna();

        boolean moved = tablero.solicitarMovimiento(filaActual, colActual, direccion);

        if (moved) {
            verificarRecoleccionFruta(helado.getFila(), helado.getColumna(), helado);
            moverPinas();
        }
    }

    public void moverHelado1(String direccion) throws BadDopoException {
        if (helado1 != null) {
            moverHelado(helado1, direccion);
        }
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

    private void verificarRecoleccionFruta(int fila, int col, Helado helado) throws BadDopoException {
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

    private void recolectarFruta(Fruta fruta, Helado helado) throws BadDopoException {
        int puntos = fruta.getGanancia();

        if (helado == helado1) {
            puntajeJugador1 += puntos;
        } else if (helado == helado2) {
            puntajeJugador2 += puntos;
        }

        // Actualizar contador de frutas recolectadas
        String tipoFruta = fruta.getCodigo();
        frutasRecolectadas.put(tipoFruta, frutasRecolectadas.getOrDefault(tipoFruta, 0) + 1);

        frutasEnJuego.remove(fruta);
        tablero.removerFruta(fruta);

        mensajeEstado = "¡+" + puntos + " puntos!";

        try {
            verificarProgresoFase();
        } catch (BadDopoException e) {
            System.err.println(e.getMessage());
    }
    }

    public void reiniciarNivel() throws BadDopoException {
        cargarNivel(nivelActual);
        inicializarHelados();
        tiempoInicioNivel = System.currentTimeMillis();
        tiempoTotalPausado = 0;
        mensajeEstado = "Nivel reiniciado";
    }

    /**
     * Verifica si se completó la fase actual y avanza a la siguiente
     */
    private void verificarProgresoFase() throws BadDopoException {
        if (nivelCompletado) return;

        if (todasLasFrutasFaseRecolectadas()) {
            faseCompletada = true;
            
            if (faseActual + 1 < fasesDelNivel.size()) {
                // Hay más fases en este nivel
                faseActual++;
                cargarFase(faseActual);
                mensajeEstado = "¡Fase completada! Nueva fruta disponible.";
                faseCompletada = false;
            } else {
                // Se completaron todas las fases del nivel
                completarNivel();
            }
        }
    }

    /**
     * Verifica si todas las frutas de la FASE ACTUAL fueron recolectadas
     */
    private boolean todasLasFrutasFaseRecolectadas() {
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

    private void completarNivel() throws BadDopoException {
         if (nivelCompletado) return;
        
        nivelesCompletados.add(nivelActual);
        nivelCompletado = true;
        esperandoDecisionNivel = true;
        
        if (nivelActual >= MAX_NIVELES - 1) {
            juegoTerminado = true;
            victoria = true;
            mensajeEstado = "¡Felicidades! Has completado todos los niveles.";
        } else {
            mensajeEstado = "¡Nivel completado! Presiona continuar para el siguiente nivel.";
        }
    }

    public void avanzarNivel() throws BadDopoException {
        esperandoDecisionNivel = false;
        nivelCompletado = false;
        
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

    private boolean requiereSegundoJugador() {
        return modo.equals("Jugador vs Jugador") ||
                modo.equals("Jugador vs Máquina") ||
                modo.equals("Máquina vs Máquina");
    }

    private boolean esJugadorHumano1() {
        return modo.equals("Un jugador") || modo.equals("Un solo jugador") ||
            modo.equals("Jugador vs Jugador") ||
            modo.equals("Jugador vs Máquina");
    }

    private boolean esJugadorHumano2() {
        return modo.equals("Jugador vs Jugador");
    }

    // ===== GETTERS =====
    
    public Tablero getTablero() { return tablero; }
    public int getNivelActual() { return nivelActual; }
    public int getFaseActual() { return faseActual; }
    public int getTotalFases() { return fasesDelNivel != null ? fasesDelNivel.size() : 0; }
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
        stats.put("faseActual", faseActual);
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

    public int[] getDimensionesTablero() {
        return tablero.getDimensiones();
    }

    public String[][] getRepresentacionTablero() {
        return this.mapaBase;
    }

    public HashMap<String, Fruta> getPosicionesFrutas() {
        return tablero.getListaPosicionesFrutas();
    }

    public HashMap<String, Enemigo> getPosicionesEnemigos() {
        return tablero.getPosicionesEnemigos();
    }

    public HashMap<String, Obstaculo> getPosicionesObstaculos() {
        return tablero.getPosicionesObstaculos();
    }

    /**
     * Método principal de actualización - IMPORTANTE: llamar desde GUI
     */
    public void actualizar() throws BadDopoException {
        if (!juegoIniciado || pausado || juegoTerminado || esperandoDecisionNivel) {
            return;
        }

        long tiempoActual = System.currentTimeMillis();

        ArrayList<Fruta> copiaFrutas = new ArrayList<>(frutasEnJuego);
        for (Fruta f : copiaFrutas) {
            f.actualizar(tiempoActual);
        }

        moverPinas();
        verificarColisiones();

        if (tiempoExcedido()) {
            juegoTerminado = true;
            mensajeEstado = "Tiempo agotado";
        }

    }

    public String getSabor1() { return sabor1; }
    public String getSabor2() { return sabor2; }
}