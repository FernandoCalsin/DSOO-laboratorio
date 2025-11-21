package controlador;

import java.util.Random;
import javax.swing.*;
import modelo.*;
import vista.VistaJuego;

public class ControladorJuego {

    private VistaJuego vista;
    private Random rand = new Random();

    private ImageIcon imgArquero;
    private ImageIcon imgEspadachin;
    private ImageIcon imgLancero;
    private ImageIcon imgCabMontado;
    private ImageIcon imgCabDesmontado;

    public ControladorJuego(VistaJuego vista) {
        this.vista = vista;

        cargarImagenes();
        agregarEventos();
    }

    private void cargarImagenes() {
        imgArquero = vista.cargarImagen("arquero.png");
        imgEspadachin = vista.cargarImagen("espadachin.png");
        imgLancero = vista.cargarImagen("lancero.png");
        imgCabMontado = vista.cargarImagen("caballero_montado.png");
        imgCabDesmontado = vista.cargarImagen("caballero_desmontado.png");
    }

    private void agregarEventos() {

        vista.getItemCompilar().addActionListener(e -> generarEjercitos());

        vista.getItemSalir().addActionListener(e -> System.exit(0));

        vista.getItemSobre().addActionListener(e ->
                vista.mostrarMensaje("Simulador de Batalla\nCreado por Luis :D"));

        vista.getItemMostrarConsola().addActionListener(
                e -> vista.toggleConsola()
        );
    }

    public void generarEjercitos() {

        vista.limpiarTablero();

        Ejercito e1 = new Ejercito("Inglaterra", 10);
        Ejercito e2 = new Ejercito("Francia", 10);

        StringBuilder sb = new StringBuilder();
        sb.append(mostrarDatos(e1));
        sb.append(mostrarDatos(e2));

        colocarSoldadosEnTablero(e1);
        colocarSoldadosEnTablero(e2);

        vista.setConsolaTexto(sb.toString());
    }

    private void colocarSoldadosEnTablero(Ejercito ej) {
        for (Soldado s : ej.getSoldados()) {
            int x, y;
            do {
                x = rand.nextInt(10);
                y = rand.nextInt(10);
            } while (!vista.isCeldaVacia(x,y));

            vista.setImagenCelda(x, y, obtenerImagenSoldado(s));
        }
    }

    private ImageIcon obtenerImagenSoldado(Soldado s) {
        if (s instanceof Arquero) return imgArquero;
        if (s instanceof Espadachin) return imgEspadachin;
        if (s instanceof Lancero) return imgLancero;

        if (s instanceof Caballero) {
            Caballero c = (Caballero) s;
            return c.isEstaMontado() ? imgCabMontado : imgCabDesmontado;
        }
        return null;
    }

    private String mostrarDatos(Ejercito e) {
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
