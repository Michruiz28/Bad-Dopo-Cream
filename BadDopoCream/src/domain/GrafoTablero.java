package domain;

import com.sun.security.auth.NTDomainPrincipal;

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


    public boolean solicitarMovimiento(int fila, int col) throws BadDopoException {
        Nodo n = getNodo(fila, col);
        Celda celda = n.getCelda();
        Elemento elemento = celda.getElemento();
        int[] nuevaPosicion = elemento.calcularPosicionesMovimieto(filas - 2, columnas - 2);
        Nodo nodoDestino = getNodo(nuevaPosicion[0], nuevaPosicion[1]);
        Celda celdaDestino = nodoDestino.getCelda();
        while (!celdaDestino.getTipo().equals("N")) {
            nuevaPosicion = elemento.calcularPosicionesMovimieto(filas - 2, columnas - 2);
            nodoDestino = getNodo(nuevaPosicion[0], nuevaPosicion[1]);
            celdaDestino = nodoDestino.getCelda();
        }
        return true;
    }

    public void ejecutarMovimientoAutonomo(Celda celdaActual, Celda celdaDestino, Elemento elemento) throws BadDopoException {
        celdaActual.setElementoConTipo("N", creador);
        celdaDestino.setElemento(elemento, creador);
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

        if (celdaDestino.getTipo().equals("H") || celdaDestino.getTipo().equals("B")){
            celdaOrigen.setElemento(elementoAMover, creador);
            celdaDestino.setElemento(elementoEnDestino, creador);
        } else if (celdaDestino.getTipo().equals("BF") || celdaDestino.getTipo().equals("CF") || celdaDestino.getTipo().equals("CAF") || celdaDestino.getTipo().equals("U") || celdaDestino.getTipo().equals("P")) {
            elementoAMover.aumentarPuntaje(elementoEnDestino.getGanancia());
        } else {
            celdaDestino.setElemento(elementoAMover, creador);
            celdaOrigen.setElemento(null, creador);
        }
        elementoAMover.mover(direccion);
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
    //REVISAR SI ESTOS METODOS MOVER SE PUEDEN HACER DE FORMA MAS RAPIDA

    public void realizarAccion(int fila, int columna, String ultimaDireccion) throws BadDopoException {
        Nodo nodo = getNodo(fila, columna);
        Celda celdaActual = nodo.getCelda();
        Elemento elementoActual = celdaActual.getElemento();
        if (ultimaDireccion.equals("DERECHA")) {
            Nodo nodoVecino = getNodo(fila, columna + 1);
            ejecutarAccion(nodoVecino, ultimaDireccion, elementoActual);
        } else if (ultimaDireccion.equals("ARRIBA")) {
            Nodo nodoVecino = getNodo(fila - 1, columna);
            ejecutarAccion(nodoVecino, ultimaDireccion, elementoActual);
        } else  if (ultimaDireccion.equals("ABAJO")) {
            Nodo nodoVecino = getNodo(fila + 1, columna);
            ejecutarAccion(nodoVecino, ultimaDireccion, elementoActual);
        } else  if (ultimaDireccion.equals("IZQUIERDA")) {
            Nodo nodoVecino = getNodo(fila, columna - 1);
            ejecutarAccion(nodoVecino, ultimaDireccion, elementoActual);
        }
        else {
            throw new BadDopoException(BadDopoException.DIRECCION_INVALIDA);
        }
    }

    public void ejecutarAccion(Nodo nodoVecino, String ultimaDireccion, Elemento elementoActual) throws BadDopoException {
        Celda celda = nodoVecino.getCelda();
        int fila = celda.getFila();
        int columna = celda.getCol();
        Elemento elemento = celda.getElemento();
        if (!(elemento.esTransitable()) && celda.getTipo().equals("H")) {
            romperHielo(fila, columna, ultimaDireccion, elementoActual);
        } else if (elemento.esTransitable()) {
            crearHielo(fila, columna, ultimaDireccion, elementoActual);
        }
    }

    public void romperHielo(int fila, int columna, String ultimaDireccion, Elemento elementoActual) throws BadDopoException {
        Nodo nodo = getNodo(fila, columna);
        Celda celda = nodo.getCelda();

        while (celda.getTipo().equals("H")) {
            Nodo nodoARomper = getNodo(fila, columna);
            Celda celdaARomper = nodo.getCelda();
            elementoActual.romperHielo(celdaARomper, creador);
            if (ultimaDireccion.equals("DERECHA")) {
                columna++;
            } else if (ultimaDireccion.equals("ARRIBA")) {
                fila--;
            }  else if (ultimaDireccion.equals("ABAJO")) {
                fila++;
            } else  if (ultimaDireccion.equals("IZQUIERDA")) {
                columna--;
            }
        }
    }

    public void crearHielo(int fila, int columna, String ultimaDireccion, Elemento elementoActual) throws BadDopoException {
        Nodo nodo = getNodo(fila, columna);
        Celda celda = nodo.getCelda();
        while (celda.getTipo().equals("H")) {
            Nodo nodoACrear = getNodo(fila, columna);
            Celda celdaACrear = nodo.getCelda();
            celdaACrear.setElementoConTipo("H", creador);
            elementoActual.crearHielo(celdaACrear, creador);
            if (ultimaDireccion.equals("DERECHA")) {
                columna++;
            } else if (ultimaDireccion.equals("ARRIBA")) {
                fila--;
            }  else if (ultimaDireccion.equals("ABAJO")) {
                fila++;
            } else  if (ultimaDireccion.equals("IZQUIERDA")) {
                columna--;
            }
        }
    }

    /**
     * Actualiza el movimiento de todos los enemigos en el grafo
     * donde cada Enemigo decide su próxima movida con una vista de solo lectura.
     */
    public void actualizarEnemigos(Helado jugador) throws BadDopoException {
        VistaTablero vista = new VistaTableroImpl();
        java.util.List<int[]> posicionesEnemigos = new java.util.ArrayList<>();
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                Nodo nodo = nodos[f][c];
                if (nodo == null) continue;
                Elemento el = nodo.getCelda().getElemento();
                if (el instanceof Enemigo) {
                    posicionesEnemigos.add(new int[]{f, c});
                }
            }
        }
        for (int[] pos : posicionesEnemigos) {
            procesarEnemigo(pos[0], pos[1], vista, jugador);
        }
    }

    /**
     * Procesa el movimiento de un enemigo específico mediante delegación polimórfica.
     */
    private void procesarEnemigo(int f, int c, VistaTablero vista, Helado jugador) throws BadDopoException {
        Nodo nodo = getNodo(f, c);
        if (nodo == null) return;
        Elemento elemento = nodo.getCelda().getElemento();
        if (!(elemento instanceof Enemigo)) return;
        Enemigo enemigo = (Enemigo) elemento;
        String direccion = enemigo.decidirProximaMovida(vista, jugador);
        if (direccion == null) return;

        // Verificar si es un Narval en embestida
        boolean esEmbestida = !enemigo.isPersigueJugador() && enemigo.canRomperBloques() 
                              && (f == jugador.getFila() || c == jugador.getColumna());
        
        if (esEmbestida) {
            ejecutarEmbestidaNarval(f, c, direccion, jugador.getFila(), jugador.getColumna());
        } else if (enemigo.canRomperBloques() && enemigo.rompeUnBloquePorVez()) {
            // Calamar: verifica si siguiente es hielo y lo rompe
            int[] siguiente = calcularNuevaPosicion(f, c, direccion);
            if (esHielo(siguiente[0], siguiente[1])) {
                romperHieloEnDireccion(f, c, direccion);
            } else {
                solicitarMovimiento(f, c, direccion);
            }
        } else {
            solicitarMovimiento(f, c, direccion);
        }
    }

    /**
     * Ejecuta la embestida del Narval donde avanza en línea recta rompiendo hielo.
     */
    private void ejecutarEmbestidaNarval(int f, int c, String direccion, int objetivoF, int objetivoC) throws BadDopoException {
        int curF = f;
        int curC = c;
        
        while (true) {
            int[] next = calcularNuevaPosicion(curF, curC, direccion);
            if (!esPosicionValida(next[0], next[1])) break;
            Nodo nodoSiguiente = getNodo(next[0], next[1]);
            if (nodoSiguiente == null) break;
            Celda celdaSiguiente = nodoSiguiente.getCelda();
            if (esHielo(next[0], next[1])) {
                romperHieloEnDireccion(curF, curC, direccion);
                if (solicitarMovimiento(curF, curC, direccion)) {
                    curF = next[0];
                    curC = next[1];
                } else {
                    break;
                }
            } else if (celdaSiguiente.esTransitable()) {
                if (solicitarMovimiento(curF, curC, direccion)) {
                    curF = next[0];
                    curC = next[1];
                } else {
                    break;
                }
            } else {
                break;
            }
            // Parar si alcanzó al jugador
            if (curF == objetivoF && curC == objetivoC) break;
        }
    }

    public int[] calcularNuevaPosicion(int f, int c, String direccion) throws BadDopoException {
        return switch (direccion) {
            case "ARRIBA" -> moverArriba(f, c);
            case "ABAJO" -> moverAbajo(f, c);
            case "DERECHA" -> moverDerecha(f, c);
            case "IZQUIERDA" -> moverIzquierda(f, c);
            default -> new int[]{f, c};
        };
    }

    public boolean puedeMoverEn(int f, int c, String direccion) throws BadDopoException {
        int[] dest = calcularNuevaPosicion(f, c, direccion);
        if (!esPosicionValida(dest[0], dest[1])) return false;
        Nodo nodo = getNodo(dest[0], dest[1]);
        return nodo != null && nodo.getCelda().esTransitable();
    }

    public String calcularDireccionHaciaObjetivo(int f0, int c0, int objetivoF, int objetivoC, boolean permitirHielo) {
        record Estado(int fila, int col, String primerPaso) {}

        Queue<Estado> cola = new LinkedList<>();
        boolean[][] visitado = new boolean[filas][columnas];

        cola.add(new Estado(f0, c0, null));
        visitado[f0][c0] = true;

        int[][] dirs = { {-1,0}, {1,0}, {0,1}, {0,-1} };
        String[] nombres = { "ARRIBA", "ABAJO", "DERECHA", "IZQUIERDA" };

        while (!cola.isEmpty()) {
            Estado actual = cola.remove();
            if (actual.fila == objetivoF && actual.col == objetivoC) return actual.primerPaso;

            for (int i = 0; i < 4; i++) {
                int nf = actual.fila + dirs[i][0];
                int nc = actual.col + dirs[i][1];
                if (!esPosicionValida(nf, nc)) continue;
                if (visitado[nf][nc]) continue;
                Nodo vecino = getNodo(nf, nc);
                if (vecino == null) continue;
                Celda celda = vecino.getCelda();
                boolean transitable = celda.esTransitable();
                if (!transitable && permitirHielo && esHielo(nf, nc)) {
                    transitable = true;
                }
                if (!transitable) continue;
                visitado[nf][nc] = true;
                String primer = (actual.primerPaso == null) ? nombres[i] : actual.primerPaso;
                cola.add(new Estado(nf, nc, primer));
            }
        }
        return null;
    }

    public String obtenerDireccionAleatoria(int f, int c) {
        java.util.List<String> posibles = new java.util.ArrayList<>();
        String[] dirs = {"ARRIBA", "ABAJO", "DERECHA", "IZQUIERDA"};
        int[][] delta = { {-1,0}, {1,0}, {0,1}, {0,-1} };
        for (int i = 0; i < dirs.length; i++) {
            int nf = f + delta[i][0];
            int nc = c + delta[i][1];
            if (!esPosicionValida(nf, nc)) continue;
            Nodo dest = getNodo(nf, nc);
            if (dest != null && dest.getCelda().esTransitable()) posibles.add(dirs[i]);
        }
        if (posibles.isEmpty()) return null;
        return posibles.get(new java.util.Random().nextInt(posibles.size()));
    }

    public boolean esHielo(int f, int c) {
        if (!esPosicionValida(f, c)) return false;
        Nodo nodo = getNodo(f, c);
        if (nodo == null) return false;
        try {
            return nodo.getCelda().getTipo().equals("H");
        } catch (Exception ex) {
            return false;
        }
    }

    public void romperHieloEnDireccion(int f, int c, String direccion) throws BadDopoException {
        int[] dest = calcularNuevaPosicion(f, c, direccion);
        if (esPosicionValida(dest[0], dest[1]) && esHielo(dest[0], dest[1])) {
            romperHielo(dest[0], dest[1], direccion);
        }
    }

    public boolean esPosicionValida(int f, int c) {
        return f >= 0 && f < filas && c >= 0 && c < columnas;
    }
    /**
     * Implementación interna de VistaTablero que proporciona acceso de solo lectura
     * al grafo para los enemigos, sin exponerles detalles internos.
     */
    private class VistaTableroImpl implements VistaTablero {

        @Override
        public boolean esTransitable(int fila, int columna) {
            if (!esPosicionValida(fila, columna)) return false;
            Nodo nodo = getNodo(fila, columna);
            return nodo != null && nodo.getCelda().esTransitable();
        }

        @Override
        public boolean esHielo(int fila, int columna) {
            return GrafoTablero.this.esHielo(fila, columna);
        }

        @Override
        public String calcularDireccionHaciaObjetivo(int filaActual, int columnaActual, 
                                                      int filaObjetivo, int columnaObjetivo, 
                                                      boolean permitirHielo) {
            return GrafoTablero.this.calcularDireccionHaciaObjetivo(filaActual, columnaActual, 
                                                                     filaObjetivo, columnaObjetivo, 
                                                                     permitirHielo);
        }

        @Override
        public int[] calcularNuevaPosicion(int fila, int columna, String direccion) throws BadDopoException {
            return GrafoTablero.this.calcularNuevaPosicion(fila, columna, direccion);
        }

        @Override
        public java.util.List<String> obtenerDireccionesValidas(int fila, int columna) {
            java.util.List<String> validas = new java.util.ArrayList<>();
            String[] dirs = {"ARRIBA", "ABAJO", "DERECHA", "IZQUIERDA"};
            try {
                for (String dir : dirs) {
                    int[] dest = calcularNuevaPosicion(fila, columna, dir);
                    if (esPosicionValida(dest[0], dest[1]) && esTransitable(dest[0], dest[1])) {
                        validas.add(dir);
                    }
                }
            } catch (BadDopoException ex) {
            }
            return validas;
        }

        @Override
        public boolean esPosicionValida(int fila, int columna) {
            return GrafoTablero.this.esPosicionValida(fila, columna);
        }
    }

}
