 
package domain;
 
 /**
  * Clase que representa una Piña en el juego Bad Dopo Cream.
  * Es una fruta en movimiento que se desplaza automáticamente por el tablero.
  */
public class Piña extends FrutaEnMovimiento {
    public static final int GANANCIA_PIÑA = 200;
    public Piña(int fila, int col, Celda celda) throws BadDopoException {
        super(fila, col, GANANCIA_PIÑA, celda);
    }

    @Override
    public void mover(Nivel nivel) throws BadDopoException {

        int nuevaFila = getFila();
        int nuevaCol = getColumna();
        switch (direccionActual) {
            case ARRIBA     -> nuevaFila--;
            case ABAJO      -> nuevaFila++;
            case IZQUIERDA  -> nuevaCol--;
            case DERECHA    -> nuevaCol++;
        }

        if (validarMovimiento(nuevaFila, nuevaCol, nivel)) {
            setPosicion(nuevaFila, nuevaCol);
        } else {
            // Cambiar dirección si choca
            cambiarDireccion();
        }
    }

    private void cambiarDireccion() {
        switch (direccionActual) {
            case ARRIBA     -> direccionActual = Direccion.ABAJO;
            case ABAJO      -> direccionActual = Direccion.ARRIBA;
            case IZQUIERDA  -> direccionActual = Direccion.DERECHA;
            case DERECHA    -> direccionActual = Direccion.IZQUIERDA;
        }
    }
    public void moverSiCorresponde(Nivel nivel) throws BadDopoException {
        mover(nivel);
    }

}
