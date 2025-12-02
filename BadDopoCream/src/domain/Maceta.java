 
package domain;
public class Maceta extends Enemigo implements Mover {

    public Maceta(int fila, int columna, Tablero tablero) {
        super(fila, columna, 8, TipoComportamiento.PERSEGUIDOR, tablero);
        this.ultimaDireccion = "ABAJO";
    }

    /**
     * Persigue al helado más cercano.
     */
    @Override
    public void realizarMovimiento(Nivel nivel) throws BadDopoException {
        Direccion direccion = buscarHelado(nivel);
        this.ultimaDireccion = direccion.name();
        mover(direccion);
    }

    /**
     * Retorna la dirección hacia el helado más cercano.
     */
    private Direccion buscarHelado(Nivel nivel) {
        if (nivel.getJugadores().isEmpty()) {
            return Direccion.valueOf(ultimaDireccion);
        }
        Jugador j = nivel.getJugadores().get(0);
        Helado h = j.getHelado();
        int hf = h.getFila();
        int hc = h.getColumna();
        if (hf < fila) return Direccion.ARRIBA;
        if (hf > fila) return Direccion.ABAJO;
        if (hc < columna) return Direccion.IZQUIERDA;
        return Direccion.DERECHA;
    }

    @Override
    public void romperHielo() {
        // Maceta NO rompe hielo
    }
}
