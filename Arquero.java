import java.util.Random;
public class Arquero extends Soldado {

    private int cantidadFlechas;
    public Arquero(String nombre, int fila, int columna, int ejercito) {

        super(nombre, fila, columna, ejercito);
        Random rand = new Random();        
        this.cantidadFlechas = rand.nextInt(6) + 5; 
    }
    public Arquero(int ejercito, int dimensionTablero) {
        super(ejercito, dimensionTablero);
        Random rand = new Random();
        this.cantidadFlechas = rand.nextInt(6) + 5;
    }
    public Arquero() {
        super();
        Random rand = new Random();
        this.cantidadFlechas = rand.nextInt(6) + 5;
    }
    public void dispararFlecha() {
        if (this.cantidadFlechas > 0) {
            atacar(); 
            this.cantidadFlechas--;
            System.out.println("¡" + getNombre() + " dispara una flecha!");
            System.out.println("  (Flechas restantes: " + this.cantidadFlechas + ")");

        } else {
            System.out.println("¡" + getNombre() + " no tiene más flechas!");
            defender(); 
        }
    }
    public int getCantidadFlechas() {
        return cantidadFlechas;
    }
    @Override
    public String toString() {
        String infoSoldado = super.toString(); 
        infoSoldado = infoSoldado.substring(0, infoSoldado.length() - 1);         
        return infoSoldado + ", flechas=" + cantidadFlechas + '}';
    }
}

