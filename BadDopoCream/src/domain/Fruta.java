package domain;

/**
 * Clase abstracta fruta
 */
public abstract class Fruta extends Elemento {

    protected int ganancia;
    private boolean reinicio;

    public Fruta(int fila, int col) throws BadDopoException {
        this(fila, col, 0);
    }

    public abstract String getCodigo();

    public Fruta(int fila, int col, int ganancia) throws BadDopoException {
        super(fila, col);
        if (fila < 0 || col < 0) {
            throw new BadDopoException(BadDopoException.POSICION_FUERA_DE_RANGO);
        }
        this.ganancia = ganancia;
        this.reinicio = false;
    }

    public int getFila() {
        return super.getFila();
    }

    public int getColumna() {
        return super.getColumna();
    }

    @Override
    public boolean esTransitable() {
        return true;
    }

    @Override
    public boolean esFruta() { return true; }

    @Override
    public int getGanancia() {
        return ganancia;
    }

    public abstract void actualizar(long timpoActual) throws BadDopoException;
}