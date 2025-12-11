package domain;

public abstract class Elemento implements Mover, RompeHielo {
    private int fila;
    private int columna;

    public Elemento(int fila, int columna){
        this.fila = fila;
        this.columna = columna;
    }

    public int getFila() { return fila; }
    public void setFila(int fila) { this.fila = fila; }

    public int getColumna() { return columna; }
    public void setColumna(int columna) { this.columna = columna;}

    public abstract void mover(String direccion) throws BadDopoException;

    public abstract void aumentarPuntaje(int puntaje);

    public abstract int getGanancia();

    public abstract void actualizarImagen(String ultimaDireccion);

    public abstract void romperHielo(Celda celdaARomper, CreadorElemento creador) throws BadDopoException;

    public abstract void crearHielo(Celda celdaACrear, CreadorElemento creador) throws BadDopoException;

    public abstract int[] calcularPosicionesMovimieto(int limiteInferior, int limiteSuperior);
}
