package domain;

public abstract class Elemento implements Mover {
    private int fila;
    private int columna;

    public Elemento(int fila, int columna){
        this.fila = fila;
        this.columna = columna;
    }

    public int getFila() { return fila; }
    public void setFila(int fila) { this.fila = fila; }

    public int getColumna() { return columna; }
    public void setColumna(int columna) { this.columna = columna;}

    public abstract void mover(String direccion) throws BadDopoException;
}
