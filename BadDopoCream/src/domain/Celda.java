package domain;

public class Celda {
    private final int fila;
    private final int col;
    private Elemento elemento;

    public Celda(int fila, int col, String tipo, CreadorElemento creador) throws BadDopoException {
        this.fila = fila;
        this.col = col;
        this.elemento = creador.creadorElemento(fila, col, tipo);
    }

    public int getFila() { return fila; }

    public int getCol() { return col; }

    public Elemento getElemento() { return elemento; }

    public void setElemento(Elemento tipo) { this.elemento = tipo; }

    //public boolean esTransitable() {
    //    return tipo == TipoCelda.VACIA;
    //}

    //public boolean esRompible() {
        // return tipo == TipoCelda.HIELO;
    //}
}
