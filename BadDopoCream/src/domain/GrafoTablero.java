package domain;

import java.util.*;

/**
 * Clase grafo tablero que obtiene la lógica de un grafo para la ejecución del juego con nodos y conexiones.
 * @version 06.12.2025 - CORREGIDO para movimiento de helados
 * @author Maria Katalina Leyva Díaz y Michelle Dayana Ruíz Carranza.
 */
public class GrafoTablero {
    private Nodo[][] nodos;
    private int filas;
    private int columnas;
    private CreadorElemento creador;
    private ArrayList<Fruta> frutasActivas;

    public GrafoTablero(int filas, int columnas, String[][] infoNivel, CreadorElemento creador) throws BadDopoException {
        this.filas = filas;
        this.columnas = columnas;
        this.creador = creador;
        construirGrafo(infoNivel);
        this.frutasActivas = new ArrayList<>();
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
        verificarGrafo();
    }

    public void verificarGrafo() {
        System.out.println("=== VERIFICACIÓN DEL GRAFO ===");
        System.out.println("Dimensiones: " + filas + "x" + columnas);

        int totalNodos = 0;
        int nodosConVecinos = 0;
        int nodosSinVecinos = 0;

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                if (nodos[f][c] != null) {
                    totalNodos++;
                    int numVecinos = nodos[f][c].getVecinos().size();
                    if (numVecinos > 0) {
                        nodosConVecinos++;
                    } else {
                        nodosSinVecinos++;
                        System.out.println("  Nodo (" + f + "," + c + ") tiene 0 vecinos. Tipo: " + nodos[f][c].getCelda().getTipo());
                    }
                }
            }
        }

        System.out.println("Total nodos: " + totalNodos);
        System.out.println("Nodos con vecinos: " + nodosConVecinos);
        System.out.println("Nodos sin vecinos: " + nodosSinVecinos);
        System.out.println("=== FIN VERIFICACIÓN ===");
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
        Nodo nodo = getNodo(fila, col);
        if (nodo != null) {
            // Solo cambiar el tipo de la celda, NO reemplazar el nodo
            nodo.getCelda().setTipo(tipo);
        }
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
        if (nodo == null) {
            System.out.println("[GRAFO] ERROR: Nodo en posición (" + fila + "," + columna + ") es null");
            return false;
        }

        System.out.println("[GRAFO] Solicitando movimiento desde (" + fila + "," + columna + ") hacia " + direccion);

        // Calcular nueva posición
        int nuevaFila = fila;
        int nuevaColumna = columna;

        switch (direccion.toUpperCase()) {
            case "ARRIBA":
                nuevaFila = fila - 1;
                break;
            case "ABAJO":
                nuevaFila = fila + 1;
                break;
            case "DERECHA":
                nuevaColumna = columna + 1;
                break;
            case "IZQUIERDA":
                nuevaColumna = columna - 1;
                break;
            default:
                throw new BadDopoException(BadDopoException.DIRECCION_DESCONOCIDA);
        }

        // Verificar límites ANTES de buscar el nodo
        if (nuevaFila < 0 || nuevaFila >= filas || nuevaColumna < 0 || nuevaColumna >= columnas) {
            System.out.println("[GRAFO] ERROR: Movimiento fuera de límites");
            return false;
        }

        System.out.println("[GRAFO] Nueva posición calculada: (" + nuevaFila + "," + nuevaColumna + ")");

        Nodo nodoDestino = getNodo(nuevaFila, nuevaColumna);
        if (nodoDestino == null) {
            System.out.println("[GRAFO] ERROR: Nodo destino es null");
            return false;
        }

        // DEBUG: Mostrar información de vecinos
        System.out.println("[GRAFO] DEBUG: Vecinos del nodo origen:");
        for (Nodo vecino : nodo.getVecinos()) {
            System.out.println("  - (" + vecino.getFila() + "," + vecino.getColumna() + ")");
        }

        if (!nodo.getVecinos().contains(nodoDestino)) {
            System.out.println("[GRAFO] ERROR: Nodo destino no es vecino");
            System.out.println("[GRAFO] DEBUG: Nodo destino buscado: (" + nodoDestino.getFila() + "," + nodoDestino.getColumna() + ")");
            return false;
        }

        ejecutarMovimiento(nodo, nodoDestino, direccion);
        System.out.println("[GRAFO] Movimiento ejecutado exitosamente");
        return true;
    }

    private void ejecutarMovimiento(Nodo origen, Nodo destino, String direccion) throws BadDopoException {
        Celda celdaOrigen = origen.getCelda();
        Celda celdaDestino = destino.getCelda();
        Elemento elementoAMover = celdaOrigen.getElemento();
        Elemento elementoEnDestino = celdaDestino.getElemento();

        System.out.println("[GRAFO] Ejecutando movimiento - Tipo origen: " + celdaOrigen.getTipo() + ", Tipo destino: " + celdaDestino.getTipo());

        // Verificar si el destino es transitable según su TIPO
        String tipoDestino = celdaDestino.getTipo();
        
        // Hielo o Borde no transitables
        if (tipoDestino.equals("H") || tipoDestino.equals("B")) {
            System.out.println("[GRAFO] Destino no transitable (Hielo o Borde)");
            return;
        }

        // Verificar si hay fruta en la celda destino
        if (tipoDestino.equals("BF") || tipoDestino.equals("CF") ||
                tipoDestino.equals("CAF") || tipoDestino.equals("U") ||
                tipoDestino.equals("P")) {
            // Recolectar fruta
            if (elementoEnDestino != null) {
                elementoAMover.aumentarPuntaje(elementoEnDestino.getGanancia());
                System.out.println("[GRAFO] Fruta recolectada: +" + elementoEnDestino.getGanancia() + " puntos");
            }
            // Colocar el helado en la celda destino y marcar la celda con el tipo correspondiente
            celdaDestino.setElemento(elementoAMover, creador);
            // Solo los Helado tienen sabor; verificar antes de llamar getSabor()
            if (elementoAMover instanceof Helado) {
                String tipoHelado = obtenerCodigoSabor(((Helado) elementoAMover).getSabor());
                celdaDestino.setTipo(tipoHelado); // Mantener el tipo correspondiente al helado
            } else {
                // Si no es un Helado, dejar como vacío o mantener comportamiento por defecto
                celdaDestino.setTipo("V");
            }
            celdaOrigen.setElementoConTipo("V", creador);

            elementoAMover.setFila(celdaDestino.getFila());
            elementoAMover.setColumna(celdaDestino.getCol());
            elementoAMover.setCelda(celdaDestino);
        } else {
            // Espacio vacío ("V") u otro tipo transitable: mover normalmente
            celdaDestino.setElemento(elementoAMover, creador);
            celdaOrigen.setElementoConTipo("V", creador);
            
            // Actualizar el tipo de la celda destino para reflejar el helado que se movió
            if (elementoAMover instanceof Helado) {
                String tipoHelado = obtenerCodigoSabor(((Helado) elementoAMover).getSabor());
                celdaDestino.setTipo(tipoHelado);
            } else {
                celdaDestino.setTipo("V");
            }

            elementoAMover.setFila(celdaDestino.getFila());
            elementoAMover.setColumna(celdaDestino.getCol());
            elementoAMover.setCelda(celdaDestino);
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

    public void realizarAccion(int fila, int columna, String ultimaDireccion) throws BadDopoException {
        if (ultimaDireccion == null) {
            System.out.println("[GRAFO] ERROR: No hay dirección previa para realizar acción");
            return;
        }

        Nodo nodo = getNodo(fila, columna);
        if (nodo == null) return;

        Celda celdaActual = nodo.getCelda();
        Elemento elementoActual = celdaActual.getElemento();

        if (ultimaDireccion.equals("DERECHA")) {
            Nodo nodoVecino = getNodo(fila, columna + 1);
            ejecutarAccion(nodoVecino, ultimaDireccion, elementoActual);
        } else if (ultimaDireccion.equals("ARRIBA")) {
            Nodo nodoVecino = getNodo(fila - 1, columna);
            ejecutarAccion(nodoVecino, ultimaDireccion, elementoActual);
        } else if (ultimaDireccion.equals("ABAJO")) {
            Nodo nodoVecino = getNodo(fila + 1, columna);
            ejecutarAccion(nodoVecino, ultimaDireccion, elementoActual);
        } else if (ultimaDireccion.equals("IZQUIERDA")) {
            Nodo nodoVecino = getNodo(fila, columna - 1);
            ejecutarAccion(nodoVecino, ultimaDireccion, elementoActual);
        } else {
            throw new BadDopoException(BadDopoException.DIRECCION_INVALIDA);
        }
    }

    public void ejecutarAccion(Nodo nodoVecino, String ultimaDireccion, Elemento elementoActual) throws BadDopoException {
        if (nodoVecino == null) return;

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
        if (nodo == null) return;

        Celda celda = nodo.getCelda();

        while (celda.getTipo().equals("H")) {
            Celda celdaARomper = celda;
            elementoActual.romperHielo(celdaARomper, creador);

            if (ultimaDireccion.equals("DERECHA")) {
                columna++;
            } else if (ultimaDireccion.equals("ARRIBA")) {
                fila--;
            } else if (ultimaDireccion.equals("ABAJO")) {
                fila++;
            } else if (ultimaDireccion.equals("IZQUIERDA")) {
                columna--;
            }

            Nodo siguienteNodo = getNodo(fila, columna);
            if (siguienteNodo == null) break;
            celda = siguienteNodo.getCelda();
        }
    }

    public void crearHielo(int fila, int columna, String ultimaDireccion, Elemento elementoActual) throws BadDopoException {
        Nodo nodo = getNodo(fila, columna);
        if (nodo == null) return;

        Celda celda = nodo.getCelda();

        if (celda.getTipo().equals("V")) {
            celda.setElementoConTipo("H", creador);
            elementoActual.crearHielo(celda, creador);
        }
    }

    public void actualizarEnemigos(Helado jugador) throws BadDopoException {
        VistaTablero vista = new VistaTableroImpl();
        List<int[]> posicionesEnemigos = new ArrayList<>();
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

    public void procesarMovimientoHelado(int filaOrigen, int columnaOrigen, String direccion, Helado jugador) throws BadDopoException {
        boolean moved = solicitarMovimiento(filaOrigen, columnaOrigen, direccion);
        if (!moved) return;

        java.util.List<int[]> posicionesPinas = new java.util.ArrayList<>();
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                Nodo nodo = nodos[f][c];
                if (nodo == null) continue;
                Elemento el = nodo.getCelda().getElemento();
                if (el instanceof Pina) posicionesPinas.add(new int[]{f, c});
            }
        }

        for (int[] pos : posicionesPinas) {
            int f = pos[0], c = pos[1];
            try {
                String mejor = null;
                int distanciaActual = Math.abs(f - jugador.getFila()) + Math.abs(c - jugador.getColumna());
                String[] dirs = {"ARRIBA", "ABAJO", "DERECHA", "IZQUIERDA"};
                for (String d : dirs) {
                    int[] dest = calcularNuevaPosicion(f, c, d);
                    if (!esPosicionValida(dest[0], dest[1])) continue;
                    Nodo nodoDestino = getNodo(dest[0], dest[1]);
                    if (nodoDestino == null) continue;
                    if (!nodoDestino.getCelda().esTransitable()) continue;
                    int dist = Math.abs(dest[0] - jugador.getFila()) + Math.abs(dest[1] - jugador.getColumna());
                    if (dist > distanciaActual) {
                        distanciaActual = dist;
                        mejor = d;
                    }
                }
                if (mejor == null) {
                    mejor = obtenerDireccionAleatoria(f, c);
                }
                if (mejor != null) {
                    solicitarMovimiento(f, c, mejor);
                }
            } catch (BadDopoException ex) {
                // Ignorar errores de movimiento individuales
            }
        }
    }

    private void procesarEnemigo(int f, int c, VistaTablero vista, Helado jugador) throws BadDopoException {
        Nodo nodo = getNodo(f, c);
        if (nodo == null) return;
        Elemento elemento = nodo.getCelda().getElemento();
        if (!(elemento instanceof Enemigo)) return;

        Enemigo enemigo = (Enemigo) elemento;
        String direccion = enemigo.decidirProximaMovida(vista, jugador);
        if (direccion == null) return;

        boolean esEmbestida = !enemigo.isPersigueJugador() && enemigo.canRomperBloques()
                && (f == jugador.getFila() || c == jugador.getColumna());

        if (esEmbestida) {
            ejecutarEmbestidaNarval(f, c, direccion, jugador.getFila(), jugador.getColumna());
        } else if (enemigo.canRomperBloques() && enemigo.rompeUnBloquePorVez()) {
            int[] siguiente = calcularNuevaPosicion(f, c, direccion);
            if (esHielo(siguiente[0], siguiente[1])) {
                romperHieloEnDireccion(f, c, direccion, elemento);
            } else {
                solicitarMovimiento(f, c, direccion);
            }
        } else {
            solicitarMovimiento(f, c, direccion);
        }
    }

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
                romperHieloEnDireccion(curF, curC, direccion, celdaSiguiente.getElemento());
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
        List<String> posibles = new ArrayList<>();
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
        return posibles.get(new Random().nextInt(posibles.size()));
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

    public void romperHieloEnDireccion(int f, int c, String direccion, Elemento elementoActual) throws BadDopoException {
        int[] dest = calcularNuevaPosicion(f, c, direccion);
        if (esPosicionValida(dest[0], dest[1]) && esHielo(dest[0], dest[1])) {
            romperHielo(dest[0], dest[1], direccion, elementoActual);
        }
    }

    public boolean esPosicionValida(int f, int c) {
        return f >= 0 && f < filas && c >= 0 && c < columnas;
    }

    public void agregarElementoEnPosicion(Elemento elemento, int fila, int col) throws BadDopoException {
        Nodo nodo = getNodo(fila, col);
        Celda celda = nodo.getCelda();

        celda.setElemento((Fruta) elemento, creador);
        
        // Actualizar el tipo de celda según la fruta agregada
        if (elemento instanceof Fruta) {
            Fruta fruta = (Fruta) elemento;
            String tipoFruta = obtenerCodigoFruta(fruta);
            if (tipoFruta != null) {
                celda.setTipo(tipoFruta);
            }
            if (!frutasActivas.contains(fruta)) {
                frutasActivas.add(fruta);
                System.out.println("[GRAFO] Fruta agregada a lista activa: " + fruta.getClass().getSimpleName() + " en (" + fila + "," + col + ")");
            }
        }
    }

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
        public List<String> obtenerDireccionesValidas(int fila, int columna) {
            List<String> validas = new ArrayList<>();
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

    public HashMap<String, Fruta> getPosicionesFrutas(){
        HashMap<String, Fruta> posicionesFrutas = new HashMap<>();
        for(int i = 0; i < nodos.length; i++){
            for(int j = 0; j < nodos[0].length; j++){
                Nodo nodo = nodos[i][j];
                if (nodo == null) continue;
                Celda celda  = nodo.getCelda();
                if (celda == null) continue;
                String codigo = celda.getTipo();
                if (codigo == null) continue;
                if (codigo.equals("U") || codigo.equals("BF") || codigo.equals("CF") || codigo.equals("P") || codigo.equals("CAF")) {
                    Fruta fruta = (Fruta) celda.getElemento();
                    String clave;
                    switch (codigo) {
                        case "U": clave = "UVA"; break;
                        case "BF": clave = "BANANA"; break;
                        case "CF": clave = "CEREZA"; break;
                        case "P": clave = "PINA"; break;
                        case "CAF": clave = "CACTUS"; break;
                        default: clave = codigo; break;
                    }
                    String key = clave + "_" + i + "_" + j;
                    posicionesFrutas.put(key, fruta);
                }
            }
        }
        return posicionesFrutas;
    }

    public HashMap<String, Obstaculo> getPosicionesObstaculos(){
        HashMap<String, Obstaculo> posicionesObstaculos = new HashMap<>();
        for(int i = 0; i < nodos.length; i++){
            for(int j = 0; j < nodos[0].length; j++){
                Nodo nodo = nodos[i][j];
                Celda celda  = nodo.getCelda();
                if(celda.getTipo().equals("B") || celda.getTipo().equals("H") || celda.getTipo().equals("BO") || celda.getTipo().equals("FO")){
                    String tipo = celda.getTipo();
                    Obstaculo obstaculo = (Obstaculo) celda.getElemento();
                    posicionesObstaculos.put(tipo, obstaculo);
                }
            }
        }
        return posicionesObstaculos;
    }

    public HashMap<String, Enemigo> getPosicionesEnemigos(){
        HashMap<String, Enemigo> posicionesEnemigos = new HashMap<>();
        for(int i = 0; i < nodos.length; i++){
            for(int j = 0; j < nodos[0].length; j++){
                Nodo nodo = nodos[i][j];
                if (nodo == null) continue;
                Celda celda  = nodo.getCelda();
                if (celda == null) continue;
                String codigo = celda.getTipo();
                if (codigo == null) continue;
                if (codigo.equals("T") || codigo.equals("C") || codigo.equals("M") || codigo.equals("NE")){
                    Enemigo enemigo = (Enemigo) celda.getElemento();
                    String clave;
                    switch (codigo) {
                        case "T": clave = "TROLL"; break;
                        case "C": clave = "CALAMAR"; break;
                        case "M": clave = "MACETA"; break;
                        case "NE": clave = "NARVAL"; break;
                        default: clave = codigo; break;
                    }
                    String key = clave + "_" + i + "_" + j;
                    posicionesEnemigos.put(key, enemigo);
                }
            }
        }
        return posicionesEnemigos;
    }

    public ArrayList<Fruta> getFrutas(){
        // Usar la lista de tracking en lugar de buscar en toda la matriz
        return new ArrayList<>(frutasActivas);
    }   

    public ArrayList<Enemigo> getEnemigos(){
        ArrayList<Enemigo> enemigos = new ArrayList<>();
        for(int i = 0; i < nodos.length; i++){
            for(int j = 0; j < nodos[0].length; j++){
                Nodo nodo = nodos[i][j];
                Celda celda  = nodo.getCelda();
                if (celda.getTipo().equals("T") || celda.getTipo().equals("C") || celda.getTipo().equals("M") || celda.getTipo().equals("NE")) {
                    Enemigo elemento = (Enemigo) celda.getElemento();
                    enemigos.add(elemento);
                }
            }
        }
        return enemigos;
    }

    public ArrayList<Obstaculo> getObstaculos(){
        ArrayList<Obstaculo> obstaculos = new ArrayList<>();
        for(int i = 0; i < nodos.length; i++){
            for(int j = 0; j < nodos.length; j++){
                Nodo nodo = nodos[i][j];
                Celda celda  = nodo.getCelda();
                if (celda.getTipo().equals("B") || celda.getTipo().equals("H") || celda.getTipo().equals("BO") || celda.getTipo().equals("FO")){
                    Obstaculo elemento = (Obstaculo) celda.getElemento();
                    obstaculos.add(elemento);
                }
            }
        }
        return obstaculos;
    }

    /**
     * MÉTODO CORREGIDO: Agrega un helado al grafo correctamente
     */
    public void agregarHelado(Helado helado) throws BadDopoException {
        int fila = helado.getFila();
        int columna = helado.getColumna();

        System.out.println("[GRAFO] Agregando helado en posición (" + fila + "," + columna + ")");

        // Obtener el nodo en esa posición
        Nodo nodo = getNodo(fila, columna);
        if (nodo == null) {
            throw new BadDopoException("No se puede agregar helado: nodo no existe en (" + fila + "," + columna + ")");
        }

        // Actualizar la celda con el helado
        Celda celda = nodo.getCelda();
        celda.setElemento(helado, creador);

        // Marcar el tipo según el sabor para mantener consistencia
        String tipoSabor = obtenerCodigoSabor(helado.getSabor());
        celda.setTipo(tipoSabor);

        // Vincular el helado con su celda
        helado.setCelda(celda);

        System.out.println("[GRAFO] Helado agregado exitosamente con sabor: " + helado.getSabor() + " (código: " + tipoSabor + ")");
    }

    /**
     * Convierte el nombre del sabor al código usado internamente
     */
    private String obtenerCodigoSabor(String sabor) {
        if (sabor == null) return "VH";
        switch (sabor) {
            case "Chocolate":
            case "CH":
                return "CH";
            case "Fresa":
            case "F":
                return "F";
            case "Vainilla":
            case "VH":
                return "VH";
            default:
                return "VH";
        }
    }

    /**
     * Obtiene el código de tipo de celda para una fruta
     */
    private String obtenerCodigoFruta(Fruta fruta) {
        if (fruta == null) return null;
        
        String nombreClase = fruta.getClass().getSimpleName();
        
        if (nombreClase.equals("Banano")) {
            return "BF";
        } else if (nombreClase.equals("Cereza")) {
            return "CF";
        } else if (nombreClase.equals("Uva")) {
            return "U";
        } else if (nombreClase.equals("Pina")) {
            return "P";
        }
        
        return null;
    }

    public void removeElemento(int fila, int col){
        Nodo nodo = getNodo(fila, col);
        if (nodo != null) {
            try {
                nodo.getCelda().setElementoConTipo("V", creador);
            } catch (BadDopoException e) {
                System.err.println("Error al remover elemento: " + e.getMessage());
            }
        }
    }

    /**
     * Teletransportar cerezas
     * @param fila
     * @param col
     * @return
     * @throws BadDopoException
     */
    public void teletransportarCerezas() throws BadDopoException {
        ArrayList<int[]> posicionesCerezas = new ArrayList<>();
        
        for (int i = 0; i < nodos.length; i++) {
            for (int j = 0; j < nodos[0].length; j++) {
                Nodo n = nodos[i][j];
                Celda c = n.getCelda();
                
                // Verificar por tipo de celda
                if (c.getTipo().equals("CF")) {
                    posicionesCerezas.add(new int[]{i, j});
                }
            }
        }

        for (int[] pos : posicionesCerezas) {
            teletransportarCereza(pos[0], pos[1]);
        }  
    
    }
    
    public String[][] construirRepresentacionActual(){
        int filas = nodos.length;
        int columnas = nodos[0].length;

        String[][] rep = new String[filas][columnas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                rep[i][j] = nodos[i][j].getCelda().getTipo();
            }
        }

        return rep;
    }

    /**
     * Teletransporta una cereza a una posición aleatoria válida
     */
    public boolean teletransportarCereza(int fila, int col) throws BadDopoException {
        System.out.println("\n[GRAFO] ========== INICIO TELETRANSPORTE ==========");
        System.out.println("[GRAFO] Cereza en posición: (" + fila + "," + col + ")");
        
        Nodo nodoActual = getNodo(fila, col);
        Celda celdaActual = nodoActual.getCelda();
        
        // Verificar que sea una celda con cereza
        if (!celdaActual.getTipo().equals("CF")) {
            System.err.println("[GRAFO] ✗ La celda no contiene una cereza. Tipo: " + celdaActual.getTipo());
            return false;
        }
        
        Elemento elemento = celdaActual.getElemento();
        if (elemento == null) {
            System.err.println("[GRAFO] ✗ No hay elemento en la celda");
            return false;
        }
        
        // ===== BUSCAR POSICIONES DISPONIBLES =====
        ArrayList<int[]> posicionesDisponibles = new ArrayList<>();
        
        for (int i = 1; i < filas - 1; i++) {
            for (int j = 1; j < columnas - 1; j++) {
                Nodo nodo = getNodo(i, j);
                Celda celda = nodo.getCelda();
                String tipo = celda.getTipo();
                
                // ✅ Criterios basados en TIPO de celda:
                // Puede ir a celdas vacías (V) o celdas con frutas (F, CF, FP, etc.)
                // NO puede ir a: Bordes (B), Hielo (H), Enemigos (M, T, C), Helados (VH, CH, F)
                boolean esDisponible = tipo.equals("V") ||      
                                    tipo.equals("N");         
                
                if (esDisponible) {
                    posicionesDisponibles.add(new int[]{i, j});
                }
            }
        }
        
        System.out.println("[GRAFO] Posiciones disponibles encontradas: " + posicionesDisponibles.size());
        
        if (posicionesDisponibles.isEmpty()) {
            System.err.println("[GRAFO] ✗ NO HAY POSICIONES DISPONIBLES");
            return false;
        }
        
        // ✅ Remover la posición actual para forzar movimiento
        final int filaActual = fila;
        final int colActual = col;
        posicionesDisponibles.removeIf(pos -> pos[0] == filaActual && pos[1] == colActual);
        
        if (posicionesDisponibles.isEmpty()) {
            System.err.println("[GRAFO] ✗ Solo está disponible la posición actual");
            return false;
        }
        
        System.out.println("[GRAFO] Posiciones diferentes a la actual: " + posicionesDisponibles.size());
        
        Cereza cereza = (Cereza) elemento;
        // ===== DELEGAR A LA CEREZA LA SELECCIÓN DE POSICIÓN =====
        int[] nuevaPosicion = cereza.calcularPosicionAleatoria(posicionesDisponibles);
        
        if (nuevaPosicion == null) {
            System.err.println("[GRAFO] ✗ La cereza no pudo calcular una posición válida");
            return false;
        }

        cereza.setFila(nuevaPosicion[0]);
        cereza.setColumna(nuevaPosicion[1]);
        
        System.out.println("[GRAFO] Posición elegida por la cereza: (" + nuevaPosicion[0] + "," + nuevaPosicion[1] + ")");
    
        // ===== REALIZAR EL MOVIMIENTO =====
        
        // 1. Limpiar posición actual
        Nodo nodoDestino = getNodo(nuevaPosicion[0], nuevaPosicion[1]);
        Celda celdaDestino = nodoDestino.getCelda();
    
        celdaActual.setElemento(null, creador);
        celdaActual.setTipo("V");  // Restaurar a vacío
        
        // 3. Colocar en nueva posición
        celdaDestino.setElemento(elemento, creador);
        celdaDestino.setTipo("CF");  // Marcar como celda con cereza
        
        System.out.println("[GRAFO] ✓ TELETRANSPORTE EXITOSO: (" + fila + "," + col + 
                        ") → (" + nuevaPosicion[0] + "," + nuevaPosicion[1] + ")");
        System.out.println("[GRAFO] ========================================\n");
        
        return true;
    }

    /**
     * Remueve el elemento en la posición solo si coincide con la instancia esperada (evita sobrescribir helados).
     */
    public void removeElementoIfMatches(int fila, int col, Elemento esperado){
        Nodo nodo = getNodo(fila, col);
        if (nodo != null) {
            try {
                Celda celda = nodo.getCelda();
                if (celda.getElemento() == esperado) {
                    celda.setElementoConTipo("V", creador);
                    
                    // ===== AGREGAR ESTAS LÍNEAS =====
                    if (esperado instanceof Fruta) {
                        frutasActivas.remove(esperado);
                        System.out.println("[GRAFO] Fruta removida de lista activa en (" + fila + "," + col + ")");
                    }
                } else {
                    System.out.println("[GRAFO] No se removió elemento en ("+fila+","+col+") porque no coincide con la instancia esperada");
                }
            } catch (BadDopoException e) {
                System.err.println("Error al remover elemento: " + e.getMessage());
            }
        }
    }

}