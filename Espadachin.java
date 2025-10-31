import java.util.Random;
public class Espadachin extends Soldado {  
    private int longitudDeEspada;
    public Espadachin(String nombre, int fila, int columna, int ejercito, int vida) {
        super(nombre, fila, columna, ejercito);
        setVidaActual(vida);
    }
    public Espadachin(String nombre, int fila, int columna, int ejercito) {   
        super(nombre, fila, columna, ejercito); 
        Random rand = new Random();
        this.longitudDeEspada = rand.nextInt(3) + 1;
    }
    public Espadachin(int ejercito, int dimensionTablero) {
        super(ejercito, dimensionTablero);
        Random rand = new Random();
        this.longitudDeEspada = rand.nextInt(3) + 1;
    }
    public Espadachin() {
        super();
        Random rand = new Random();
        this.longitudDeEspada = rand.nextInt(3) + 1;
    }
    public void crearMuroDeEscudos() {
        defender(); 
        System.out.println("¡" + getNombre() + " forma un muro de escudos!");
    }
    public int getLongitudDeEspada() {
        return longitudDeEspada;
    }
    @Override
    public String toString() {
        String base = super.toString();
        base = base.substring(0, base.length() - 1);
        return base + ", espada=" + longitudDeEspada + "}";
    }
}