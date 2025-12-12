package modelo;
import java.io.Serializable;
import java.util.Random;
public class Soldado implements Serializable{
    private String nombre;
    private int nivelAtaque;
    private int nivelDefensa;
    private int nivelVida;
    private int vidaActual;
    private int velocidad = 0;
    private String actitud = "Defensiva";
    private boolean vive = true;
    private int fila;
    private int columna;
    private int ejercito;
    private int puntos;
    

    
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

    public Soldado(int ejercito, int dimensionTablero) {
        this("SoldadoDefault", 0, 0, ejercito);
        Random rand = new Random();      
        this.fila = rand.nextInt(dimensionTablero);
        this.columna = rand.nextInt(dimensionTablero);
    }

    public Soldado() {
        this("SoldadoBase", 0, 0, 1);
    }

    public String getNombre() { return nombre; }
    public int getVidaActual() { return vidaActual; }
    public void setVidaActual(int vida) { this.vidaActual = vida; }

    public int getFila() { return fila; }
    public int getColumna() { return columna; }
    public int getEjercito() { return ejercito; }
    public boolean estaVivo() { return vive; }

    public void setNivelDefensa(int nivelDefensa){this.nivelDefensa=nivelDefensa;}
    public void setNivelAtaque(int nivelAtaque){this.nivelAtaque=nivelAtaque;}
    public void setNivelVida(int nivelVida){this.nivelVida=nivelVida;}

    public int getNivelDefensa() {return this.nivelDefensa;}
    public int getNivelAtaque(){return this.nivelAtaque;}
    public int getNivelVida(){return this.nivelVida;}

    public void setFila(int fila) {this.fila = fila;}
    public void setColumna(int columna) {this.columna = columna;}

    public void setEjercito(int ejercito) {this.ejercito = ejercito;}
    public void setNombre(String nombre) {this.nombre = nombre;}

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

    public void mover(int nuevaFila, int nuevaColumna){
        this.fila = nuevaFila;
        this.columna = nuevaColumna;
        avanzar();
    }
    public int getPuntos() {return puntos;}

    public void setPuntos(int puntos) {this.puntos = puntos;}

    public void agregarPuntos(int cantidad) {
        this.puntos += cantidad;
    }


    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "nombre='" + nombre + '\'' +
                ", ejercito=" + ejercito +
                ", vida=" + vidaActual + "/" + nivelVida +
                ", pos=(" + fila + "," + columna + ')' +
                ", actitud=" + actitud +
                ", vivo=" + vive +
                '}';
    }
}
