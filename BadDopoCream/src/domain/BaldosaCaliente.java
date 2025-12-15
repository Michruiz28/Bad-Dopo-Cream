package domain;
public class BaldosaCaliente extends Obstaculo {
    public BaldosaCaliente(int fila, int col) throws BadDopoException {
        super(fila, col);
    }
    @Override
    public boolean esSolido() {
        return false;
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
        return true;
    }

    @Override
    public boolean esPeligroso() {
        return true;
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
