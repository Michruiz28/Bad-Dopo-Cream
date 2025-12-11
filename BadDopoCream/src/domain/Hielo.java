package domain;

/**
 * Hielo: puede ser roto por ciertos enemigos
 */
public class Hielo extends Obstaculo {

    public Hielo(int fila, int col) throws BadDopoException {
        super(fila, col);
    }

    @Override
    public void mover(String direccion) throws BadDopoException {

    }

    @Override
    public void aumentarPuntaje(int puntaje) {

    }

    @Override
    public int getGanancia() {
        return 0;
    }

    @Override
    public void actualizarImagen(String ultimaDireccion) {

    }

    @Override
    public void romperHielo(Celda celdaARomper, CreadorElemento creador) throws BadDopoException {

    }

    @Override
    public void crearHielo(Celda celdaACrear, CreadorElemento creador) throws BadDopoException {

    }

    @Override
    public int[] calcularPosicionesMovimieto(int limiteInferior, int limiteSuperior) {
        return new int[0];
    }

    @Override
    public boolean esTransitable() {
        return false;
    }

    @Override
    public int[] calcularPosicionesMovimieto() {
        return new int[0];
    }

    @Override
    public boolean esRompible() {
        return false;
    }
}
