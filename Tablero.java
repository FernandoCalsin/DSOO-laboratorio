import java.util.ArrayList;

public class Tablero {
    private ArrayList<ArrayList<Soldado>> matrizTablero;
    private int dimension;

    public Tablero(int dimension) {
        this.dimension = dimension;
        this.matrizTablero = new ArrayList<>();    
        for (int i = 0; i < dimension; i++) {
            ArrayList<Soldado> fila = new ArrayList<>();
            for (int j = 0; j < dimension; j++) {
                fila.add(null);
            }
            matrizTablero.add(fila);
        }
    }

    public Soldado getSoldado(int fila, int columna) {
        return matrizTablero.get(fila).get(columna);
    }
  
    public void colocarSoldado(Soldado s) {
        matrizTablero.get(s.getFila()).set(s.getColumna(), s);
    }    
 
    public void removerSoldado(int fila, int columna) {
        matrizTablero.get(fila).set(columna, null);
    }
  
    public void moverSoldado(int fOrigen, int cOrigen, int fDestino, int cDestino) {
        Soldado soldadoAMover = getSoldado(fOrigen, cOrigen);
        if (soldadoAMover != null) {
            removerSoldado(fOrigen, cOrigen);
            soldadoAMover.mover(fDestino, cDestino);
            colocarSoldado(soldadoAMover);
        }
    }    
   
    public boolean estaOcupado(int fila, int columna) {
        return getSoldado(fila, columna) != null;
    }    

    public void imprimirTablero() {
        System.out.println("\n===== TABLERO DE BATALLA =====");
        System.out.print("   ");
        for (int j = 0; j < dimension; j++) System.out.print(j + " ");
        System.out.println();
        System.out.println("  ----------------------");
        
        for (int i = 0; i < dimension; i++) {
            System.out.print(i + " |");
            for (int j = 0; j < dimension; j++) {
                Soldado s = matrizTablero.get(i).get(j);
                if (s != null) {
                    // Ejército 1 se muestra como '$', Ejército 2 como '#'
                    System.out.print((s.getEjercito() == 1 ? "$ " : "# "));
                } else {
                    System.out.print("_ ");
                }
            }
            System.out.println("|");
        }
        System.out.println("  ----------------------");
    }
}