package domain;
/**
 * Clase Banano
 */
public class Banano extends FrutaEstatica {
    public static final int GANANCIA_BANANO = 100;
    private static final String imagen = "src/presentation/images/platano.png";
    private static final String codigo = "BF";
    public Banano (int fila, int columna) throws BadDopoException {
        super(fila,columna,GANANCIA_BANANO);
    }

    @Override
    public void mover(String direccion) throws BadDopoException {

    }

    @Override
    public void aumentarPuntaje(int puntaje) {

    }

    @Override
    public String getCodigo(){
        return codigo;
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

    public Banano(int fila, int columna, int ganancia, Celda celda) throws BadDopoException {
        super(fila, columna, ganancia);
        if (celda != null) {
            setCelda(celda);
        }
    }
}
