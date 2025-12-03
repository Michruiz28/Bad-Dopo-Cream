package domain;

public class JugadorVsJugador extends CreadorElemento {
    public Helado crearHelado1(int fila, int col, String sabor) throws BadDopoException {
        return new JugadorHumano(fila, col, sabor);
    }

    @Override
    public Helado crearHelado2(int fila, int col, String sabor) throws BadDopoException {
        return new JugadorHumano(fila, col, sabor);
    }

}
