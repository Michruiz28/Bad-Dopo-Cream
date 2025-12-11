 
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
     public void mover(String direccion) throws BadDopoException {

     }

     @Override
     public void aumentarPuntaje(int puntaje) {

     }

     @Override
     public int getGanancia() {
         return 0;
     }

     @Override
     public void actualizarImagen(String ultimaDireccion) {

     }

     @Override
     public void romperHielo(Celda celdaARomper, CreadorElemento creador) throws BadDopoException {

     }

     @Override
     public void crearHielo(Celda celdaACrear, CreadorElemento creador) throws BadDopoException {

     }

     @Override
     public int[] calcularPosicionesMovimieto(int limiteInferior, int limiteSuperior) {
         return new int[0];
     }

     @Override
     public void moverConPosicion(int filaNueva, int columnaNueva) {

     }

     @Override
    public void actualizar(long tiempoActual) throws BadDopoException{
        if (direccionActual.equals("DERECHA"))
            mover("IZQUIERDA");
        else
            mover("DERECHA");
    }

}
