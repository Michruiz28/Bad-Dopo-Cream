package presentation;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

public class ConfigDialog extends JDialog {

    private String modo;
    private String sabor1;
    private String sabor2;
    private String perfilMaq1;
    private String perfilMaq2;
    private int nivel;

    private JComboBox<String> comboModo;
    private JComboBox<String> comboSabor1;
    private JComboBox<String> comboSabor2;
    private JComboBox<String> comboNivel;
    private JComboBox<String> perfilMaquina1;
    private JComboBox<String> perfilMaquina2;

    private JLabel labelSabor1;
    private JLabel labelSabor2;
    private JLabel labelModoMaquina1;
    private JLabel labelModoMaquina2;

    private int nivelMaximoAlcanzado = 1;
    private boolean configuracionAceptada = false;

    public ConfigDialog(JFrame parent) {
        super(parent, "Configuración", true);

        comboModo = new JComboBox<>(new String[]{"Un solo jugador", "Jugador vs Jugador", "Jugador vs Máquina", "Máquina vs Máquina"});
        perfilMaquina1 = new JComboBox<>(new String[]{"Hungry", "Fearful", "Expert"});
        perfilMaquina2 = new JComboBox<>(new String[]{"Hungry", "Fearful", "Expert"});
        comboSabor1 = new JComboBox<>(new String[]{"Vainilla", "Chocolate", "Fresa"});
        comboSabor2 = new JComboBox<>(new String[]{"Vainilla", "Chocolate", "Fresa"});
        comboNivel = new JComboBox<>(new String[]{"1", "2", "3"});

        labelSabor1 = new JLabel("Sabor helado 1:");
        labelSabor2 = new JLabel("Sabor helado 2:");
        labelModoMaquina1 = new JLabel("Perfil máquina 1:");
        labelModoMaquina2 = new JLabel("Perfil máquina 2:");

        // Configuración inicial: sabor1 visible, resto oculto
        comboSabor1.setVisible(true);   // Siempre visible
        labelSabor1.setVisible(true);   // Siempre visible
        comboSabor2.setVisible(false);
        labelSabor2.setVisible(false);
        perfilMaquina1.setVisible(false);
        labelModoMaquina1.setVisible(false);
        perfilMaquina2.setVisible(false);
        labelModoMaquina2.setVisible(false);
        actualizarNivelesDisponibles();

        // Listener para cambios en el modo de juego
        comboModo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                actualizarInterfazSegunModo();
            }
        });

        JButton aceptar = new JButton("Aceptar");
        JButton cancelar = new JButton("Cancelar");

        aceptar.addActionListener(e -> {
            // Validar que los sabores no sean iguales cuando ambos están visibles
            if (comboSabor2.isVisible()) {
                String sabor1Temp = (String) comboSabor1.getSelectedItem();
                String sabor2Temp = (String) comboSabor2.getSelectedItem();

                if (sabor1Temp.equals(sabor2Temp)) {
                    JOptionPane.showMessageDialog(this,
                            "Los sabores de los helados deben ser diferentes.\nPor favor, selecciona sabores distintos.",
                            "Error en configuración",
                            JOptionPane.WARNING_MESSAGE);
                    return; // No cerrar el diálogo
                }
            }

            // Si la validación pasa, guardar los valores
            modo = (String) comboModo.getSelectedItem();
            sabor1 = (String) comboSabor1.getSelectedItem();

            if (comboSabor2.isVisible()) {
                sabor2 = (String) comboSabor2.getSelectedItem();
            } else {
                sabor2 = null;
            }

            if (perfilMaquina1.isVisible()) {
                perfilMaq1 = (String) perfilMaquina1.getSelectedItem();
            } else {
                perfilMaq1 = null;
            }

            if (perfilMaquina2.isVisible()) {
                perfilMaq2 = (String) perfilMaquina2.getSelectedItem();
            } else {
                perfilMaq2 = null;
            }

            nivel = Integer.parseInt((String) comboNivel.getSelectedItem());
            configuracionAceptada = true;
            dispose();
        });

        cancelar.addActionListener(e -> {
            configuracionAceptada = false;
            dispose();
        });

        // Layout principal
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Modo
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Modo:"), gbc);
        gbc.gridx = 1;
        panel.add(comboModo, gbc);

        // Sabor 1
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(labelSabor1, gbc);
        gbc.gridx = 1;
        panel.add(comboSabor1, gbc);

        // Sabor 2
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(labelSabor2, gbc);
        gbc.gridx = 1;
        panel.add(comboSabor2, gbc);

        // Perfil Máquina 1
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(labelModoMaquina1, gbc);
        gbc.gridx = 1;
        panel.add(perfilMaquina1, gbc);

        // Perfil Máquina 2
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(labelModoMaquina2, gbc);
        gbc.gridx = 1;
        panel.add(perfilMaquina2, gbc);

        // Nivel
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Nivel:"), gbc);
        gbc.gridx = 1;
        panel.add(comboNivel, gbc);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotones.add(aceptar);
        panelBotones.add(cancelar);

        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(panelBotones, gbc);

        add(panel);
        pack();
        setLocationRelativeTo(parent);
    }

    /**
     * Actualiza dinámicamente la interfaz según el modo seleccionado
     */
    private void actualizarInterfazSegunModo() {
        String modoSeleccionado = (String) comboModo.getSelectedItem();

        // Primero ocultar todo
        comboSabor1.setVisible(false);
        labelSabor1.setVisible(false);
        comboSabor2.setVisible(false);
        labelSabor2.setVisible(false);
        perfilMaquina1.setVisible(false);
        labelModoMaquina1.setVisible(false);
        perfilMaquina2.setVisible(false);
        labelModoMaquina2.setVisible(false);

        // Mostrar según el modo
        switch (modoSeleccionado) {
            case "Un solo jugador":
                comboSabor1.setVisible(true);
                labelSabor1.setVisible(true);
                break;

            case "Jugador vs Jugador":
                comboSabor1.setVisible(true);
                labelSabor1.setVisible(true);
                comboSabor2.setVisible(true);
                labelSabor2.setVisible(true);
                break;

            case "Jugador vs Máquina":
                comboSabor1.setVisible(true);
                labelSabor1.setVisible(true);
                comboSabor2.setVisible(true);
                labelSabor2.setVisible(true);
                perfilMaquina2.setVisible(true);
                labelModoMaquina2.setVisible(true);
                break;

            case "Máquina vs Máquina":
                comboSabor1.setVisible(true);
                labelSabor1.setVisible(true);
                comboSabor2.setVisible(true);
                labelSabor2.setVisible(true);
                perfilMaquina1.setVisible(true);
                labelModoMaquina1.setVisible(true);
                perfilMaquina2.setVisible(true);
                labelModoMaquina2.setVisible(true);
                break;
        }

        // Reajustar el tamaño del diálogo
        pack();
        setLocationRelativeTo(getParent());
    }

    /**
     * Actualiza los niveles disponibles según el progreso del jugador
     */
    private void actualizarNivelesDisponibles() {
        comboNivel.removeAllItems();

        for (int i = 1; i <= nivelMaximoAlcanzado; i++) {
            comboNivel.addItem(String.valueOf(i));
        }
    }

    /**
     * Método para actualizar el nivel máximo alcanzado
     */
    public void setNivelMaximoAlcanzado(int nivel) {
        if (nivel >= 1 && nivel <= 3) {
            this.nivelMaximoAlcanzado = nivel;
            actualizarNivelesDisponibles();
        }
    }

    public int getNivelMaximoAlcanzado() {
        return nivelMaximoAlcanzado;
    }

    public boolean isConfiguracionAceptada() {
        return configuracionAceptada;
    }

    // Getters
    public String getModo() { return modo; }
    public String getSabor1() { return sabor1; }
    public String getSabor2() { return sabor2; }
    public String getPerfilMaq1() { return perfilMaq1; }
    public String getPerfilMaq2() { return perfilMaq2; }
    public int getNivel() { return nivel; }
}