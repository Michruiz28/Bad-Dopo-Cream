package presentation;

import domain.BadDopoCream;
import domain.BadDopoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


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
                    juego.actualizar();
                    verificarEstadoJuego();
                } catch (BadDopoException e) {
                    LOGGER.log(Level.SEVERE, "Error en el game loop: " + e.getMessage(), e);
                }
            }
            boardPanel.actualizar();
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

    private void verificarEstadoJuego() {

        if (mostrandoDialogoFinNivel) return;

        if (juego.isNivelCompletado()) {
            gui.nivelCompletado(juego.getNivelActual());
            mostrandoDialogoFinNivel = true;
            pausar();

            javax.swing.SwingUtilities.invokeLater(() -> {
            if (juego.esUltimoNivel()) {
                mostrarVictoriaFinal();  
            } else {
                mostrarNivelCompletado();
            }

            });
        }
    }

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
                juego.setNivelCompletado(false);
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

        mostrandoDialogoFinNivel = false;
    }


    private void mostrarVictoriaFinal() {

        detener();

        javax.swing.SwingUtilities.invokeLater(() -> {
            int opcion = JOptionPane.showOptionDialog(
                    boardPanel,
                    "¡FELICIDADES!\n\nHas completado todos los niveles de Bad DOPO Cream \n\n¿Qué deseas hacer?",
                    "¡Victoria Total!",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[]{"Ver resultados", "Salir"},
                    "Ver resultados"
            );

            if (opcion == 0) {
                boardPanel.mostrarPantallaFinal();
            } else {
                System.exit(0);
            }
        });

        mostrandoDialogoFinNivel = false;
    }

    public void pausar() {
        pausado = true;
        if (juego != null) {
            juego.pausar();
        }
    }

    public void reanudar() {
        pausado = false;
        if (juego != null) {
            juego.reanudar();
        }
    }

    public void detener() {
        ejecutando = false;
        pausado = true;
    }

    public boolean isPausado() {
        return pausado;
    }

    public boolean isEjecutando() {
        return ejecutando;
    }

    public void setJuego(BadDopoCream juego) {
        this.juego = juego;
    }
}

