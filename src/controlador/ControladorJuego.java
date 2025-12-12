package controlador;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    private final Color COLOR_AZUL = new Color(50, 50, 255);
    private final Color COLOR_ROJO = new Color(255, 50, 50);


    private Soldado soldadoSeleccionado = null;

    public void seleccionarSoldado(int fila, int columna) {
        for (Soldado s : listaSoldados) {
            if (s.getFila() == fila && s.getColumna() == columna) {
                soldadoSeleccionado = s;
                vista.mostrarMensaje("Soldado seleccionado: " + s.getNombre());
                return;
            }
        }
        vista.mostrarMensaje("No hay soldado en esa posición.");
    }

    private void mover(int df, int dc) {
        if (soldadoSeleccionado == null) {
            vista.mostrarMensaje("Selecciona un soldado primero.");
            return;
        }
        int nuevaFila = soldadoSeleccionado.getFila() + df;
        int nuevaCol = soldadoSeleccionado.getColumna() + dc;

        if (nuevaFila < 0 || nuevaFila >= 10 || nuevaCol < 0 || nuevaCol >= 10) {
            vista.mostrarMensaje("Movimiento fuera del tablero.");
            return;
        }

        for (Soldado s : listaSoldados) {
            if (s.getFila() == nuevaFila && s.getColumna() == nuevaCol) {
                vista.mostrarMensaje("Hay un soldado bloqueando el paso.");
                return;
            }
        }
        vista.limpiarCelda(soldadoSeleccionado.getFila(), soldadoSeleccionado.getColumna());
    
        soldadoSeleccionado.setFila(nuevaFila);
        soldadoSeleccionado.setColumna(nuevaCol);
        vista.setImagenCelda(nuevaFila, nuevaCol, obtenerImagenSoldado(soldadoSeleccionado));
    }

    public void atacar() {
        if (soldadoSeleccionado == null) {
            vista.mostrarMensaje("Selecciona un soldado primero.");
            return;
        }

        int[][] dirs = { {-1,0}, {1,0}, {0,-1}, {0,1} };

        for (int[] d : dirs) {
            int f = soldadoSeleccionado.getFila() + d[0];
            int c = soldadoSeleccionado.getColumna() + d[1];

            for (Soldado enemigo : new ArrayList<>(listaSoldados)) {
                if (enemigo != soldadoSeleccionado &&
                    enemigo.getFila() == f &&
                    enemigo.getColumna() == c) {

                    int daño = soldadoSeleccionado.getNivelAtaque() - enemigo.getNivelDefensa();
                    if (daño < 1) daño = 1;

                    int nuevaVida = enemigo.getVidaActual() - daño;
                    enemigo.setVidaActual(nuevaVida);

                    if (nuevaVida <= 0) {
                        vista.mostrarMensaje("¡" + enemigo.getNombre() + " murió!");
                        vista.limpiarCelda(enemigo.getFila(), enemigo.getColumna());
                        listaSoldados.remove(enemigo);
                    } else {
                        vista.mostrarMensaje("Hit: -" + daño + " (vida restante: " + nuevaVida + ")");
                    }
                    return;
                }
            }
        }
        vista.mostrarMensaje("No hay enemigos adyacentes.");
    }


    public ControladorJuego(VistaJuego vista) {
        this.vista = vista;

        System.out.println("RUTA USADA PARA DATA = " + RUTA_DATA);
        crearCarpetaData();
        cargarImagenes();
        agregarEventos();
        vista.crearPanelControles();
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

        vista.getBtnArriba().addActionListener(e -> moverSoldado(0, -1));
        vista.getBtnAbajo().addActionListener(e -> moverSoldado(0, 1));
        vista.getBtnIzquierda().addActionListener(e -> moverSoldado(-1, 0));
        vista.getBtnDerecha().addActionListener(e -> moverSoldado(1, 0));
        vista.getBtnAtacar().addActionListener(e -> atacar());


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

        Ejercito e1 = new Ejercito("Inglaterra", 10, 1);
        Ejercito e2 = new Ejercito("Francia", 10, 2);

        colocarSoldadosEnTablero(e1);
        colocarSoldadosEnTablero(e2);

        for (Soldado s : e1.getSoldados()) s.setEjercito(1);
        for (Soldado s : e2.getSoldados()) s.setEjercito(2);

        StringBuilder sb = new StringBuilder();
        sb.append(mostrarDatos(e1));
        sb.append(mostrarDatos(e2));

        listaSoldados.clear();
        listaSoldados.addAll(e1.getSoldados());
        listaSoldados.addAll(e2.getSoldados());

        Map<Soldado, ImageIcon> imagenes = new HashMap<>();
        for (Soldado s : listaSoldados) {
            imagenes.put(s, obtenerImagenSoldado(s));
        }

        vista.setConsolaTexto(sb.toString());

        vista.actualizarTablero(e1.getSoldados(), e2.getSoldados(), imagenes);
    }

    private void colocarSoldadosEnTablero(Ejercito ej) {
        boolean[][] ocupado = new boolean[10][10];

        for (Soldado s : ej.getSoldados()) {
            int x, y;
            do {
                x = rand.nextInt(10);
                y = rand.nextInt(10);
            } while (ocupado[x][y]);

            ocupado[x][y] = true;

            s.setFila(x);
            s.setColumna(y);
        }
    }
    
    private ImageIcon obtenerImagenSoldado(Soldado s) {
        if (s == null) return null;
        ImageIcon base = null;
        if (s instanceof Arquero) {
            base = imgArquero;
        } else if (s instanceof Espadachin) {
            base = imgEspadachin;
        } else if (s instanceof Lancero) {
            base = imgLancero;
        } else if (s instanceof Caballero) {
            Caballero c = (Caballero) s;
            base = c.isEstaMontado() ? imgCabMontado : imgCabDesmontado;
        }
        if (base == null) return null;
        Color colorEquipo = (s.getEjercito() == 1)
                ? COLOR_AZUL
                : COLOR_ROJO;
        return vista.tintarImagen(base, colorEquipo);
    }


    private void moverSoldado(int dx, int dy) {
        if (soldadoSeleccionado == null) {
            vista.mostrarMensaje("No hay soldado seleccionado.");
            return;
        }

        int nuevaFila = soldadoSeleccionado.getFila() + dy;
        int nuevaCol = soldadoSeleccionado.getColumna() + dx;

        if (nuevaFila < 0 || nuevaFila >= 10 || nuevaCol < 0 || nuevaCol >= 10) {
            vista.mostrarMensaje("Movimiento fuera del tablero.");
            return;
        }   

        if (!vista.isCeldaVacia(nuevaFila, nuevaCol)) {
            vista.mostrarMensaje("La celda está ocupada.");
            return;
        }

        vista.limpiarCelda(soldadoSeleccionado.getFila(), soldadoSeleccionado.getColumna());

        soldadoSeleccionado.setFila(nuevaFila);
        soldadoSeleccionado.setColumna(nuevaCol);

        vista.setImagenCelda(nuevaFila, nuevaCol, obtenerImagenSoldado(soldadoSeleccionado));
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
