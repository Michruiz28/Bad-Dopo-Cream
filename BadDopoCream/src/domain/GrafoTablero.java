package domain;

/**
 * Clase grafo tablero que obtiene la lógica de un grafo para la ejecución del juego con nodos y conexiones.
 * @version 06.12.2025
 * @author Maria Katalina Leyva Díaz y Michelle Dayana Ruíz Carranza.
 */
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
     */
    public final void construirGrafo(String[][] infoNivel) throws BadDopoException {
        nodos = new Nodo[filas][columnas];
        String tipo;

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                tipo = infoNivel[f][c];
                nodos[f][c] = new Nodo(f, c, tipo, creador);
            }
        }

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

    public boolean solicitarMovimiento(int fila, int columna, String direccion) throws BadDopoException {
        Nodo nodo = getNodo(fila, columna);
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new BadDopoException(BadDopoException.DIRECCION_INVALIDA);
        }

        int[] nuevaPosicion = new int[2];
        switch (direccion.toUpperCase()) {
            case "ARRIBA":
                nuevaPosicion = moverArriba(fila, columna);
                break;
            case "ABAJO":
                nuevaPosicion = moverAbajo(fila, columna);
                break;
            case "DERECHA":
                nuevaPosicion = moverDerecha (fila, columna);
                break;
            case "IZQUIERDA":
                nuevaPosicion = moverIzquierda(fila, columna);
                break;
            default:
                throw new BadDopoException(BadDopoException.DIRECCION_DESCONOCIDA);
        }

        Nodo nodoDestino = getNodo(nuevaPosicion[0], nuevaPosicion[1]);
        if (nodo == null || nodoDestino == null) { return false; }

        if (!nodo.getVecinos().contains(nodoDestino)) { return false; }

        ejecutarMovimiento(nodo, nodoDestino, direccion);
        return true;

    }

    private void ejecutarMovimiento(Nodo origen, Nodo destino, String direccion) throws BadDopoException {
        Celda celdaOrigen = origen.getCelda();
        Celda celdaDestino = destino.getCelda();
        Elemento elementoAMover = celdaOrigen.getElemento();
        Elemento elementoEnDestino = celdaDestino.getElemento();
        elementoAMover.mover(direccion);

        if (celdaDestino.getTipo().equals("H") || celdaDestino.getTipo().equals("B")){
            celdaOrigen.setElemento(elementoAMover, creador);
            celdaDestino.setElemento(elementoEnDestino, creador);
        } else {
            celdaDestino.setElemento(elementoAMover, creador);
            celdaOrigen.setElemento(null, creador);
        }
    }

    public int[] moverArriba(int fila, int columna) throws BadDopoException {
        int[] posiciones = new int[2];
        posiciones[0] = fila - 1;
        posiciones[1] = columna;
        return posiciones;
    }
    public int[] moverAbajo(int fila, int columna) throws BadDopoException {
        int[] posiciones = new int[2];
        posiciones[0] = fila + 1;
        posiciones[1] = columna;
        return posiciones;
    }

    public int[] moverDerecha(int fila, int columna) throws BadDopoException {
        int[] posiciones = new int[2];
        posiciones[0] = fila;
        posiciones[1] = columna + 1;
        return posiciones;
    }

    public int[] moverIzquierda(int fila, int columna) throws BadDopoException {
        int[] posiciones = new int[2];
        posiciones[0] = fila;
        posiciones[1] = columna - 1;
        return posiciones;
    }

    public boolean permitirRomperHielo(int fila, int columna, String ultimaDireccion) throws BadDopoException {
        Nodo nodo = getNodo(fila, columna);
        if (ultimaDireccion.equals("DERECHA")) {
            Nodo nodoVecino = getNodo(fila, columna + 1);
            return romperHieloEnNodo(nodoVecino);
        } else if (ultimaDireccion.equals("ARRIBA")) {
            Nodo nodoVecino = getNodo(fila - 1, columna);
            return romperHieloEnNodo(nodoVecino);
        } else  if (ultimaDireccion.equals("ABAJO")) {
            Nodo nodoVecino = getNodo(fila + 1, columna);
            return romperHieloEnNodo(nodoVecino);
        } else  if (ultimaDireccion.equals("IZQUIERDA")) {
            Nodo nodoVecino = getNodo(fila, columna - 1);
            return romperHieloEnNodo(nodoVecino);
        }
        return false;
    }

    public boolean romperHieloEnNodo(Nodo nodoVecino) throws BadDopoException {
        Celda celda = nodoVecino.getCelda();
        if (celda.getTipo().equals("H")) {
            return true;
        } else {
            return false;
        }
    }

    public void romperHielo(int fila, int columna, String ultimaDireccion) throws BadDopoException {
        Nodo nodo = getNodo(fila, columna);
        if (ultimaDireccion.equals("DERECHA")) {
            while (permitirRomperHielo(fila, columna, ultimaDireccion)) {
                Celda celda = nodo.getCelda();
                celda.setElementoConTipo("V", creador);
            }
        }
    }




}
