package domain;
// Tablero.java
public class Tablero {
    private final int filas;
    private final int columnas;
    private final Celda[][] celdas;

    /**
     * Constructor
     */
    public Tablero(int filas, int columnas, boolean autoBordes) {
        this.filas = filas;
        this.columnas = columnas;
        this.celdas = new Celda[filas][columnas];

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                TipoCelda tipo = TipoCelda.VACIA;
                if (autoBordes && (f == 0 || f == filas - 1 || c == 0 || c == columnas - 1)) {
                    tipo = TipoCelda.BORDE;
                }
                celdas[f][c] = new Celda(f, c, tipo);
            }
        }
    }

    public int getFilas() { return filas; }
    public int getColumnas() { return columnas; }

    public Celda getCelda(int fila, int col) {
        if (fila < 0 || fila >= filas || col < 0 || col >= columnas) return null;
        return celdas[fila][col];
    }

    public void setCelda(int fila, int col, TipoCelda tipo) {
        if (fila < 0 || fila >= filas || col < 0 || col >= columnas) return;
        celdas[fila][col].setTipo(tipo);
    }

    /**
     * Intentar romper un bloque de hielo en (fila,col).
     * Retorna true si se rompió (y la celda pasó a VACIA).
     * No permite romper BORDE ni VACIA.
     */
    public boolean romperHielo(int fila, int col) {
        Celda cel = getCelda(fila, col);
        if (cel == null) return false;
        if (cel.esRompible()) {
            cel.setTipo(TipoCelda.VACIA);
            return true;
        }
        return false;
    }
}
