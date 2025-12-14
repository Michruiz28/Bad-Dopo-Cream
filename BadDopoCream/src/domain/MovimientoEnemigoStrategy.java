package domain;

public interface MovimientoEnemigoStrategy {
    /**
     * Ejecuta el comportamiento completo del enemigo para el turno.
     * Puede solicitar movimientos al grafo y realizar acciones (romper/crear hielo).
     */
    void ejecutarTurno(Enemigo enemigo, VistaTablero vista, Helado jugador, GrafoTablero grafo) throws BadDopoException;
}
