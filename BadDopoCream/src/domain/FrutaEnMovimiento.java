package domain;

public abstract class FrutaEnMovimiento extends Fruta {

    protected Direccion direccionActual;

    public FrutaEnMovimiento(int fila, int col)
            throws BadDopoException {
        super(fila, col);
        this.direccionActual = Direccion.ABAJO;
    }

    @Override
    public abstract String getCodigo();

    public FrutaEnMovimiento(int fila, int col, int ganancia) throws BadDopoException {
        super(fila, col, ganancia);
        this.direccionActual = Direccion.ABAJO;
    }


    public abstract void moverConPosicion(int filaNueva, int columnaNueva);
}