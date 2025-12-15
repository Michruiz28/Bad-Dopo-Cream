package domain;

/**
 * Clase que define jugador en modo de juego un jugador
 */
public class UnJugador extends CreadorElemento{
    public Helado crearHelado1(int fila, int col, String sabor) throws BadDopoException {
        return new JugadorHumano(fila, col, sabor);
    }

    public Helado crearHelado2(int fila, int col, String sabor) throws BadDopoException {
        return new JugadorHumano(fila, col, sabor);
    }

}
