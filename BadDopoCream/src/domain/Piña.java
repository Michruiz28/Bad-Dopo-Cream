 
package domain;
 
 /**
  * Clase que representa una Pina en el juego Bad Dopo Cream.
  * Es una fruta en movimiento que se desplaza automáticamente por el tablero.
  */
public class Pina extends FrutaEnMovimiento {
    public static final int GANANCIA_PINA = 200;
    public Pina(int fila, int columna) throws BadDopoException {
        super(fila, columna, GANANCIA_PINA);
    }

    @Override
    public void actualizar(long tiempoActual) throws BadDopoException{
        if (direccionActual.equals("DERECHA"))
            mover("IZQUIERDA");
        else
            mover("DERECHA");
    }

}
