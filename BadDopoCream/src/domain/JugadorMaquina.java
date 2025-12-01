package domain;

public class JugadorMaquina extends Jugador {

    private EstrategiaMovimiento estrategia;

    public JugadorMaquina(String nombre, Helado helado, EstrategiaMovimiento estrategia)
            throws BadDopoException {
        super(nombre, helado);

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
        Direccion dir = estrategia.calcularMovimiento(helado, nivel);
        helado.mover(dir, nivel);
    }
}
