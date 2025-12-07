package domain;

public abstract class Elemento implements Mover {
    private int fila;
    private int columna;
    private Celda celda;

    public Elemento(int fila, int columna){
        this.fila = fila;
        this.columna = columna;
        this.celda = null;
    }

    public int getFila() { return fila; }
    public void setFila(int fila) { this.fila = fila; }

    public int getColumna() { return columna; }
    public void setColumna(int columna) { this.columna = columna;}

    public Celda getCelda() {return celda;}
    public void setCelda(Celda celda) {this.celda = celda;}


    /**
     * Indica si el elemento es solido
     * si es asi bloquea el paso
     * Este metodo lo sobreescriben las clases de los elementos segun la logica
     * @return
     */
    public boolean esSolido() {
        return false;
    }

    public abstract void mover(String direccion) throws BadDopoException;

