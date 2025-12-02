 
package domain;
public class Calamar extends Enemigo implements Mover {

    public Calamar(int fila, int columna, Tablero tablero) {
        super(fila, columna, 12, TipoComportamiento.ROMPEHIELO, tablero);
        this.ultimaDireccion = "ARRIBA";
    }

    @Override
    public void realizarMovimiento(Nivel nivel) throws BadDopoException {

        Direccion direccion = buscarHelado(nivel);
        this.ultimaDireccion = direccion.name();

        if (hayHielo(direccion)) {
            romperHielo();
            return;
        }

        mover(direccion);
    }

    /**
     * Busca el helado más cercano y retorna la dirección hacia él.
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

    private boolean hayHielo(Direccion direccion) {
        int f = fila;
        int c = columna;
        return switch (direccion) {
            case ARRIBA    -> !tablero.getCelda(f - 1, c).esTransitable();
            case ABAJO     -> !tablero.getCelda(f + 1, c).esTransitable();
            case IZQUIERDA -> !tablero.getCelda(f, c - 1).esTransitable();
            case DERECHA   -> !tablero.getCelda(f, c + 1).esTransitable();
            default          -> false;
        };
    }

    @Override
    public void romperHielo() {
        int f = fila;
        int c = columna;
        Direccion direccion = Direccion.valueOf(ultimaDireccion);
        switch (ultimaDireccion) {
            case "ARRIBA"    -> tablero.romperHielo(f - 1, c);
            case "ABAJO"     -> tablero.romperHielo(f + 1, c);
            case "IZQUIERDA" -> tablero.romperHielo(f, c - 1);
            case "DERECHA"   -> tablero.romperHielo(f, c + 1);
        }
    }
}
