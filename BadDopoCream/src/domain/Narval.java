package domain;

import java.util.*;

public class Narval extends Enemigo {

    public Narval(int fila, int col) {
        super(fila, col, 1, TipoComportamiento.PERSEGUIDOR);
    }

    /**
     * Narval persigue al jugador usando BFS.
     * - Si hay ruta válida → devuelve la dirección del primer paso.
     * - Si no hay ruta → intenta movimiento aleatorio.
     * - Si ni eso puede → se queda con última dirección válida.
     */
    @Override
    public String decidirMovimiento(Tablero tablero, Helado jugador) throws BadDopoException {

        int destinoFila = jugador.getFila();
        int destinoCol = jugador.getColumna();

        // BFS desde mi posición hacia el jugador
        String direccion = bfsDireccionHacia(tablero, destinoFila, destinoCol);

        if (direccion != null) {
            ultimaDireccion = direccion;
            return direccion;
        }

        // Si no hay ruta → movimiento aleatorio seguro
        String aleatoria = elegirDireccionAleatoria(tablero);
        if (aleatoria != null) {
            ultimaDireccion = aleatoria;
            return aleatoria;
        }

        // No se puede mover → se queda en su dirección actual
        return ultimaDireccion;
    }


    /**
     * BFS mínimo para obtener SOLO la primera dirección hacia el jugador.
     */
    private String bfsDireccionHacia(Tablero tablero, int objetivoFila, int objetivoCol) {

        record Estado(int fila, int col, String primerPaso) {}

        Queue<Estado> cola = new LinkedList<>();
        boolean[][] visitado = new boolean[tablero.getFilas()][tablero.getColumnas()];

        int f0 = getFila();
        int c0 = getColumna();

        // Expandimos desde la posición del Narval
        cola.add(new Estado(f0, c0, null));
        visitado[f0][c0] = true;

        int[][] dirs = { {-1,0}, {1,0}, {0,1}, {0,-1} };
        String[] nombres = { "ARRIBA", "ABAJO", "DERECHA", "IZQUIERDA" };

        while (!cola.isEmpty()) {
            Estado actual = cola.remove();

            // ¿Llegamos al jugador?
            if (actual.fila == objetivoFila && actual.col == objetivoCol) {
                return actual.primerPaso;
            }

            // Expandimos vecinos
            for (int i = 0; i < 4; i++) {
                int nf = actual.fila + dirs[i][0];
                int nc = actual.col + dirs[i][1];

                if (!tablero.esPosicionValida(nf, nc)) continue;
                if (visitado[nf][nc]) continue;

                Celda celdaDestino = tablero.getNodo(nf, nc).getCelda();

                // Narval NO entra en hielo, ni obstáculos, ni enemigos
                if (!celdaDestino.esTransitableParaEnemigos()) continue;

                visitado[nf][nc] = true;

                // Si aún estamos en la raíz, el primer paso es este
                String primer = (actual.primerPaso == null) ? nombres[i] : actual.primerPaso;

                cola.add(new Estado(nf, nc, primer));
            }
        }

        return null; // Sin camino encontrado
    }


    /**
     * Movimiento aleatorio solo a celdas válidas.
     */
    private String elegirDireccionAleatoria(Tablero tablero) {

        List<String> posibles = new ArrayList<>();

        String[] dirs = {"ARRIBA", "ABAJO", "DERECHA", "IZQUIERDA"};
        int[][] delta = { {-1,0},{1,0},{0,1},{0,-1} };

        int f = getFila();
        int c = getColumna();

        for (int i = 0; i < dirs.length; i++) {
            int nf = f + delta[i][0];
            int nc = c + delta[i][1];

            if (!tablero.esPosicionValida(nf, nc)) continue;

            Celda dest = tablero.getNodo(nf, nc).getCelda();

            if (dest.esTransitableParaEnemigos()) {
                posibles.add(dirs[i]);
            }
        }

        if (posibles.isEmpty()) return null;

        return posibles.get(new Random().nextInt(posibles.size()));
    }
}
