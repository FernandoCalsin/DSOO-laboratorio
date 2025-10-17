import java.util.Random;

public class Soldado {
    private String nombre;
    private int nivelAtaque;
    private int nivelDefensa;
    private int nivelVida;
    private int vidaActual;
    private int velocidad = 0;
    private String actitud = "Defensiva";
    private boolean vive = true;
    
    // Atributos de posición y equipo
    private int fila;
    private int columna;
    private int ejercito;

    //Constructores Sobrecargados

    public Soldado(String nombre, int fila, int columna, int ejercito) {
        Random rand = new Random();
        this.nombre = nombre;
        this.fila = fila;
        this.columna = columna;
        this.ejercito = ejercito;

        this.nivelAtaque = rand.nextInt(5) + 1;
        this.nivelDefensa = rand.nextInt(5) + 1;
        this.nivelVida = rand.nextInt(5) + 1;
        this.vidaActual = this.nivelVida;
    }

    // 2. Constructor para crear un soldado de un ejército en una posición aleatoria
    public Soldado(int ejercito, int dimensionTablero) {
        this("SoldadoDefault", 0, 0, ejercito);
        Random rand = new Random();      
        this.fila = rand.nextInt(dimensionTablero);
        this.columna = rand.nextInt(dimensionTablero);
    }
    
    //Constructor por defecto para un soldado del ejército 1 en (0,0)
    public Soldado() {
        this("SoldadoBase", 0, 0, 1);
    }

    //  Getters y Setters 
    public String getNombre() { return nombre; }
    public int getVidaActual() { return vidaActual; }
    public void setVidaActual(int vida) { this.vidaActual = vida; }
    public int getFila() { return fila; }
    public int getColumna() { return columna; }
    public int getEjercito() { return ejercito; }
    public boolean estaVivo() { return vive; }

    //comportamiento de soldado
    public void atacar() {
        this.actitud = "Ofensiva";
        avanzar();
    }

    public void defender() {
        this.actitud = "Defensiva";
        this.velocidad = 0;
        System.out.println(this.nombre + " está en modo defensivo.");
    }

    public void avanzar() {
        this.velocidad++;
    }

    public void retroceder() {
        if (this.velocidad > 0) {
            defender();
        } else {
            this.velocidad--;
        }
    }

    public void serAtacado(int dano) {
        this.vidaActual -= dano;
        if (this.vidaActual <= 0) {
            morir();
        }
    }

    public void huir() {
        this.actitud = "Fuga";
        this.velocidad += 2;
    }

    public void morir() {
        this.vive = false;
        this.vidaActual = 0;
        System.out.println("¡" + this.nombre + " ha sido derrotado!");
    }
    
    // Método para actualizar las coordenadas internas del soldado
    public void mover(int nuevaFila, int nuevaColumna){
        this.fila = nuevaFila;
        this.columna = nuevaColumna;
        avanzar();
    }

    @Override
    public String toString() {
        return "Soldado{" +
                "nombre='" + nombre + '\'' +
                ", ejercito=" + ejercito +
                ", vida=" + vidaActual + "/" + nivelVida +
                ", pos=(" + fila + "," + columna + ')' +
                '}';
    }
}