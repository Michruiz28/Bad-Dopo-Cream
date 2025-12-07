package domain;

/**
 * Obstáculo estándar del juego.
 * Por defecto es sólido y bloquea paso.
 */
public abstract class Obstaculo extends Elemento {
    public Obstaculo(int fila, int col) throws BadDopoException {
        super(fila, col);
    }
    /**
     * Por defecto un obstáculo es sólido.
     */
    @Override
    public boolean esSolido() {
        return true;
    }
    /**
     * Por defecto no es peligroso.
     */
    public boolean esPeligroso() {
        return false;
    }
    /**
     * Por defecto no es rompible.
     */
    public boolean esRompible() {
        return false;
    }
    @Override
    public void mover(String direccion) {
        // Los obstáculos NO se mueven — no implementado
    }
}
