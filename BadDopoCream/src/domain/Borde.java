package domain;

public class Borde extends Obstaculo{
    private static final String imagen = "src/presentation/images/Borde.png";

    public Borde(int fila, int col) throws BadDopoException {
        super(fila, col);

    }

    @Override
    public void mover(String direccion) throws BadDopoException {
        //
    }

    @Override
    public void aumentarPuntaje(int puntaje) {
        //
    }

    @Override
    public int getGanancia() {
        return 0;
    }

    @Override
    public void actualizarImagen(String ultimaDireccion) {
        //
    }

    @Override
    public void romperHielo(Celda celdaARomper, CreadorElemento creador) throws BadDopoException {
        //
    }

    @Override
    public void crearHielo(Celda celdaACrear, CreadorElemento creador) throws BadDopoException {
        //
    }
}
