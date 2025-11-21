package modelo;
import java.util.ArrayList;
import java.util.Random;
public class Ejercito {
    private String nombre;
    private ArrayList<Soldado> soldados = new ArrayList<>();
    public Ejercito(String nombre, int cantidad) {
        this.nombre = nombre;
        crearSoldados(cantidad);
    }
    private void crearSoldados(int cantidad) {
        Random random = new Random();
        for (int i = 0; i < cantidad; i++) {
            int tipo = random.nextInt(4);
            Soldado s;
            switch (tipo) {
                case 0 -> s = new Espadachin();
                case 1 -> s = new Arquero();
                case 2 -> s = new Caballero();
                default -> s = new Lancero();
            }
            s.setNivelVida(10 + random.nextInt(21));
            s.setNivelAtaque(5 + random.nextInt(11));
            s.setNivelDefensa(3 + random.nextInt(8));
            soldados.add(s);
        }
    }
    public int getVidaTotal() {
        int total = 0;
        for (Soldado s : soldados) {
            total += s.getNivelVida();
        }
        return total;
    }
    public int contarTipo(Class<?> tipo) {
        int c = 0;
        for (Soldado s : soldados) {
            if (s.getClass() == tipo) c++;
        }
        return c;
    }
    public int getCantidad() {return soldados.size();}
    public String getNombre() {return nombre;}
    public ArrayList<Soldado> getSoldados() {return soldados;}
}
