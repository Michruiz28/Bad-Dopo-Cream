package domain;

public class Maceta extends Enemigo  {
    private String imagenAbajo;
    private String imagenDerecha;
    private String imagenIzquierda;
    private String imagenArriba;
    private String imagenActual;
    public Maceta(int fila, int col) {
        super(fila, col, 1, TipoComportamiento.PERSEGUIDOR);
        this.persigueJugador = true;
        this.puedeRomperBloques = false;
        this.rompeUnBloquePorVez = false;
        // Imagenes para Maceta
        this.imagenAbajo = "src/presentation/images/MacetaAbajo.png";
        this.imagenDerecha = "src/presentation/images/MacetaDerecha.png";
        this.imagenIzquierda = "src/presentation/images/MacetaIzquierda.png";
        this.imagenArriba = "src/presentation/images/MacetaArriba.png";
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
     * Maceta persigue al jugador usando BFS.
     * No puede romper bloques.
     */
    @Override
    public String decidirProximaMovida(VistaTablero vista, Helado jugador) throws BadDopoException {
        String direccion = vista.calcularDireccionHaciaObjetivo(
            getFila(), getColumna(),
            jugador.getFila(), jugador.getColumna(),
            false // no permite pasar por hielo
        );
        
        if (direccion != null) {
            setUltimaDireccion(direccion);
            return direccion;
        }
        
        // Si no hay camino, intenta movimiento aleatorio
        java.util.List<String> validas = vista.obtenerDireccionesValidas(getFila(), getColumna());
        if (!validas.isEmpty()) {
            String aleatoria = validas.get(new java.util.Random().nextInt(validas.size()));
            setUltimaDireccion(aleatoria);
            return aleatoria;
        }
        
        // No puede moverse
        return getUltimaDireccion();
    }
}
