import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;

public class VideoJuego3 {

    public static void main(String[] args) {
        // 1. Inicialización del juego
        int dimensionTablero = 10;
        Tablero tablero = new Tablero(dimensionTablero);
        Random random = new Random();

        ArrayList<Soldado> ejercito1 = new ArrayList<>();
        ArrayList<Soldado> ejercito2 = new ArrayList<>();

        int numSoldados1 = random.nextInt(10) + 1;
        int numSoldados2 = random.nextInt(10) + 1;

        crearEjercito(ejercito1, 1, numSoldados1, tablero, dimensionTablero);
        crearEjercito(ejercito2, 2, numSoldados2, tablero, dimensionTablero);
        
        // 2. Mostrar estado inicial
        tablero.imprimirTablero();
        System.out.println("\n     REPORTE INICIAL DE EJÉRCITOS     ");
        System.out.println("\n    EJÉRCITO 1 ($)    ");
        reporteEjercito(ejercito1);
        System.out.println("\n   EJÉRCITO 2 (#)   ");
        reporteEjercito(ejercito2);
        
        // 3. Inicia el Bucle del Juego Interactivo
        Scanner sc = new Scanner(System.in);
        int turno = 1;

        while (!ejercito1.isEmpty() && !ejercito2.isEmpty()) {
            System.out.println("\n=============================================");
            System.out.println("TURNO DEL JUGADOR " + turno + " (Ejército " + (turno == 1 ? "$" : "#") + ")");
            
            Soldado soldadoAMover = null;
            int filaOrigen = -1, colOrigen = -1;
            boolean datosValidos = false;

            // Bucle para validar la selección del soldado
            while (!datosValidos) {
                System.out.print("Ingrese la fila del soldado a mover: ");
                filaOrigen = sc.nextInt();
                System.out.print("Ingrese la columna del soldado a mover: ");
                colOrigen = sc.nextInt();

                if (filaOrigen < 0 || filaOrigen >= dimensionTablero || colOrigen < 0 || colOrigen >= dimensionTablero) {
                    System.out.println("Error: Coordenadas fuera del tablero.");
                    continue;
                }
                soldadoAMover = tablero.getSoldado(filaOrigen, colOrigen);
                if (soldadoAMover == null) {
                    System.out.println("Error: No hay ningún soldado en esa posición.");
                } else if (soldadoAMover.getEjercito() != turno) {
                    System.out.println("Error: Ese soldado no pertenece a tu ejército.");
                } else {
                    datosValidos = true;
                }
            }

            // Bucle para validar el movimiento
            datosValidos = false;
            while (!datosValidos) {
                System.out.print("Mover hacia (W/A/S/D): ");
                char direccion = sc.next().toUpperCase().charAt(0);
                
                int filaDestino = filaOrigen;
                int colDestino = colOrigen;

                switch (direccion) {
                    case 'W': filaDestino--; break;
                    case 'A': colDestino--; break;
                    case 'S': filaDestino++; break;
                    case 'D': colDestino++; break;
                    default: 
                        System.out.println("Dirección no válida."); 
                        continue;
                }

                if (filaDestino < 0 || filaDestino >= dimensionTablero || colDestino < 0 || colDestino >= dimensionTablero) {
                    System.out.println("Error: Movimiento fuera del tablero.");
                    continue;
                }

                Soldado soldadoEnDestino = tablero.getSoldado(filaDestino, colDestino);
                if (soldadoEnDestino != null && soldadoEnDestino.getEjercito() == turno) {
                    System.out.println("Error: No puedes moverte a una casilla ocupada por un aliado.");
                    continue;
                }
                
                // Si el movimiento es válido, se ejecuta la acción
                if (soldadoEnDestino == null) { // Movimiento a casilla vacía
                    tablero.moverSoldado(filaOrigen, colOrigen, filaDestino, colDestino);
                    System.out.println(soldadoAMover.getNombre() + " se ha movido.");
                } else { // Hay un enemigo: ¡Batalla!
                    System.out.println("¡BATALLA! " + soldadoAMover.getNombre() + " vs " + soldadoEnDestino.getNombre());
                    
                    if (soldadoAMover.getVidaActual() >= soldadoEnDestino.getVidaActual()) {
                        System.out.println("GANA " + soldadoAMover.getNombre());
                        soldadoEnDestino.morir();
                        (turno == 1 ? ejercito2 : ejercito1).remove(soldadoEnDestino);
                        tablero.moverSoldado(filaOrigen, colOrigen, filaDestino, colDestino);
                    } else {
                        System.out.println("GANA " + soldadoEnDestino.getNombre());
                        soldadoAMover.morir();
                        (turno == 1 ? ejercito1 : ejercito2).remove(soldadoAMover);
                        tablero.removerSoldado(filaOrigen, colOrigen);
                    }
                }
                datosValidos = true;
            }
            
            // Mostrar estado y cambiar de turno
            tablero.imprimirTablero();
            turno = (turno == 1) ? 2 : 1;
        }

        // 4. Fin del juego
        System.out.println("\n  FIN DEL JUEGO  ");
        if (ejercito1.isEmpty()) {
            System.out.println("EL GANADOR ES EL JUGADOR 2");
        } else {
            System.out.println("EL GANADOR ES EL JUGADOR 1");
        }
        sc.close();
    }
    
    // Los métodos de creación y reporte se mantienen igual, solo se ajusta el constructor
    public static void crearEjercito(ArrayList<Soldado> ejercito, int numEjercito, int cantidad, Tablero tablero, int dimension) {
        for (int i = 0; i < cantidad; i++) {
            int fila, columna;
            do {
                fila = new Random().nextInt(dimension);
                columna = new Random().nextInt(dimension);
            } while (tablero.estaOcupado(fila, columna));

            String nombre = "Soldado" + i + "X" + numEjercito;
            Soldado nuevoSoldado = new Soldado(nombre, fila, columna, numEjercito);
            
            ejercito.add(nuevoSoldado);
            tablero.colocarSoldado(nuevoSoldado);
        }
    }
    
    public static void reporteEjercito(ArrayList<Soldado> ejercito) {
        if (ejercito.isEmpty()) {
            System.out.println("El ejército no tiene soldados.");
            return;
        }
        Soldado mayorVida = Collections.max(ejercito, Comparator.comparing(Soldado::getVidaActual));
        System.out.println("Soldado con mayor vida: " + mayorVida);
        
        double promedioVida = ejercito.stream().mapToDouble(Soldado::getVidaActual).average().orElse(0.0);
        System.out.printf("Promedio de vida: %.2f\n", promedioVida);

        System.out.println("\nSoldados por orden de creación:");
        ejercito.forEach(System.out::println);
        System.out.println("\nRanking de poder (mayor a menor vida):");
        ejercito.stream()
                .sorted(Comparator.comparing(Soldado::getVidaActual).reversed())
                .forEach(System.out::println);
    }
}