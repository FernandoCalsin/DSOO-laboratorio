import java.util.Random;

public class VideoJuego3 {
    public static void main(String[] args) {
        Ejercito ejercito1 = new Ejercito("Inglaterra", 10);
        Ejercito ejercito2 = new Ejercito("Francia", 3);

        // --- Mostrar datos detallados ---
        mostrarDatos(ejercito1);
        mostrarDatos(ejercito2);

        // --- Calcular probabilidades ---
        int vida1 = ejercito1.getVidaTotal();
        int vida2 = ejercito2.getVidaTotal();
        double totalVida = vida1 + vida2;

        double prob1 = (vida1 / totalVida) * 100;
        double prob2 = (vida2 / totalVida) * 100;

        System.out.printf("\nEjercito 1: %s: %d %.2f%% de probabilidad de victoria\n", ejercito1.getNombre(), vida1, prob1);
        System.out.printf("Ejercito 2: %s: %d %.2f%% de probabilidad de victoria\n", ejercito2.getNombre(), vida2, prob2);

        // --- Simular batalla (aleatorio) ---
        Random random = new Random();
        double aleatorio = random.nextDouble() * 100;
        System.out.printf("\nNúmero aleatorio generado: %.2f\n", aleatorio);

        if (aleatorio <= prob1) {
            System.out.printf("El ganador es el ejército 1 de: %s.\n", ejercito1.getNombre());
            System.out.println("Ya que al generar los porcentajes de probabilidad de victoria basada en los niveles de vida de sus soldados y aplicando un experimento aleatorio salió vencedor.");
        } else {
            System.out.printf("El ganador es el ejército 2 de: %s.\n", ejercito2.getNombre());
            System.out.println("Ya que al generar los porcentajes de probabilidad de victoria basada en los niveles de vida de sus soldados y aplicando un experimento aleatorio salió vencedor.");
        }
    }

    private static void mostrarDatos(Ejercito e) {
        System.out.println("\n==============================");
        System.out.println("Ejército: " + e.getNombre());
        System.out.println("Cantidad total de soldados creados: " + e.getCantidad());
        System.out.println("Espadachines: " + e.contarTipo(Espadachin.class));
        System.out.println("Arqueros: " + e.contarTipo(Arquero.class));
        System.out.println("Caballeros: " + e.contarTipo(Caballero.class));
        System.out.println("Lanceros: " + e.contarTipo(Lancero.class));
        System.out.println("------------------------------");
        System.out.println("Detalles de soldados:");

        int contador = 1;
        for (Soldado s : e.getSoldados()) {
            System.out.printf("%02d) %-12s | Vida: %2d | Ataque: %2d | Defensa: %2d\n",
                    contador++, s.getClass().getSimpleName(), s.getNivelVida(), s.getNivelAtaque(), s.getNivelDefensa());
        }
        System.out.println("==============================\n");
    }
}
