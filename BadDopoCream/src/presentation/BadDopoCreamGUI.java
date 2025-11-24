package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BadDopoCreamGUI extends JFrame {
    private String modo;
    private String sabor;
    private int nivel;


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

    //Botones
    private JButton puntaje;
    private JButton pausa;
    private JButton continuar;

    //Ventana inicial
    //Botones
    private JButton empezarJuego;

    //
    private JButton volver;
    private JButton jugar;

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
        ImageIcon icon = new ImageIcon("C:/portada.png");
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
            config.setVisible(true);

            setInitialValues(config);
            prepareElements();
            prepareActions();

            add(panel); // Agregar el panel del juego
            revalidate();
            repaint();
        });

        setVisible(true);
    }

    private void setInitialValues(ConfigDialog config) {
        this.modo = config.getModo();
        this.sabor = config.getSabor();
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
        //este.setLayout(new GridBagLayout());
        //GridBagConstraints gbc = new GridBagConstraints();
        //gbc.fill = GridBagConstraints.BOTH;
        //gbc.insets = new Insets(5, 5, 5, 5); // Espaciado
        //gbc.weightx = 1.0;
        //gbc.weighty = 1.0;

        //moveWest = new JButton("Move west");
        //moveEast = new JButton("Move east");
        //rotate = new JButton("rotate");

        // Move West (fila 0, columna 0)
        //gbc.gridx = 0;
        //gbc.gridy = 0;
        //gbc.gridwidth = 1;
        //eastPanel.add(moveWest, gbc);

        // Move East (fila 0, columna 1)
        //gbc.gridx = 1;
        //gbc.gridy = 0;
        //gbc.gridwidth = 1;
        //eastPanel.add(moveEast, gbc);

        // Rotate
        //gbc.gridx = 0;
        //gbc.gridy = 1;
        //gbc.gridwidth = 2;
        //eastPanel.add(rotate, gbc);

        // Panel sur
        sur = new JPanel();
        sur.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        puntaje = new JButton("Puntaje");
        continuar = new JButton("Continuar");
        pausa = new JButton("Pausar");

        buttonPanel.add(puntaje);
        buttonPanel.add(Box.createHorizontalStrut(20)); // Espacio
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
        String[] opciones = {"PvsP", "PvsM", "MvsM"};

        JComboBox<String> comboModo = new JComboBox<>(opciones);
        comboModo.setSelectedItem(this.modo); // Seleccionar el modo actual

        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JLabel("Modo de juego:"));
        panel.add(comboModo);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Cambiar Modo de Juego",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        if (result == JOptionPane.OK_OPTION) {
            this.modo = (String) comboModo.getSelectedItem();
            // Actualizar lógica del juego según el nuevo modo
        }
    }

    private void cambiarSaborAccion() {
        String[] opciones = {"Vainilla", "Chocolate", "Fresa"};

        JComboBox<String> comboSabor = new JComboBox<>(opciones);
        comboSabor.setSelectedItem(this.sabor);

        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JLabel("Sabor:"));
        panel.add(comboSabor);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Cambiar Sabor",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            this.sabor = (String) comboSabor.getSelectedItem();
            // Actualizar lógica del juego según el nuevo sabor
        }
    }

    private void cambiarNivelAccion() {
        String[] opciones = {"1", "2", "3"};

        JComboBox<String> comboNivel = new JComboBox<>(opciones);
        comboNivel.setSelectedItem(String.valueOf(this.nivel));

        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JLabel("Nivel:"));
        panel.add(comboNivel);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Cambiar Nivel",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            this.nivel = Integer.parseInt((String) comboNivel.getSelectedItem());
            // Actualizar lógica del juego según el nuevo nivel
        }
    }

    private void puntajeAccion() {
        // Crear un panel con información del puntaje
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("Información del Juego");

        JLabel lblModo = new JLabel("Modo: " + this.modo);
        JLabel lblSabor = new JLabel("Sabor: " + this.sabor);
        JLabel lblNivel = new JLabel("Nivel: " + this.nivel);
        JLabel lblPuntaje = new JLabel("Puntaje: 0"); // Aquí pondrás el puntaje real

        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblModo);
        panel.add(lblSabor);
        panel.add(lblNivel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblPuntaje);

        JOptionPane.showMessageDialog(
                this,
                panel,
                "Puntaje",
                JOptionPane.INFORMATION_MESSAGE
        );

    }

    private void pausaAccion() {
        JOptionPane.showMessageDialog(
                this,
                "La funcionalidad de Pausar no ha sido implementada aún",
                "Pausar",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void continuarAccion() {
        JOptionPane.showMessageDialog(
                this,
                "La funcionalidad de Continuar no ha sido implementada aún",
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
            ;
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

    //para probar
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BadDopoCreamGUI();
            }
        });
    }

}
