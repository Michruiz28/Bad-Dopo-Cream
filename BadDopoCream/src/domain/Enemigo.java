package domain;

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
     * Ejecuta el comportamiento/turno del enemigo delegando a la estrategia
     */
    public void ejecutarComportamiento(GrafoTablero grafo, VistaTablero vista, Helado jugador) throws BadDopoException {
        if (estrategiaMovimiento != null) {
            estrategiaMovimiento.ejecutarTurno(this, vista, jugador, grafo);
        }
    }

    @Override
    public boolean esEnemigo() { return true; }

    @Override
    public String codigoTipo() { return "V"; }
}
