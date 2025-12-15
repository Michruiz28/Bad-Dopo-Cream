package domain;

/**
 * Clase troll
 */
public class Troll extends Enemigo  {
    private String imagenAbajo;
    private String imagenDerecha;
    private String imagenIzquierda;
    private String imagenArriba;
    private String imagenActual;

    public Troll(int fila, int col) {
        super(fila, col, 1);
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

    private int contadorTicks = 0;
    private int intervaloMovimiento = 12;

    @Override
    public void ejecutarComportamiento(GrafoTablero grafo, VistaTablero vista, Helado jugador) throws BadDopoException {
        if (jugador != null) {
            long last = jugador.getUltimoMovimientoTime();
            long now = System.currentTimeMillis();
            long delta = now - last;
            int computed = (int) Math.max(6, Math.min(80, 12 + (delta / 200)));
            this.intervaloMovimiento = computed;
        }

        contadorTicks++;
        if (contadorTicks < intervaloMovimiento) return;
        contadorTicks = 0;

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

}