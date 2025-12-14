package domain;

public abstract class Enemigo extends Elemento {

    protected int velocidad;
    protected String ultimaDireccion;
    protected boolean persigueJugador;
    protected boolean puedeRomperBloques;
    protected boolean rompeUnBloquePorVez;
    protected MovimientoEnemigoStrategy movimientoStrategy;

    public Enemigo(int fila, int col, int velocidad) {
        super(fila, col);
        this.velocidad = velocidad;
        this.ultimaDireccion = "ARRIBA";
        this.persigueJugador = false;
        this.puedeRomperBloques = false;
        this.rompeUnBloquePorVez = false;
    }

    protected void setMovimientoStrategy(MovimientoEnemigoStrategy estrategia) {
        this.movimientoStrategy = estrategia;
    }
    public static boolean esSolidoEstatico() {
        return true;
    }

    @Override
    public boolean esSolido() {
        return true;
    }

    @Override
    public void mover(String direccion) throws BadDopoException {
        if (direccion == null || direccion.isEmpty()) {
            throw new BadDopoException(BadDopoException.DIRECCION_INVALIDA);
        }
        this.ultimaDireccion = direccion;
        // Actualizar la imagen según la dirección tras el movimiento
        try {
            actualizarImagen(direccion);
        } catch (Exception e) {
            // Si la subclase no implementa la actualización correctamente, ignorar
        }
    }
    public boolean isPersigueJugador() { return persigueJugador; }
    public boolean canRomperBloques() { return puedeRomperBloques; }
    public boolean rompeUnBloquePorVez() { return rompeUnBloquePorVez; }

    public int getVelocidad() { return velocidad; }
    public String getUltimaDireccion() { return ultimaDireccion; }
    protected void setUltimaDireccion(String dir) { this.ultimaDireccion = dir; }

    /**
     * Ejecuta el comportamiento/turno del enemigo delegando a la estrategia.
     * {@link GrafoTablero} invoca este método para que el enemigo realice su acción.
     */
    public void ejecutarComportamiento(GrafoTablero grafo, VistaTablero vista, Helado jugador) throws BadDopoException {
        if (movimientoStrategy != null) {
            movimientoStrategy.ejecutarTurno(this, vista, jugador, grafo);
        }
    }
}
