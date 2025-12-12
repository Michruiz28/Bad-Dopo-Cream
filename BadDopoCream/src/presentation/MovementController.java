package presentation;

import domain.BadDopoCream;
import domain.BadDopoException;
import domain.Helado;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Controlador de movimientos del teclado para los helados
 * VERSIÓN CORREGIDA con mejor debugging
 */
public class MovementController extends KeyAdapter {

    private BoardPanel boardPanel;
    private BadDopoCream juego;
    private String modo;

    // Teclas para jugador 1 (WASD + F para acción)
    private static final int TECLA_P1_ARRIBA = KeyEvent.VK_W;
    private static final int TECLA_P1_ABAJO = KeyEvent.VK_S;
    private static final int TECLA_P1_IZQUIERDA = KeyEvent.VK_A;
    private static final int TECLA_P1_DERECHA = KeyEvent.VK_D;
    private static final int TECLA_P1_EJECUTAR_ACCION = KeyEvent.VK_F;

    // Teclas para jugador 2 (Flechas + Espacio para acción)
    private static final int TECLA_P2_ARRIBA = KeyEvent.VK_UP;
    private static final int TECLA_P2_ABAJO = KeyEvent.VK_DOWN;
    private static final int TECLA_P2_IZQUIERDA = KeyEvent.VK_LEFT;
    private static final int TECLA_P2_DERECHA = KeyEvent.VK_RIGHT;
    private static final int TECLA_P2_EJECUTAR_ACCION = KeyEvent.VK_SPACE;

    public MovementController(BoardPanel boardPanel, String modo) {
        this.boardPanel = boardPanel;
        this.juego = boardPanel.getJuego();
        this.modo = modo;
        System.out.println("[CONTROLLER] MovementController creado para modo: " + modo);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        String keyText = KeyEvent.getKeyText(keyCode);

        System.out.println("\n[CONTROLLER] ========== TECLA PRESIONADA ==========");
        System.out.println("[CONTROLLER] Código: " + keyCode + " (" + keyText + ")");

        // Actualizar referencia al juego (por si cambió)
        if (juego == null || juego != boardPanel.getJuego()) {
            juego = boardPanel.getJuego();
            System.out.println("[CONTROLLER] Actualizando referencia al juego");
        }

        // Verificaciones de estado
        if (juego == null) {
            System.out.println("[CONTROLLER] ERROR: juego es null - no se puede procesar movimiento");
            return;
        }

        if (!juego.isJuegoIniciado()) {
            System.out.println("[CONTROLLER] ERROR: juego no iniciado");
            return;
        }

        if (juego.isPausado()) {
            System.out.println("[CONTROLLER] Juego pausado - movimiento ignorado");
            return;
        }

        if (juego.isJuegoTerminado()) {
            System.out.println("[CONTROLLER] Juego terminado - movimiento ignorado");
            return;
        }

        System.out.println("[CONTROLLER] Estado del juego: OK para procesar movimiento");

        try {
            boolean teclaReconocida = false;

            // Controles del Jugador 1
            if (esJugadorHumano1()) {
                if (esTeclaJugador1(keyCode)) {
                    System.out.println("[CONTROLLER] Tecla reconocida para Jugador 1");
                    manejarTeclasJugador1(keyCode);
                    teclaReconocida = true;
                }
            }

            // Controles del Jugador 2
            if (esJugadorHumano2()) {
                if (esTeclaJugador2(keyCode)) {
                    System.out.println("[CONTROLLER] Tecla reconocida para Jugador 2");
                    manejarTeclasJugador2(keyCode);
                    teclaReconocida = true;
                }
            }

            if (!teclaReconocida) {
                System.out.println("[CONTROLLER] Tecla no reconocida o no aplicable al modo actual");
            }

            System.out.println("[CONTROLLER] =======================================\n");

        } catch (BadDopoException ex) {
            System.err.println("[CONTROLLER] ERROR en movimiento: " + ex.getMessage());
            ex.printStackTrace();
            mostrarError(ex.getMessage());
        } catch (Exception ex) {
            System.err.println("[CONTROLLER] ERROR inesperado: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Verifica si la tecla corresponde al jugador 1
     */
    private boolean esTeclaJugador1(int keyCode) {
        return keyCode == TECLA_P1_ARRIBA ||
            keyCode == TECLA_P1_ABAJO ||
            keyCode == TECLA_P1_IZQUIERDA ||
            keyCode == TECLA_P1_DERECHA ||
            keyCode == TECLA_P1_EJECUTAR_ACCION ||
            // Permitir también ESPACIO como acción del jugador1 cuando no hay jugador2 humano (modo un jugador)
            (keyCode == TECLA_P2_EJECUTAR_ACCION && !esJugadorHumano2());
    }

    /**
     * Verifica si la tecla corresponde al jugador 2
     */
    private boolean esTeclaJugador2(int keyCode) {
        return keyCode == TECLA_P2_ARRIBA ||
                keyCode == TECLA_P2_ABAJO ||
                keyCode == TECLA_P2_IZQUIERDA ||
                keyCode == TECLA_P2_DERECHA ||
                keyCode == TECLA_P2_EJECUTAR_ACCION;
    }

    /**
     * Maneja las teclas del jugador 1
     */
    private void manejarTeclasJugador1(int keyCode) throws BadDopoException {
        Helado helado1 = juego.getHelado1();
        if (helado1 == null) {
            System.out.println("[CONTROLLER] ERROR: Helado1 es null");
            return;
        }

        System.out.println("[CONTROLLER] Helado1 en posición: (" + helado1.getFila() + "," + helado1.getColumna() + ")");

        String accion = "";
        switch (keyCode) {
            case TECLA_P1_ARRIBA:
                accion = "ARRIBA";
                System.out.println("[CONTROLLER] Moviendo Jugador 1 hacia ARRIBA");
                juego.moverHelado1("ARRIBA");
                break;
            case TECLA_P1_ABAJO:
                accion = "ABAJO";
                System.out.println("[CONTROLLER] Moviendo Jugador 1 hacia ABAJO");
                juego.moverHelado1("ABAJO");
                break;
            case TECLA_P1_IZQUIERDA:
                accion = "IZQUIERDA";
                System.out.println("[CONTROLLER] Moviendo Jugador 1 hacia IZQUIERDA");
                juego.moverHelado1("IZQUIERDA");
                break;
            case TECLA_P1_DERECHA:
                accion = "DERECHA";
                System.out.println("[CONTROLLER] Moviendo Jugador 1 hacia DERECHA");
                juego.moverHelado1("DERECHA");
                break;
            case TECLA_P1_EJECUTAR_ACCION:
                if (helado1.getUltimaDireccion() != null) {
                    System.out.println("[CONTROLLER] Ejecutando acción en dirección: " + helado1.getUltimaDireccion());
                    juego.realizarAccion(helado1.getFila(), helado1.getColumna(), helado1.getUltimaDireccion());
                } else {
                    System.out.println("[CONTROLLER] No se puede ejecutar acción: no hay dirección previa");
                }
                break;
        }

        System.out.println("[CONTROLLER] Nueva posición Helado1: (" + helado1.getFila() + "," + helado1.getColumna() + ")");
    }

    /**
     * Maneja las teclas del jugador 2
     */
    private void manejarTeclasJugador2(int keyCode) throws BadDopoException {
        Helado helado2 = juego.getHelado2();
        if (helado2 == null) {
            System.out.println("[CONTROLLER] ERROR: Helado2 es null");
            return;
        }

        System.out.println("[CONTROLLER] Helado2 en posición: (" + helado2.getFila() + "," + helado2.getColumna() + ")");

        switch (keyCode) {
            case TECLA_P2_ARRIBA:
                System.out.println("[CONTROLLER] Moviendo Jugador 2 hacia ARRIBA");
                juego.moverHelado2("ARRIBA");
                break;
            case TECLA_P2_ABAJO:
                System.out.println("[CONTROLLER] Moviendo Jugador 2 hacia ABAJO");
                juego.moverHelado2("ABAJO");
                break;
            case TECLA_P2_IZQUIERDA:
                System.out.println("[CONTROLLER] Moviendo Jugador 2 hacia IZQUIERDA");
                juego.moverHelado2("IZQUIERDA");
                break;
            case TECLA_P2_DERECHA:
                System.out.println("[CONTROLLER] Moviendo Jugador 2 hacia DERECHA");
                juego.moverHelado2("DERECHA");
                break;
            case TECLA_P2_EJECUTAR_ACCION:
                if (helado2.getUltimaDireccion() != null) {
                    System.out.println("[CONTROLLER] Ejecutando acción en dirección: " + helado2.getUltimaDireccion());
                    juego.realizarAccion(helado2.getFila(), helado2.getColumna(), helado2.getUltimaDireccion());
                } else {
                    System.out.println("[CONTROLLER] No se puede ejecutar acción: no hay dirección previa");
                }
                break;
        }

        System.out.println("[CONTROLLER] Nueva posición Helado2: (" + helado2.getFila() + "," + helado2.getColumna() + ")");
    }

    /**
     * Verifica si el jugador 1 es humano
     */
    private boolean esJugadorHumano1() {
        boolean esHumano = modo.equals("Un jugador") ||
                modo.equals("Un solo jugador") ||
                modo.equals("Jugador vs Jugador") ||
                modo.equals("Jugador vs Máquina");
        System.out.println("[CONTROLLER] ¿Jugador 1 es humano? " + esHumano + " (modo: " + modo + ")");
        return esHumano;
    }

    /**
     * Verifica si el jugador 2 es humano
     */
    private boolean esJugadorHumano2() {
        boolean esHumano = modo.equals("Jugador vs Jugador");
        System.out.println("[CONTROLLER] ¿Jugador 2 es humano? " + esHumano + " (modo: " + modo + ")");
        return esHumano;
    }

    /**
     * Muestra un mensaje de error en consola
     */
    private void mostrarError(String mensaje) {
        System.err.println("[CONTROLLER] ERROR de movimiento: " + mensaje);
    }

    /**
     * Actualiza el modo de juego
     */
    public void setModo(String modo) {
        this.modo = modo;
        System.out.println("[CONTROLLER] Modo actualizado a: " + modo);
    }

    /**
     * Actualiza la referencia al juego
     */
    public void setJuego(BadDopoCream juego) {
        this.juego = juego;
        System.out.println("[CONTROLLER] Referencia al juego actualizada");
    }

    /**
     * Obtiene las instrucciones de control
     */
    public static String getInstrucciones() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== CONTROLES ===\n\n");
        sb.append("JUGADOR 1:\n");
        sb.append("  W - Mover arriba\n");
        sb.append("  S - Mover abajo\n");
        sb.append("  A - Mover izquierda\n");
        sb.append("  D - Mover derecha\n");
        sb.append("  F - Crear/Romper hielo\n\n");
        sb.append("JUGADOR 2:\n");
        sb.append("  ↑ - Mover arriba\n");
        sb.append("  ↓ - Mover abajo\n");
        sb.append("  ← - Mover izquierda\n");
        sb.append("  → - Mover derecha\n");
        sb.append("  ESPACIO - Crear/Romper hielo\n\n");
        sb.append("OBJETIVO:\n");
        sb.append("Recolecta todas las frutas antes de que se acabe el tiempo.\n");
        sb.append("Evita a los enemigos y usa el hielo estratégicamente.\n");
        return sb.toString();
    }
}