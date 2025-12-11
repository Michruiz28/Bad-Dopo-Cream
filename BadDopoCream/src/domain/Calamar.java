package domain;

public class Calamar extends Enemigo {

    public Calamar(int fila, int col) {
        super(fila, col, 1, TipoComportamiento.ROMPEHIELO);
        this.ultimaDireccion = "ARRIBA";
        this.persigueJugador = true;
        this.puedeRomperBloques = true;
        this.rompeUnBloquePorVez = true;
        // Imagenes para Calamar
        this.imagenAbajo = "src/presentation/images/CalamarAbajo.png";
        this.imagenDerecha = "src/presentation/images/CalamarDerecha.png";
        this.imagenIzquierda = "src/presentation/images/CalamarIzquierda.png";
        this.imagenArriba = "src/presentation/images/CalamarArriba.png";
        this.imagenActual = this.imagenAbajo;
    }

    /**
     * Calamar persigue al jugador.
     * Si el siguiente casillero es hielo, lo rompe y se detiene.
     */
    @Override
    public String decidirProximaMovida(VistaTablero vista, Helado jugador) throws BadDopoException {
        String direccion = vista.calcularDireccionHaciaObjetivo(
            getFila(), getColumna(),
            jugador.getFila(), jugador.getColumna(),
            true
        );
        if (direccion == null) {
            return getUltimaDireccion();
        }
        // Verificar si el siguiente paso es hielo
        int[] siguiente = vista.calcularNuevaPosicion(getFila(), getColumna(), direccion);
        if (vista.esHielo(siguiente[0], siguiente[1])) {
            setUltimaDireccion(direccion);
            return direccion; 
        }
        setUltimaDireccion(direccion);
        return direccion;
    }
}
