package domain;

public class JugadorHumano extends Jugador {

    private Direccion ultimaDireccion;

    public JugadorHumano(String nombre, Helado helado) throws BadDopoException {
        super(nombre, helado);
        this.ultimaDireccion = Direccion.NINGUNA;
    }

    public void setDireccion(Direccion dir) {
        this.ultimaDireccion = dir;
    }

    @Override
    public void realizarMovimiento(Nivel nivel) throws BadDopoException {
        if (ultimaDireccion == Direccion.NINGUNA) return;
        helado.mover(ultimaDireccion, nivel);
    }
}
