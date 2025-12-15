package domain;
/**
 * Clase nodo
 */

import java.util.ArrayList;
import java.util.List;

public class Nodo {
    private final List<Nodo> vecinos = new ArrayList<>();
    private Celda celda;

    public Nodo(int fila, int columna, String tipo, CreadorElemento creador) throws BadDopoException {
        this.celda = new Celda(fila, columna, tipo, creador);
    }

    public int getFila(){
        return this.celda.getFila();
    }

    public int getColumna(){
        return this.celda.getCol();
    }

    public Celda getCelda() { return celda; }

    public List<Nodo> getVecinos() { return vecinos; }

    public void agregarVecino(Nodo n) {
        if (n != null && !vecinos.contains(n)) vecinos.add(n);
    }

    @Override
    public String toString() {
        return "Nodo " + celda.toString();
    }
}

