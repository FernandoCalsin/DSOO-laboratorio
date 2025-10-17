import java.util.ArrayList;

public class Tablero {
    private ArrayList<ArrayList<Soldado>> matrizTablero;
    private int dimension;

    public Tablero(int dimension) {
        this.dimension = dimension;
        this.matrizTablero = new ArrayList<>();
        // Inicializamos el tablero con valores nulos
        for (int i = 0; i < dimension; i++) {
            ArrayList<Soldado> fila = new ArrayList<>();
            for (int j = 0; j < dimension; j++) {
                fila.add(null);
            }
            matrizTablero.add(fila);
        }
    }

    // == MÉTODOS QUE FALTABAN EN TU ARCHIVO ==

    /**
     * Devuelve el soldado que se encuentra en una coordenada específica.
     * @param fila La fila de la casilla.
     * @param columna La columna de la casilla.
     * @return El objeto Soldado, o null si la casilla está vacía.
     */
    public Soldado getSoldado(int fila, int columna) {
        return matrizTablero.get(fila).get(columna);
    }
    
    /**
     * Coloca un soldado en el tablero en su posición interna.
     * @param s El soldado a colocar.
     */
    public void colocarSoldado(Soldado s) {
        matrizTablero.get(s.getFila()).set(s.getColumna(), s);
    }
    
    /**
     * Elimina a un soldado de una posición específica, dejándola vacía.
     * @param fila La fila de la casilla a vaciar.
     * @param columna La columna de la casilla a vaciar.
     */
    public void removerSoldado(int fila, int columna) {
        matrizTablero.get(fila).set(columna, null);
    }

    /**
     * Mueve un soldado de una posición de origen a una de destino.
     * @param fOrigen Fila original.
     * @param cOrigen Columna original.
     * @param fDestino Fila de destino.
     * @param cDestino Columna de destino.
     */
    public void moverSoldado(int fOrigen, int cOrigen, int fDestino, int cDestino) {
        Soldado soldadoAMover = getSoldado(fOrigen, cOrigen);
        if (soldadoAMover != null) {
            removerSoldado(fOrigen, cOrigen);
            soldadoAMover.mover(fDestino, cDestino); // Actualiza coordenadas internas del soldado
            colocarSoldado(soldadoAMover);
        }
    }
    
    // == MÉTODOS QUE PROBABLEMENTE YA TENÍAS ==

    /**
     * Verifica si una posición ya está ocupada por cualquier soldado.
     * @param fila La fila a verificar.
     * @param columna La columna a verificar.
     * @return true si está ocupado, false en caso contrario.
     */
    public boolean estaOcupado(int fila, int columna) {
        return getSoldado(fila, columna) != null;
    }
    
    /**
     * Imprime el estado actual del tablero en la consola.
     */
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
                    System.out.print("_ "); // Espacio vacío
                }
            }
            System.out.println("|");
        }
        System.out.println("  ----------------------");
    }
}