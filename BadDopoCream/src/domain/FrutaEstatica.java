package domain;
/**
 * Clase que representa una fruta estatica en el juego Bad Dopo Cream
 * Las frutas estáticas permanecen en una posición fija durante todo el nivel
 */
public abstract class FrutaEstatica extends Fruta {
    public FrutaEstatica(int fila, int columna) throws BadDopoException {
        super(fila, columna);
    }

    public FrutaEstatica(int fila, int columna, int ganancia) throws BadDopoException {
        super(fila, columna, ganancia);
    }

    @Override
    public abstract String getCodigo();

    @Override
    public void actualizar(long timpoActual) throws BadDopoException {

    }
}
