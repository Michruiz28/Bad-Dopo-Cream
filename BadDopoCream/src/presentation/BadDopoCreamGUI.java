package Presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BadDopoCreamGUI extends JFrame {
    private String modo;
    private String sabor1;
    private String sabor2;
    private String perfilMaq1;
    private String perfilMaq2;
    private int nivel;
    private int nivelMaximoAlcanzado = 1; // Controla el progreso

    //Principal
    //Menu
    private JMenu menu;
    private JMenuBar menuBar;
    private JMenuItem nuevo;
    private JMenuItem guardar;
    private JMenuItem abrir;
    private JMenuItem salir;

    //JPanel
    private JPanel panel;
    private JPanel sur;
    private JPanel oeste;
    private JPanel este;
    private JPanel boardPanel;

    //Botones
    private JButton cambiarModoJuego;
    private JButton cambiarSabor;
    private JButton cambiarNivel;
    private JButton puntaje;
    private JButton pausa;
    private JButton continuar;

    public BadDopoCreamGUI() {
        mostrarVentanaInicial();
    }

    private void mostrarVentanaInicial() {
        setTitle("BadDopoCream");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Imagen arriba ---
        ImageIcon icon = new ImageIcon("C:/BadIceCream-copy/Presentation/images/portada.png");
        JLabel lblImagen = new JLabel(icon, JLabel.CENTER);
        add(lblImagen, BorderLayout.CENTER);

        // --- Botón abajo ---
        JButton btnEmpezar = new JButton("Empezar");
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.add(btnEmpezar);
        add(panelBoton, BorderLayout.SOUTH);

        // --- Evento del botón ---
        btnEmpezar.addActionListener(e -> {
            getContentPane().removeAll(); // Limpiar ventana actual

            ConfigDialog config = new ConfigDialog(this);
            config.setNivelMaximoAlcanzado(nivelMaximoAlcanzado);
            config.setVisible(true);

            if (config.isConfiguracionAceptada()) {
                setInitialValues(config);
                prepareElements();
                prepareActions();

                add(panel);
                revalidate();
                repaint();
            } else {
                // Si canceló, volver a mostrar la ventana inicial
                mostrarVentanaInicial();
            }
        });

        setVisible(true);
    }

    private void setInitialValues(ConfigDialog config) {
        this.modo = config.getModo();
        this.sabor1 = config.getSabor1();
        this.sabor2 = config.getSabor2();
        this.perfilMaq1 = config.getPerfilMaq1();
        this.perfilMaq2 = config.getPerfilMaq2();
        this.nivel = config.getNivel();
    }

    private void prepareElements() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        prepareElementsBoard();
        prepareElementsMenu();
    }

    private void prepareElementsMenu() {
        menuBar = new JMenuBar();
        menu = new JMenu("menu");

        nuevo = new JMenuItem("Nuevo");
        abrir = new JMenuItem("Abrir");
        guardar = new JMenuItem("Guardar");
        salir = new JMenuItem("Salir");

        menu.add(nuevo);
        menu.addSeparator();
        menu.add(abrir);
        menu.addSeparator();
        menu.add(guardar);
        menu.addSeparator();
        menu.add(salir);

        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    private void prepareElementsBoard() {
        //Panel del Oeste
        oeste = new JPanel();
        oeste.setLayout(new GridLayout(3,1,5,5));

        cambiarModoJuego = new JButton("Cambiar Modo de juego");
        cambiarSabor = new JButton("Cambiar Sabor");
        cambiarNivel = new JButton("Cambiar Nivel");

        oeste.add(cambiarModoJuego);
        oeste.add(cambiarSabor);
        oeste.add(cambiarNivel);

        //Panel del Este
        este = new JPanel();

        // Panel sur
        sur = new JPanel();
        sur.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        puntaje = new JButton("Puntaje");
        continuar = new JButton("Continuar");
        pausa = new JButton("Pausar");

        buttonPanel.add(puntaje);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(continuar);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(pausa);

        sur.add(buttonPanel, BorderLayout.CENTER);

        //Panel central
        boardPanel = new JPanel();
        boardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        boardPanel.setBorder(BorderFactory.createTitledBorder("Board"));

        panel.add(oeste, BorderLayout.WEST);
        panel.add(boardPanel, BorderLayout.CENTER);
        panel.add(este, BorderLayout.EAST);
        panel.add(sur, BorderLayout.SOUTH);
    }

    private void prepareActions() {
        prepareActionsMenu();
        prepareActionsBoard();
    }

    private void prepareActionsBoard() {
        cambiarModoJuego.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarModoJuegoAccion();
            }
        });
        cambiarSabor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarSaborAccion();
            }
        });
        cambiarNivel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarNivelAccion();
            }
        });
        puntaje.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                puntajeAccion();
            }
        });
        pausa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pausaAccion();
            }
        });
        continuar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                continuarAccion();
            }
        });
    }

    private void cambiarModoJuegoAccion() {
        String[] opciones = {"Un solo jugador", "Jugador vs Jugador", "Jugador vs Máquina", "Máquina vs Máquina"};

        JComboBox<String> comboModo = new JComboBox<>(opciones);
        comboModo.setSelectedItem(this.modo);

        JPanel panelDialog = new JPanel(new FlowLayout());
        panelDialog.add(new JLabel("Modo de juego:"));
        panelDialog.add(comboModo);

        int result = JOptionPane.showConfirmDialog(
                this,
                panelDialog,
                "Cambiar Modo de Juego",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String nuevoModo = (String) comboModo.getSelectedItem();

            // Si cambió el modo, solicitar nueva configuración de sabores y perfiles
            if (!nuevoModo.equals(this.modo)) {
                solicitarConfiguracionSegunModo(nuevoModo);
            }
        }
    }

    private void solicitarConfiguracionSegunModo(String nuevoModo) {
        JPanel panelConfig = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JComboBox<String> comboSabor1 = new JComboBox<>(new String[]{"Vainilla", "Chocolate", "Fresa"});
        JComboBox<String> comboSabor2 = new JComboBox<>(new String[]{"Vainilla", "Chocolate", "Fresa"});
        JComboBox<String> comboPerfil1 = new JComboBox<>(new String[]{"Hungry", "Fearful", "Expert"});
        JComboBox<String> comboPerfil2 = new JComboBox<>(new String[]{"Hungry", "Fearful", "Expert"});

        int fila = 0;

        // Mostrar campos según el modo
        boolean necesitaSabor1 = true;
        boolean necesitaSabor2 = !nuevoModo.equals("Un solo jugador");
        boolean necesitaPerfil1 = nuevoModo.equals("Máquina vs Máquina");
        boolean necesitaPerfil2 = nuevoModo.equals("Jugador vs Máquina") || nuevoModo.equals("Máquina vs Máquina");

        if (necesitaSabor1) {
            gbc.gridx = 0; gbc.gridy = fila;
            panelConfig.add(new JLabel("Sabor helado 1:"), gbc);
            gbc.gridx = 1;
            panelConfig.add(comboSabor1, gbc);
            fila++;
        }

        if (necesitaSabor2) {
            gbc.gridx = 0; gbc.gridy = fila;
            panelConfig.add(new JLabel("Sabor helado 2:"), gbc);
            gbc.gridx = 1;
            panelConfig.add(comboSabor2, gbc);
            fila++;
        }

        if (necesitaPerfil1) {
            gbc.gridx = 0; gbc.gridy = fila;
            panelConfig.add(new JLabel("Perfil máquina 1:"), gbc);
            gbc.gridx = 1;
            panelConfig.add(comboPerfil1, gbc);
            fila++;
        }

        if (necesitaPerfil2) {
            gbc.gridx = 0; gbc.gridy = fila;
            panelConfig.add(new JLabel("Perfil máquina 2:"), gbc);
            gbc.gridx = 1;
            panelConfig.add(comboPerfil2, gbc);
            fila++;
        }

        int result = JOptionPane.showConfirmDialog(
                this,
                panelConfig,
                "Configurar " + nuevoModo,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            // Validar sabores diferentes si ambos están presentes
            if (necesitaSabor2) {
                String s1 = (String) comboSabor1.getSelectedItem();
                String s2 = (String) comboSabor2.getSelectedItem();

                if (s1.equals(s2)) {
                    JOptionPane.showMessageDialog(this,
                            "Los sabores deben ser diferentes.\nPor favor, intenta de nuevo.",
                            "Error",
                            JOptionPane.WARNING_MESSAGE);
                    return; // No aplicar cambios
                }
            }

            // Aplicar cambios
            this.modo = nuevoModo;
            this.sabor1 = (String) comboSabor1.getSelectedItem();
            this.sabor2 = necesitaSabor2 ? (String) comboSabor2.getSelectedItem() : null;
            this.perfilMaq1 = necesitaPerfil1 ? (String) comboPerfil1.getSelectedItem() : null;
            this.perfilMaq2 = necesitaPerfil2 ? (String) comboPerfil2.getSelectedItem() : null;

            JOptionPane.showMessageDialog(this, "Modo de juego actualizado correctamente");
        }
    }

    private void cambiarSaborAccion() {
        JPanel panelConfig = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JComboBox<String> comboSabor1 = new JComboBox<>(new String[]{"Vainilla", "Chocolate", "Fresa"});
        comboSabor1.setSelectedItem(this.sabor1);

        int fila = 0;

        // Sabor 1 siempre se muestra
        gbc.gridx = 0; gbc.gridy = fila;
        panelConfig.add(new JLabel("Sabor helado 1:"), gbc);
        gbc.gridx = 1;
        panelConfig.add(comboSabor1, gbc);
        fila++;

        JComboBox<String> comboSabor2 = null;
        // Sabor 2 solo si el modo lo requiere
        if (!modo.equals("Un solo jugador")) {
            comboSabor2 = new JComboBox<>(new String[]{"Vainilla", "Chocolate", "Fresa"});
            comboSabor2.setSelectedItem(this.sabor2);

            gbc.gridx = 0; gbc.gridy = fila;
            panelConfig.add(new JLabel("Sabor helado 2:"), gbc);
            gbc.gridx = 1;
            panelConfig.add(comboSabor2, gbc);
        }

        int result = JOptionPane.showConfirmDialog(
                this,
                panelConfig,
                "Cambiar Sabores",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String nuevoSabor1 = (String) comboSabor1.getSelectedItem();
            String nuevoSabor2 = comboSabor2 != null ? (String) comboSabor2.getSelectedItem() : null;

            // Validar que sean diferentes si ambos existen
            if (comboSabor2 != null && nuevoSabor1.equals(nuevoSabor2)) {
                JOptionPane.showMessageDialog(this,
                        "Los sabores deben ser diferentes.",
                        "Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            this.sabor1 = nuevoSabor1;
            this.sabor2 = nuevoSabor2;
            JOptionPane.showMessageDialog(this, "Sabores actualizados correctamente");
        }
    }

    private void cambiarNivelAccion() {
        // Crear combo solo con niveles disponibles
        JComboBox<String> comboNivel = new JComboBox<>();
        for (int i = 1; i <= nivelMaximoAlcanzado; i++) {
            comboNivel.addItem(String.valueOf(i));
        }
        comboNivel.setSelectedItem(String.valueOf(this.nivel));

        JPanel panelDialog = new JPanel(new FlowLayout());
        panelDialog.add(new JLabel("Nivel:"));
        panelDialog.add(comboNivel);

        // Agregar mensaje informativo
        JLabel lblInfo = new JLabel("<html><i>Completa los niveles anteriores para desbloquear más</i></html>");
        lblInfo.setForeground(Color.GRAY);

        JPanel panelConInfo = new JPanel(new BorderLayout());
        panelConInfo.add(panelDialog, BorderLayout.CENTER);
        panelConInfo.add(lblInfo, BorderLayout.SOUTH);

        int result = JOptionPane.showConfirmDialog(
                this,
                panelConInfo,
                "Cambiar Nivel (Desbloqueados: " + nivelMaximoAlcanzado + "/3)",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            this.nivel = Integer.parseInt((String) comboNivel.getSelectedItem());
            JOptionPane.showMessageDialog(this, "Nivel cambiado a: " + this.nivel);
        }
    }

    // Método para llamar cuando se complete un nivel
    public void nivelCompletado(int nivelCompletado) {
        if (nivelCompletado >= nivelMaximoAlcanzado && nivelCompletado < 3) {
            nivelMaximoAlcanzado = nivelCompletado + 1;
            JOptionPane.showMessageDialog(this,
                    "¡Felicidades! Has desbloqueado el nivel " + nivelMaximoAlcanzado,
                    "Nivel Desbloqueado",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void puntajeAccion() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("Información del Juego");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel lblModo = new JLabel("Modo: " + this.modo);
        JLabel lblSabor1 = new JLabel("Sabor jugador 1: " + this.sabor1);
        JLabel lblSabor2 = new JLabel("Sabor jugador 2: " + (this.sabor2 != null ? this.sabor2 : "N/A"));
        JLabel lblNivel = new JLabel("Nivel actual: " + this.nivel);
        JLabel lblNivelMax = new JLabel("Niveles desbloqueados: " + this.nivelMaximoAlcanzado + "/3");
        JLabel lblPuntaje = new JLabel("Puntaje: 0");

        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblModo);
        panel.add(lblSabor1);
        panel.add(lblSabor2);
        panel.add(lblNivel);
        panel.add(lblNivelMax);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblPuntaje);

        JOptionPane.showMessageDialog(
                this,
                panel,
                "Información del Juego",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void pausaAccion() {
        JOptionPane.showMessageDialog(
                this,
                "Juego pausado",
                "Pausa",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void continuarAccion() {
        JOptionPane.showMessageDialog(
                this,
                "Continuando el juego...",
                "Continuar",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void prepareActionsMenu() {
        nuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nuevoAcccion();
            }
        });
        abrir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirAccion();
            }
        });
        salir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salirAccion();
            }
        });
        guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarAccion();
            }
        });
    }

    private void nuevoAcccion() {
        JOptionPane.showMessageDialog(this, "Nuevo juego no implementado aún");
    }

    private void abrirAccion() {
        JOptionPane.showMessageDialog(this, "Abrir juego no implementado aún");
    }

    private void guardarAccion() {
        JOptionPane.showMessageDialog(this, "Guardar juego no implementado aún");
    }

    private void salirAccion() {
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro que desea salir?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BadDopoCreamGUI();
            }
        });
    }
}