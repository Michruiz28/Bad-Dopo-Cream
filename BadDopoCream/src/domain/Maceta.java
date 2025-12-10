package domain;

public class Maceta extends Enemigo  {

    public Maceta(int fila, int col) {
        super(fila, col, 1, TipoComportamiento.PERSEGUIDOR);
        this.persigueJugador = true;
        this.puedeRomperBloques = false;
        this.rompeUnBloquePorVez = false;
    }

    /**
     * Maceta persigue al jugador usando BFS.
     * No puede romper bloques.
     */
    @Override
    public String decidirProximaMovida(VistaTablero vista, Helado jugador) throws BadDopoException {
        String direccion = vista.calcularDireccionHaciaObjetivo(
            getFila(), getColumna(),
            jugador.getFila(), jugador.getColumna(),
            false // no permite pasar por hielo
        );
        
        if (direccion != null) {
            setUltimaDireccion(direccion);
            return direccion;
        }
        
        // Si no hay camino, intenta movimiento aleatorio
        java.util.List<String> validas = vista.obtenerDireccionesValidas(getFila(), getColumna());
        if (!validas.isEmpty()) {
            String aleatoria = validas.get(new java.util.Random().nextInt(validas.size()));
            setUltimaDireccion(aleatoria);
            return aleatoria;
        }
        
        // No puede moverse
        return getUltimaDireccion();
    }
}
