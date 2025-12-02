package domain;

import java.util.ArrayList;

public class Helado implements Poder, Mover, RompeHielo {
    private String sabor;
    private int puntaje;
    private ArrayList<Integer> posicion;
    private Tablero tablero;
    private GrafoTablero grafo;
    private String ultimaDireccion;


    public Helado(String sabor) throws BadDopoException {
        if (sabor == null || sabor.trim().isEmpty()) {
            throw new BadDopoException(BadDopoException.SABOR_INVALIDO);
        }
        this.sabor = sabor;
        this.puntaje = 0;
        this.posicion = new ArrayList<>();
        this.posicion.add(0);
        this.posicion.add(0);
    }

    public void setTablero(Tablero tablero) throws BadDopoException {
        if (tablero == null) {
            throw new BadDopoException(BadDopoException.TABLERO_NULO);
        }
        this.tablero = tablero;
    }
    
    public void setGrafo(GrafoTablero grafo) throws BadDopoException {
        if (grafo == null) {
            throw new BadDopoException(BadDopoException.GRAFO_NULO);
        }
        this.grafo = grafo;
    }
    
    public void setPosicionInicial(int fila, int col) throws BadDopoException {
        if (tablero == null) {
            throw new BadDopoException(BadDopoException.TABLERO_NO_CONFIGURADO);
        }
        
        if (fila < 0 || fila >= tablero.getFilas() || col < 0 || col >= tablero.getColumnas()) {
            throw new BadDopoException(BadDopoException.POSICION_FUERA_DE_RANGO);
        }
        
        Celda celda = tablero.getCelda(fila, col);
        if (celda == null || !celda.esTransitable()) {
            throw new BadDopoException(BadDopoException.POSICION_NO_TRANSITABLE);
        }
        
        this.posicion.set(0, fila);
        this.posicion.set(1, col);
    }

    @Override
    public void mover(Direccion direccion) throws BadDopoException {
        if (direccion == null ){
            throw new BadDopoException(BadDopoException.DIRECCION_INVALIDA);
        }
        moverEnDireccion(direccion.name());
    }
    /**
     * Metodo moverEnDireccion  que permite mover el helado
     * en las cuatro direcciones, este metodo usa los metodos privados moverArriba,Abajo etc
     * @param direccion La direccion en la que se desea mover el helado (ARRIBA, ABAJO, DERECHA, IZQUIERDA)
     * @throws BadDopoException Si la configuracion es incompleta, la direccion es invalida o desconocida
     */
    public void moverEnDireccion(String direccion) throws BadDopoException {
        if (tablero == null || grafo == null) {
            throw new BadDopoException(BadDopoException.CONFIGURACION_INCOMPLETA);
        }
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new BadDopoException(BadDopoException.DIRECCION_INVALIDA);
        }
        switch (direccion.toUpperCase()) {
            case "ARRIBA":
            case "UP":
                moverArriba();
                break;
            case "ABAJO":
            case "DOWN":
                moverAbajo();
                break;
            case "DERECHA":
            case "RIGHT":
                moverDerecha();
                break;
            case "IZQUIERDA":
            case "LEFT":
                moverIzquierda();
                break;
            default:
                throw new BadDopoException(BadDopoException.DIRECCION_DESCONOCIDA);
        }
    }
    
    private void moverArriba() throws BadDopoException {
        int filaActual = posicion.get(0);
        int colActual = posicion.get(1);
        int nuevaFila = filaActual - 1;
        
        if (!validarMovimiento(nuevaFila, colActual)) {
            throw new BadDopoException(BadDopoException.MOVIMIENTO_INVALIDO);
        }
        
        posicion.set(0, nuevaFila);
    }
    
    private void moverAbajo() throws BadDopoException {
        int filaActual = posicion.get(0);
        int colActual = posicion.get(1);
        int nuevaFila = filaActual + 1;
        
        if (!validarMovimiento(nuevaFila, colActual)) {
            throw new BadDopoException(BadDopoException.MOVIMIENTO_INVALIDO);
        }
        
        posicion.set(0, nuevaFila);
    }
    
    private void moverDerecha() throws BadDopoException {
        int filaActual = posicion.get(0);
        int colActual = posicion.get(1);
        int nuevaCol = colActual + 1;
        
        if (!validarMovimiento(filaActual, nuevaCol)) {
            throw new BadDopoException(BadDopoException.MOVIMIENTO_INVALIDO);
        }
        
        posicion.set(1, nuevaCol);
    }
    
    private void moverIzquierda() throws BadDopoException {
        int filaActual = posicion.get(0);
        int colActual = posicion.get(1);
        int nuevaCol = colActual - 1;
        
        if (!validarMovimiento(filaActual, nuevaCol)) {
            throw new BadDopoException(BadDopoException.MOVIMIENTO_INVALIDO);
        }
        
        posicion.set(1, nuevaCol);
    }
    /**
     * Metodo para validar  los movimientos especificos del helado en el tablero
     * @param fila La fila a la que se desea mover
     * @param col La columna a la que se desea mover
     * @return true si el movimiento es valido, false en caso contrario
     * @throws BadDopoException Si la configuracion es incompleta
     */
    private boolean validarMovimiento(int fila, int col) throws BadDopoException {
        if (tablero == null || grafo == null) {
            throw new BadDopoException(BadDopoException.CONFIGURACION_INCOMPLETA);
        }
        if (fila < 0 || fila >= tablero.getFilas() || col < 0 || col >= tablero.getColumnas()) {
            return false;
        }
        Celda celdaDestino = tablero.getCelda(fila, col);
        if (celdaDestino == null || !celdaDestino.esTransitable()) {
            return false;
        }
        int filaActual = posicion.get(0);
        int colActual = posicion.get(1);
        return grafo.puedeMover(filaActual, colActual, fila, col);
    }

    @Override
    public void romperHielo() throws BadDopoException {
        if (tablero == null || grafo == null) {
            throw new BadDopoException(BadDopoException.CONFIGURACION_INCOMPLETA);
        }
        int fila = posicion.get(0);
        int col = posicion.get(1);

        boolean seRompioAlgo = false;
        
        try {
            // Arriba
            if (tablero.romperHielo(fila - 1, col)) seRompioAlgo = true;
            // Abajo
            if (tablero.romperHielo(fila + 1, col)) seRompioAlgo = true;
            // Izquierda
            if (tablero.romperHielo(fila, col - 1)) seRompioAlgo = true;
            // Derecha
            if (tablero.romperHielo(fila, col + 1)) seRompioAlgo = true;
            
            // Si se rompió algo, reconstruir el grafo
            if (seRompioAlgo) {
                grafo.reconstruir();
            } else {
                throw new BadDopoException(BadDopoException.NO_HAY_HIELO_PARA_ROMPER);
            }
        } catch (BadDopoException e) {
            throw e;
        } catch (Exception e) {
            throw new BadDopoException(BadDopoException.ERROR_AL_ROMPER_HIELO);
        }
    }
    
    // Implementación de Poder
    @Override
    public void poder() throws BadDopoException {
        // El poder del helado es crear hielo
        crearHielo();
    }
    
    public void crearHielo() throws BadDopoException {
        if (tablero == null || grafo == null) {
            throw new BadDopoException(BadDopoException.CONFIGURACION_INCOMPLETA);
        }
        
        int fila = posicion.get(0);
        int col = posicion.get(1);
        
        Celda celdaActual = tablero.getCelda(fila, col);
        if (celdaActual == null) {
            throw new BadDopoException(BadDopoException.CELDA_INVALIDA);
        }
        
        if (celdaActual.getTipo() != TipoCelda.VACIA) {
            throw new BadDopoException(BadDopoException.NO_SE_PUEDE_CREAR_HIELO);
        }
        tablero.setCelda(fila, col, TipoCelda.HIELO);
        grafo.reconstruir(); 
    }
    
    public void cambiarSabor(String nuevoSabor) throws BadDopoException {
        if (nuevoSabor == null || nuevoSabor.trim().isEmpty()) {
            throw new BadDopoException(BadDopoException.SABOR_INVALIDO);
        }
        this.sabor = nuevoSabor;
    }
    
    public void comerFruta(Fruta fruta) throws BadDopoException {
        if (fruta == null) {
            throw new BadDopoException(BadDopoException.FRUTA_NULA);
        }
        if (fruta.getPosicion().get(0).equals(posicion.get(0)) && 
            fruta.getPosicion().get(1).equals(posicion.get(1))) {
            puntaje += fruta.getGANANCIA();
        } else {
            throw new BadDopoException(BadDopoException.FRUTA_FUERA_DE_ALCANCE);
        }
    }
    
//    public void tomarGanancia(Ganancia ganancia) throws BadDopoException {
//        if (ganancia == null) {
//            throw new BadDopoException(BadDopoException.GANANCIA_NULA);
//        }
//        if (ganancia.getPosicion().get(0).equals(posicion.get(0)) &&
//            ganancia.getPosicion().get(1).equals(posicion.get(1))) {
//            puntaje += ganancia.getValor();
//            ganancia.setRecolectada(true);
//        } else {
//            throw new BadDopoException(BadDopoException.GANANCIA_FUERA_DE_ALCANCE);
//        }
//    }
    
    public void escogerNivel(int nivel) throws BadDopoException {
        if (nivel < 1) {
            throw new BadDopoException(BadDopoException.NIVEL_INVALIDO);
        }
        this.puntaje = 0;
    }
    
    public String getSabor() {
        return sabor;
    }
    
    public void setSabor(String sabor) throws BadDopoException {
        if (sabor == null || sabor.trim().isEmpty()) {
            throw new BadDopoException(BadDopoException.SABOR_INVALIDO);
        }
        this.sabor = sabor;
    }
    
    public int getPuntaje() {
        return puntaje;
    }
    
    public void setPuntaje(int puntaje) throws BadDopoException {
        if (puntaje < 0) {
            throw new BadDopoException(BadDopoException.PUNTAJE_INVALIDO);
        }
        this.puntaje = puntaje;
    }
    
    public ArrayList<Integer> getPosicion() {
        return posicion;
    }
    
    public void setPosicion(int fila, int col) throws BadDopoException {
        if (tablero == null) {
            throw new BadDopoException(BadDopoException.TABLERO_NO_CONFIGURADO);
        }
        
        if (fila < 0 || fila >= tablero.getFilas() || col < 0 || col >= tablero.getColumnas()) {
            throw new BadDopoException(BadDopoException.POSICION_FUERA_DE_RANGO);
        }
        
        this.posicion.set(0, fila);
        this.posicion.set(1, col);
    }
    
    public int getFila() {
        return posicion.get(0);
    }
    
    public int getColumna() {
        return posicion.get(1);
    }
    
    public Tablero getTablero() {
        return tablero;
    }
    
    public GrafoTablero getGrafo() {
        return grafo;
    }
}