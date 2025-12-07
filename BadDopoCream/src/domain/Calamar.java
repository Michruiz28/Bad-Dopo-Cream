 
package domain;
public class Calamar extends Enemigo implements Mover {

    public Calamar(int fila, int col) {
        super(fila, col, 1, TipoComportamiento.ROMPEHIELO);
        this.ultimaDireccion = "ARRIBA";
    }
    @Override
    public String decidirMovimiento(Tablero tablero, Helado jugador) throws BadDopoException {
        int filaActual = getFila();
        int colActual = getColumna();
        String direccion = ultimaDireccion;
        int nf = filaActual;
        int nc = colActual;
        // Calculamos destino según la última dirección
        if (direccion.equals("ARRIBA")) nf--;
        else nf++;
        Nodo destino = tablero.getNodo(nf, nc);
        // Si el movimiento NO es posible cambiamos dirección
        if (destino == null || !destino.getCelda().esTransitableParaEnemigos()) {
            // Cambiamos dirección
            direccion = direccion.equals("ARRIBA") ? "ABAJO" : "ARRIBA";
            // Recalculamos destino
            nf = filaActual + (direccion.equals("ARRIBA") ? -1 : 1);
            destino = tablero.getNodo(nf, nc);
            // Si aún así no puede moverse, se queda en su casilla
            if (destino == null || !destino.getCelda().esTransitableParaEnemigos()) {
                return ultimaDireccion; // se queda con la misma que tenía
            }
        }
        Celda celdaDestino = destino.getCelda();
        // ROMPEHIELO: si el destino tiene hielo → romper
        if (celdaDestino.esHielo()) {
            celdaDestino.romper();
        }
        ultimaDireccion = direccion;
        return direccion;
    }
}
