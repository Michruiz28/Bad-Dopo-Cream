package presentation;

import domain.BadDopoCream;
import domain.BadDopoException;
import domain.Helado;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;

/**
 * Controlador de movimientos del teclado para los helados
 */
public class MovementController extends KeyAdapter {

    private BoardPanel boardPanel;
    private BadDopoCream juego;
    private String modo;

    // Teclas para jugador 1 (WASD + Espacio/Shift)
    private static final int TECLA_P1_ARRIBA = KeyEvent.VK_W;
    private static final int TECLA_P1_ABAJO = KeyEvent.VK_S;
    private static final int TECLA_P1_IZQUIERDA = KeyEvent.VK_A;
    private static final int TECLA_P1_DERECHA = KeyEvent.VK_D;
    private static final int TECLA_P1_EJECUTAR_ACCION = KeyEvent.VK_F;

    // Teclas para jugador 2 (Flechas + NumPad)
    private static final int TECLA_P2_ARRIBA = KeyEvent.VK_UP;
    private static final int TECLA_P2_ABAJO = KeyEvent.VK_DOWN;
    private static final int TECLA_P2_IZQUIERDA = KeyEvent.VK_LEFT;
    private static final int TECLA_P2_DERECHA = KeyEvent.VK_RIGHT;
    private static final int TECLA_P2_EJECUTAR_ACCION = KeyEvent.VK_SPACE;

    public MovementController(BoardPanel boardPanel, String modo) {
        this.boardPanel = boardPanel;
        this.juego = boardPanel.getJuego();
        this.modo = modo;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (juego == null || !juego.isJuegoIniciado() || juego.isPausado() || juego.isJuegoTerminado()) {
            return;
        }

        int keyCode = e.getKeyCode();
        System.out.println("[DEBUG] keyPressed code=" + keyCode + " modo=" + modo + " juegoIniciado=" + juego.isJuegoIniciado());

        try {
            // Controles del Jugador 1 (siempre activos si es humano)
            if (esJugadorHumano1()) {
                manejarTeclasJugador1(keyCode);
            }

            // Controles del Jugador 2 (solo en modos multijugador)
            if (esJugadorHumano2()) {
                manejarTeclasJugador2(keyCode);
            }

        } catch (BadDopoException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Maneja las teclas del jugador 1
     */
    private void manejarTeclasJugador1(int keyCode) throws BadDopoException {
        Helado helado1 = juego.getHelado1();
        if (helado1 == null) return;

        switch (keyCode) {
            case TECLA_P1_ARRIBA:
                juego.moverHelado1("ARRIBA");
                break;
            case TECLA_P1_ABAJO:
                juego.moverHelado1("ABAJO");
                break;
            case TECLA_P1_IZQUIERDA:
                juego.moverHelado1("IZQUIERDA");
                break;
            case TECLA_P1_DERECHA:
                juego.moverHelado1("DERECHA");
                break;
            case TECLA_P1_EJECUTAR_ACCION:
                juego.realizarAccion(helado1.getFila(), helado1.getColumna(), helado1.getUltimaDireccion());
                break;
        }
    }

    /**
     * Maneja las teclas del jugador 2
     */
    private void manejarTeclasJugador2(int keyCode) throws BadDopoException {
        Helado helado2 = juego.getHelado2();
        if (helado2 == null) return;

        switch (keyCode) {
            case TECLA_P2_ARRIBA:
                juego.moverHelado2("ARRIBA");
                break;
            case TECLA_P2_ABAJO:
                juego.moverHelado2("ABAJO");
                break;
            case TECLA_P2_IZQUIERDA:
                juego.moverHelado2("IZQUIERDA");
                break;
            case TECLA_P2_DERECHA:
                juego.moverHelado2("DERECHA");
                break;
            case TECLA_P2_EJECUTAR_ACCION:
                juego.realizarAccion(helado2.getFila(), helado2.getColumna(), helado2.getUltimaDireccion());
                break;
        }
    }

    /**
     * Verifica si el jugador 1 es humano
     */
    private boolean esJugadorHumano1() {
        return modo.equals("Un jugador") || modo.equals("Un solo jugador") ||
            modo.equals("Jugador vs Jugador") ||
            modo.equals("Jugador vs Máquina");
    }

    /**
     * Verifica si el jugador 2 es humano
     */
    private boolean esJugadorHumano2() {
        return modo.equals("Jugador vs Jugador");
    }

    /**
     * Muestra un mensaje de error
     */
    private void mostrarError(String mensaje) {
        System.err.println("Error de movimiento: " + mensaje);
        // Opcional: mostrar en pantalla
        // JOptionPane.showMessageDialog(boardPanel, mensaje, "Error", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Actualiza el modo de juego
     */
    public void setModo(String modo) {
        this.modo = modo;
    }

    /**
     * Actualiza la referencia al juego
     */
    public void setJuego(BadDopoCream juego) {
        this.juego = juego;
    }

    /**
     * Obtiene las instrucciones de control
     */
    public static String getInstrucciones() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== CONTROLES ===\n\n");
        sb.append("JUGADOR 1:\n");
        sb.append("  W/A/S/D - Mover\n");
        sb.append("  ESPACIO - Crear hielo\n");
        sb.append("  SHIFT - Romper hielo\n\n");
        sb.append("JUGADOR 2:\n");
        sb.append("  Flechas - Mover\n");
        sb.append("  NumPad 0 - Crear hielo\n");
        sb.append("  CTRL - Romper hielo\n");
        return sb.toString();
    }
}