package vista;

import javax.swing.*;

public class VistaArchivos extends JFrame {

    private JTextArea area;

    public VistaArchivos(String titulo, String contenido) {
        setTitle(titulo);
        setSize(500, 500);

        area = new JTextArea(contenido);
        area.setEditable(false);

        add(new JScrollPane(area));
        setVisible(true);
    }
}
