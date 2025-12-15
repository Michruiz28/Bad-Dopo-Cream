package domain;

/**
 * Interfaz poder
 */
public interface Poder {
    public void crearHielo(Celda celdaACrear, CreadorElemento creador) throws BadDopoException;
}