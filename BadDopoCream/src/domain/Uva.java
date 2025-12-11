package domain; 

/**
 * Clase que representa Uvas en el juego Bad Dopo Cream
 * Las uvas son frutas estáticas que permanecen en su posición hasta ser recolectadas
 */
public class Uva extends FrutaEstatica {
    public static final int GANANCIA_UVA = 50;
    
    public Uva(int fila, int col) throws BadDopoException {
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
        return GANANCIA_UVA;
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
}