package domain;

/**
 * Clase para jugador máquina
 */
public class JugadorMaquina extends Helado {

    private String estrategia;

    public JugadorMaquina(int fila, int col, String sabor, String estrategia)
            throws BadDopoException {
        super(fila, col, sabor);
        if (estrategia == null || estrategia.trim().isEmpty()) {
            this.estrategia = "RANDOM";
        } else {
            this.estrategia = estrategia;
        }
    }

    public void setEstrategia(String estrategia) {
        this.estrategia = estrategia;
    }

}
