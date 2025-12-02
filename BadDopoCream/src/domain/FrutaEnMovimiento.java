package domain;

public abstract class FrutaEnMovimiento extends Fruta {

    protected Direccion direccionActual;

    public FrutaEnMovimiento(int fila, int col, int ganancia, Celda celda)
            throws BadDopoException {
        super(fila, col, ganancia, celda);
        this.direccionActual = Direccion.ABAJO;
    }

    /**
     * Mueve la fruta una casilla según su lógica interna.
     * Debe ser implementado por Piña y Cereza.
     */
    public abstract void mover(Nivel nivel) throws BadDopoException;
    /**
     * Verifica si la fruta puede moverse a una celda.
     */
    protected boolean validarMovimiento(int nuevaFila, int nuevaCol, Nivel nivel) {
        Tablero tablero = nivel.getTablero();
        if (nuevaFila < 0 || nuevaFila >= tablero.getFilas() ||
            nuevaCol < 0 || nuevaCol >= tablero.getColumnas()) {
            return false;
        }
        Celda celdaDestino = tablero.getCelda(nuevaFila, nuevaCol);
        if (celdaDestino == null || !celdaDestino.esTransitable()) return false;
        for (Enemigo e : nivel.getEnemigos()) {
            if (e.getFila() == nuevaFila && e.getColumna() == nuevaCol) {
                return false;
            }
        }
        for (Jugador j : nivel.getJugadores()) {
            Helado h = j.getHelado();
            if (h.getFila() == nuevaFila && h.getColumna() == nuevaCol) {
                return false;
            }
        }
        return true;
    }
}
