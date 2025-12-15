package domain;

import java.io.*;
import java.util.*;

/**
 * Clase principal BadDopoCream
 */
public class BadDopoCream implements Serializable {

    // Atributos principales
    private Tablero tablero;
    private GrafoTablero grafo;
    private String modo;
    private int nivelActual;
    private ArrayList<Integer> nivelesCompletados;

    // Control de fases
    private int faseActual;
    private ArrayList<InfoNivel.FaseNivel> fasesDelNivel;
    private boolean faseCompletada;

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
    private boolean nivel2Desbloqueado = false;

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
    private final long INTERVALO_CEREZA = 8_000;
    private final long INTERVALO_CACTUS = 30_000;
    private final long INTERVALO_PINA = 500;

    private static final int MAX_NIVELES = 2;

    /**
     * Constructor
     * @param nivel que se jugará
     * @param modo Modo de juego
     * @param sabor1 Sabor del primer jugador
     * @param sabor2 Sabor del segundo jugador
     * @param nombre1 nombre del primer jugador
     * @param nombre2 nombre del segundo jugador
     * @param perfil1 perfil del jugador 1 si está en modo máquina
     * @param perfil2 perfil del juagdor 2 si está en modo máquina
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

    /**
     * Creador elemento que crea los elementossegún configuración previa del juego.
     * @param modo
     * @param perfil1
     * @param perfil2
     * @return
     */
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

    /**
     * inciar juego permite iniciar el nivel del juego y el tiempo para su conteo.
     * @throws BadDopoException
     */
    public void iniciarJuego() throws BadDopoException {
        System.out.println("[BADDOPO] INICIANDO JUEGO");
        System.out.println("[BADDOPO] Nivel: " + nivelActual);
        System.out.println("[BADDOPO] Modo: " + modo);

        try {
            cargarNivel(nivelActual);
            inicializarHelados();
            juegoIniciado = true;
            tiempoInicioNivel = System.currentTimeMillis();
            tiempoTotalPausado = 0;
            mensajeEstado = "¡Juego iniciado! Recolecta todas las frutas.";
            System.out.println("[BADDOPO] Juego iniciado correctamente");
        } catch (Exception e) {
            System.err.println("[BADDOPO] ERROR al iniciar juego: " + e.getMessage());
            e.printStackTrace();
            throw new BadDopoException(BadDopoException.ERROR_INICIO_JUEGO);
        }
    }

    /**
     * Método para cargar nivel que se jugara con sus respectivas fases.
     * @param nivel que se jugará
     * @throws BadDopoException NIVEL_INVALIDO, FASES_NO_ENCONTRADAS
     */
    private void cargarNivel(int nivel) throws BadDopoException {
        this.mapaBase = InfoNivel.getNivelBase(nivel);

        if (mapaBase == null) {
            throw new BadDopoException(BadDopoException.NIVEL_INVALIDO);
        }

        this.fasesDelNivel = InfoNivel.getFasesNivel(nivel);
        if (fasesDelNivel == null || fasesDelNivel.isEmpty()) {
            throw new BadDopoException(BadDopoException.FASES_NO_ENCONTRADAS);
        }

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

        cargarFase(faseActual);

        ultimoMovimientoPina = System.currentTimeMillis();
        ultimoTeletransporteCereza = System.currentTimeMillis();

        nivelCompletado = false;
    }

    /**
     * Marcarnivel como completado
     * @param nivelCompletado
     */
    public void setNivelCompletado(boolean nivelCompletado) {
        this.nivelCompletado = nivelCompletado;
    }

    /**
     * Carga una fase específica donde agrega las frutas en sus posiciones sin importar el estado actual del tablero
     * @param indiceFase Fase que se actualizará
     * @param BadDopoException
     */
    private void cargarFase(int indiceFase) throws BadDopoException {
        System.out.println("\nCARGANDO FASE");

        if (indiceFase >= fasesDelNivel.size()) {
            throw new BadDopoException("Fase inválida");
        }

        InfoNivel.FaseNivel fase = fasesDelNivel.get(indiceFase);

        tablero.limpiarFrutas();
        frutasEnJuego.clear();

        frutasRequeridas.clear();
        frutasRecolectadas.clear();
        frutasRequeridas.putAll(fase.getFrutasRequeridas());

        for (String tipo : frutasRequeridas.keySet()) {
            frutasRecolectadas.put(tipo, 0);
        }

        System.out.println("Frutas requeridas en esta fase:");
        for (Map.Entry<String, Integer> entry : frutasRequeridas.entrySet()) {
            System.out.println("  - " + entry.getKey() + ": " + entry.getValue());
        }

        int frutasCreadas = 0;
        for (InfoNivel.PosicionFruta pf : fase.getPosicionesFrutas()) {
            try {
                Fruta fruta = creador.crearFruta(pf.fila, pf.columna, pf.tipo);

                if (fruta != null) {
                    tablero.agregarFrutaEnPosicion(fruta, pf.fila, pf.columna);
                    frutasEnJuego.add(fruta);
                    frutasCreadas++;
                } else {
                    System.err.println("FALLO al crear fruta: " + pf.tipo);
                }
            } catch (BadDopoException e) {
                System.err.println("Error creando fruta: " + e.getMessage());
            }
        }

        System.out.println("frutas creadas: " + frutasCreadas);
        System.out.println("frutas en juego: " + frutasEnJuego.size());

        mensajeEstado = "Fase " + (indiceFase + 1) + " de " + fasesDelNivel.size();
    }

    /**
     * Verificar si el segundo nivel ya está desbloqueado
     * @return si el segundo nivel ya está desbloqueado
     */
    public boolean isNivel2Desbloqueado() {
        return nivel2Desbloqueado;
    }

    /**
     * Asignar nivel 2 como desbloqueado
     */
    public void desbloquearNivel2() {
        nivel2Desbloqueado = true;
    }

    /**
     * Agregar fruta con tipo en posición dada
     * @param tipo de la fruta
     * @param fila donde se ubicará
     * @param col donde se ubicará
     * @throws BadDopoException
     */
    private void agregarFrutaEnPosicion(String tipo, int fila, int col) throws BadDopoException {
        Fruta fruta = creador.crearFruta(fila, col, tipo);

        if (fruta != null) {
            tablero.agregarFrutaEnPosicion(fruta, fila, col);
            frutasEnJuego.add(fruta);
        } else {
            throw new BadDopoException(BadDopoException.ERROR_CREAR_FRUTA);
        }
    }

    /**
     * sincronizar elementos desde tablero a través de la lista de las frutas, enemigos y obstaculos.
     */
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

    /**
     * Genera y retorna un diccionario con la lista de los helados y sus respectivas posiciones
     * @return diccionario de helados y sus posiciones
     * @throws BadDopoException
     */
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

    /**
     * Método para verificar si se puede pasar a la siguiente fase o no
     * @throws BadDopoException
     */
    private void verificarFinDeFase() throws BadDopoException {
        if (faseActual >= fasesDelNivel.size() - 1) {
            nivelCompletado = true;
        } else {
            faseActual++;
            cargarFase(faseActual);
        }
    }

    /**
     * Revisar si existe el nivel
     * @param nivelIndex
     * @return si existe o no
     */
    public static boolean existeNivel(int nivelIndex) {
        try {
            return nivelIndex >= 0 && nivelIndex < MAX_NIVELES;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Inicializar helados con sus respectivas posiciones y sabores
     * @throws BadDopoException
     */
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

    /**
     * moverHelado revisa el helado que se desea mover y la dirección en la que se quiere mover realizando las respectivas verificaciones para realizar el movimiento
     * @param helado a mover
     * @param direccion en la que se desea mover el helado
     * @throws BadDopoException JUEGO_NO_INICIADO
     */
    public void moverHelado(Helado helado, String direccion) throws BadDopoException {
        System.out.println("\nINICIO MOVIMIENTO");

        if (!juegoIniciado || pausado || juegoTerminado) {
            System.out.println("ERROR: No se puede mover");
            throw new BadDopoException(BadDopoException.JUEGO_NO_INICIADO);
        }

        int filaActual = helado.getFila();
        int colActual = helado.getColumna();

        boolean moved = tablero.solicitarMovimientoHacia(filaActual, colActual, direccion);

        if (moved) {
            verificarRecoleccionFruta(helado.getFila(), helado.getColumna(), helado);
            moverPinas();
        }
    }

    /**
     * Mover helado 1 (jugador 1) en dirección deseada
     * @param direccion a la que se desea mover
     * @throws BadDopoException
     */
    public void moverHelado1(String direccion) throws BadDopoException {
        if (helado1 != null) {
            moverHelado(helado1, direccion);
        }
    }

    /**
     * Mover helado 2 (jugador 2) en dirección deseada
     * @param direccion a la que se desea mover
     * @throws BadDopoException
     */
    public void moverHelado2(String direccion) throws BadDopoException {
        if (helado2 != null) {
            moverHelado(helado2, direccion);
        }
    }

    /**
     * Se solicita al tablero realizar dirección
     * @param fila de donde se quiere realizar el movimiento
     * @param columna de donde se quiere realizar el movimiento
     * @param ultimaDireccion última dirección hacia donde se dirigía el elemento
     * @throws BadDopoException
     */
    public void realizarAccion(int fila, int columna, String ultimaDireccion) throws BadDopoException {
        tablero.realizarAccion(fila, columna, ultimaDireccion);
    }

    /**
     * Mover piñas método que se implementa para movimiento continuo de la fruta
     * @throws BadDopoException
     */
    private void moverPinas() throws BadDopoException {
        long tiempoActual = System.currentTimeMillis();
        if (tiempoActual - ultimoMovimientoPina < INTERVALO_PINA) {
            return;
        }

        for (Fruta fruta : frutasEnJuego) {
            if (fruta != null && fruta.getCelda() != null && "P".equals(fruta.getCelda().getTipo())) {
                ((Pina) fruta).mover();
            }
        }
        ultimoMovimientoPina = tiempoActual;
    }

    /**
     * Verificar colisiones para saber si el helado se elimina luego de tocar un enemigo.
     * @throws BadDopoException
     */
    private void verificarColisiones() throws BadDopoException {
        if (helado1 != null && colisionaConEnemigo(helado1)) {
            heladoEliminado(helado1);
        }
        if (helado2 != null && colisionaConEnemigo(helado2)) {
            heladoEliminado(helado2);
        }
    }

    /**
     * Revisa las posiciones de los helados y enemigos para asignar una colisión
     * @param helado
     * @return si colisiona con un enemigo
     */
    private boolean colisionaConEnemigo(Helado helado) throws BadDopoException {
        int[] posHelado = tablero.getPosicionHelado(helado);
        if (posHelado == null) return false;

        for (Enemigo enemigo : enemigosEnJuego) {
            int[] posEnemigo = tablero.getPosicionEnemigo(enemigo);
            if (posEnemigo != null &&
                    posHelado[0] == posEnemigo[0] &&
                    posHelado[1] == posEnemigo[1]) {
                helado.setPuntaje(0);
                return true;
            }
        }
        return false;
    }

    /**
     * Reiniciar nivel si se elimina el helado
     * @param helado que se verifica
     * @throws BadDopoException
     */
    private void heladoEliminado(Helado helado) throws BadDopoException {
        mensajeEstado = "¡Helado eliminado! Reiniciando nivel...";
        reiniciarNivel();
    }

    /**
     * Verificar si se recolecta la fruta o no
     * @param fila donde se recoletará fruta
     * @param col donde se recolectará fruta
     * @param helado helado que recolectará la fruta
     * @throws BadDopoException
     */
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

    /**
     *
     * @param fruta
     * @param helado
     * @throws BadDopoException
     */
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
                faseActual++;
                cargarFase(faseActual);
                mensajeEstado = "¡Fase completada! Nueva fruta disponible.";
                faseCompletada = false;
            } else {
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

    /**
     * Confirmar el que se completó un nivel
     * @throws BadDopoException
     */
    private void completarNivel() throws BadDopoException {
        if (nivelCompletado) return;

        if (nivelActual == 0) {
            desbloquearNivel2();
        }

        nivelesCompletados.add(nivelActual);
        nivelCompletado = true;
        esperandoDecisionNivel = true;

        if (nivelActual >= 2) {
            juegoTerminado = true;
            victoria = true;
            mensajeEstado = "¡Felicidades! Has completado todos los niveles.";
        } else {
            mensajeEstado = "¡Nivel completado! Presiona continuar para el siguiente nivel.";
        }
    }

    /**
     * Avanzar al siguiente nivel
     * @throws BadDopoException
     */
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

    /**
     * Recibir el tiempo que queda de juego
     * @return
     */
    public long getTiempoRestante() {
        if (!juegoIniciado || juegoTerminado) {
            return TIEMPO_MAXIMO;
        }

        long tiempoTranscurrido = System.currentTimeMillis() - tiempoInicioNivel - tiempoTotalPausado;
        long tiempoRestante = TIEMPO_MAXIMO - tiempoTranscurrido;
        return Math.max(0, tiempoRestante);
    }

    /**
     * Revisar el tiempo restante de juego
     * @return formato string del juego
     */
    public String getTiempoRestanteFormato() {
        long milisegundos = getTiempoRestante();
        long segundos = milisegundos / 1000;
        long minutos = segundos / 60;
        segundos = segundos % 60;
        return String.format("%02d:%02d", minutos, segundos);
    }

    /**
     * Evalúa si se acabó el tiempo
     * @return
     */
    private boolean tiempoExcedido() {
        return getTiempoRestante() == 0;
    }

    /**
     * Pausa el juego
     */
    public void pausar() {
        if (juegoIniciado && !pausado && !juegoTerminado) {
            pausado = true;
            tiempoPausado = System.currentTimeMillis();
        }
    }

    /**
     * Reanuda el juego
     */
    public void reanudar() {
        if (pausado) {
            pausado = false;
            tiempoTotalPausado += System.currentTimeMillis() - tiempoPausado;
        }
    }

    /**
     * Revisa si se requiere segundo jugador en el juego
     * @return boolean
     */
    private boolean requiereSegundoJugador() {
        return modo.equals("Jugador vs Jugador") ||
                modo.equals("Jugador vs Máquina") ||
                modo.equals("Máquina vs Máquina");
    }

    /**
     * Revisa si es humano el jugador 1
     * @return boolean
     */
    private boolean esJugadorHumano1() {
        return modo.equals("Un jugador") || modo.equals("Un solo jugador") ||
            modo.equals("Jugador vs Jugador") ||
            modo.equals("Jugador vs Máquina");
    }

    /**
     * Revisa si es humano el jugador 2
     * @return boolean
     */
    private boolean esJugadorHumano2() {
        return modo.equals("Jugador vs Jugador");
    }

    /**
     * retorna tablero del juego
     * @return tablero
     */
    public Tablero getTablero() { return tablero; }

    /**
     * retorna nivel actual del juego
     * @return niveLactual
     */
    public int getNivelActual() { return nivelActual; }

    /**
     * Retorna fase actual del juego
     * @return faseActual
     */
    public int getFaseActual() { return faseActual; }

    /**
     * Retorna cantidad total de fases
     * @return TotalFases
     */
    public int getTotalFases() { return fasesDelNivel != null ? fasesDelNivel.size() : 0; }

    /**
     * Retorna modo de juego
     * @return modo
     */
    public String getModo() { return modo; }

    /**
     * Retorna si se inició el juego
     * @return juegoInciado
     */
    public boolean isJuegoIniciado() { return juegoIniciado; }

    /**
     * Retrona si ya se acabó el juego
     * @return juegoTerminado
     */
    public boolean isJuegoTerminado() { return juegoTerminado; }

    /**
     * Retorna si el juego está pausado
     * @return pausado
     */
    public boolean isPausado() { return pausado; }

    /**
     * Retorna si el nivel está completado
     * @return nivelCompletado
     */
    public boolean isNivelCompletado() { return nivelCompletado; }

    /**
     * Retorna si fue victoria
     * @return victoria
     */
    public boolean isVictoria() { return victoria; }

    /**
     * Retorna mensaje de estado del juego
     * @return mensaje estado
     */
    public String getMensajeEstado() { return mensajeEstado; }

    /**
     * Retorna puntaje jugador 1
     * @return
     */
    public int getPuntajeJugador1() { return puntajeJugador1; }

    /**
     * Retorna puntaje jugador 2
     * @return
     */
    public int getPuntajeJugador2() { return puntajeJugador2; }

    /**
     * Retorna helado primer jugador
     * @return Helado1
     */
    public Helado getHelado1() { return helado1; }

    /**
     * Retorna helado segundo jugador
     * @return Helado2
     */
    public Helado getHelado2() { return helado2; }

    /**
     * Retorna arraylist de frutas en juego
     * @return ArrayList de frutas
     */
    public ArrayList<Fruta> getFrutasEnJuego() { return new ArrayList<>(frutasEnJuego); }

    /**
     * Retorna arraylist de enemigos en juego
     * @return arrayList de enemigos en juego
     */
    public ArrayList<Enemigo> getEnemigosEnJuego() { return new ArrayList<>(enemigosEnJuego); }

    /**
     * Retorna arraylist de frutas que se deben recolectar
     * @return diccionario de frutas que se necesitan
     */
    public HashMap<String, Integer> getFrutasRequeridas() { return new HashMap<>(frutasRequeridas); }

    /**
     * retorna diccionario de frutas recolectadas
     * @return retorna frutas recolectadas
     */
    public HashMap<String, Integer> getFrutasRecolectadas() { return new HashMap<>(frutasRecolectadas); }

    /**
     * retorna cantidad de niveles completadas
     * @return
     */
    public int getCantidadNivelesCompletados() { return nivelesCompletados.size(); }

    /**
     * retorna el progreso que llevan las frutas
     * @param tipoFruta que se va a revisar el progreso
     * @return progreso de frutas en formato String
     */
    public String getProgresoFrutas(String tipoFruta) {
        int recolectadas = frutasRecolectadas.getOrDefault(tipoFruta, 0);
        int requeridas = frutasRequeridas.getOrDefault(tipoFruta, 0);
        return recolectadas + "/" + requeridas;
    }

    /**
     * Retorna el ganador
     * @return String ganador
     */
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

    /**
     * Persistencia para guardar juego
     * @param archivo
     * @throws IOException
     */
    public void guardar(String archivo) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(this);
        }
    }

    /**
     * persistencia para cargar juego
     * @param archivo
     * @return juego
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static BadDopoCream cargar(String archivo) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            BadDopoCream juego = (BadDopoCream) ois.readObject();
            if (juego.pausado) {
                juego.tiempoPausado = System.currentTimeMillis();
            }
            return juego;
        }
    }

    /**
     * Termina el juego cambiando estados
     */
    public void terminarJuego() {
        juegoTerminado = true;
        pausado = false;
        mensajeEstado = "Juego terminado por el usuario";
    }

    /**
     * Se reinicia juego
     * @throws BadDopoException
     */
    public void reiniciarJuego() throws BadDopoException {
        nivelActual = 1;
        nivelesCompletados.clear();
        puntajeJugador1 = 0;
        puntajeJugador2 = 0;
        juegoTerminado = false;
        victoria = false;
        iniciarJuego();
    }

    /**
     * Se retorna resumen final del juego
     * @return
     */
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

    /**
     * Revisa si se finalizó con éxito el juego
     * @return si es juego ganado
     */
    public boolean isJuegoGanado() {
      return nivelActual == 1 && frutasEnJuego.isEmpty();
    }

    /**
     * Retorna estadísticas finales del juego
     * @return estadísticas del juego
     */
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

    /**
     * Retorna dimensiones del tablero
     * @return dimensiones del tablero como array de int
     */
    public int[] getDimensionesTablero() {
        return tablero.getDimensiones();
    }

    /**
     * Retorna presentación del tablero como String[][]
     */
    public String[][] getRepresentacionTablero() {
        return tablero.construirRepresentacionActual();
    }

    /**
     * Retorna las posiciones de las frutas
     * @return lista de posiciones
     */
    public HashMap<String, Fruta> getPosicionesFrutas() {
        return tablero.getListaPosicionesFrutas();
    }

    /**
     * Retorna las posiciones de los enemigos
     * @return lista de posiciones
     */
    public HashMap<String, Enemigo> getPosicionesEnemigos() {
        return tablero.getPosicionesEnemigos();
    }

    /**
     * Retorna posiciones de los obstáculos
     * @return lista de posiciones
     */
    public HashMap<String, Obstaculo> getPosicionesObstaculos() {
        return tablero.getPosicionesObstaculos();
    }

    /**
     * Revisa si está en la última posición del juego
     * @return si es último nivel
     */
    public boolean esUltimoNivel() {
        return nivelActual == 1;
    }

    /**
     * Revisa si se completó el juego
     * @return si se completó el juego
     */
    public boolean isJuegoCompletado() {
        return esUltimoNivel() && frutasEnJuego.isEmpty();
    }

    /**
     * Método principal de actualización del juego
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

        try {
            if (tablero != null && helado1 != null) tablero.actualizarEnemigos(helado1);
            if (tablero != null && helado2 != null) tablero.actualizarEnemigos(helado2);
        } catch (BadDopoException ex) {
        }

        moverPinas();
        teletransportarCerezas();
        verificarColisiones();
        verificarColisiones();
        if (tiempoExcedido()) {
            juegoTerminado = true;
            mensajeEstado = "Tiempo agotado";
        }

    }

    /**
     * Teletransporta las cerezas a posiciones aleatorias del tablero
     */
    public void teletransportarCerezas() throws BadDopoException {
        if (!juegoIniciado || pausado || juegoTerminado) {
            return;
        }

        long tiempoActual = System.currentTimeMillis();

        if (tiempoActual - ultimoTeletransporteCereza < INTERVALO_CEREZA) {
            return;
        }

        tablero.teletransportarCerezas();

        ultimoTeletransporteCereza = tiempoActual;
    }

    /**
     * Construye representación actual del juego
     * @return representación del tablero como String
     */
    public String[][] construirRepresentacionActual(){
        return  tablero.construirRepresentacionActual();
    }

    /**
     * Retorna sabor del primer helado
     * @return String del sabor
     */
    public String getSabor1() { return sabor1; }

    /**
     * Retorna sabor del segundo helado
     * @return String del sabor
     */
    public String getSabor2() { return sabor2; }

    public String getModoJuego() {
        return modo.toString();
    }
}