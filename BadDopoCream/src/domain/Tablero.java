package domain;

import java.util.ArrayList;

// Tablero.java
public class Tablero {
    private String[][] infoNivel;
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

        this.infoNivel = infoNivel;
        Tablero.filas = infoNivel.length;
        Tablero.columnas = infoNivel[0].length;

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
    public int getFilas(){ return filas;}

    public boolean solicitarMovimiento(int fila, int columna, String direccion) throws BadDopoException {
        return grafo.solicitarMovimiento(fila, columna, direccion);
    }
    public void realizarAccion(int fila, int columna, String ultimaDireccion) throws BadDopoException {
        grafo.realizarAccion(fila, columna, ultimaDireccion);
    }

    public ArrayList<Fruta> getFrutas(){
        return grafo.getFrutas();
    }

    public ArrayList<Enemigo> getEnemigos(){
        return grafo.getEnemigos();
    }

    public ArrayList<Obstaculo> getObstaculos(){
        return grafo.getObstaculos();
    }

    public void agregarHelado(Helado helado) throws BadDopoException {
        grafo.agregarHelado(helado);
    }

    public int[] getPosicionHelado(Helado helado){
        int[] posicionHelado = new int[2];
        posicionHelado[0] = helado.getFila();
        posicionHelado[1] = helado.getColumna();
        return posicionHelado;
    }

    public int[] getPosicionEnemigo(Enemigo enemigo)  {
        int[] posicionEnemigo = new int[2];
        posicionEnemigo[0] = enemigo.getFila();
        posicionEnemigo[1] = enemigo.getColumna();
        return posicionEnemigo;
    }

    public int[] getPosicionObstaculo(Obstaculo obstaculo)  {
        int[] posicionObstaculo = new int[2];
        posicionObstaculo[0] = obstaculo.getFila();
        posicionObstaculo[1] = obstaculo.getColumna();
        return posicionObstaculo;
    }

    public int[] getPosicionFruta(Fruta fruta) {
        int[] posicionFruta = new int[2];
        posicionFruta[0] = fruta.getFila();
        posicionFruta[1] = fruta.getColumna();
        return posicionFruta;
    }

    public void removerFruta(Fruta fruta){
        int fila =  fruta.getFila();
        int columna = fruta.getColumna();
        grafo.removeElemento(fila, columna);
    }
}
