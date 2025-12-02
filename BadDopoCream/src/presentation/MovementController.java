package Presentation;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Controlador de movimiento para los jugadores
 * Maneja teclas presionadas simultáneamente y actualiza posiciones
 */
public class MovementController implements KeyListener {

    private BoardPanel boardPanel;
    private String modo;
    private String sabor1;
    private String sabor2;

    // Posiciones actuales de los jugadores
    private int jugador1Fila;
    private int jugador1Col;
    private int jugador2Fila;
    private int jugador2Col;

    // Direcciones actuales (para las imágenes)
    private String direccionJ1 = "Abajo";
    private String direccionJ2 = "Abajo";

    // Set de teclas presionadas actualmente
    private Set<Integer> teclasPresionadas;

    // Timer para movimiento continuo
    private Timer movementTimer;
    private static final int VELOCIDAD_MOVIMIENTO = 150; // ms entre movimientos

    // Constantes de direcciones
    public static final String ARRIBA = "Detras";
    public static final String ABAJO = "Abajo";
    public static final String IZQUIERDA = "Izquierda";
    public static final String DERECHA = "Derecha";

    public MovementController(BoardPanel boardPanel, String modo, String sabor1, String sabor2) {
        this.boardPanel = boardPanel;
        this.modo = modo;
        this.sabor1 = sabor1;
        this.sabor2 = sabor2;
        this.teclasPresionadas = new HashSet<>();

        inicializarPosicionesJugadores();
        inicializarTimer();
    }

    /**
     * Encuentra las posiciones iniciales de los jugadores en el tablero
     */
    private void inicializarPosicionesJugadores() {
        for (int i = 0; i < boardPanel.getFilas(); i++) {
            for (int j = 0; j < boardPanel.getColumnas(); j++) {
                int elemento = boardPanel.getElemento(i, j);
                if (elemento == BoardPanel.JUGADOR1) {
                    jugador1Fila = i;
                    jugador1Col = j;
                    System.out.println("Jugador 1 encontrado en: (" + i + "," + j + ")");
                } else if (elemento == BoardPanel.JUGADOR2) {
                    jugador2Fila = i;
                    jugador2Col = j;
                    System.out.println("Jugador 2 encontrado en: (" + i + "," + j + ")");
                }
            }
        }
    }

    /**
     * Inicializa el timer que procesa movimiento continuo
     */
    private void inicializarTimer() {
        movementTimer = new Timer(VELOCIDAD_MOVIMIENTO, e -> {
            procesarMovimiento();
        });
        movementTimer.start();
    }

    /**
     * Procesa el movimiento basado en las teclas presionadas
     */
    private void procesarMovimiento() {
        // Movimiento Jugador 1 (WASD)
        if (debeControlarJugador1()) {
            if (teclasPresionadas.contains(KeyEvent.VK_W)) {
                moverJugador1(ARRIBA);
            }
            if (teclasPresionadas.contains(KeyEvent.VK_S)) {
                moverJugador1(ABAJO);
            }
            if (teclasPresionadas.contains(KeyEvent.VK_A)) {
                moverJugador1(IZQUIERDA);
            }
            if (teclasPresionadas.contains(KeyEvent.VK_D)) {
                moverJugador1(DERECHA);
            }
            // Poder con F
            if (teclasPresionadas.contains(KeyEvent.VK_F)) {
                usarPoderJugador1();
            }
        }

        // Movimiento Jugador 2 (Flechas)
        if (debeControlarJugador2()) {
            if (teclasPresionadas.contains(KeyEvent.VK_UP)) {
                moverJugador2(ARRIBA);
            }
            if (teclasPresionadas.contains(KeyEvent.VK_DOWN)) {
                moverJugador2(ABAJO);
            }
            if (teclasPresionadas.contains(KeyEvent.VK_LEFT)) {
                moverJugador2(IZQUIERDA);
            }
            if (teclasPresionadas.contains(KeyEvent.VK_RIGHT)) {
                moverJugador2(DERECHA);
            }
            // Poder con Espacio
            if (teclasPresionadas.contains(KeyEvent.VK_SPACE)) {
                usarPoderJugador2();
            }
        }
    }

    /**
     * Determina si se debe controlar al jugador 1
     */
    private boolean debeControlarJugador1() {
        return modo.equals("Jugador vs Jugador") || modo.equals("Jugador vs Máquina");
    }

    /**
     * Determina si se debe controlar al jugador 2
     */
    private boolean debeControlarJugador2() {
        return modo.equals("Un solo jugador") ||
                modo.equals("Jugador vs Jugador") ||
                modo.equals("Jugador vs Máquina");
    }

    /**
     * Mueve al jugador 1 en la dirección especificada
     */
    private void moverJugador1(String direccion) {
        direccionJ1 = direccion;

        int nuevaFila = jugador1Fila;
        int nuevaCol = jugador1Col;

        switch (direccion) {
            case ARRIBA:
                nuevaFila--;
                break;
            case ABAJO:
                nuevaFila++;
                break;
            case IZQUIERDA:
                nuevaCol--;
                break;
            case DERECHA:
                nuevaCol++;
                break;
        }

        // Verificar si el movimiento es válido
        if (esMovimientoValido(nuevaFila, nuevaCol)) {
            // Actualizar posición en el tablero
            boardPanel.moverElemento(jugador1Fila, jugador1Col, nuevaFila, nuevaCol, sabor1, direccion);
            jugador1Fila = nuevaFila;
            jugador1Col = nuevaCol;
        } else {
            // Solo cambiar dirección sin moverse
            boardPanel.cambiarDireccion(jugador1Fila, jugador1Col, sabor1, direccion);
        }
    }

    /**
     * Mueve al jugador 2 en la dirección especificada
     */
    private void moverJugador2(String direccion) {
        direccionJ2 = direccion;

        int nuevaFila = jugador2Fila;
        int nuevaCol = jugador2Col;

        switch (direccion) {
            case ARRIBA:
                nuevaFila--;
                break;
            case ABAJO:
                nuevaFila++;
                break;
            case IZQUIERDA:
                nuevaCol--;
                break;
            case DERECHA:
                nuevaCol++;
                break;
        }

        // Verificar si el movimiento es válido
        if (esMovimientoValido(nuevaFila, nuevaCol)) {
            // Actualizar posición en el tablero
            String saborActual = modo.equals("Un solo jugador") ? sabor1 : sabor2;
            boardPanel.moverElemento(jugador2Fila, jugador2Col, nuevaFila, nuevaCol, saborActual, direccion);
            jugador2Fila = nuevaFila;
            jugador2Col = nuevaCol;
        } else {
            // Solo cambiar dirección sin moverse
            String saborActual = modo.equals("Un solo jugador") ? sabor1 : sabor2;
            boardPanel.cambiarDireccion(jugador2Fila, jugador2Col, saborActual, direccion);
        }
    }

    /**
     * Verifica si un movimiento a la posición es válido
     */
    private boolean esMovimientoValido(int fila, int col) {
        // Verificar límites
        if (fila < 0 || fila >= boardPanel.getFilas() ||
                col < 0 || col >= boardPanel.getColumnas()) {
            return false;
        }

        int elemento = boardPanel.getElemento(fila, col);

        // Puede moverse a espacios vacíos o frutas
        return elemento == BoardPanel.VACIO || elemento == BoardPanel.FRUTA;
    }

    /**
     * Usar poder del jugador 1 (crear bloque de hielo)
     */
    private void usarPoderJugador1() {
        // Crear bloque de hielo en la dirección que mira
        int bloqueF = jugador1Fila;
        int bloqueC = jugador1Col;

        switch (direccionJ1) {
            case ARRIBA: bloqueF--; break;
            case ABAJO: bloqueF++; break;
            case IZQUIERDA: bloqueC--; break;
            case DERECHA: bloqueC++; break;
        }

        boardPanel.crearBloqueHielo(bloqueF, bloqueC);
    }

    /**
     * Usar poder del jugador 2 (crear bloque de hielo)
     */
    private void usarPoderJugador2() {
        int bloqueF = jugador2Fila;
        int bloqueC = jugador2Col;

        switch (direccionJ2) {
            case ARRIBA: bloqueF--; break;
            case ABAJO: bloqueF++; break;
            case IZQUIERDA: bloqueC--; break;
            case DERECHA: bloqueC++; break;
        }

        boardPanel.crearBloqueHielo(bloqueF, bloqueC);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        teclasPresionadas.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        teclasPresionadas.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // No usado
    }

    /**
     * Detiene el timer de movimiento
     */
    public void detener() {
        if (movementTimer != null) {
            movementTimer.stop();
        }
    }

    /**
     * Reanuda el timer de movimiento
     */
    public void reanudar() {
        if (movementTimer != null) {
            movementTimer.start();
        }
    }

    /**
     * Actualiza el modo de juego
     */
    public void actualizarModo(String nuevoModo) {
        this.modo = nuevoModo;
    }

    /**
     * Actualiza los sabores
     */
    public void actualizarSabores(String sabor1, String sabor2) {
        this.sabor1 = sabor1;
        this.sabor2 = sabor2;
    }

    /**
     * Reinicia las posiciones al cargar un nuevo nivel
     */
    public void reiniciarPosiciones() {
        teclasPresionadas.clear();
        direccionJ1 = "Abajo";
        direccionJ2 = "Abajo";
        inicializarPosicionesJugadores();
    }
}