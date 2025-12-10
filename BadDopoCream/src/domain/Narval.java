package domain;

public class Narval extends Enemigo {

    public Narval(int fila, int col) {
        super(fila, col, 1, TipoComportamiento.PERSEGUIDOR);
        // Narval no persigue directo, embiste cuando está alineado.
        this.persigueJugador = false;
        // Durante embestida rompe hielo en el camino
        this.puedeRomperBloques = true;
        this.rompeUnBloquePorVez = false;
    }

    /**
     * Narval embiste si está alineado horizontal o verticalmente con el jugador.
     * En caso contrario, movimiento aleatorio.
     * Durante embestida rompe hielo en el camino.
     */
    @Override
    public String decidirProximaMovida(VistaTablero vista, Helado jugador) throws BadDopoException {
        int fj = jugador.getFila();
        int cj = jugador.getColumna();
        int f = getFila();
        int c = getColumna();

        // ¿Alineado horizontal o verticalmente?
        if (f == fj || c == cj) {
            // Determinar dirección de embestida
            String direccion = null;
            if (f == fj) {
                direccion = (c < cj) ? "DERECHA" : "IZQUIERDA";
            } else {
                direccion = (f < fj) ? "ABAJO" : "ARRIBA";
            }
            
            // La embestida la ejecuta GrafoTablero detectando alineación
            setUltimaDireccion(direccion);
            return direccion; // marca embestida
        }

        // Si no está alineado, movimiento aleatorio
        java.util.List<String> validas = vista.obtenerDireccionesValidas(f, c);
        if (!validas.isEmpty()) {
            String aleatoria = validas.get(new java.util.Random().nextInt(validas.size()));
            setUltimaDireccion(aleatoria);
            return aleatoria;
        }

        return getUltimaDireccion();
    }
}
