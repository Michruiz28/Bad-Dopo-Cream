package domain;

import java.util.ArrayList;
import java.util.HashMap;

// Tablero.java
public class Tablero {
    private String[][] infoNivel;
    private static int filas;
    private static int columnas;
    private CreadorElemento creador;
    private GrafoTablero grafo;
    private ArrayList<Fruta> frutas;
    private HashMap<String, Fruta> posicionesFrutas;

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
        this.frutas = new ArrayList<>();
        this.posicionesFrutas = new HashMap<>();

        // El Tablero pasa toda la información necesaria al Grafo para que este lo construya
        // El Grafo se encarga de crear Nodos, que crean Celdas, que crean Elementos.
        this.grafo = new GrafoTablero(filas, columnas, infoNivel, creador);
    }

    public void setElementoEnGrafo(int fila, int col, String tipo) throws BadDopoException {
        grafo.setNodo(fila, col, tipo);
    }

    public int getColumnas() {
        return columnas;
    }

    public int getFilas() {
        return filas;
    }

    public boolean solicitarMovimientoHacia(int fila, int columna, String direccion) throws BadDopoException {
        return grafo.solicitarMovimientoHacia(fila, columna, direccion);
    }

    public void realizarAccion(int fila, int columna, String ultimaDireccion) throws BadDopoException {
        grafo.realizarAccion(fila, columna, ultimaDireccion);
    }

    public ArrayList<Fruta> getListaFrutas() {
        return grafo.getFrutas();
    }

    public ArrayList<Fruta> getFrutas(){
        return frutas = grafo.getFrutas();
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
        // Remover la fruta solo si la celda aún contiene esa instancia (evita sobrescribir un helado que ya se movió allí)
        grafo.removeElementoIfMatches(fila, columna, fruta);
    }

    public int[] getDimensiones(){
        int[] dimensiones = new int[2];
        dimensiones[0] = filas;
        dimensiones[1] = columnas;
        return dimensiones;
    }


    public void limpiarFrutas() {
        // Hacer copia para evitar ConcurrentModificationException
        ArrayList<Fruta> copiaFrutas = new ArrayList<>(frutas);

        for (Fruta f : copiaFrutas) {
            removerFruta(f);
        }

        frutas.clear();
        posicionesFrutas.clear();

        System.out.println("[TABLERO] Todas las frutas limpiadas");
    }

    public void getPosicionesFrutas(){
        this.posicionesFrutas = grafo.getPosicionesFrutas();
    }
    public HashMap<String, Fruta> getListaPosicionesFrutas(){
        return grafo.getPosicionesFrutas();
    }

    public HashMap<String, Enemigo> getPosicionesEnemigos(){
        return grafo.getPosicionesEnemigos();
    }

    /**
     * Ejecuta la actualización/autómata de los enemigos delegando al grafo.
     */
    public void actualizarEnemigos(Helado jugador) throws BadDopoException {
        grafo.actualizarEnemigos(jugador);
    }

    public HashMap<String, Obstaculo> getPosicionesObstaculos(){
        return grafo.getPosicionesObstaculos();
    }

    // Agregar este método a tu clase Tablero.java

    /**
     * Agrega una fruta en una posición específica del tablero
     * @param fruta La fruta a agregar
     * @param fila Fila donde colocar la fruta
     * @param col Columna donde colocar la fruta
     * @throws BadDopoException si la posición es inválida
     */
    public void agregarFrutaEnPosicion(Fruta fruta, int fila, int col) throws BadDopoException {
        if (fila < 0 || fila >= filas || col < 0 || col >= columnas) {
            throw new BadDopoException(BadDopoException.POSICION_FUERA_DE_RANGO);
        }
        grafo.agregarElementoEnPosicion(fruta, fila, col);
        frutas.add(fruta);
    }

    public void verificarGrafo() {
        grafo.verificarGrafo();
    }

}
