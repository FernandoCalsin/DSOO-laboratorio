public class Caballero extends Soldado {
    private boolean estaMontado;
    private String armaActual;

    public Caballero(String nombre, int fila, int columna, int ejercito, int vida) {
        super(nombre, fila, columna, ejercito);
        setVidaActual(vida);
    }
    public Caballero(String nombre, int fila, int columna, int ejercito) {
        super(nombre, fila, columna, ejercito);
        this.estaMontado = true;
        this.armaActual = "lanza";
    }
    public Caballero(int ejercito, int dimensionTablero) {
        super(ejercito, dimensionTablero);
        this.estaMontado = true;
        this.armaActual = "lanza";
    }
    public Caballero() {
        super();
        this.estaMontado = true;
        this.armaActual = "lanza";
    }
    public void desmontar() {
        if (this.estaMontado) {
            System.out.println("¡" + getNombre() + " ha desmontado!");
            defender(); 
            this.armaActual = "espada";
            this.estaMontado = false;
            System.out.println(getNombre() + " ahora usa la " + this.armaActual + ".");
            
        } else {
            System.out.println(getNombre() + " ya estaba desmontado.");
        }
    }
    public void montar() {
        if (!this.estaMontado) {
            System.out.println("¡" + getNombre() + " ha montado a caballo!");            
            this.estaMontado = true;
            this.armaActual = "lanza";
            System.out.println(getNombre() + " ahora usa la " + this.armaActual + ".");
            envestir(); 

        } else {
            System.out.println(getNombre() + " ya estaba montado.");
        }
    }
    public void envestir() {
        if (this.estaMontado) {        
            System.out.println("¡" + getNombre() + " (montado) enviste con furia! (x3)");
            atacar();
            atacar();
            atacar();
        } else {
            System.out.println("¡" + getNombre() + " (desmontado) carga! (x2)");
            atacar();
            atacar();
        }
    }

    public boolean isEstaMontado() {
        return estaMontado;
    }
    public boolean estaMontado() {
        return this.estaMontado;
    }
    public String getArmaActual() {
        return armaActual;
    }
    @Override
    public String toString() {
        String base = super.toString();
        base = base.substring(0, base.length() - 1); // eliminar la llave final '}'
        return base + ", montado=" + estaMontado + ", arma='" + armaActual + "'}";
    }
}
