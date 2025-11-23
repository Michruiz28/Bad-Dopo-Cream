

// GrafoTablero.java
public class GrafoTablero {
    private Nodo[][] nodos;
    private final Tablero tablero;

    public GrafoTablero(Tablero tablero) {
        this.tablero = tablero;
        construirGrafo();
    }

    /**
     * Construye/reconstruye todo el grafo con las celdas VACIA actuales.
     * (Simple y seguro; para mapas pequeños/medianos esto está bien).
     */
    public final void construirGrafo() {
        int filas = tablero.getFilas();
        int columnas = tablero.getColumnas();
        nodos = new Nodo[filas][columnas];

        // Crear nodos para celdas transitables (VACIA)
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                Celda cel = (Celda) tablero.getCelda(f, c);
                if (cel != null && cel.esTransitable()) {
                    nodos[f][c] = new Nodo(cel);
                }
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
            if (nf >= 0 && nf < tablero.getFilas() && nc >= 0 && nc < tablero.getColumnas()) {
                if (nodos[nf][nc] != null) {
                    nodos[f][c].agregarVecino(nodos[nf][nc]);
                }
            }
        }
    }

    public Nodo getNodo(int fila, int col) {
        if (fila < 0 || fila >= tablero.getFilas() || col < 0 || col >= tablero.getColumnas()) return null;
        return nodos[fila][col];
    }

    /**
     * Reconstruye el grafo por completo (útil luego de romper hielo).
     */
    public void reconstruir() {
        construirGrafo();
    }

    /**
     * Chequea si se puede mover desde (f1,c1) a (f2,c2) considerando el grafo.
     */
    public boolean puedeMover(int f1, int c1, int f2, int c2) {
        Nodo desde = getNodo(f1, c1);
        Nodo hasta = getNodo(f2, c2);
        if (desde == null || hasta == null) return false;
        return desde.getVecinos().contains(hasta);
    }
}
