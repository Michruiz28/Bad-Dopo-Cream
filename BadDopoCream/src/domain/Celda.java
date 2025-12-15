package domain;

public class Celda {
    private final int fila;
    private final int col;
    private Elemento elemento;
    private String tipo;
    // Indica si en esta celda se permite reconstruir hielo (true por defecto).
    // Cuando un enemigo rompe un bloque, se marcará en false para evitar
    // que el hielo vuelva a aparecer a menos que lo construya un Helado.
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

    /**
     * En tu clase Celda, mejora el método setElemento:
     */
    public void setElemento(Elemento elemento, CreadorElemento creador) throws BadDopoException {
        // Evitar dejar elemento en null: si se pasa null, creamos el elemento acorde al tipo
        if (elemento == null) {
            String tipoParaCrear = (this.tipo == null) ? "V" : this.tipo;
            this.elemento = creador.creadorElemento(fila, col, tipoParaCrear);
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

    /**
     * Método auxiliar para verificar si hay una fruta
     */
    public boolean tieneFruta() {
        return elemento instanceof Fruta;
    }

    public boolean esTransitable(){
        if (this.tipo == null) return true;
        // Marcar hielo y borde como no transitables para alinear con la lógica del grafo
        if (this.tipo.equals("H") || this.tipo.equals("B")) return false;
        return !elemento.esSolido();
    }
}