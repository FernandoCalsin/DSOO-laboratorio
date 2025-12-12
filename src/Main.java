
import controlador.ControladorJuego;
import vista.VistaJuego;

public class Main {
    public static void main(String[] args) {
        VistaJuego vista = new VistaJuego();
        ControladorJuego controlador = new ControladorJuego(vista);
        vista.setControlador(controlador);
        vista.setVisible(true);
    }
}
