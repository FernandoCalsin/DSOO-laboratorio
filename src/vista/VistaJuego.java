package vista;

import java.awt.*;
import javax.swing.*;

public class VistaJuego extends JFrame {

    private JLabel[][] celdas = new JLabel[10][10];
    private JTextArea areaTexto;
    private JScrollPane scrollConsola;

    private JMenuItem itemCompilar;
    private JMenuItem itemSobre;
    private JMenuItem itemSalir;
    private JCheckBoxMenuItem itemMostrarConsola;

    public VistaJuego() {

        setTitle("Simulador de Batalla - MVC");
        setSize(900, 700);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        crearMenu();
        crearTablero();
        crearConsola();
        setVisible(true);
    }

    private void crearMenu() {
        JMenuBar barra = new JMenuBar();

        JMenu archivo = new JMenu("Archivo");
        itemCompilar = new JMenuItem("Compilar soldados");
        JMenuItem itemGuardar = new JMenuItem("Guardar");
        JMenuItem itemAbrir = new JMenuItem("Abrir");
        itemSalir = new JMenuItem("Salir");

        archivo.add(itemCompilar);
        archivo.add(itemGuardar);
        archivo.add(itemAbrir);
        archivo.addSeparator();
        archivo.add(itemSalir);

        JMenu ver = new JMenu("Ver");
        itemMostrarConsola = new JCheckBoxMenuItem("Mostrar consola", true);
        ver.add(itemMostrarConsola);

        JMenu ayuda = new JMenu("Ayuda");
        itemSobre = new JMenuItem("Sobre el juego");
        ayuda.add(itemSobre);

        barra.add(archivo);
        barra.add(ver);
        barra.add(ayuda);

        setJMenuBar(barra);
    }

    private void crearTablero() {
        JPanel tablero = new JPanel();
        tablero.setLayout(new GridLayout(10, 10));
        tablero.setBounds(20, 20, 520, 520);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                celdas[i][j] = new JLabel();
                celdas[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                celdas[i][j].setHorizontalAlignment(JLabel.CENTER);
                tablero.add(celdas[i][j]);
            }
        }
        add(tablero);
    }

    private void crearConsola() {
        areaTexto = new JTextArea();
        areaTexto.setEditable(false);

        scrollConsola = new JScrollPane(areaTexto);
        scrollConsola.setBounds(560, 20, 300, 520);

        add(scrollConsola);
    }

    public JMenuItem getItemCompilar() { return itemCompilar; }
    public JMenuItem getItemSobre()   { return itemSobre; }
    public JMenuItem getItemSalir()   { return itemSalir; }
    public JCheckBoxMenuItem getItemMostrarConsola() { return itemMostrarConsola; }

    public void setImagenCelda(int x, int y, ImageIcon icon) {
        celdas[x][y].setIcon(icon);
    }

    public boolean isCeldaVacia(int x, int y) {
        return celdas[x][y].getIcon() == null;
    }

    public void limpiarTablero() {
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                celdas[i][j].setIcon(null);
    }

    public void setConsolaTexto(String texto) {
        areaTexto.setText(texto);
    }

    public void toggleConsola() {
        scrollConsola.setVisible(!scrollConsola.isVisible());
    }

    public void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public ImageIcon cargarImagen(String nombre) {
        try {

            java.net.URL url = getClass().getClassLoader()
                    .getResource("imagenes/" + nombre);

            if (url == null) {
                System.out.println("❌ Imagen NO encontrada: " + nombre);
                return null;
            }

            ImageIcon icon = new ImageIcon(url);
            Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);

            return new ImageIcon(img);

        } catch (Exception e) {
            System.out.println("⚠ Error cargando imagen: " + nombre);
            return null;
        }
    }
}
