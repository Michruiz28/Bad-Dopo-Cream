package domain;

public class Calamar extends Enemigo {
    private String imagenAbajo;
    private String imagenDerecha;
    private String imagenIzquierda;
    private String imagenArriba;
    private String imagenActual;

    public Calamar(int fila, int col) {
        super(fila, col, 1, TipoComportamiento.ROMPEHIELO);
        this.ultimaDireccion = "ARRIBA";
        this.persigueJugador = true;
        this.puedeRomperBloques = true;
        this.rompeUnBloquePorVez = true;
        // Imagenes para Calamar
        this.imagenAbajo = "src/presentation/images/CalamarAbajo.png";
        this.imagenDerecha = "src/presentation/images/CalamarDerecha.png";
        this.imagenIzquierda = "src/presentation/images/CalamarIzquierda.png";
        this.imagenArriba = "src/presentation/images/CalamarArriba.png";
        this.imagenActual = this.imagenAbajo;
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
     * Calamar persigue al jugador.
     * Si el siguiente casillero es hielo, lo rompe y se detiene.
     */
    @Override
    public String decidirProximaMovida(VistaTablero vista, Helado jugador) throws BadDopoException {
        String direccion = vista.calcularDireccionHaciaObjetivo(
            getFila(), getColumna(),
            jugador.getFila(), jugador.getColumna(),
            true
        );
        if (direccion == null) {
            return getUltimaDireccion();
        }
        // Verificar si el siguiente paso es hielo
        int[] siguiente = vista.calcularNuevaPosicion(getFila(), getColumna(), direccion);
        if (vista.esHielo(siguiente[0], siguiente[1])) {
            setUltimaDireccion(direccion);
            return direccion; 
        }
        setUltimaDireccion(direccion);
        return direccion;
    }
}
