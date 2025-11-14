import java.util.Random;
import javax.swing.*;

public class controler {

    public static void main(String[] args) {

        JFrame ventana = new JFrame("Simulador de Batalla");
        ventana.setSize(700, 700);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLayout(null);

        JLabel etiqueta = new JLabel("Presiona el botón para iniciar la batalla", JLabel.CENTER);
        etiqueta.setBounds(150, 20, 400, 30);
        ventana.add(etiqueta);

        JButton boton = new JButton("Iniciar batalla");
        boton.setBounds(250, 70, 180, 40);
        ventana.add(boton);
 
        JTextArea areaTexto = new JTextArea();
        areaTexto.setEditable(false);

        JScrollPane scroll = new JScrollPane(areaTexto);
        scroll.setBounds(40, 130, 600, 500);
        ventana.add(scroll);

        boton.addActionListener(e -> {

            Ejercito ejercito1 = new Ejercito("Inglaterra", 10);
            Ejercito ejercito2 = new Ejercito("Francia", 3);
 
            StringBuilder sb = new StringBuilder();

            sb.append(mostrarDatos(ejercito1));
            sb.append(mostrarDatos(ejercito2));

            int vida1 = ejercito1.getVidaTotal();
            int vida2 = ejercito2.getVidaTotal();
            double totalVida = vida1 + vida2;

            double prob1 = (vida1 / totalVida) * 100;
            double prob2 = (vida2 / totalVida) * 100;

            sb.append(String.format("\nProbabilidades:\n"));
            sb.append(String.format("%s: %d vida | %.2f%% prob. victoria\n", ejercito1.getNombre(), vida1, prob1));
            sb.append(String.format("%s: %d vida | %.2f%% prob. victoria\n", ejercito2.getNombre(), vida2, prob2));

            Random random = new Random();
            double aleatorio = random.nextDouble() * 100;
            sb.append(String.format("\nNúmero aleatorio generado: %.2f\n", aleatorio));

            if (aleatorio <= prob1) {
                sb.append(String.format("\nGANADOR: %s\n", ejercito1.getNombre()));
            } else {
                sb.append(String.format("\nGANADOR: %s\n", ejercito2.getNombre()));
            }
            areaTexto.setText(sb.toString());
        });

        ventana.setVisible(true);
    }

    private static String mostrarDatos(Ejercito e) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n==============================\n");
        sb.append("Ejército: ").append(e.getNombre()).append("\n");
        sb.append("Cantidad total de soldados creados: ").append(e.getCantidad()).append("\n");
        sb.append("Espadachines: ").append(e.contarTipo(Espadachin.class)).append("\n");
        sb.append("Arqueros: ").append(e.contarTipo(Arquero.class)).append("\n");
        sb.append("Caballeros: ").append(e.contarTipo(Caballero.class)).append("\n");
        sb.append("Lanceros: ").append(e.contarTipo(Lancero.class)).append("\n");
        sb.append("------------------------------\n");

        int contador = 1;
        for (Soldado s : e.getSoldados()) {
            sb.append(String.format("%02d) %-12s | Vida: %2d | Ataque: %2d | Defensa: %2d\n",
                    contador++, s.getClass().getSimpleName(), s.getNivelVida(), s.getNivelAtaque(), s.getNivelDefensa()));
        }
        sb.append("==============================\n");

        return sb.toString();
    }
}
