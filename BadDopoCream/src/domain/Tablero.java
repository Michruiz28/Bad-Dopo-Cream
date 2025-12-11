package domain;

import java.util.ArrayList;

// Tablero.java
public class Tablero {
    private String[][] InfoNivel;
    private static int filas;
    private static int columnas;
    private CreadorElemento creador;
    private GrafoTablero grafo;

    /**
     * Constructor
     */
    public Tablero(String[][] infoNivel, CreadorElemento creador) throws BadDopoException {
        if (infoNivel == null || infoNivel.length == 0 || infoNivel[0].length == 0) {
            throw new BadDopoException(BadDopoException.INFONIVEL_VACIO);
        }

        this.InfoNivel = InfoNivel;
        Tablero.filas = InfoNivel.length;
        Tablero.columnas = InfoNivel[0].length;

        // El Tablero pasa toda la información necesaria al Grafo para que este lo construya
        // El Grafo se encarga de crear Nodos, que crean Celdas, que crean Elementos.
        this.grafo = new GrafoTablero(filas, columnas, infoNivel, creador);
    }

    public void setElementoEnGrafo(int fila, int col, String tipo) throws BadDopoException{
        grafo.setNodo(fila, col, tipo);
    }

    public int getColumnas() {
        return columnas;
    }

    public boolean solicitarMovimiento(int fila, int columna, String direccion) throws BadDopoException {
        return grafo.solicitarMovimiento(fila, columna, direccion);
    }

    public void realizarAccion(int fila, int columna, String ultimaDireccion) throws BadDopoException {
        grafo.realizarAccion(fila, columna, ultimaDireccion);
    }
}
