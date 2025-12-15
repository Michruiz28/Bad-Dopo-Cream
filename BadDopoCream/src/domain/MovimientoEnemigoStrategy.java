package domain;

/**
 * Interfaz para mover enemigo con estrategia
 */
public interface MovimientoEnemigoStrategy {
    void ejecutarTurno(Enemigo enemigo, VistaTablero vista, Helado jugador, GrafoTablero grafo) throws BadDopoException;
}
