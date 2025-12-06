package domain;

// GrafoTablero.java
public class GrafoTablero {
    private Nodo[][] nodos;
    private int filas;
    private int columnas;
    private CreadorElemento creador;

    public GrafoTablero(int filas, int columnas, String[][] infoNivel, CreadorElemento creador) throws BadDopoException {
        this.filas = filas;
        this.columnas = columnas;
        this.creador = creador;
        construirGrafo(infoNivel);
    }

    /**
     * Construye/reconstruye todo el grafo con las celdas VACIA actuales.
     * (Simple y seguro; para mapas pequeños/medianos esto está bien).
     */
    public final void construirGrafo(String[][] infoNivel) throws BadDopoException {
        nodos = new Nodo[filas][columnas];
        String tipo;

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                tipo = infoNivel[f][c];
                // El Nodo recibe el creador y la info para que construya su Celda
                nodos[f][c] = new Nodo(f, c, tipo, creador);
            }
        }

        // Conectar nodos adyacentes (4 direcciones)
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                if (nodos[f][c] != null) conectarVecinos(f, c);
            }
        }
    }

    private void conectarVecinos(int f, int c) {
        int[][] dirs = { {1,0},{-1,0},{0,1},{0,-1} };
        for (int[] d : dirs) {
            int nf = f + d[0], nc = c + d[1];
            if (nf >= 0 && nf < nodos.length && nc >= 0 && nc < nodos[0].length) {
                if (nodos[nf][nc] != null) {
                    nodos[f][c].agregarVecino(nodos[nf][nc]);
                }
            }
        }
    }

    public Nodo getNodo(int fila, int col) {
        if (fila < 0 || fila >= filas || col < 0 || col >= columnas) return null;
        return nodos[fila][col];
    }

    public void setNodo(int fila, int col, String tipo) throws BadDopoException {
        nodos[fila][col] = new Nodo(fila, col, tipo, creador);
    }
    /**
     * Reconstruye el grafo por completo.
     */
    public void reconstruir() {
        construirGrafo();
    }

    private void construirGrafo() {
    }

    /**
     * Revisa movimiento según la estructura del grafo
     */
    //public boolean puedeMover(int f1, int c1, int f2, int c2) {
    //    Nodo desde = getNodo(f1, c1);
    //    Nodo hasta = getNodo(f2, c2);
    //    if (desde == null || hasta == null) return false;
    //    return desde.getVecinos().contains(hasta);
    //}
}
