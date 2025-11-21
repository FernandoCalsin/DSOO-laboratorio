import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class controler {
    public static void main(String[] args) {

        Random rand = new Random();
        JFrame ventana = new JFrame("Simulador de Batalla");
        ventana.setSize(900, 700);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLayout(null);

        JMenuBar barraMenu = new JMenuBar();
        JMenu menuArchivo = new JMenu("Archivo");
        JMenu menuEditar = new JMenu("Editar");
        JMenu menuVer = new JMenu("Ver");
        JMenu menuAyuda = new JMenu("Ayuda");

        JMenuItem botonCompilar = new JMenuItem("Compilar soldados");
        menuArchivo.add(botonCompilar);

        barraMenu.add(menuArchivo);
        barraMenu.add(menuEditar);
        barraMenu.add(menuVer);
        barraMenu.add(menuAyuda);

        ventana.setJMenuBar(barraMenu);

        JPanel tablero = new JPanel();
        tablero.setLayout(new GridLayout(10, 10));
        tablero.setBounds(20, 20, 520, 520);
        ventana.add(tablero);

        JLabel[][] celdas = new JLabel[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                celdas[i][j] = new JLabel();
                celdas[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                celdas[i][j].setHorizontalAlignment(JLabel.CENTER);
                tablero.add(celdas[i][j]);
            }
        }
        JTextArea areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaTexto);
        scroll.setBounds(560, 20, 300, 520);
        ventana.add(scroll);

        ImageIcon imgArquero       = cargarImagen("arquero.png");
        ImageIcon imgEspadachin    = cargarImagen("espadachin.png");
        ImageIcon imgLancero       = cargarImagen("lancero.png");
        ImageIcon imgCabMontado    = cargarImagen("caballero_montado.png");
        ImageIcon imgCabDesmontado = cargarImagen("caballero_desmontado.png");

        menuAyuda.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                JOptionPane.showMessageDialog(
                    null,
                    "Simulador de Batalla\nVersión 1.0\nCreado por Luis :D",
                    "Ayuda",
                    JOptionPane.INFORMATION_MESSAGE
                    );
            }
        });

        botonCompilar.addActionListener(e -> {

            for (int i = 0; i < 10; i++)
                for (int j = 0; j < 10; j++)
                    celdas[i][j].setIcon(null);

            Ejercito ejercito1 = new Ejercito("Inglaterra", 10);
            Ejercito ejercito2 = new Ejercito("Francia", 10);
            StringBuilder sb = new StringBuilder();
            sb.append(mostrarDatos(ejercito1));
            sb.append(mostrarDatos(ejercito2));            

            for (Soldado s : ejercito1.getSoldados()) {
                int x = rand.nextInt(10);
                int y = rand.nextInt(10);
                celdas[x][y].setIcon(obtenerImagenSoldado(
                        s, imgArquero, imgEspadachin, imgLancero, imgCabMontado, imgCabDesmontado
                ));
            }

            for (Soldado s : ejercito2.getSoldados()) {
                int x = rand.nextInt(10);
                int y = rand.nextInt(10);
                celdas[x][y].setIcon(obtenerImagenSoldado(
                        s, imgArquero, imgEspadachin, imgLancero, imgCabMontado, imgCabDesmontado
                ));
            }
            areaTexto.setText(sb.toString());
        });
        ventana.setVisible(true);
    }
    private static ImageIcon cargarImagen(String nombre) {
        try {
            java.net.URL url = controler.class.getResource("/imagenes/" + nombre);
            if (url == null) {
                System.out.println("NO SE ENCONTRÓ: " + nombre);
                return null;
            }
            ImageIcon icon = new ImageIcon(url);
            Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            System.out.println("Error cargando: " + nombre);
            return null;
        }
    }
    private static ImageIcon obtenerImagenSoldado(Soldado s,
            ImageIcon a, ImageIcon e, ImageIcon l, ImageIcon cm, ImageIcon cd) {

        if (s instanceof Arquero) return a;
        if (s instanceof Espadachin) return e;
        if (s instanceof Lancero) return l;

        if (s instanceof Caballero) {
            Caballero c = (Caballero) s;
            return c.estaMontado() ? cm : cd;
        }

        return null;
    }
    private static String mostrarDatos(Ejercito e) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n==============================\n");
        sb.append("Ejército: ").append(e.getNombre()).append("\n");
        sb.append("Cantidad total de soldados: ").append(e.getCantidad()).append("\n");
        sb.append("Espadachines: ").append(e.contarTipo(Espadachin.class)).append("\n");
        sb.append("Arqueros: ").append(e.contarTipo(Arquero.class)).append("\n");
        sb.append("Caballeros: ").append(e.contarTipo(Caballero.class)).append("\n");
        sb.append("Lanceros: ").append(e.contarTipo(Lancero.class)).append("\n");
        sb.append("------------------------------\n");

        int contador = 1;
        for (Soldado s : e.getSoldados()) {
            sb.append(String.format(
                    "%02d) %-12s | Vida: %2d | Ataque: %2d | Defensa: %2d\n",
                    contador++,
                    s.getClass().getSimpleName(),
                    s.getNivelVida(),
                    s.getNivelAtaque(),
                    s.getNivelDefensa()
            ));
        }
        sb.append("==============================\n");
        return sb.toString();
    }    
}
