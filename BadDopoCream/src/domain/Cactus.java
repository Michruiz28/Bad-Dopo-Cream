package domain;

/**
 * Clase que representa un Cactus en el juego Bad Dopo Cream.
 * Es una fruta estática que alterna entre estado normal y estado con púas.
 */
public class Cactus extends FrutaEstatica {

    public static final int GANANCIA_CACTUS = 250;
    private boolean tienePuas;        // true = peligroso y el helado no lo puede recolectar
    private long tiempoCambioEstado;

    public Cactus(int fila, int col, Celda celda) throws BadDopoException {
        super(fila, col,GANANCIA_CACTUS, celda, "CACTUS");
        this.tienePuas = false;             // inicia sin púas
        this.tiempoCambioEstado = System.currentTimeMillis();
    }

    /**
     * Alterna el estado cada 30 segundos:
     */
    public void actualizarEstado() {
        long ahora = System.currentTimeMillis();
        long delta = ahora - tiempoCambioEstado;

        if (delta >= 30_000) { 
            tienePuas = !tienePuas;  // alterna estado
            tiempoCambioEstado = ahora;
        }
    }

    /**
     * Indica si este cactus tiene actualmente púas activas.
     */
    public boolean tienePuas() {
        return tienePuas;
    }

    /**
     * El helado solo puede recolectar el cactus si NO tiene púas.
     */
    public boolean esRecolectable() {
        return !tienePuas;
    }

}
