package domain;

import java.util.ArrayList;

/**
 * Clase que representa una fruta estatica en el juego Bad Dopo Cream
 * Las frutas estáticas permanecen en una posición fija durante todo el nivel
 */
public class FrutaEstatica extends Fruta {
    private String tipo;
    
    public FrutaEstatica(int fila, int col, Celda celda, String tipo) throws BadDopoException {
        super(fila, col, celda);
        this.tipo = tipo;
    }
    
    /**
     * Las frutas estáticas NO pueden cambiar de posición
     * @throws BadDopoException Siempre lanza excepción porque no se pueden mover
     */
    @Override
    public void setPosicion(int fila, int col) throws BadDopoException {
        throw new BadDopoException("Las frutas estáticas no pueden cambiar de posición");
    }
    
    /**
     * Método para verificar si la fruta es estática
     * @return true siempre, ya que esta clase representa frutas estáticas
     */
    public boolean esEstatica() {
        return true;
    }
    
    public String getTipo(){
        return tipo; 
    }
}
