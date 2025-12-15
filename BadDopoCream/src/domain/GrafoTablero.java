package domain;

import java.util.*;

/**
 * Clase grafo tablero que obtiene la lógica de un grafo para la ejecución del juego con nodos y conexiones.
 * @version 06.12.2025 - CORREGIDO para movimiento de helados
 * @author Maria Katalina Leyva Díaz y Michelle Dayana Ruíz Carranza.
 */
public class GrafoTablero {
    private static final boolean DEBUG = true;
    private Nodo[][] nodos;
    private int filas;
    private int columnas;
    private CreadorElemento creador;
    private ArrayList<Fruta> frutasActivas;
    private java.util.Map<String, Elemento> elementosSubyacentes = new java.util.HashMap<>();
    private java.util.Map<String, String> tiposSubyacentes = new java.util.HashMap<>();
    // Flag para controlar si las piñas deben moverse cuando se mueve un helado
    private boolean moverPinasAlMoverHelado = false;


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
            // Actualizar el tipo/elemento de la celda existente en lugar de reemplazar el Nodo
            // Reemplazar el Nodo rompe las referencias de vecinos (vecinos referencian instancias antiguas)
            try {
                nodo.getCelda().setElementoConTipo(tipo, creador);
                nodo.getCelda().setTipo(tipo);
            } catch (BadDopoException ex) {
                // Si falla la creación del elemento, al menos actualizamos el tipo
                nodo.getCelda().setTipo(tipo);
            }
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

    public boolean solicitarMovimientoHacia(int fila, int columna, String direccion) throws BadDopoException {
        Nodo nodo = getNodo(fila, columna);
        if (nodo == null) {
            if (DEBUG) System.out.println("[GRAFO] ERROR: Nodo en posición (" + fila + "," + columna + ") es null");
            return false;
        }

        if (direccion == null || direccion.trim().isEmpty()) {
            throw new BadDopoException(BadDopoException.DIRECCION_INVALIDA);
        }

        if (DEBUG) System.out.println("[GRAFO] Solicitando movimiento desde (" + fila + "," + columna + ") hacia " + direccion);

        int[] nuevaPosicion = new int[2];
        switch (direccion.toUpperCase()) {
            case "ARRIBA":
                nuevaPosicion = moverArriba(fila, columna);
                break;
            case "ABAJO":
                nuevaPosicion = moverAbajo(fila, columna);
                break;
            case "DERECHA":
                nuevaPosicion = moverDerecha(fila, columna);
                break;
            case "IZQUIERDA":
                nuevaPosicion = moverIzquierda(fila, columna);
                break;
            default:
                throw new BadDopoException(BadDopoException.DIRECCION_DESCONOCIDA);
        }

        if (DEBUG) System.out.println("[GRAFO] Nueva posición calculada: (" + nuevaPosicion[0] + "," + nuevaPosicion[1] + ")");

        Nodo nodoDestino = getNodo(nuevaPosicion[0], nuevaPosicion[1]);
        if (nodoDestino == null) {
            if (DEBUG) System.out.println("[GRAFO] ERROR: Nodo destino es null");
            return false;
        }

        if (!nodo.getVecinos().contains(nodoDestino)) {
            if (DEBUG) System.out.println("[GRAFO] ERROR: Nodo destino no es vecino");
            return false;
        }

        ejecutarMovimiento(nodo, nodoDestino, direccion);
        if (DEBUG) System.out.println("[GRAFO] Movimiento ejecutado exitosamente");
        return true;
    }

    private void ejecutarMovimiento(Nodo origen, Nodo destino, String direccion) throws BadDopoException {
        Celda celdaOrigen = origen.getCelda();
        Celda celdaDestino = destino.getCelda();
        Elemento elementoAMover = celdaOrigen.getElemento();
        Elemento elementoEnDestino = celdaDestino.getElemento();

        if (DEBUG) System.out.println("[GRAFO] Ejecutando movimiento - Tipo origen: " + celdaOrigen.getTipo() + ", Tipo destino: " + celdaDestino.getTipo());

        String tipoDestino = celdaDestino.getTipo();
        if ("H".equals(tipoDestino) || "B".equals(tipoDestino)) {
            // No transitable: mantener en posición
            if (DEBUG) System.out.println("[GRAFO] Destino no transitable (Hielo o Borde)");
            return;
        }

        // Si el destino tiene fruta (BF, CF, CAF, U, P)
        if ("BF".equals(tipoDestino) || "CF".equals(tipoDestino) || "CAF".equals(tipoDestino) || "U".equals(tipoDestino) || "P".equals(tipoDestino)) {
            // Si quien se mueve es un Enemigo, permitir pasar por encima sin aumentar puntaje
            if (elementoAMover.esEnemigo()) {
                // Guardar el elemento subyacente (fruta) para restaurarlo cuando el enemigo salga
                String keyDestino = celdaDestino.getFila() + "_" + celdaDestino.getCol();
                if (elementoEnDestino != null) {
                    elementosSubyacentes.put(keyDestino, elementoEnDestino);
                    tiposSubyacentes.put(keyDestino, celdaDestino.getTipo());
                }
                // Colocar al enemigo en la celda destino y marcar tipo de enemigo
                celdaDestino.setElemento(elementoAMover, creador);
                String codigo = elementoAMover.codigoTipo();
                celdaDestino.setTipo(codigo);

                // Restaurar la celda origen: si había un subyacente guardado, restaurarlo
                String keyOrigen = celdaOrigen.getFila() + "_" + celdaOrigen.getCol();
                if (tiposSubyacentes.containsKey(keyOrigen)) {
                    Elemento sub = elementosSubyacentes.remove(keyOrigen);
                    String tipoSub = tiposSubyacentes.remove(keyOrigen);
                    if (sub != null) {
                        celdaOrigen.setElemento(sub, creador);
                        celdaOrigen.setTipo(tipoSub);
                    } else {
                        celdaOrigen.setElementoConTipo("V", creador);
                    }
                } else {
                    celdaOrigen.setElementoConTipo("V", creador);
                }

                elementoAMover.setFila(celdaDestino.getFila());
                elementoAMover.setColumna(celdaDestino.getCol());
                elementoAMover.setCelda(celdaDestino);
            } else {
                // Comportamiento original para helados u otros elementos
                if (elementoEnDestino != null) {
                    elementoAMover.aumentarPuntaje(elementoEnDestino.getGanancia());
                    if (DEBUG) System.out.println("[GRAFO] Fruta recolectada: +" + elementoEnDestino.getGanancia() + " puntos");
                }
                celdaDestino.setElemento(elementoAMover, creador);
                if (elementoAMover.esHelado()) {
                    String tipoHelado = obtenerCodigoSabor(((Helado) elementoAMover).getSabor());
                    celdaDestino.setTipo(tipoHelado);
                } else {
                    celdaDestino.setTipo("V");
                }
                celdaOrigen.setElementoConTipo("V", creador);

                elementoAMover.setFila(celdaDestino.getFila());
                elementoAMover.setColumna(celdaDestino.getCol());
                elementoAMover.setCelda(celdaDestino);
            }
        } else {
            // Espacio vacío u otro: mover normalmente
            celdaDestino.setElemento(elementoAMover, creador);
            // Si el elemento que se mueve es un enemigo, asegurarnos de marcar el tipo de la celda
            if (elementoAMover.esEnemigo()) {
                String codigo = elementoAMover.codigoTipo();
                celdaDestino.setTipo(codigo);
            }
            celdaOrigen.setElementoConTipo("V", creador);

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
        System.out.println("[GRAFO] === ejecutarAccion llamado ===");
        System.out.println("[GRAFO] nodoVecino: " + (nodoVecino != null ? "presente" : "null"));
        System.out.println("[GRAFO] ultimaDireccion: " + ultimaDireccion);
        System.out.println("[GRAFO] elementoActual: " + elementoActual);
        
        if (nodoVecino == null) {
            System.out.println("[GRAFO] nodoVecino es null - retornando");
            return;
        }

        Celda celda = nodoVecino.getCelda();
        int fila = celda.getFila();
        int columna = celda.getCol();
        Elemento elemento = celda.getElemento();
        String tipo = celda.getTipo();
        
        System.out.println("[GRAFO] Celda analizada: (" + fila + "," + columna + ")");
        System.out.println("[GRAFO] Tipo de celda: " + tipo);
        System.out.println("[GRAFO] Elemento en celda: " + (elemento != null ? elemento.getClass().getSimpleName() : "null"));

        // Si es hielo, romper
        if (tipo.equals("H")) {
            System.out.println("[GRAFO] Ejecutando: romperHielo");
            romperHielo(fila, columna, ultimaDireccion, elementoActual);
        } 
        // Si está vacío, crear hielo
        else if (tipo.equals("V") || tipo.equals("N")) {
            System.out.println("[GRAFO] Ejecutando: crearHielo");
            crearHielo(fila, columna, ultimaDireccion, elementoActual);
        } 
        else {
            System.out.println("[GRAFO] Celda no vacía y no es hielo (" + tipo + ") - no se hace nada");
        }
    }

    public void romperHielo(int fila, int columna, String ultimaDireccion, Elemento elementoActual) throws BadDopoException { 
        Nodo nodo = getNodo(fila, columna);
        if (nodo == null) return;
        Celda celda = nodo.getCelda();

        // Si el actor es un enemigo que solo rompe un bloque por vez,
        // romper únicamente la primera celda de hielo y regresar.
        if (elementoActual != null && elementoActual.esEnemigo()) {
            Enemigo enemigo = (Enemigo) elementoActual;
            if (enemigo.rompeUnBloquePorVez()) {
                if (celda.getTipo().equals("H")) {
                    elementoActual.romperHielo(celda, creador);
                }
                return;
            }
        }

        // Comportamiento por defecto: romper todos los bloques de hielo consecutivos en la dirección
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
        System.out.println("[GRAFO] === Iniciando creación de hielo ===");
        System.out.println("[GRAFO] Posición inicial: (" + fila + "," + columna + ")");
        System.out.println("[GRAFO] Dirección: " + ultimaDireccion);
        
        int celdasCreadas = 0;
        
        while (true) {
            Nodo nodo = getNodo(fila, columna);
            
            // Verificar límites del tablero
            if (nodo == null) {
                System.out.println("[GRAFO] Límite del tablero alcanzado");
                break;
            }
            
            Celda celda = nodo.getCelda();
            String tipo = celda.getTipo();
            Elemento elemento = celda.getElemento();
            
            System.out.println("[GRAFO] Analizando celda (" + fila + "," + columna + ") - Tipo: " + tipo);
            
            // Solo crear hielo en celdas vacías y transitables
            if (!tipo.equals("V")) {
                System.out.println("[GRAFO] Celda no está vacía (tipo: " + tipo + ") - deteniendo");
                break;
            }
            
            if (elemento != null && !elemento.esTransitable()) {
                System.out.println("[GRAFO] Celda no es transitable - deteniendo");
                break;
            }
            
                // Sólo permitir que los Helados construyan hielo y sólo si la celda permite reconstrucción.
                if (elementoActual == null || !elementoActual.esHelado() || !celda.permiteReconstruccion()) {
                    System.out.println("[GRAFO] Ignorado: solo helados pueden crear hielo (actor=" +
                            (elementoActual == null ? "null" : elementoActual.getClass().getSimpleName()) + ")");
                    break;
                }

            // Crear hielo en esta celda
            System.out.println("[GRAFO] Creando hielo en (" + fila + "," + columna + ")");
            elementoActual.crearHielo(celda, creador);
            try {
                if (elementoActual != null && elementoActual.esHelado()) {
                    // Permitir reconstrucción cuando un Helado crea el hielo
                    celda.setPermiteReconstruccion(true);
                }
            } catch (Exception ex) {
                // No crítico
            }
            celdasCreadas++;
            
            // Avanzar a la siguiente posición en la dirección
            switch (ultimaDireccion) {
                case "DERECHA":
                    columna++;
                    break;
                case "IZQUIERDA":
                    columna--;
                    break;
                case "ARRIBA":
                    fila--;
                    break;
                case "ABAJO":
                    fila++;
                    break;
            }
        }
    }


    public void actualizarEnemigos(Helado jugador) throws BadDopoException {
        VistaTablero vista = new VistaTableroImpl();
        java.util.Set<Enemigo> procesados = new java.util.HashSet<>();
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                Nodo nodo = nodos[f][c];
                if (nodo == null) continue;
                Elemento el = nodo.getCelda().getElemento();
                if (el != null && el.esEnemigo()) {
                    Enemigo enemigo = (Enemigo) el;
                    // Evitar procesar el mismo enemigo más de una vez por ciclo (puede moverse dentro del loop)
                    if (procesados.contains(enemigo)) continue;
                    if (DEBUG) System.out.println("[GRAFO] Actualizando enemigo " + enemigo.getClass().getSimpleName() +
                            " en (" + f + "," + c + ") ultimaDir=" + enemigo.getUltimaDireccion());
                    enemigo.ejecutarComportamiento(this, vista, jugador);
                    procesados.add(enemigo);
                }
            }
        }
    }

    /** Setter para activar o desactivar la mecánica: "las piñas se mueven cuando se mueve un helado" */
    public void setMoverPinasAlMoverHelado(boolean activar) {
        this.moverPinasAlMoverHelado = activar;
        if (DEBUG) System.out.println("[GRAFO] moverPinasAlMoverHelado = " + activar);
    }

    public boolean procesarMovimientoHelado(int filaOrigen, int columnaOrigen, String direccion, Helado jugador) throws BadDopoException {
        boolean moved = solicitarMovimientoHacia(filaOrigen, columnaOrigen, direccion);
        if (!moved) return false;
        // Solo mover piñas si la mecánica está activada (ej: nivel 2 fase 2)
        if (!moverPinasAlMoverHelado) return true;

        java.util.List<int[]> posicionesPinas = new java.util.ArrayList<>();
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                Nodo nodo = nodos[f][c];
                if (nodo == null) continue;
                Elemento el = nodo.getCelda().getElemento();
                if (nodo.getCelda().getTipo() != null && nodo.getCelda().getTipo().equals("P")) posicionesPinas.add(new int[]{f, c});
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
                    solicitarMovimientoHacia(f, c, mejor);
                }
            } catch (BadDopoException ex) {
                // Ignorar errores de movimiento individuales
            }
        }
        return true;
    }

    public int[] calcularNuevaPosicion(int f, int c, String direccion) throws BadDopoException {
        if (direccion == null) return new int[]{f, c};
        if (direccion.equals("ARRIBA")) return moverArriba(f, c);
        if (direccion.equals("ABAJO")) return moverAbajo(f, c);
        if (direccion.equals("DERECHA")) return moverDerecha(f, c);
        if (direccion.equals("IZQUIERDA")) return moverIzquierda(f, c);
        return new int[]{f, c};
    }

    public boolean puedeMoverEn(int f, int c, String direccion) throws BadDopoException {
        int[] dest = calcularNuevaPosicion(f, c, direccion);
        if (!esPosicionValida(dest[0], dest[1])) return false;
        Nodo nodo = getNodo(dest[0], dest[1]);
        return nodo != null && nodo.getCelda().esTransitable();
    }

    public String calcularDireccionHaciaObjetivo(int f0, int c0, int objetivoF, int objetivoC, boolean permitirHielo) {
        class Estado {
            int fila;
            int col;
            String primerPaso;
            Estado(int fila, int col, String primerPaso) { this.fila = fila; this.col = col; this.primerPaso = primerPaso; }
        }

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

    /** Teletransporta todas las piñas encontradas en el grafo */
    public void teletransportarPinas() throws BadDopoException {
        ArrayList<int[]> posicionesPinas = new ArrayList<>();
        for (int i = 0; i < nodos.length; i++) {
            for (int j = 0; j < nodos[0].length; j++) {
                Nodo n = nodos[i][j];
                Celda c = n.getCelda();
                if (c.getTipo().equals("P")) {
                    posicionesPinas.add(new int[]{i, j});
                }
            }
        }

        for (int[] pos : posicionesPinas) {
            teletransportarPina(pos[0], pos[1]);
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

    public void actualizarRepresentacion(int fila, int columna, String tipo) throws BadDopoException {
       nodos[fila][columna] = new Nodo(fila, columna, tipo, creador);
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
        
        // 1. Limpiar posición actual y colocar en destino
        Nodo nodoDestino = getNodo(nuevaPosicion[0], nuevaPosicion[1]);
        Celda celdaDestino = nodoDestino.getCelda();

        // Usar setElementoConTipo para asegurar que la celda quede en estado consistente
        celdaActual.setElementoConTipo("V", creador);

        // 3. Colocar en nueva posición
        celdaDestino.setElemento(elemento, creador);
        celdaDestino.setTipo("CF");  // Marcar como celda con cereza
        
        System.out.println("[GRAFO] ✓ TELETRANSPORTE EXITOSO: (" + fila + "," + col + 
                        ") → (" + nuevaPosicion[0] + "," + nuevaPosicion[1] + ")");
        System.out.println("[GRAFO] ========================================\n");
        
        return true;
    }

    /** Teletransporta una piña a una posición aleatoria válida (solo celdas vacías) */
    public boolean teletransportarPina(int fila, int col) throws BadDopoException {
        System.out.println("\n[GRAFO] ========== INICIO TELETRANSPORTE PINA ==========");
        System.out.println("[GRAFO] Piña en posición: (" + fila + "," + col + ")");

        Nodo nodoActual = getNodo(fila, col);
        Celda celdaActual = nodoActual.getCelda();

        if (!celdaActual.getTipo().equals("P")) {
            System.err.println("[GRAFO] ✗ La celda no contiene una piña. Tipo: " + celdaActual.getTipo());
            return false;
        }

        Elemento elemento = celdaActual.getElemento();
        if (elemento == null) {
            System.err.println("[GRAFO] ✗ No hay elemento en la celda");
            return false;
        }

        ArrayList<int[]> posicionesDisponibles = new ArrayList<>();
        // Buscar solo celdas internas (excluye bordes) y únicamente celdas vacías 'V'
        for (int i = 1; i < filas - 1; i++) {
            for (int j = 1; j < columnas - 1; j++) {
                Nodo nodo = getNodo(i, j);
                Celda celda = nodo.getCelda();
                String tipo = celda.getTipo();
                if (tipo.equals("V")) {
                    posicionesDisponibles.add(new int[]{i, j});
                }
            }
        }

        System.out.println("[GRAFO] Posiciones disponibles encontradas para piña: " + posicionesDisponibles.size());

        if (posicionesDisponibles.isEmpty()) {
            System.err.println("[GRAFO] ✗ NO HAY POSICIONES DISPONIBLES PARA PINA");
            return false;
        }

        final int filaActual = fila;
        final int colActual = col;
        posicionesDisponibles.removeIf(pos -> pos[0] == filaActual && pos[1] == colActual);

        if (posicionesDisponibles.isEmpty()) {
            System.err.println("[GRAFO] ✗ Solo está disponible la posición actual para la piña");
            return false;
        }

        Pina pina = (Pina) elemento;
        int[] nuevaPosicion = pina.calcularPosicionAleatoria(posicionesDisponibles);
        if (nuevaPosicion == null) {
            System.err.println("[GRAFO] ✗ La piña no pudo calcular una posición válida");
            return false;
        }

        pina.setFila(nuevaPosicion[0]);
        pina.setColumna(nuevaPosicion[1]);

        System.out.println("[GRAFO] Posición elegida por la piña: (" + nuevaPosicion[0] + "," + nuevaPosicion[1] + ")");

        Nodo nodoDestino = getNodo(nuevaPosicion[0], nuevaPosicion[1]);
        Celda celdaDestino = nodoDestino.getCelda();

        celdaActual.setElementoConTipo("V", creador);

        celdaDestino.setElemento(elemento, creador);
        celdaDestino.setTipo("P");

        System.out.println("[GRAFO] ✓ TELETRANSPORTE PINA EXITOSO: (" + fila + "," + col + 
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
                } else {
                    if (DEBUG) System.out.println("[GRAFO] No se removió elemento en ("+fila+","+col+") porque no coincide con la instancia esperada");
                }
            } catch (BadDopoException e) {
                System.err.println("Error al remover elemento: " + e.getMessage());
            }
        }
    }

}