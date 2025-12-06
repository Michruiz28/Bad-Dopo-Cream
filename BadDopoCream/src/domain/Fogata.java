package domain;

/**
 * Clase que representa una fogata como obstáculo del juego.
 * La fogata puede estar encendida o apagada.
 */
public class Fogata extends Obstaculo {

    private boolean encendida;

    public Fogata(int fila, int col) throws BadDopoException {
        super(fila, col);
        this.encendida = true;
    }

    public void encenderFogata() {
        this.encendida = true;
    }

    public void apagarFogata() {
        this.encendida = false;
    }

    public boolean estaEncendida() {
        return encendida;
    }
}
