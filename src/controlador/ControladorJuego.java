package controlador;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import modelo.*;
import vista.VistaArchivos;
import vista.VistaJuego;

public class ControladorJuego {

    private final String RUTA_DATA = "data";

    private VistaJuego vista;
    private Random rand = new Random();

    private ImageIcon imgArquero;
    private ImageIcon imgEspadachin;
    private ImageIcon imgLancero;
    private ImageIcon imgCabMontado;
    private ImageIcon imgCabDesmontado;

    private ArrayList<Soldado> listaSoldados = new ArrayList<>();

    public ControladorJuego(VistaJuego vista) {
        this.vista = vista;

        System.out.println("RUTA USADA PARA DATA = " + RUTA_DATA);
        crearCarpetaData();
        cargarImagenes();
        agregarEventos();
    }

    private void crearCarpetaData() {
        File carpeta = new File(RUTA_DATA);
        if (!carpeta.exists()) carpeta.mkdir();
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
        vista.getItemSobre().addActionListener(e -> vista.mostrarMensaje("Simulador de Batalla\nCreado por Luis :D"));
        vista.getItemMostrarConsola().addActionListener(e -> vista.toggleConsola());

        vista.getItemNuevo().addActionListener(e -> nuevoJuego());
        vista.getItemGuardarLogs().addActionListener(e -> guardarLogs());
        vista.getItemAbrirLogs().addActionListener(e -> abrirArchivo("logs.txt"));
        vista.getItemGuardarRanking().addActionListener(e -> guardarRanking());
        vista.getItemGuardarConfig().addActionListener(e -> guardarConfig());

        vista.getItemGuardarBinario().addActionListener(e -> guardarBinario(listaSoldados));
        vista.getItemAbrirBinario().addActionListener(e -> abrirBinario());
    }

    public void guardarBinario(Object datos) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar archivo binario");
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(chooser.getSelectedFile()))) {
                oos.writeObject(datos);
                JOptionPane.showMessageDialog(null, "Archivo guardado correctamente");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al guardar: " + e.getMessage());
            }
        }
    }

    public void abrirBinario() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Abrir archivo binario");
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(chooser.getSelectedFile()))) {
                ArrayList<Soldado> cargados = (ArrayList<Soldado>) ois.readObject();
                if (cargados == null) {
                    JOptionPane.showMessageDialog(null, "El archivo está vacío.");
                    return;
                }
                this.listaSoldados = cargados;
                vista.limpiarTablero();
                for (Soldado s : listaSoldados) {
                    ImageIcon icon = obtenerImagenSoldado(s);
                    if (icon != null) {
                        vista.setImagenCelda(s.getFila(), s.getColumna(), icon);
                    }
                }
                int esp = 0, arq = 0, cab = 0, lan = 0;

                for (Soldado s : listaSoldados) {
                    if (s instanceof Espadachin) esp++;
                    else if (s instanceof Arquero) arq++;
                    else if (s instanceof Caballero) cab++;
                    else if (s instanceof Lancero) lan++;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("==============================\n");
                sb.append("DATOS CARGADOS DESDE ARCHIVO\n");
                sb.append("Cantidad total: ").append(listaSoldados.size()).append("\n");
                sb.append("Espadachines: ").append(esp).append("\n");
                sb.append("Arqueros: ").append(arq).append("\n");
                sb.append("Caballeros: ").append(cab).append("\n");
                sb.append("Lanceros: ").append(lan).append("\n");
                sb.append("------------------------------\n");
                int i = 1;
                for (Soldado s : listaSoldados) {
                    sb.append(String.format(
                            "%02d) %-12s | Vida: %2d | Ataque: %2d | Defensa: %2d\n",
                            i++,
                            s.getClass().getSimpleName(),
                            s.getNivelVida(),
                            s.getNivelAtaque(),
                            s.getNivelDefensa()
                    ));
                }
                sb.append("==============================\n");
                vista.setConsolaTexto(sb.toString());
                JOptionPane.showMessageDialog(null, "Datos cargados correctamente.");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al abrir: " + e.getMessage());
            }
        }
    }


    private void guardarLogs() {
        try {
            FileWriter fw = new FileWriter(RUTA_DATA + "/logs.txt");
            fw.write(vista.getConsolaTexto());
            fw.close();
            vista.mostrarMensaje("Logs guardados correctamente.");
        } catch (Exception e) {
            vista.mostrarMensaje("Error guardando logs.");
            e.printStackTrace();
        }
    }

    private void guardarRanking() {
        try {
            FileWriter fw = new FileWriter(RUTA_DATA + "/ranking.txt");
            fw.write("Ranking de Jugadores\n");
            fw.write("Jugador1 - 150 pts\n");
            fw.write("Jugador2 - 90 pts\n");
            fw.close();
            vista.mostrarMensaje("Ranking guardado.");
        } catch (Exception e) {
            vista.mostrarMensaje("No se pudo guardar ranking.");
            e.printStackTrace();
        }
    }

    private void guardarConfig() {
        try {
            FileWriter fw = new FileWriter(RUTA_DATA + "/config.txt");
            fw.write("mostrarConsola=" + vista.getItemMostrarConsola().isSelected());
            fw.close();
            vista.mostrarMensaje("Configuración guardada.");
        } catch (Exception e) {
            vista.mostrarMensaje("Error guardando configuración.");
            e.printStackTrace();
        }
    }

    private void abrirArchivo(String nombre) {
        try {
            File file = new File(RUTA_DATA + "/" + nombre);

            if (!file.exists()) {
                vista.mostrarMensaje("El archivo no existe.");
                return;
            }

            String contenido = new String(Files.readAllBytes(file.toPath()));
            new VistaArchivos("Visualizando " + nombre, contenido);

        } catch (Exception e) {
            vista.mostrarMensaje("Error al abrir archivo.");
            e.printStackTrace();
        }
    }

    private void nuevoJuego() {
        vista.limpiarTablero();
        vista.setConsolaTexto("");
        vista.mostrarMensaje("Juego reiniciado.");
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

        listaSoldados.clear();
        listaSoldados.addAll(e1.getSoldados());
        listaSoldados.addAll(e2.getSoldados());

        vista.setConsolaTexto(sb.toString());
    }

    private void colocarSoldadosEnTablero(Ejercito ej) {
        for (Soldado s : ej.getSoldados()) {
            int x, y;
            do {
                x = rand.nextInt(10);
                y = rand.nextInt(10);
            } while (!vista.isCeldaVacia(x, y));

            s.mover(x, y);
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
