package domain;

public class JugadorMaquina extends Helado {

    private EstrategiaMovimiento estrategia;

    public JugadorMaquina(int fila, int col, String sabor, String estrategia)
            throws BadDopoException {
        super(fila, col, sabor);

        if (estrategia == null) {
            throw new BadDopoException("La máquina debe tener una estrategia de movimiento");
        }

        this.estrategia = estrategia;
    }

    public void setEstrategia(EstrategiaMovimiento estrategia) {
        this.estrategia = estrategia;
    }

    @Override
    public void realizarMovimiento(Nivel nivel) throws BadDopoException {
        //Direccion dir = estrategia.calcularMovimiento(helado, nivel);
        //helado.mover(dir);
    }
}
