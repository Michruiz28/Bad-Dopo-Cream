package domain;

import java.util.ArrayList;

/**
 * Clase que representa una fruta en el juego Bad Dopo Cream
 * Las frutas deben ser recolectadas por el helado para ganar puntos
 */
public abstract class Fruta extends Elemento {

    private int fila;
    private int col;
    private boolean reinicio;

    public Fruta(int fila, int col) throws BadDopoException {
        super(fila, col);
        if (fila < 0 || col < 0) {
            throw new BadDopoException(BadDopoException.POSICION_FUERA_DE_RANGO);
        }

        this.reinicio = false;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return col;
    }

    @Override
    public boolean esTransitable() {
        return true;
    }

    public abstract void actualizar(long timpoActual) throws BadDopoException;
}