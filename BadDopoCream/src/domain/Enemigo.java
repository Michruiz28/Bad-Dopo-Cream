package domain;

/**
 * Representa un enemigo del tablero.
 * <p>
 * Los enemigos son elementos sólidos del juego que poseen una velocidad,
 * una última dirección conocida y una estrategia de movimiento opcional.
 * Subclases concretas implementan la representación gráfica y el comportamiento
 * específico (p. ej. `Troll`, `Calamar`, `Maceta`).
 */
public abstract class Enemigo extends Elemento {

    protected int velocidad;
    protected String ultimaDireccion;
    protected boolean persigueJugador;
    protected boolean puedeRomperBloques;
    protected boolean rompeUnBloquePorVez;
    protected MovimientoEnemigoStrategy estrategiaMovimiento;

    public Enemigo(int fila, int col, int velocidad) {
        super(fila, col);
        this.velocidad = velocidad;
        this.ultimaDireccion = "ARRIBA";
        this.persigueJugador = false;
        this.puedeRomperBloques = false;
        this.rompeUnBloquePorVez = false;
    }

    protected void setMovimientoStrategy(MovimientoEnemigoStrategy estrategia) {
        this.estrategiaMovimiento = estrategia;
    }

    public static boolean esSolidoEstatico() {
        return true;
    }

    @Override
    public boolean esSolido() {
        return true;
    }

    /**
     * Mueve (lógica interna) el enemigo en la dirección indicada.
     * Actualiza la última dirección y delega la actualización de la imagen
     *
     * @param direccion dirección de movimiento (p. ej. "ARRIBA", "DERECHA")
     * @throws BadDopoException si la dirección es nula o vacía
     */
    @Override
    public void mover(String direccion) throws BadDopoException {
        if (direccion == null || direccion.isEmpty()) {
            throw new BadDopoException(BadDopoException.DIRECCION_INVALIDA);
        }
        this.ultimaDireccion = direccion;
        try {
            actualizarImagen(direccion);
        } catch (Exception e) {
        }
    }
    public boolean isPersigueJugador() { return persigueJugador; }

    public boolean canRomperBloques() { return puedeRomperBloques; }

    public boolean rompeUnBloquePorVez() { return rompeUnBloquePorVez; }


    public int getVelocidad() { return velocidad; }

    public String getUltimaDireccion() { return ultimaDireccion; }

    protected void setUltimaDireccion(String dir) { this.ultimaDireccion = dir; }

    /**
     * Ejecuta el comportamiento de movimiento del enemigo para el turno actual.
     * Si se ha configurado un MovimientoEnemigoStrategy, delega en ella
     * la ejecución del turno; en caso contrario no hace nada.
     *
     * @param grafo  grafo del tablero usado por las estrategias
     * @param vista  vista del tablero (para interacción visual o consulta)
     * @param jugador referencia al jugador
     * @throws BadDopoException si la estrategia lanza una excepción de juego
     */
    public void ejecutarComportamiento(GrafoTablero grafo, VistaTablero vista, Helado jugador) throws BadDopoException {
        if (estrategiaMovimiento != null) {
            estrategiaMovimiento.ejecutarTurno(this, vista, jugador, grafo);
        }
    }

    @Override
    public boolean esEnemigo() { return true; }

    /**
     * Código de tipo que identifica a los enemigos en el tablero.
     *
     * @return cadena con el código de tipo ("V")
     */
    @Override
    public String codigoTipo() { return "V"; }
}
