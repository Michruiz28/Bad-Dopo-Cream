package presentation;

import domain.BadDopoCream;
import domain.BadDopoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Hilo que maneja el loop principal del juego
 * Actualiza el estado del juego y refresca la visualización
 */
public class GameLoop extends Thread {
    private static final Logger LOGGER = Logger.getLogger(GameLoop.class.getName());

    private final BoardPanel boardPanel;
    private BadDopoCream juego;
    private boolean ejecutando;
    private boolean pausado;
    private final BadDopoCreamGUI gui;
    private static final int FPS = 60; // Frames por segundo
    private static final long FRAME_TIME = 1000 / FPS; // Tiempo por frame en ms
    private boolean mostrandoDialogoFinNivel = false;

    public GameLoop(BoardPanel boardPanel, BadDopoCreamGUI gui) {
        this.boardPanel = boardPanel;
        this.juego = boardPanel.getJuego();
        this.gui = gui;
        this.ejecutando = false;
        this.pausado = false;
    }
    @Override
    public void run() {
        ejecutando = true;
        while (ejecutando && juego != null) {
            long startTime = System.currentTimeMillis();
            if (!pausado && !juego.isJuegoTerminado()) {
                try {
                    // Actualizar lógica del juego
                    juego.actualizar();
                    // Verificar condiciones de victoria/derrota
                    verificarEstadoJuego();
                } catch (BadDopoException e) {
                    LOGGER.log(Level.SEVERE, "Error en el game loop: " + e.getMessage(), e);
                }
            }
            // Refrescar visualización
            boardPanel.actualizar();
            // Controlar FPS
            long elapsedTime = System.currentTimeMillis() - startTime;
            long sleepTime = FRAME_TIME - elapsedTime;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    /**
     * Verifica si el nivel fue completado o si se perdió
     */
    private void verificarEstadoJuego() {

        if (mostrandoDialogoFinNivel) return;

        if (juego.isNivelCompletado()) {
            mostrandoDialogoFinNivel = true;
            pausar();

            javax.swing.SwingUtilities.invokeLater(() -> {
                if (juego.isVictoria()) {
                    mostrarVictoriaFinal();
                } else {
                    mostrarNivelCompletado();
                }
            });
        }
    }


    /**
     * Muestra mensaje de nivel completado
     */
    private void mostrarNivelCompletado() {
        int nivelActual = juego.getNivelActual();
        int puntaje1 = juego.getPuntajeJugador1();
        int puntaje2 = juego.getPuntajeJugador2();

        StringBuilder mensaje = new StringBuilder();
        mensaje.append("¡Nivel ").append(nivelActual).append(" completado!\n\n");
        mensaje.append("Puntaje Jugador 1: ").append(puntaje1).append("\n");

        if (juego.getHelado2() != null) {
            mensaje.append("Puntaje Jugador 2: ").append(puntaje2).append("\n");
            mensaje.append("\nGanador: ").append(juego.getGanador());
        }

        mensaje.append("\n\n¿Deseas continuar al siguiente nivel?");

        int opcion = JOptionPane.showConfirmDialog(
                boardPanel,
                mensaje.toString(),
                "¡Felicidades!",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                juego.avanzarNivel();
                gui.nivelCompletado(nivelActual);
                reanudar();
            } catch (BadDopoException e) {
                JOptionPane.showMessageDialog(
                        boardPanel,
                        "Error al avanzar de nivel: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                detener();
            }
        } else {
            detener();
        }

        // 🔑 LIBERA EL EVENTO
        mostrandoDialogoFinNivel = false;
        juego.setNivelCompletado(false);
    }


    /**
     * Muestra mensaje de victoria final
     */
    private void mostrarVictoriaFinal() {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("¡FELICIDADES!\n\n");
        mensaje.append("Has completado todos los niveles de Bad DOPO Cream\n\n");
        mensaje.append(juego.getResumenFinal());

        JOptionPane.showMessageDialog(
                boardPanel,
                mensaje.toString(),
                "¡Victoria Total!",
                JOptionPane.INFORMATION_MESSAGE
        );

        mostrandoDialogoFinNivel = false;
        detener();
    }

    /**
     * Pausa el game loop
     */
    public void pausar() {
        pausado = true;
        if (juego != null) {
            juego.pausar();
        }
    }
    /**
     * Reanuda el game loop
     */
    public void reanudar() {
        pausado = false;
        if (juego != null) {
            juego.reanudar();
        }
    }
    /**
     * Detiene el game loop completamente
     */
    public void detener() {
        ejecutando = false;
        pausado = true;
    }
    /**
     * Verifica si el juego está pausado
     */
    public boolean isPausado() {
        return pausado;
    }
    /**
     * Verifica si el game loop está ejecutándose
     */
    public boolean isEjecutando() {
        return ejecutando;
    }
    /**
     * Actualiza la referencia al juego (útil cuando se carga una partida)
     */
    public void setJuego(BadDopoCream juego) {
        this.juego = juego;
    }
}

