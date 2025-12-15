package domain;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Fachada de alto nivel sobre el grafo del tablero.
 *
 * <p>Administra la representación del nivel, delega la lógica de movimiento y
 * operaciones sobre celdas a {@link GrafoTablero} y expone métodos útiles para
 * la capa de presentación y el bucle de juego.</p>
 */
public class Tablero {
    private String[][] infoNivel;
    private static int filas;
    private static int columnas;
    private CreadorElemento creador;
    private GrafoTablero grafo;
    private ArrayList<Fruta> frutas;
    private HashMap<String, Fruta> posicionesFrutas;

    public Tablero(String[][] infoNivel, CreadorElemento creador) throws BadDopoException {
        if (infoNivel == null || infoNivel.length == 0 || infoNivel[0].length == 0) {
            throw new BadDopoException(BadDopoException.INFONIVEL_VACIO);
        }

        this.infoNivel = infoNivel;
        Tablero.filas = infoNivel.length;
        Tablero.columnas = infoNivel[0].length;
        this.frutas = new ArrayList<>();
        this.posicionesFrutas = new HashMap<>();

        this.grafo = new GrafoTablero(filas, columnas, infoNivel, creador);
    }

    /**
     * Crea un tablero a partir de la representación textual del nivel y un
     * CreadorElemento para instanciar elementos.
     *
     * @param infoNivel matriz de códigos por celda
     * @param creador factor para crear elementos
     * @throws BadDopoException si la representación del nivel es inválida
     */

    public void setElementoEnGrafo(int fila, int col, String tipo) throws BadDopoException {
        grafo.setNodo(fila, col, tipo);
    }

    /**
     * Actualiza el tipo/elemento de una celda del grafo.
     *
     * @param fila fila de la celda
     * @param col  columna de la celda
     * @param tipo código de tipo a aplicar
     * @throws BadDopoException si la operación no es válida
     */

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


    /**  
     * Procesa un movimiento de helado delegando al grafo (mueve helado y, si corresponde, las piñas).
    */
    public boolean procesarMovimientoHelado(int filaOrigen, int columnaOrigen, String direccion, Helado jugador) throws BadDopoException {
        return grafo.procesarMovimientoHelado(filaOrigen, columnaOrigen, direccion, jugador);
    }

    /**
     * Procesa el movimiento de un helado en el tablero y aplica lógica
     * adicional delegada al grafo (p. ej. movimiento de piñas si está activado).
     */

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
        grafo.removeElementoIfMatches(fila, columna, fruta);
    }

    public int[] getDimensiones(){
        int[] dimensiones = new int[2];
        dimensiones[0] = filas;
        dimensiones[1] = columnas;
        return dimensiones;
    }


    public void limpiarFrutas() {
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

    public void actualizarEnemigos(Helado jugador) throws BadDopoException {
        grafo.actualizarEnemigos(jugador);
    }

    public void setMoverPinasAlMoverHelado(boolean activar) {
        try {
            grafo.setMoverPinasAlMoverHelado(activar);
        } catch (Exception e) {
            System.err.println("[TABLERO] Error al cambiar flag de movimiento de piñas: " + e.getMessage());
        }
    }

    public HashMap<String, Obstaculo> getPosicionesObstaculos(){
        return grafo.getPosicionesObstaculos();
    }

    public void agregarFrutaEnPosicion(Fruta fruta, int fila, int col) throws BadDopoException {
        if (fila < 0 || fila >= filas || col < 0 || col >= columnas) {
            throw new BadDopoException(BadDopoException.POSICION_FUERA_DE_RANGO);
        }
        grafo.agregarElementoEnPosicion(fruta, fila, col);
        frutas.add(fruta);
    }

    /**
     * Agrega una fruta en la posición indicada y la registra internamente.
     *
     * @param fruta instancia de fruta a agregar
     * @param fila  fila destino
     * @param col   columna destino
     * @throws BadDopoException si la posición está fuera de rango o la operación falla
     */

    public void verificarGrafo() {
        grafo.verificarGrafo();
    }

    public void teletransportarCerezas() throws BadDopoException {
        grafo.teletransportarCerezas();
    }
    public String[][] construirRepresentacionActual(){
        return  grafo.construirRepresentacionActual();
    }

}
