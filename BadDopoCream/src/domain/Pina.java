 
package domain;
 
 /**
  * Clase que representa una Pina en el juego Bad Dopo Cream.
  * Es una fruta en movimiento que se desplaza automáticamente por el tablero.
  */
public class Pina extends FrutaEnMovimiento {
    public static final int GANANCIA_PINA = 200;
   private static final String imagen = "src/presentation/images/Piña.png";
    public Pina(int fila, int columna) throws BadDopoException {
        super(fila, columna);
    }

     @Override
     public void mover(String direccion) throws BadDopoException {
        if (direccion == null) throw new BadDopoException(BadDopoException.DIRECCION_INVALIDA);
        int f = getFila();
        int c = getColumna();
        switch (direccion) {
            case "ARRIBA": setFila(f - 1); break;
            case "ABAJO": setFila(f + 1); break;
            case "DERECHA": setColumna(c + 1); break;
            case "IZQUIERDA": setColumna(c - 1); break;
            default: throw new BadDopoException(BadDopoException.DIRECCION_INVALIDA);
        }
        actualizarImagen(direccion);
     }

     @Override
     public void aumentarPuntaje(int puntaje) {
        // Las frutas no aumentan puntaje por sí mismas; implementación vacía.
     }

     @Override
     public int getGanancia() {
          return GANANCIA_PINA;
     }

     @Override
     public void actualizarImagen(String ultimaDireccion) {
      // Actualizar imagen según dirección; implementación por defecto no necesita más
     }

     @Override
     public void romperHielo(Celda celdaARomper, CreadorElemento creador) throws BadDopoException {
        // La Piña no rompe hielo por comportamiento por defecto
     }

     @Override
     public void crearHielo(Celda celdaACrear, CreadorElemento creador) throws BadDopoException {
        // La Piña no crea hielo
     }

     @Override
     public int[] calcularPosicionesMovimieto(int limiteInferior, int limiteSuperior) {
          return new int[]{getFila(), getColumna()};
     }

     @Override
     public void moverConPosicion(int filaNueva, int columnaNueva) {
        setFila(filaNueva);
        setColumna(columnaNueva);
     }

     @Override
    public void actualizar(long tiempoActual) throws BadDopoException{
        // Por defecto no se mueve automáticamente; el movimiento debe ser disparado por
        // la lógica que detecta movimiento del Helado (GrafoTablero/Controlador).
    }

     public void mover() {
     }
 }
