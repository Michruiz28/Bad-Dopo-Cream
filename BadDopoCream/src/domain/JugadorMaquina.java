package domain;

public class JugadorMaquina extends Helado {

    private String estrategia;

    public JugadorMaquina(int fila, int col, String sabor, String estrategia)
            throws BadDopoException {
        super(fila, col, sabor);

        if (estrategia == null) {
            throw new BadDopoException("La máquina debe tener una estrategia de movimiento");
        }

        this.estrategia = estrategia;
    }

    public void setEstrategia(String estrategia) {
        this.estrategia = estrategia;
    }

}
