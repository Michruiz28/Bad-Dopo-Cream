package domain;
/**
 * Clase que representa un Banano en el juego Bad Dopo Cream
 * El banano es una fruta estática que permanece en su posición hasta ser recolectada
 */
public class Banano extends FrutaEstatica {
    public static final int GANANCIA_BANANO = 100;
    /**
     * Constructor con ganancia personalizada
     * @param fila Fila donde se ubica el banano
     * @param col Columna donde se ubica el banano
     * @param ganancia Puntos que otorga (personalizado)
     * @param celda Celda a la que pertenece
     * @throws BadDopoException Si los parámetros son inválidos
     */
    public Banano(int fila, int col, int ganancia, Celda celda) throws BadDopoException {
        super(fila, col, celda, "BANANO");
    }
    
}
