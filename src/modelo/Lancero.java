package modelo;
import java.io.Serializable;
import java.util.Random;
public class Lancero extends Soldado implements Serializable{
    private int longitudDeLanza;
    public Lancero(String nombre, int fila, int columna, int ejercito, int vida) {
        super(nombre, fila, columna, ejercito);
        setVidaActual(vida);
    }
    public Lancero(String nombre, int fila, int columna, int ejercito) {
        super(nombre, fila, columna, ejercito);
        Random rand = new Random();
        this.longitudDeLanza = rand.nextInt(4) + 2; 
    }
    public Lancero(int ejercito, int dimensionTablero) {
        super(ejercito, dimensionTablero);
        Random rand = new Random();
        this.longitudDeLanza = rand.nextInt(4) + 2;
    }
    public Lancero() {
        super();
        Random rand = new Random();
        this.longitudDeLanza = rand.nextInt(4) + 2;
    }
    public void schiltrom() {        
        defender(); 
        int defActual = getNivelDefensa();
        setNivelDefensa(defActual + 1);
        System.out.println("¡" + getNombre() + " forma un schiltrom (falange)!");
        System.out.println("  (Defensa aumentada a " + getNivelDefensa() + ")");
    }
    public int getLongitudDeLanza() {
        return longitudDeLanza;
    }
    @Override
    public String toString() {
        String infoSoldado = super.toString(); 
        infoSoldado = infoSoldado.substring(0, infoSoldado.length() - 1);
        return infoSoldado + ", lanza=" + longitudDeLanza + '}';
    }
}
