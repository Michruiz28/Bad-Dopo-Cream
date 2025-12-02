package domain;

/**
 * Hielo: puede ser roto por ciertos enemigos
 */
public class Hielo extends Obstaculo {

    public Hielo(int fila, int col, Celda celda) throws BadDopoException {
        super(fila, col, celda);
    }
}
