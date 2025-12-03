 
package domain;
public class JugadorHumano extends Helado {

    private Direccion ultimaDireccion;

    public JugadorHumano(int fila, int col, String sabor) throws BadDopoException {
        super(fila, col, sabor);
        this.ultimaDireccion = null;
    }

    public void setDireccion(Direccion dir) {
        this.ultimaDireccion = dir;
    }

    @Override
    public void realizarMovimiento(Nivel nivel) throws BadDopoException {
       // if (ultimaDireccion == null) return;
       // if (helado == null) {
       //     throw new BadDopoException("El jugador humano no tiene helado asignado.");
       // }
       // helado.mover(ultimaDireccion);
        this.ultimaDireccion = null;
    }
}
