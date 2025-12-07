package domain; 

/**
 * Clase que representa Uvas en el juego Bad Dopo Cream
 * Las uvas son frutas estáticas que permanecen en su posición hasta ser recolectadas
 */
public class Uva extends FrutaEstatica {
    public static final int GANANCIA_UVA = 50;
    
    public Uva(int fila, int columna) throws BadDopoException {
        super(fila, columna, GANANCIA_UVA);
    }

}