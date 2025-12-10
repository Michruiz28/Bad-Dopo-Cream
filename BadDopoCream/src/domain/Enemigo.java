package domain;

public abstract class Enemigo extends Elemento {

    protected int velocidad;
    protected String ultimaDireccion;
    protected TipoComportamiento comportamiento;
    protected boolean persigueJugador;
    protected boolean puedeRomperBloques;
    protected boolean rompeUnBloquePorVez;

    public Enemigo(int fila, int col, int velocidad, TipoComportamiento comportamiento) {
        super(fila, col);
        this.velocidad = velocidad;
        this.comportamiento = comportamiento;
        this.ultimaDireccion = "ARRIBA";
        this.persigueJugador = false;
        this.puedeRomperBloques = false;
        this.rompeUnBloquePorVez = false;
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
    }
    public boolean isPersigueJugador() { return persigueJugador; }
    public boolean canRomperBloques() { return puedeRomperBloques; }
    public boolean rompeUnBloquePorVez() { return rompeUnBloquePorVez; }

    public int getVelocidad() { return velocidad; }
    public String getUltimaDireccion() { return ultimaDireccion; }
    protected void setUltimaDireccion(String dir) { this.ultimaDireccion = dir; }

    /**
     * Método de delegación polimórfica: cada subclase implementa su estrategia de movimiento.
     * GrafoTablero llama este método para orquestar el movimiento
     * @param vista referencia a VistaTablero para acceder a información de solo lectura
     * @param jugador referencia al jugador para cálculos de persecución/alineación
     */
    public abstract String decidirProximaMovida(VistaTablero vista, Helado jugador) throws BadDopoException;
}
