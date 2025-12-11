package test;

import domain.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

/**
 * Pruebas unitarias para las decisiones de movimiento de los enemigos.
 * Usamos un Mock de `VistaTablero` para controlar el entorno sin depender de GrafoTablero.
 */
public class EnemigoTest {

    private Tablero tablero;
    private GrafoTablero grafo;
    private Helado jugador;

    @Before
    public void setUp() throws BadDopoException {
        tablero = new Tablero(10, 10, true);
        grafo = new GrafoTablero(tablero);
        jugador = new Helado("TestPlayer");
        jugador.setTablero(tablero);
        jugador.setGrafo(grafo);
        // posición por defecto del jugador se establecerá en cada prueba
    }

    /**
     * Mock simple de VistaTablero basado en una grilla booleana para transitabilidad y hielo.
     */
    private static class MockVista implements VistaTablero {
        private final int filas, columnas;
        private final boolean[][] transitable;
        private final boolean[][] hielo;

        public MockVista(int filas, int columnas) {
            this.filas = filas;
            this.columnas = columnas;
            this.transitable = new boolean[filas][columnas];
            this.hielo = new boolean[filas][columnas];
            for (int i = 0; i < filas; i++) for (int j = 0; j < columnas; j++) transitable[i][j] = true;
        }

        public void setTransitable(int f, int c, boolean val) { transitable[f][c] = val; }
        public void setHielo(int f, int c, boolean val) { hielo[f][c] = val; if (val) transitable[f][c] = true; }

        @Override
        public boolean esTransitable(int fila, int columna) {
            if (!esPosicionValida(fila, columna)) return false;
            return transitable[fila][columna];
        }

        @Override
        public boolean esHielo(int fila, int columna) {
            if (!esPosicionValida(fila, columna)) return false;
            return hielo[fila][columna];
        }

        @Override
        public String calcularDireccionHaciaObjetivo(int filaActual, int columnaActual, int filaObjetivo, int columnaObjetivo, boolean permitirHielo) {
            // BFS que considera hielo como transitables solo si permitirHielo==true
            boolean[][] visited = new boolean[filas][columnas];
            int[][] prev = new int[filas * columnas][2];
            for (int i = 0; i < prev.length; i++) prev[i] = new int[]{-1, -1};
            Queue<int[]> q = new LinkedList<>();
            q.add(new int[]{filaActual, columnaActual});
            visited[filaActual][columnaActual] = true;

            int[] dirsF = {-1, 1, 0, 0};
            int[] dirsC = {0, 0, 1, -1};
            String[] dirNames = {"ARRIBA", "ABAJO", "DERECHA", "IZQUIERDA"};

            boolean found = false;
            while (!q.isEmpty()) {
                int[] cur = q.poll();
                if (cur[0] == filaObjetivo && cur[1] == columnaObjetivo) { found = true; break; }
                for (int k = 0; k < 4; k++) {
                    int nf = cur[0] + dirsF[k];
                    int nc = cur[1] + dirsC[k];
                    if (!esPosicionValida(nf, nc) || visited[nf][nc]) continue;
                    boolean cellIsPassable = transitable[nf][nc] || (permitirHielo && hielo[nf][nc]);
                    if (!cellIsPassable) continue;
                    visited[nf][nc] = true;
                    prev[nf * columnas + nc] = cur;
                    q.add(new int[]{nf, nc});
                }
            }

            if (!found) return null;
            // reconstruir primer paso desde destino hacia origen
            int cf = filaObjetivo;
            int cc = columnaObjetivo;
            int pf = prev[cf * columnas + cc][0];
            int pc = prev[cf * columnas + cc][1];
            if (pf == -1) return null; // estaba en el mismo lugar
            while (!(pf == filaActual && pc == columnaActual)) {
                int tmpf = prev[pf * columnas + pc][0];
                int tmpc = prev[pf * columnas + pc][1];
                pf = tmpf; pc = tmpc;
                if (pf == -1) break;
            }
            if (pf == -1) return null;
            if (pf == filaActual && pc == columnaActual) {
                if (cf == filaActual - 1 && cc == columnaActual) return "ARRIBA";
                if (cf == filaActual + 1 && cc == columnaActual) return "ABAJO";
                if (cf == filaActual && cc == columnaActual + 1) return "DERECHA";
                if (cf == filaActual && cc == columnaActual - 1) return "IZQUIERDA";
            }
            // Fallback: comparar primera step relative
            if (pf == filaActual && pc == columnaActual) {
                if (cf < pf) return "ARRIBA";
                if (cf > pf) return "ABAJO";
                if (cc > pc) return "DERECHA";
                if (cc < pc) return "IZQUIERDA";
            }
            // Si no pudimos deducir bien, devolver null
            return null;
        }

        @Override
        public int[] calcularNuevaPosicion(int fila, int columna, String direccion) throws BadDopoException {
            switch (direccion) {
                case "ARRIBA": return new int[]{fila - 1, columna};
                case "ABAJO": return new int[]{fila + 1, columna};
                case "DERECHA": return new int[]{fila, columna + 1};
                case "IZQUIERDA": return new int[]{fila, columna - 1};
                case "UP": return new int[]{fila - 1, columna};
                case "DOWN": return new int[]{fila + 1, columna};
                case "RIGHT": return new int[]{fila, columna + 1};
                case "LEFT": return new int[]{fila, columna - 1};
                default: throw new BadDopoException(BadDopoException.DIRECCION_INVALIDA);
            }
        }

        @Override
        public java.util.List<String> obtenerDireccionesValidas(int fila, int columna) {
            java.util.List<String> list = new java.util.ArrayList<>();
            try {
                for (String d : new String[]{"ARRIBA","ABAJO","DERECHA","IZQUIERDA"}) {
                    int[] dest = calcularNuevaPosicion(fila, columna, d);
                    if (esPosicionValida(dest[0], dest[1]) && esTransitable(dest[0], dest[1])) list.add(d);
                }
            } catch (BadDopoException ex) { }
            return list;
        }

        @Override
        public boolean esPosicionValida(int fila, int columna) {
            return fila >= 0 && fila < filas && columna >= 0 && columna < columnas;
        }
    }

    @Test
    public void testTrollInvierteDireccionSiHayObstaculo() throws BadDopoException {
        MockVista vista = new MockVista(10, 10);
        // hacer no transitable la celda arriba de (5,5)
        vista.setTransitable(4, 5, false);
        vista.setTransitable(6, 5, true);

        Troll troll = new Troll(5, 5);
        // por defecto ultimaDireccion = ARRIBA
        String dir = troll.decidirProximaMovida(vista, jugador);
        assertEquals("Troll debe invertir y moverse ABAJO", "ABAJO", dir);
    }

    @Test
    public void testMacetaPersigueJugadorConBFS() throws BadDopoException {
        MockVista vista = new MockVista(10, 10);
        Maceta maceta = new Maceta(2, 2);
        jugador.setPosicionInicial(2, 4);

        String dir = maceta.decidirProximaMovida(vista, jugador);
        assertEquals("Maceta debe moverse DERECHA hacia jugador", "DERECHA", dir);
    }

    @Test
    public void testCalamarDetectaHieloSiguiente() throws BadDopoException {
        MockVista vista = new MockVista(10, 10);
        // poner hielo en (2,3)
        vista.setHielo(2, 3, true);

        Calamar calamar = new Calamar(2, 2);
        jugador.setPosicionInicial(2, 4);

        String dir = calamar.decidirProximaMovida(vista, jugador);
        assertEquals("Calamar debe intentar avanzar DERECHA y romper hielo", "DERECHA", dir);
        int[] nxt = vista.calcularNuevaPosicion(2, 2, dir);
        assertTrue("La siguiente celda debe ser hielo", vista.esHielo(nxt[0], nxt[1]));
    }

    @Test
    public void testNarvalEmbisteSiAlineado() throws BadDopoException {
        MockVista vista = new MockVista(10, 10);
        Narval narval = new Narval(3, 2);
        jugador.setPosicionInicial(3, 6);

        String dir = narval.decidirProximaMovida(vista, jugador);
        assertEquals("Narval debe embestir hacia DERECHA cuando está alineado", "DERECHA", dir);
    }
}
