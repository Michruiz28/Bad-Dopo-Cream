package domain;

// Celda.java
public class Celda {
    private final int fila;
    private final int col;
    private TipoCelda tipo;

    public Celda(int fila, int col, TipoCelda tipo) {
        this.fila = fila;
        this.col = col;
        this.tipo = tipo;
    }

    public int getFila() { return fila; }
    public int getCol() { return col; }

    public TipoCelda getTipo() { return tipo; }
    public void setTipo(TipoCelda tipo) { this.tipo = tipo; }

    // Solo VACIA es transitable
    public boolean esTransitable() {
        return tipo == TipoCelda.VACIA;
    }

    // Solo HIELO puede romperse
    public boolean esRompible() {
        return tipo == TipoCelda.HIELO;
    }

    @Override
    public String toString() {
        return "(" + fila + "," + col + ") " + tipo;
    }
}
