package domain;

import java.util.ArrayList;

/**
 * Clase que representa una fruta en el juego Bad Dopo Cream
 * Las frutas deben ser recolectadas por el helado para ganar puntos
 */
public class Fruta {

    private ArrayList<Integer> posicion;
    private static final int GANANCIA = 50;
    private boolean reinicio;
    private Celda celda; 

    public Fruta(int fila, int col, Celda celda) throws BadDopoException {
        if (fila < 0 || col < 0) {
            throw new BadDopoException(BadDopoException.POSICION_FUERA_DE_RANGO);
        }

        if (celda == null) {
            throw new BadDopoException("La celda no puede ser nula");
        }
        
        this.posicion = new ArrayList<>();
        this.posicion.add(fila);
        this.posicion.add(col);
        this.reinicio = false;
        this.celda = celda;
    }
    
    public ArrayList<Integer> getPosicion() {
        return posicion;
    }
    
    public void setPosicion(int fila, int col) throws BadDopoException {
        if (fila < 0 || col < 0) {
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
    
    public int getGANANCIA() {
        return GANANCIA;
    }

    public Celda getCelda() {
        return celda;
    }
    
    public void setCelda(Celda celda) throws BadDopoException {
        if (celda == null) {
            throw new BadDopoException("La celda no puede ser nula");
        }
        this.celda = celda;
    }
}