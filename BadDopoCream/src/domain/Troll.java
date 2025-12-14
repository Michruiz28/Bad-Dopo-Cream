package domain;

public class Troll extends Enemigo  {
    private String imagenAbajo;
    private String imagenDerecha;
    private String imagenIzquierda;
    private String imagenArriba;
    private String imagenActual;

    public Troll(int fila, int col) {
        super(fila, col, 1);
        // Dirección inicial aleatoria entre ARRIBA/ABAJO/DERECHA/IZQUIERDA
        String[] dirs = new String[]{"ARRIBA", "ABAJO", "DERECHA", "IZQUIERDA"};
        this.ultimaDireccion = dirs[new java.util.Random().nextInt(dirs.length)];
        this.persigueJugador = false;
        this.puedeRomperBloques = false;
        this.rompeUnBloquePorVez = false;
        this.imagenAbajo = "src/presentation/images/TrollAbajo.png";
        this.imagenDerecha = "src/presentation/images/TrollDerecha.png";
        this.imagenIzquierda = "src/presentation/images/TrolIzquierda.png";
        this.imagenArriba = "src/presentation/images/TrollDetras.png";
        this.imagenActual = this.imagenAbajo;
        setMovimientoStrategy(new EstrategiaTroll());
    }

    // Contador para ralentizar movimiento (ejecuta movimiento cada N ticks)
    private int contadorTicks = 0;
    // Ajusta este valor para controlar la velocidad del Troll (mayor = más lento)
    private int intervaloMovimiento = 12; // valor inicial más lento

    @Override
    public void ejecutarComportamiento(GrafoTablero grafo, VistaTablero vista, Helado jugador) throws BadDopoException {
        // Ajustar la velocidad del Troll en función del último movimiento del helado
        if (jugador != null) {
            long last = jugador.getUltimoMovimientoTime();
            long now = System.currentTimeMillis();
            long delta = now - last; // ms desde último movimiento del helado
            // Calcular intervalo en ticks: cuanto más reciente movió el helado, más rápido el troll
            // Base 12 ticks, y escala con el tiempo desde el último movimiento del helado
            int computed = (int) Math.max(6, Math.min(80, 12 + (delta / 200)));
            this.intervaloMovimiento = computed;
        }

        // Solo ejecutar la estrategia cuando el cooldown se cumpla
        contadorTicks++;
        if (contadorTicks < intervaloMovimiento) return;
        contadorTicks = 0;

        // Delegar a la estrategia (movimiento en línea recta). La estrategia
        // ya implementa invertir la dirección cuando encuentra un obstáculo.
        if (estrategiaMovimiento != null) {
            estrategiaMovimiento.ejecutarTurno(this, vista, jugador, grafo);
        }
    }

    @Override
    public void aumentarPuntaje(int puntaje) {

    }

    @Override
    public int getGanancia() {
        return 0;
    }

    @Override
    public void actualizarImagen(String ultimaDireccion) {
        if (ultimaDireccion == null) return;
        switch (ultimaDireccion) {
            case "DERECHA": imagenActual = imagenDerecha; break;
            case "IZQUIERDA": imagenActual = imagenIzquierda; break;
            case "ABAJO": imagenActual = imagenAbajo; break;
            case "ARRIBA": imagenActual = imagenArriba; break;
            default: break;
        }
    }

    @Override
    public String codigoTipo() { return "T"; }

    @Override
    public void romperHielo(Celda celdaARomper, CreadorElemento creador) throws BadDopoException {

    }

    @Override
    public void crearHielo(Celda celdaACrear, CreadorElemento creador) throws BadDopoException {

    }

    @Override
    public int[] calcularPosicionesMovimieto(int limiteInferior, int limiteSuperior) {
        return new int[0];
    }

    @Override
    public boolean esTransitable() {
        return false;
    }

    /**
     * El Troll se mueve en línea recta.
     * Si encuentra obstáculo, invierte la dirección.
     */
    // El movimiento queda delegado a la estrategia asignada en el constructor.
}