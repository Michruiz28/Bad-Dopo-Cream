package presentation;

import domain.BadDopoCream;
import domain.BadDopoException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class BadDopoCreamGUI extends JFrame {
    private String modo;
    private String sabor1;
    private String sabor2;
    private String nombre1;
    private String nombre2;
    private String perfilMaq1;
    private String perfilMaq2;
    private int nivel;
    private int nivelMaximoAlcanzado = 1;

    // Controladores
    private MovementController movementController;
    private GameLoop gameLoop;

    // Componentes de UI
    private JMenu menu;
    private JMenuBar menuBar;
    private JMenuItem nuevo, guardar, abrir, salir, instrucciones;

    private JPanel panel, sur, oeste, este;
    private BoardPanel boardPanel;

    private JButton cambiarModoJuego, cambiarSabor, cambiarNivel;
    private JButton puntaje, pausa, continuar;

    public BadDopoCreamGUI() {
        mostrarVentanaInicial();
    }

    private void mostrarVentanaInicial() {
        setTitle("Bad DOPO Cream");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 850);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Imagen de portada
        try {
            ImageIcon icon = new ImageIcon("src/presentation/images/portada.png");
            JLabel lblImagen = new JLabel(icon, JLabel.CENTER);
            add(lblImagen, BorderLayout.CENTER);
        } catch (Exception e) {
            JLabel lblTitulo = new JLabel("BAD DOPO CREAM", JLabel.CENTER);
            lblTitulo.setFont(new Font("Arial", Font.BOLD, 48));
            add(lblTitulo, BorderLayout.CENTER);
        }

        // Botón empezar
        JButton btnEmpezar = new JButton("Empezar Juego");
        btnEmpezar.setFont(new Font("Arial", Font.BOLD, 18));
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.add(btnEmpezar);
        add(panelBoton, BorderLayout.SOUTH);

        // Acción del botón
        btnEmpezar.addActionListener(e -> {
            getContentPane().removeAll();

            ConfigDialog config = new ConfigDialog(this);
            config.setNivelMaximoAlcanzado(nivelMaximoAlcanzado);
            config.setVisible(true);

            if (config.isConfiguracionAceptada()) {
                setInitialValues(config);
                iniciarJuego();
            } else {
                mostrarVentanaInicial();
            }
        });

        setVisible(true);
    }

    private void setInitialValues(ConfigDialog config) {
        this.modo = config.getModo();
        this.sabor1 = convertirSabor(config.getSabor1());
        this.sabor2 = config.getSabor2() != null ? convertirSabor(config.getSabor2()) : null;
        this.perfilMaq1 = config.getPerfilMaq1();
        this.perfilMaq2 = config.getPerfilMaq2();
        this.nivel = config.getNivel();
        this.nombre1 = "Jugador 1";
        this.nombre2 = "Jugador 2";

        System.out.println("[GUI] Configuración inicial:");
        System.out.println("  Modo: " + modo);
        System.out.println("  Sabor1: " + sabor1);
        System.out.println("  Sabor2: " + sabor2);
        System.out.println("  Nivel: " + nivel);
    }

    /**
     * Convierte el nombre del sabor de la GUI al código del dominio
     */
    private String convertirSabor(String saborGUI) {
        if (saborGUI == null) return null;
        switch (saborGUI) {
            case "Vainilla": return "VH";
            case "Chocolate": return "CH";
            case "Fresa": return "F";
            default: return "VH";
        }
    }

    private void iniciarJuego() {
        System.out.println("[GUI] Iniciando juego...");
        prepareElements();
        prepareActions();

        // Inicializar el juego en el BoardPanel
        boardPanel.inicializarJuego(nivel, modo, sabor1, sabor2,
                nombre1, nombre2, perfilMaq1, perfilMaq2);

        // Inicializar controladores
        movementController = new MovementController(boardPanel, modo);
        gameLoop = new GameLoop(boardPanel, this);

        // CONFIGURACIÓN MEJORADA DE LISTENERS Y FOCO
        configurarListeners();

        // Iniciar el game loop
        gameLoop.start();

        add(panel);
        revalidate();
        repaint();

        System.out.println("[GUI] Juego iniciado correctamente");
    }

    /**
     * Configura los listeners de teclado y mouse para asegurar el foco correcto
     */
    private void configurarListeners() {
        // Agregar el KeyListener al frame Y al boardPanel
        addKeyListener(movementController);
        boardPanel.addKeyListener(movementController);

        // Hacer focusable ambos componentes
        setFocusable(true);
        boardPanel.setFocusable(true);

        // Agregar MouseListener para recuperar el foco al hacer clic
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                boardPanel.requestFocusInWindow();
                System.out.println("[GUI] Foco devuelto al boardPanel por clic de mouse");
            }
        });

        // Agregar MouseListener al frame completo
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                boardPanel.requestFocusInWindow();
                System.out.println("[GUI] Foco devuelto al boardPanel por clic en frame");
            }
        });

        // Solicitar foco inicial
        SwingUtilities.invokeLater(() -> {
            boardPanel.requestFocusInWindow();
            System.out.println("[GUI] Foco inicial solicitado para boardPanel");
        });
    }

    private void prepareElements() {
        panel = new JPanel(new BorderLayout());
        prepareElementsBoard();
        prepareElementsMenu();
    }

    private void prepareElementsMenu() {
        menuBar = new JMenuBar();
        menu = new JMenu("Menú");

        nuevo = new JMenuItem("Nuevo Juego");
        abrir = new JMenuItem("Abrir Partida");
        guardar = new JMenuItem("Guardar Partida");
        instrucciones = new JMenuItem("Instrucciones");
        salir = new JMenuItem("Salir");

        menu.add(nuevo);
        menu.addSeparator();
        menu.add(abrir);
        menu.add(guardar);
        menu.addSeparator();
        menu.add(instrucciones);
        menu.addSeparator();
        menu.add(salir);

        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    private void prepareElementsBoard() {
        // Panel del Oeste
        oeste = new JPanel(new GridLayout(3, 1, 5, 5));
        oeste.setPreferredSize(new Dimension(160, 0));

        cambiarModoJuego = new JButton("<html><center>Cambiar<br>Modo</center></html>");
        cambiarSabor = new JButton("<html><center>Cambiar<br>Sabor</center></html>");
        cambiarNivel = new JButton("<html><center>Cambiar<br>Nivel</center></html>");

        oeste.add(cambiarModoJuego);
        oeste.add(cambiarSabor);
        oeste.add(cambiarNivel);

        // Panel del Este (vacío por ahora)
        este = new JPanel();
        este.setPreferredSize(new Dimension(160, 0));

        // Panel Sur con botones
        sur = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel();

        puntaje = new JButton("Info");
        continuar = new JButton("Reanudar");
        pausa = new JButton("Pausar");

        buttonPanel.add(puntaje);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(pausa);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(continuar);

        sur.add(buttonPanel, BorderLayout.CENTER);

        // Panel Central - Tablero del juego
        boardPanel = new BoardPanel();
        boardPanel.setBorder(BorderFactory.createTitledBorder("Nivel " + nivel));

        panel.add(oeste, BorderLayout.WEST);
        panel.add(boardPanel, BorderLayout.CENTER);
        panel.add(este, BorderLayout.EAST);
        panel.add(sur, BorderLayout.SOUTH);
    }

    private void prepareActions() {
        prepareActionsMenu();
        prepareActionsBoard();
    }

    private void prepareActionsMenu() {
        nuevo.addActionListener(e -> nuevoJuegoAccion());
        abrir.addActionListener(e -> abrirAccion());
        guardar.addActionListener(e -> guardarAccion());
        instrucciones.addActionListener(e -> mostrarInstrucciones());
        salir.addActionListener(e -> salirAccion());
    }

    private void prepareActionsBoard() {
        cambiarModoJuego.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Cambia el modo desde 'Nuevo Juego'",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            boardPanel.requestFocusInWindow();
        });

        cambiarSabor.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Cambia el sabor desde 'Nuevo Juego'",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            boardPanel.requestFocusInWindow();
        });

        cambiarNivel.addActionListener(e -> cambiarNivelAccion());
        puntaje.addActionListener(e -> puntajeAccion());
        pausa.addActionListener(e -> pausaAccion());
        continuar.addActionListener(e -> continuarAccion());
    }

    private void cambiarNivelAccion() {
        if (gameLoop != null) {
            gameLoop.pausar();
        }

        JComboBox<String> comboNivel = new JComboBox<>();
        for (int i = 1; i <= nivelMaximoAlcanzado; i++) {
            comboNivel.addItem(String.valueOf(i));
        }
        comboNivel.setSelectedItem(String.valueOf(this.nivel));

        int result = JOptionPane.showConfirmDialog(
                this,
                comboNivel,
                "Seleccionar Nivel (Desbloqueados: " + nivelMaximoAlcanzado + "/3)",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            this.nivel = Integer.parseInt((String) comboNivel.getSelectedItem());
            reiniciarJuego();
        } else if (gameLoop != null) {
            gameLoop.reanudar();
        }

        boardPanel.requestFocusInWindow();
    }

    private void puntajeAccion() {
        if (boardPanel.getJuego() == null) return;

        StringBuilder info = new StringBuilder();
        info.append("=== INFORMACIÓN DEL JUEGO ===\n\n");
        info.append("Modo: ").append(modo).append("\n");
        info.append("Nivel: ").append(nivel).append(" / 3\n");
        info.append("Niveles desbloqueados: ").append(nivelMaximoAlcanzado).append("\n\n");
        info.append("Tiempo restante: ").append(boardPanel.getJuego().getTiempoRestanteFormato()).append("\n\n");
        info.append("Puntaje Jugador 1: ").append(boardPanel.getJuego().getPuntajeJugador1()).append("\n");

        if (boardPanel.getJuego().getHelado2() != null) {
            info.append("Puntaje Jugador 2: ").append(boardPanel.getJuego().getPuntajeJugador2()).append("\n");
        }

        JOptionPane.showMessageDialog(this, info.toString(), "Información", JOptionPane.INFORMATION_MESSAGE);
        boardPanel.requestFocusInWindow();
    }

    private void pausaAccion() {
        if (gameLoop != null && !gameLoop.isPausado()) {
            gameLoop.pausar();
            JOptionPane.showMessageDialog(this, "Juego pausado", "Pausa", JOptionPane.INFORMATION_MESSAGE);
        }
        boardPanel.requestFocusInWindow();
    }

    private void continuarAccion() {
        if (gameLoop != null && gameLoop.isPausado()) {
            gameLoop.reanudar();
        }
        boardPanel.requestFocusInWindow();
    }

    private void nuevoJuegoAccion() {
        if (gameLoop != null) {
            gameLoop.detener();
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Iniciar un nuevo juego? Se perderá el progreso actual.",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            getContentPane().removeAll();
            mostrarVentanaInicial();
        } else {
            boardPanel.requestFocusInWindow();
        }
    }

    private void abrirAccion() {
        if (gameLoop != null) {
            gameLoop.pausar();
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Abrir Partida");
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                BadDopoCream juegoC = BadDopoCream.cargar(file.getAbsolutePath());

                boardPanel.inicializarJuego(
                        juegoC.getNivelActual(),
                        juegoC.getModo(),
                        juegoC.getSabor1(),
                        juegoC.getSabor2()
                );

                if (gameLoop != null) {
                    gameLoop.setJuego(juegoC);
                    gameLoop.reanudar();
                }

                JOptionPane.showMessageDialog(this, "Partida cargada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al cargar partida: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        boardPanel.requestFocusInWindow();
    }

    private void guardarAccion() {
        if (gameLoop != null) {
            gameLoop.pausar();
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Partida");
        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                String path = file.getAbsolutePath();
                if (!path.endsWith(".bdcr")) {
                    path += ".bdcr";
                }

                boardPanel.getJuego().guardar(path);
                JOptionPane.showMessageDialog(this, "Partida guardada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (gameLoop != null) {
            gameLoop.reanudar();
        }
        boardPanel.requestFocusInWindow();
    }

    private void mostrarInstrucciones() {
        if (gameLoop != null) {
            gameLoop.pausar();
        }

        JOptionPane.showMessageDialog(this,
                MovementController.getInstrucciones(),
                "Instrucciones",
                JOptionPane.INFORMATION_MESSAGE);

        if (gameLoop != null) {
            gameLoop.reanudar();
        }
        boardPanel.requestFocusInWindow();
    }

    private void salirAccion() {
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro que desea salir?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (gameLoop != null) {
                gameLoop.detener();
            }
            System.exit(0);
        }
    }

    public void nivelCompletado(int nivelCompletado) {
        if (nivelCompletado >= nivelMaximoAlcanzado && nivelCompletado < 3) {
            nivelMaximoAlcanzado = nivelCompletado + 1;
        }
    }

    private void reiniciarJuego() {
        if (gameLoop != null) {
            gameLoop.detener();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        getContentPane().removeAll();
        iniciarJuego();
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        // Configurar look and feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new BadDopoCreamGUI());
    }
}