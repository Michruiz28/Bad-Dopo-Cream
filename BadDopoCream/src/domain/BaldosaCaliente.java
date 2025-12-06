package domain;

/**
 * Baldosa caliente: daña al helado si la pisa.
 */
public class BaldosaCaliente extends Obstaculo {

    public BaldosaCaliente(int fila, int col) throws BadDopoException {
        super(fila, col);
    }
}
