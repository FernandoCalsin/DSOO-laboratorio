import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;

public class VideoJuego3 {
    public static void main(String[] args) {

        int dimensionTablero = 10;
        Tablero tablero = new Tablero(dimensionTablero);
        Random random = new Random();

        ArrayList<Soldado> ejercito1 = new ArrayList<>();
        ArrayList<Soldado> ejercito2 = new ArrayList<>();

        int numSoldados1 = random.nextInt(8) + 5; // 5–12 soldados
        int numSoldados2 = random.nextInt(8) + 5;

        crearEjercito(ejercito1, 1, numSoldados1, tablero, dimensionTablero);
        crearEjercito(ejercito2, 2, numSoldados2, tablero, dimensionTablero);

        tablero.imprimirTablero();

        System.out.println("\n===== INICIO DEL JUEGO =====");
        Scanner sc = new Scanner(System.in);
        int turno = 1;

        while (!ejercito1.isEmpty() && !ejercito2.isEmpty()) {
            System.out.println("\n=============================================");
            System.out.println("TURNO DEL JUGADOR " + turno + " (Ejército " + (turno == 1 ? "$" : "#") + ")");
            Soldado soldadoAMover = null;
            int filaOrigen, colOrigen;
            boolean valido = false;

            while (!valido) {
                System.out.print("Fila del soldado a mover: ");
                filaOrigen = sc.nextInt();
                System.out.print("Columna del soldado a mover: ");
                colOrigen = sc.nextInt();

                if (filaOrigen < 0 || filaOrigen >= dimensionTablero || colOrigen < 0 || colOrigen >= dimensionTablero) {
                    System.out.println("Coordenadas fuera del tablero.");
                    continue;
                }

                soldadoAMover = tablero.getSoldado(filaOrigen, colOrigen);
                if (soldadoAMover == null) {
                    System.out.println("No hay soldado en esa posición.");
                } else if (soldadoAMover.getEjercito() != turno) {
                    System.out.println("Ese soldado pertenece al enemigo.");
                } else {
                    valido = true;
                }
            }
            valido = false;
            while (!valido) {
                System.out.print("Mover hacia (W/A/S/D): ");
                char dir = sc.next().toUpperCase().charAt(0);

                int fDestino = soldadoAMover.getFila();
                int cDestino = soldadoAMover.getColumna();

                switch (dir) {
                    case 'W' -> fDestino--;
                    case 'A' -> cDestino--;
                    case 'S' -> fDestino++;
                    case 'D' -> cDestino++;
                    default -> {
                        System.out.println("Dirección inválida.");
                        continue;
                    }
                }
                if (fDestino < 0 || fDestino >= dimensionTablero || cDestino < 0 || cDestino >= dimensionTablero) {
                    System.out.println("Movimiento fuera del tablero.");
                    continue;
                }
                Soldado destino = tablero.getSoldado(fDestino, cDestino);
            
                if (destino == null) {
                    tablero.moverSoldado(soldadoAMover.getFila(), soldadoAMover.getColumna(), fDestino, cDestino);
                    System.out.println(soldadoAMover.getNombre() + " se movió.");
                } else {
                    System.out.println(" Batalla: " + soldadoAMover.getNombre() + " VS " + destino.getNombre());
                    
                    activarHabilidadEspecial(soldadoAMover);

                    int vidaA = soldadoAMover.getVidaActual();
                    int vidaD = destino.getVidaActual();
                    double total = vidaA + vidaD;
                    double probA = vidaA / total;

                    double roll = Math.random();
                    if (roll < probA) {
                        System.out.println("Gana " + soldadoAMover.getNombre());
                        soldadoAMover.setVidaActual(vidaA + 1);
                        destino.morir();
                        (turno == 1 ? ejercito2 : ejercito1).remove(destino);
                        tablero.moverSoldado(soldadoAMover.getFila(), soldadoAMover.getColumna(), fDestino, cDestino);
                    } else {
                        System.out.println("Gana " + destino.getNombre());
                        destino.setVidaActual(vidaD + 1);
                        soldadoAMover.morir();
                        (turno == 1 ? ejercito1 : ejercito2).remove(soldadoAMover);
                        tablero.removerSoldado(soldadoAMover.getFila(), soldadoAMover.getColumna());
                    }
                }
                valido = true;
            }

            tablero.imprimirTablero();
            turno = (turno == 1) ? 2 : 1;
        }

        System.out.println("\n FIN DEL JUEGO");
        if (ejercito1.isEmpty()) System.out.println("🏁 GANA EL EJÉRCITO 2 (#)");
        else System.out.println("GANA EL EJÉRCITO 1 ($)");

        System.out.println("\n===== REPORTE FINAL =====");
        System.out.println("\nEJÉRCITO 1 ($)");
        reporteEjercito(ejercito1);
        System.out.println("\nEJÉRCITO 2 (#)");
        reporteEjercito(ejercito2);

        sc.close();
    }

    public static void crearEjercito(ArrayList<Soldado> ejercito, int numEjercito, int cantidad, Tablero tablero, int dimension) {
        Random random = new Random();

        for (int i = 0; i < cantidad; i++) {
            int fila, columna;
            do {
                fila = random.nextInt(dimension);
                columna = random.nextInt(dimension);
            } while (tablero.estaOcupado(fila, columna));

            int tipo = random.nextInt(4);
            Soldado nuevo;
            String nombre;

            switch (tipo) {
                case 0 -> {
                    nombre = "Espadachin" + i + "X" + numEjercito;
                    nuevo = new Espadachin(nombre, fila, columna, numEjercito, random.nextInt(3) + 4);
                }
                case 1 -> {
                    nombre = "Arquero" + i + "X" + numEjercito;
                    nuevo = new Arquero(nombre, fila, columna, numEjercito, random.nextInt(2) + 2);
                }
                case 2 -> {
                    nombre = "Caballero" + i + "X" + numEjercito;
                    nuevo = new Caballero(nombre, fila, columna, numEjercito, random.nextInt(3) + 5);
                }
                default -> {
                    nombre = "Lancero" + i + "X" + numEjercito;
                    nuevo = new Lancero(nombre, fila, columna, numEjercito, random.nextInt(3) + 3);
                }
            }

            ejercito.add(nuevo);
            tablero.colocarSoldado(nuevo);
        }
    }

    public static void activarHabilidadEspecial(Soldado s) {
        if (s instanceof Arquero a) a.dispararFlecha();
        else if (s instanceof Caballero c) c.envestir();
        else if (s instanceof Espadachin e) e.crearMuroDeEscudos();
        else if (s instanceof Lancero l) l.schiltrom();
    }

    public static void reporteEjercito(ArrayList<Soldado> ejercito) {
        if (ejercito.isEmpty()) {
            System.out.println("El ejército fue derrotado.");
            return;
        }

        Soldado mayorVida = Collections.max(ejercito, Comparator.comparing(Soldado::getVidaActual));
        double promedio = ejercito.stream().mapToDouble(Soldado::getVidaActual).average().orElse(0.0);

        System.out.println("Soldado con mayor vida: " + mayorVida);
        System.out.printf("Promedio de vida: %.2f\n", promedio);

        System.out.println("\nSoldados por orden de creación:");
        ejercito.forEach(System.out::println);

        System.out.println("\nRanking de poder (mayor a menor vida):");
        ejercito.stream()
                .sorted(Comparator.comparing(Soldado::getVidaActual).reversed())
                .forEach(s -> System.out.println("  " + s.getNombre() + " -> " + s.getVidaActual() + " pts"));
    }
}
