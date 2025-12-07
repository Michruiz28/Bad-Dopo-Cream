package domain;
public class Troll extends Enemigo  {
    
    public Troll(int fila, int col) {
        super(fila, col, 1, TipoComportamiento.LINEAL);
        this.ultimaDireccion = "ARRIBA";
    }
    /**
     * El Troll intenta moverse en la misma dirección.
     * Si adelante hay algo NO transitable, invierte la dirección.
     * No persigue a jugadores.
     */
    @Override
    public String decidirMovimiento(Tablero tablero, Helado jugador) throws BadDopoException {
        // Nodo actual
        Nodo nodoActual = tablero.getNodo(getFila(), getColumna());
        if (nodoActual == null) return ultimaDireccion;
        // Determinar nodo destino según ultimaDireccion
        int nuevaFila = getFila();
        int nuevaCol = getColumna();
        switch (ultimaDireccion) {
            case "ARRIBA"    -> nuevaFila--;
            case "ABAJO"     -> nuevaFila++;
            case "IZQUIERDA" -> nuevaCol--;
            case "DERECHA"   -> nuevaCol++;
        }
        Nodo nodoDestino = tablero.getNodo(nuevaFila, nuevaCol);
        // Caso 1: si no existe o no es transitable → invertir dirección
        if (nodoDestino == null || !nodoDestino.getCelda().esTransitable()) {
            invertirDireccion();
        }
        return ultimaDireccion;
    }
    /**
     * Cambia la dirección actual a la opuesta.
     */
    private void invertirDireccion() {
        ultimaDireccion = switch (ultimaDireccion) {
            case "ARRIBA"    -> "ABAJO";
            case "ABAJO"     -> "ARRIBA";
            case "IZQUIERDA" -> "DERECHA";
            case "DERECHA"   -> "IZQUIERDA";
            default -> ultimaDireccion;
        };
    }
}