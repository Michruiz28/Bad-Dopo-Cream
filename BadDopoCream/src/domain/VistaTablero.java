package domain;

/**
 * Interfaz vista tablero
 */
public interface VistaTablero {
    boolean esTransitable(int fila, int columna);


    boolean esHielo(int fila, int columna);

    String calcularDireccionHaciaObjetivo(int filaActual, int columnaActual, 
                                          int filaObjetivo, int columnaObjetivo, 
                                          boolean permitirHielo);

    int[] calcularNuevaPosicion(int fila, int columna, String direccion) throws BadDopoException;

    java.util.List<String> obtenerDireccionesValidas(int fila, int columna);
  
    boolean esPosicionValida(int fila, int columna);
}
