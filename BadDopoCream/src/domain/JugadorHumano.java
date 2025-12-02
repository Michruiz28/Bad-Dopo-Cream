 
package domain;
public class JugadorHumano extends Jugador {

    private Direccion ultimaDireccion;

    public JugadorHumano(String nombre, Helado helado) throws BadDopoException {
        super(nombre, helado);
        this.ultimaDireccion = null;
    }

    public void setDireccion(Direccion dir) {
        this.ultimaDireccion = dir;
    }

    @Override
    public void realizarMovimiento(Nivel nivel) throws BadDopoException {
        if (ultimaDireccion == null) return; 
        if (helado == null) {
            throw new BadDopoException("El jugador humano no tiene helado asignado.");
        }
        helado.mover(ultimaDireccion);
        this.ultimaDireccion = null;
    }
}
