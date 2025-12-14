package domain;

public class Calamar extends Enemigo {
    private String imagenAbajo;
    private String imagenDerecha;
    private String imagenIzquierda;
    private String imagenArriba;
    private String imagenActual;

    public Calamar(int fila, int col) {
        super(fila, col, 1);
        this.ultimaDireccion = "ARRIBA";
        this.persigueJugador = true;
        this.puedeRomperBloques = true;
        this.rompeUnBloquePorVez = true;
        this.imagenAbajo = "src/presentation/images/CalamarAbajo.png";
        this.imagenDerecha = "src/presentation/images/CalamarDerecha.png";
        this.imagenIzquierda = "src/presentation/images/CalamarIzquierda.png";
        this.imagenArriba = "src/presentation/images/CalamarDetras.png";
        this.imagenActual = this.imagenAbajo;
        setMovimientoStrategy(new EstrategiaRompeHielo());
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

    // El comportamiento delegado a la estrategia (EstrategiaRompeHielo)
}
