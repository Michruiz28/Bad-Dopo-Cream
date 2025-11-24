package presentation;
import javax.swing.*;
import java.awt.*;

public class ConfigDialog extends JDialog{

    private String modo;
    private String sabor;
    private int nivel;

    private JComboBox<String> comboModo;
    private JComboBox<String> comboSabor;
    private JComboBox<String> comboNivel;

    public ConfigDialog(JFrame parent) {
        super(parent, "Configuración", true);

        comboModo = new JComboBox<>(new String[]{"PvsP", "PvsM", "MvsM"});
        comboSabor = new JComboBox<>(new String[]{"Vainilla", "Chocolate", "Fresa"});
        comboNivel = new JComboBox<>(new String[]{"1", "2", "3"});

        JButton aceptar = new JButton("Aceptar");
        aceptar.addActionListener(e -> {
            modo = (String) comboModo.getSelectedItem();
            sabor = (String) comboSabor.getSelectedItem();
            nivel = Integer.parseInt((String) comboNivel.getSelectedItem());
            dispose(); // cerrar
        });

        setLayout(new GridLayout(4, 2));
        add(new JLabel("Modo:")); add(comboModo);
        add(new JLabel("Sabor:")); add(comboSabor);
        add(new JLabel("Nivel:")); add(comboNivel);
        add(aceptar);

        pack();
        setLocationRelativeTo(parent);
    }

    public String getModo() { return modo; }
    public String getSabor() { return sabor; }
    public int getNivel() { return nivel; }

}
