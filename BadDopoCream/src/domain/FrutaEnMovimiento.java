package domain;

public abstract class FrutaEnMovimiento extends Fruta {

    protected String direccionActual = "NINGUNA";

    public FrutaEnMovimiento(int fila, int columna, int ganancia) throws BadDopoException {
        super(fila, columna, ganancia);
    }
    @Override
    public void mover(String direccion) throws BadDopoException{
        moverEnDireccion(direccion);
    }

    protected void moverEnDireccion(String direccion) throws BadDopoException{
        if (direccion == null || direccion.isEmpty())
            throw new BadDopoException(BadDopoException.DIRECCION_INVALIDA);
        switch (direccion.toUpperCase()){
            case "ARRIBA": setFila(getFila() - 1); break;
            case "ABAJO" : setFila(getFila() + 1); break;
            case "IZQUIERDA": setColumna(getColumna() - 1); break;
            case "DERECHA" : setColumna(getColumna() + 1); break;
            default: throw new BadDopoException(BadDopoException.DIRECCION_DESCONOCIDA);
        }
        direccionActual = direccion.toUpperCase();
    }

}
