package vista;

import controlador.ControladorJuego;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;
import modelo.Soldado;

public class VistaJuego extends JFrame {

    private JLabel[][] celdas = new JLabel[10][10];
    private JTextArea areaTexto;
    private JScrollPane scrollConsola;

    private JMenuItem itemCompilar;
    private JMenuItem itemSobre;
    private JMenuItem itemSalir;
    private JCheckBoxMenuItem itemMostrarConsola;

    private JMenuItem itemNuevo;
    private JMenuItem itemAbrirLogs;
    private JMenuItem itemGuardarLogs;
    private JMenuItem itemGuardarRanking;
    private JMenuItem itemGuardarConfig;
    
    private JMenuItem itemGuardarBinario;
    private JMenuItem itemAbrirBinario;
    
    private JButton btnArriba;
    private JButton btnAbajo;
    private JButton btnIzquierda;
    private JButton btnDerecha;
    private JButton btnAtacar;

    private JPanel panelTablero = new JPanel();
    private JPanel panelDerecha;

    private ControladorJuego controlador;

    private int filaSeleccionada = -1;
    private int colSeleccionada = -1;

    private JMenuItem itemMostrarRankingBD;

    public VistaJuego() {
        setTitle("Simulador de Batalla - MVC");
        setSize(900, 700);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        crearMenu();
        crearTablero();
        crearConsola();
        crearPanelControles();

        setVisible(true);
    }

    public void crearPanelControles() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 3, 5, 5));

        btnArriba = new JButton("↑");
        btnAbajo = new JButton("↓");
        btnIzquierda = new JButton("←");
        btnDerecha = new JButton("→");
        btnAtacar = new JButton("Atacar");

        panel.add(new JLabel(""));
        panel.add(btnArriba);
        panel.add(new JLabel(""));

        panel.add(btnIzquierda);
        panel.add(btnAtacar);
        panel.add(btnDerecha);

        panel.add(new JLabel(""));
        panel.add(btnAbajo);
        panel.add(new JLabel(""));

        panelDerecha.add(panel, BorderLayout.SOUTH);
    }

    public void resaltarCelda(int fila, int col) {
        if (filaSeleccionada != -1 && colSeleccionada != -1) {
            celdas[filaSeleccionada][colSeleccionada].setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }    
        celdas[fila][col].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        filaSeleccionada = fila;
        colSeleccionada = col;
    }

    private void crearMenu() {
        JMenuBar barra = new JMenuBar();

        JMenu archivo = new JMenu("Archivo");
        itemNuevo = new JMenuItem("Nuevo");
        itemCompilar = new JMenuItem("Compilar soldados");
        JMenuItem itemGuardar = new JMenuItem("Guardar");
        JMenuItem itemAbrir = new JMenuItem("Abrir");
        itemAbrirLogs = new JMenuItem("Abrir Logs");
        itemGuardarLogs = new JMenuItem("Guardar Logs");
        itemGuardarRanking = new JMenuItem("Guardar Ranking");
        itemGuardarConfig = new JMenuItem("Guardar Configuración");
        itemGuardarBinario = new JMenuItem("Guardar Binario");
        itemAbrirBinario = new JMenuItem("Abrir Binario");
        itemSalir = new JMenuItem("Salir");

        archivo.add(itemNuevo);
        archivo.add(itemCompilar);
        archivo.add(itemGuardar);
        archivo.add(itemAbrir);
        archivo.addSeparator();
        archivo.add(itemAbrirLogs);
        archivo.add(itemGuardarLogs);
        archivo.add(itemGuardarRanking);
        archivo.add(itemGuardarConfig);
        archivo.addSeparator();
        archivo.add(itemGuardarBinario);
        archivo.add(itemAbrirBinario);
        archivo.addSeparator();
        archivo.add(itemSalir);

        JMenu ver = new JMenu("Ver");
        itemMostrarConsola = new JCheckBoxMenuItem("Mostrar consola", true);
        itemMostrarRankingBD = new JMenuItem("Ranking BD"); // ✅ aquí lo integras
        ver.add(itemMostrarConsola);
        ver.add(itemMostrarRankingBD);

        JMenu ayuda = new JMenu("Ayuda");
        itemSobre = new JMenuItem("Sobre el juego");
        ayuda.add(itemSobre);

        barra.add(archivo);
        barra.add(ver);
        barra.add(ayuda);

        setJMenuBar(barra);
    }


    public ImageIcon tintarImagen(ImageIcon icon, Color color) {
        int w = icon.getIconWidth();
        int h = icon.getIconHeight();

        BufferedImage buffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buffer.createGraphics();

        g.drawImage(icon.getImage(), 0, 0, null);

        g.setComposite(AlphaComposite.SrcAtop);
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 120));
        g.fillRect(0, 0, w, h);

        g.dispose();
        return new ImageIcon(buffer);
    }

    private void crearTablero() {
        panelTablero = new JPanel();
        panelTablero.setLayout(new GridLayout(10, 10));
        panelTablero.setBackground(Color.WHITE);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                celdas[i][j] = new JLabel();
                celdas[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                celdas[i][j].setHorizontalAlignment(JLabel.CENTER);

                final int fila = i;
                final int col = j;
                celdas[i][j].addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        if (controlador != null) {
                            controlador.seleccionarSoldado(fila, col);
                            celdas[fila][col].setBorder(
                                BorderFactory.createLineBorder(Color.YELLOW, 3)
                            );
                        }
                    }
                });

                panelTablero.add(celdas[i][j]);
            }
        }  
        add(panelTablero, BorderLayout.CENTER);
    }

 
    public void actualizarTablero(ArrayList<Soldado> ejercito1, ArrayList<Soldado> ejercito2,
                              java.util.Map<Soldado, ImageIcon> imagenes) {

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                celdas[i][j].setIcon(null);
                celdas[i][j].setOpaque(false);
                celdas[i][j].setBackground(null);
            }
        }

        for (Soldado s : ejercito1) {
            ImageIcon img = imagenes.get(s);
            if (img != null) {
                int f = s.getFila();
                int c = s.getColumna();
                celdas[f][c].setIcon(img);
                celdas[f][c].setOpaque(true);
            }
        }

        for (Soldado s : ejercito2) {
            ImageIcon img = imagenes.get(s);
            if (img != null) {
                int f = s.getFila();
                int c = s.getColumna();
                celdas[f][c].setIcon(img);
                celdas[f][c].setOpaque(true);
            }
        }

        panelTablero.revalidate();
        panelTablero.repaint();
    }

    private void crearConsola() {
        areaTexto = new JTextArea();
        areaTexto.setEditable(false);

        scrollConsola = new JScrollPane(areaTexto);

        panelDerecha = new JPanel();
        panelDerecha.setLayout(new BorderLayout());

        panelDerecha.add(scrollConsola, BorderLayout.CENTER);

        add(panelDerecha, BorderLayout.EAST);
    }

    // Getters
    public JMenuItem getItemNuevo() { return itemNuevo; }
    public JMenuItem getItemAbrirLogs() { return itemAbrirLogs; }
    public JMenuItem getItemGuardarLogs() { return itemGuardarLogs; }
    public JMenuItem getItemGuardarRanking() { return itemGuardarRanking; }
    public JMenuItem getItemGuardarConfig() { return itemGuardarConfig; }

    public JMenuItem getItemCompilar() { return itemCompilar; }
    public JMenuItem getItemSobre()   { return itemSobre; }
    public JMenuItem getItemSalir()   { return itemSalir; }
    public JCheckBoxMenuItem getItemMostrarConsola() { return itemMostrarConsola; }
    public String getConsolaTexto() {return areaTexto.getText();}

    public JMenuItem getItemGuardarBinario() {return itemGuardarBinario;}
    public JMenuItem getItemAbrirBinario() {return itemAbrirBinario;}

    public JButton getBtnArriba() { return btnArriba; }
    public JButton getBtnAbajo() { return btnAbajo; }
    public JButton getBtnIzquierda() { return btnIzquierda; }
    public JButton getBtnDerecha() { return btnDerecha; }    
    public JButton getBtnAtacar() { return btnAtacar; }

    public void setControlador(ControladorJuego c) {this.controlador = c;}

    public JMenuItem getItemMostrarRankingBD() {return itemMostrarRankingBD;}

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
    public void limpiarCelda(int fila, int columna) {
        celdas[fila][columna].setIcon(null);
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
                System.out.println("Imagen NO encontrada: " + nombre);
                return null;
            }

            ImageIcon icon = new ImageIcon(url);
            Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);

            return new ImageIcon(img);

        } catch (Exception e) {
            System.out.println("Error cargando imagen: " + nombre);
            return null;
        }
    }
    
}
