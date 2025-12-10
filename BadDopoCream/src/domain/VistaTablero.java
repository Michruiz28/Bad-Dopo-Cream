package domain;

/**
 * Interfaz que proporciona una vista de solo lectura del tablero para que los enemigos
 * puedan tomar decisiones sin acceso a la manipulación interna del grafo.
 * 
 * Esta interfaz define que los enemigos pueden consultar estado, pero no modificar.
 * GrafoTablero es responsable de ejecutar los movimientos.
 * 
 */
public interface VistaTablero {
    
    /**
     * Verifica si una posición es transitable (no es borde ni tiene obstáculo sólido).
     */
    boolean esTransitable(int fila, int columna);
    
    /**
     * Verifica si una posición contiene un bloque de hielo.
     */
    boolean esHielo(int fila, int columna);
    
    /**
     * Calcula la dirección del primer paso hacia un objetivo usando BFS.
     * 
     * @param filaActual fila del buscador
     * @param columnaActual columna del buscador
     * @param filaObjetivo fila del destino
     * @param columnaObjetivo columna del destino
     * @param permitirHielo si true, considera hielo transitable para planificación
     * @return "ARRIBA", "ABAJO", "IZQUIERDA", "DERECHA", o null si no hay camino
     */
    String calcularDireccionHaciaObjetivo(int filaActual, int columnaActual, 
                                          int filaObjetivo, int columnaObjetivo, 
                                          boolean permitirHielo);
    
    /**
     * Calcula la nueva posición después de moverse en una dirección.
     * No verifica validez; solo realiza el cálculo.
     * 
     * @return array [nuevaFila, nuevaColumna]
     */
    int[] calcularNuevaPosicion(int fila, int columna, String direccion) throws BadDopoException;
    
    /**
     * Obtiene todas las direcciones válidas desde una posición (donde se puede transitar).
     * 
     * @return Lista de strings: "ARRIBA", "ABAJO", "IZQUIERDA", "DERECHA"
     */
    java.util.List<String> obtenerDireccionesValidas(int fila, int columna);
    
    /**
     * Verifica si una posición es válida (dentro de los límites del tablero).
     */
    boolean esPosicionValida(int fila, int columna);
}
