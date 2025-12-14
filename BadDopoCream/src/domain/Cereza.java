package domain;

import java.util.Random;

public class Cereza extends FrutaEnMovimiento {
    public static final int GANANCIA_CEREZA = 150;
    private long ultimoTeletransporte;
    private Random random = new Random();
    private int fila;
    private int columna;
    private static final String imagen = "src/presentation/images/Cereza.png";
    private static final String codigo = "CF";

    public Cereza(int fila, int col) throws BadDopoException {
        super(fila, col);
        this.ultimoTeletransporte = System.currentTimeMillis();
    }

    @Override
    public String getCodigo(){
        return codigo;
    }

    @Override
    public void actualizar(long timpoActual) throws BadDopoException {
        //
    }

    @Override
    public void aumentarPuntaje(int puntaje) {
        //
    }

    @Override
    public int getGanancia() {
        return GANANCIA_CEREZA;
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

    @Override
    public int[] calcularPosicionesMovimieto(int limiteinferior, int limitesuperior) {
        int[] nuevaPosicion = new int[2];
        Random random = new Random();
        int min = limiteinferior;
        int max = limitesuperior;

        int NuevaFila = random.nextInt(max - min + 1) + min;
        int NuevaColumna = random.nextInt(max - min + 1) + min;
        nuevaPosicion[0] = NuevaFila;
        nuevaPosicion[1] = NuevaColumna;

        moverConPosicion(nuevaPosicion[0], nuevaPosicion[1]);
        return nuevaPosicion;
    }

    @Override
    public void mover(String direccion) throws BadDopoException {
        //
    }

    @Override
    public void moverConPosicion(int filaNueva, int columnaNueva){
        this.fila = filaNueva;
        this.columna = columnaNueva;
    }
}