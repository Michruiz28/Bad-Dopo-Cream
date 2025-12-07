package domain;
/**
 * Clase que representa un Banano en el juego Bad Dopo Cream
 * El banano es una fruta estática que permanece en su posición hasta ser recolectada
 */
public class Banano extends FrutaEstatica {
    public static final int GANANCIA_BANANO = 100;
    public Banano (int fila, int columna) throws BadDopoException {
        super(fila,columna,GANANCIA_BANANO);
    }
    
}
