package domain;

import java.util.Random;

public class Cereza extends FrutaEnMovimiento {
    public static final int GANANCIA_CEREZA = 150;
    private long ultimoTeletransporte;
    private Random random = new Random();

    public Cereza(int fila, int col, Celda celda) throws BadDopoException {
        super(fila, col, GANANCIA_CEREZA, celda);
        this.ultimoTeletransporte = System.currentTimeMillis();
    }
    @Override
    public void mover(Nivel nivel) throws BadDopoException {}

    /**
     * Verifica si ya pasaron 20 segundos y teletransporta la cereza.
     */
    public void actualizar(Nivel nivel) throws BadDopoException {
        long ahora = System.currentTimeMillis();
        if (ahora - ultimoTeletransporte < 20_000) return;
        Tablero tablero = nivel.getTablero();
        int nuevaFila, nuevaCol;
        do {
            nuevaFila = random.nextInt(tablero.getFilas());
            nuevaCol = random.nextInt(tablero.getColumnas());
        } while (!celdaValida(tablero, nuevaFila, nuevaCol));
        setPosicion(nuevaFila, nuevaCol);
        this.ultimoTeletransporte = ahora;
    }

    private boolean celdaValida(Tablero tablero, int fila, int col) {
        Celda celda = tablero.getCelda(fila, col);
        return celda != null && celda.esTransitable();
    }
    
    public void actualizarTeleportacion(long ahora, Nivel nivel) throws BadDopoException {
        if (ahora - ultimoTeletransporte < 20_000) return;
        Tablero tablero = nivel.getTablero();
        int nuevaFila, nuevaCol;
        do {
            nuevaFila = random.nextInt(tablero.getFilas());
            nuevaCol = random.nextInt(tablero.getColumnas());
        } while (!celdaValida(tablero, nuevaFila, nuevaCol));
        setPosicion(nuevaFila, nuevaCol);
        ultimoTeletransporte = ahora;
    }

}
