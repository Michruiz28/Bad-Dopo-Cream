 
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

}
