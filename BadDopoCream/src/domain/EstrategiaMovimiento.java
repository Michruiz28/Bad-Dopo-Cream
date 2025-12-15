package domain;

/**
 * Patrón de diseño de comportamiento para EstrategiaMovimiento
 */
public interface EstrategiaMovimiento {
    /**
     * Calcula la próxima dirección que debe tomar el helado.
     * @param helado Helado a mover
     * @param nivel Nivel actual del juego
     * @return Dirección que debe tomar
     * @throws BadDopoException Si la estrategia falla o no tiene suficientes datos
     */
    Direccion calcularMovimiento(Helado helado, int nivel) throws BadDopoException;
}
