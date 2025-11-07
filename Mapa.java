import java.util.Random;
public class Mapa{
    private String tipoTerritorio;
    private Soldado[][] tablero;
    private Ejercito ejercito1;
    private Ejercito ejercito2;
    private int dimension;

    public Mapa(int dimension){
        this.dimension=dimension;
        this.tablero=new Soldado[dimension][dimension];
        this.tipoTerritorio=generarTerritorio();
        inicializarEjercitos();
    }
    private String generarTerritorio(){
        String[] tipos={"bosque","campo abierto","montaña","desierto","playa"};
        return tipos[new Random().nextInt(tipos.length)];
    }
    private void inicializarEjercitos(){
        ejercito1 = new Ejercito("Inglaterra", 10);
        ejercito2 = new Ejercito("Francia", 10);

        colocarEjercitoEnMapa(ejercito1);
        colocarEjercitoEnMapa(ejercito2);
        aplicarBonusTerritorial();
    }

    private void colocarEjercitoEnMapa(Ejercito ejercito) {
        Random rand = new Random();
        for (Soldado s : ejercito.getSoldados()) {
            int fila, columna;
            do {
                fila = rand.nextInt(dimension);
                columna = rand.nextInt(dimension);
            } while (tablero[fila][columna] != null);
            tablero[fila][columna] = s;
        }
    }

    private void aplicarBonusTerritorial() {
        if (tipoTerritorio.equals("bosque")) {
            if (ejercito1.getNombre().equals("Inglaterra")) {
                for (Soldado s : ejercito1.getSoldados()) s.setVidaActual(s.getVidaActual() + 1);
            }
        } else if (tipoTerritorio.equals("campo abierto")) {
            if (ejercito2.getNombre().equals("Francia")) {
                for (Soldado s : ejercito2.getSoldados()) s.setVidaActual(s.getVidaActual() + 1);
            }
        }
    }

    public void mostrarMapa() {
        System.out.println("=== Territorio: " + tipoTerritorio + " ===");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (tablero[i][j] == null) System.out.print("[ ] ");
                else System.out.print("[" + tablero[i][j].getEjercito() + "] ");
            }
            System.out.println();
        }
    }

    public Ejercito getEjercito1() { return ejercito1; }
    public Ejercito getEjercito2() { return ejercito2; }
    public String getTipoTerritorio() {
        return tipoTerritorio;
    }
    public void setTipoTerritorio(String tipoTerritorio) {
        this.tipoTerritorio = tipoTerritorio;
    }
    public Soldado[][] getTablero() {
        return tablero;
    }
    public void setTablero(Soldado[][] tablero) {
        this.tablero = tablero;
    }
    public void setEjercito1(Ejercito ejercito1) {
        this.ejercito1 = ejercito1;
    }
    public void setEjercito2(Ejercito ejercito2) {
        this.ejercito2 = ejercito2;
    }
    public int getDimension() {
        return dimension;
    }
    public void setDimension(int dimension) {
        this.dimension = dimension;
    }
}

}