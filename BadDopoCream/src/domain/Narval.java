package domain;
public class Narval extends Enemigo {

    public Narval(int fila, int columna, Tablero tablero) {
        super(fila, columna, 14, TipoComportamiento.EMBESTIDA, tablero);
        this.ultimaDireccion = "IZQUIERDA";
    }

    @Override
    public void realizarMovimiento(Nivel nivel) throws BadDopoException {
        Direccion dir = estaAlineado(nivel);
        if (dir != null) {
            embestida(dir);
            return;
        }
        mover(Direccion.valueOf(ultimaDireccion));
    }

    /**
     * Determina si algún helado está alineado en fila o columna.
     */
    private Direccion estaAlineado(Nivel nivel) {
        if (nivel.getJugadores().isEmpty()) return null;
        for (Jugador j : nivel.getJugadores()) {
            Helado h = j.getHelado();
            if (h.getFila() == this.fila) {
                return (h.getColumna() < this.columna) ?
                        Direccion.IZQUIERDA : Direccion.DERECHA;
            }

            if (h.getColumna() == this.columna) {
                return (h.getFila() < this.fila) ?
                        Direccion.ARRIBA : Direccion.ABAJO;
            }
        }
        return null;
    }

    /**
     * La embestida avanza 3 casillas en la misma dirección,
     * rompiendo hielo en cada paso.
     */
    public void embestida(Direccion direccion) throws BadDopoException {
        this.ultimaDireccion = direccion.name();
        for (int i = 0; i < 3; i++) {
            romperHielo(); 
            mover(direccion); 
        }
    }

    @Override
    public void romperHielo() {
        int f = fila;
        int c = columna;
        Direccion dir = Direccion.valueOf(ultimaDireccion);
        switch (dir) {
            case ARRIBA    -> tablero.romperHielo(f - 1, c);
            case ABAJO     -> tablero.romperHielo(f + 1, c);
            case IZQUIERDA -> tablero.romperHielo(f, c - 1);
            case DERECHA   -> tablero.romperHielo(f, c + 1);
        }
    }
}
