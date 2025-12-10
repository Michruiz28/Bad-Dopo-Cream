package domain;

import java.util.Random;

public class Cereza extends FrutaEnMovimiento {
    public static final int GANANCIA_CEREZA = 150;
    private long ultimoTeleport = 0;
    private static final int COOLDOWN = 20000;
    private Random random = new Random();

    public Cereza(int fila, int columna) throws BadDopoException {
        super(fila, columna, GANANCIA_CEREZA);
    }
    @Override
    public void actualizar(long tiempoActual) throws BadDopoException{
        if (tiempoActual - ultimoTeleport >= COOLDOWN) {
            int nuevaFila = random.nextInt(18);
            int nuevaColumna = random.nextInt(18);
            setFila(nuevaFila);
            setColumna(nuevaColumna);
            ultimoTeleport = tiempoActual;
        }
    }

}
