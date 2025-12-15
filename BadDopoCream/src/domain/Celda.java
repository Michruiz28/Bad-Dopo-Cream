package domain;

public class Celda {
    private final int fila;
    private final int col;
    private Elemento elemento;
    private String tipo;
    private boolean permiteReconstruccion = true;

    public Celda(int fila, int col, String tipo, CreadorElemento creador) throws BadDopoException {
        this.fila = fila;
        this.col = col;
        this.tipo = tipo;
        this.elemento = creador.creadorElemento(fila, col, tipo);
    }

    public int getFila() { return fila; }

    public int getCol() { return col; }

    public String getTipo() { return tipo; }

    public Elemento getElemento() { return elemento; }

    public void setElemento(Elemento elemento, CreadorElemento creador) throws BadDopoException {
        if (tipo == null) {
            this.elemento = creador.creadorElemento(fila, col, "V");
        } else {
            this.elemento = elemento;
        }
    }

    public void setTipo(String tipo) { this.tipo = tipo; }

    public void setElementoConTipo(String tipo, CreadorElemento creador) throws BadDopoException {
        this.elemento = creador.creadorElemento(fila, col, tipo);
        this.tipo = tipo;
    }

    public boolean permiteReconstruccion() { return permiteReconstruccion; }
    public void setPermiteReconstruccion(boolean permiteReconstruccion) { this.permiteReconstruccion = permiteReconstruccion; }

    public boolean tieneFruta() {
        return elemento instanceof Fruta;
    }

    public boolean esTransitable(){
        if (this.tipo == null) return true;
        if (this.tipo.equals("H") || this.tipo.equals("B")) return false;
        if (this.elemento == null) return true;
        return !elemento.esSolido();
    }
}