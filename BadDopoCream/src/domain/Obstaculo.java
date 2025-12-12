package domain;

import java.util.ArrayList;

/**
 * Clase abstracta que representa un obstáculo en el juego Bad Dopo Cream.
 */
public abstract class Obstaculo extends Elemento {
    private int fila;
    private int columna;

    public Obstaculo(int fila, int col) throws BadDopoException {
        super(fila, col);

    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public abstract boolean esPeligroso();

    public abstract int[] calcularPosicionesMovimieto();

    public abstract boolean esRompible();
}
