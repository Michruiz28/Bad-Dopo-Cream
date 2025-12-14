package domain;

public class Troll extends Enemigo  {
    private String imagenAbajo;
    private String imagenDerecha;
    private String imagenIzquierda;
    private String imagenArriba;
    private String imagenActual;

    public Troll(int fila, int col) {
        super(fila, col, 1);
        this.ultimaDireccion = "ARRIBA";
        this.persigueJugador = false;
        this.puedeRomperBloques = false;
        this.rompeUnBloquePorVez = false;
        this.imagenAbajo = "src/presentation/images/TrollAbajo.png";
        this.imagenDerecha = "src/presentation/images/TrollDerecha.png";
        this.imagenIzquierda = "src/presentation/images/TrolIzquierda.png";
        this.imagenArriba = "src/presentation/images/TrollDetras.png";
        this.imagenActual = this.imagenAbajo;
        setMovimientoStrategy(new EstrategiaLineal());
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