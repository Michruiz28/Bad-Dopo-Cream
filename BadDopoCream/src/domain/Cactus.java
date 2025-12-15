 
package domain;
/**
 * Clase cactus
 */
public class Cactus extends FrutaEstatica {

    public static final int GANANCIA_CACTUS = 250;
    private boolean peligroso = false;
    private long ultimoCambio = 0;
    private static final int TIEMPO_CAMBIO = 30000;
    public Cactus(int fila, int columna) throws BadDopoException {
        super(fila, columna ,GANANCIA_CACTUS);
    }

    @Override
    public String getCodigo() {
        return "CACO";
    }

    @Override
    public void mover(String direccion) throws BadDopoException {

    }

    @Override
    public void aumentarPuntaje(int puntaje) {

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

    public boolean esPeligroso(){
        return peligroso;
    }

    @Override
    public void actualizar(long tiempoActual){
        if (tiempoActual - ultimoCambio >= TIEMPO_CAMBIO){
            peligroso =! peligroso;
            ultimoCambio = tiempoActual;
        }
    }

}
