package domain;

import java.util.ArrayList;

/**
 * Clase abstracta que representa un obstáculo en el juego Bad Dopo Cream.
 */
public abstract class Obstaculo {

    protected ArrayList<Integer> posicion;
    protected Celda celda;

    public Obstaculo(int fila, int col, Celda celda) throws BadDopoException {

        if (fila < 0 || col < 0) {
            throw new BadDopoException(BadDopoException.POSICION_FUERA_DE_RANGO);
        }

        if (celda == null) {
            throw new BadDopoException("La celda no puede ser nula");
        }

        this.posicion = new ArrayList<>();
        this.posicion.add(fila);
        this.posicion.add(col);

        this.celda = celda;
    }

    public int getFila() {
        return posicion.get(0);
    }

    public int getColumna() {
        return posicion.get(1);
    }

    public ArrayList<Integer> getPosicion() {
        return posicion;
    }

    public Celda getCelda() {
        return celda;
    }
}
