package domain;
import java.util.ArrayList;

public abstract class Enemigo implements Mover, RompeHielo{

    protected int fila;
    protected int columna;
    protected int velocidad;
    protected Direccion direccionActual;
    protected TipoComportamiento comportamiento;
    protected Tablero tablero;
    private ArrayList<Integer> posicion;
    private GrafoTablero grafo;
    private String ultimaDireccion; 


    public Enemigo(int fila, int columna, int velocidad, TipoComportamiento comportamiento, Tablero tablero) {
        this.fila = fila;
        this.columna = columna;
        this.velocidad = velocidad;
        this.comportamiento = comportamiento;
        this.tablero = tablero;
        this.direccionActual = Direccion.ABAJO;
    }

    @Override
    public void mover(String direccion) throws BadDopoException {
        // Este método genérico usa la última dirección guardada
        if (ultimaDireccion == null) {
            throw new BadDopoException(BadDopoException.DIRECCION_INVALIDA);
        }
        moverEnDireccion(ultimaDireccion);
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
    public abstract void romperHielo();

    public int getFila() { return fila; }
    public int getColumna() { return columna; }

}
