import vista.VistaJuego;
import controlador.ControladorJuego;

public class Main {
    public static void main(String[] args) {
        VistaJuego vista = new VistaJuego();
        new ControladorJuego(vista);
        vista.setVisible(true);
    }
}
