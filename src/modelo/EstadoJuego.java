package modelo;

import java.io.Serializable;

public class EstadoJuego implements Serializable {
    public String consola;

    public EstadoJuego(String consola) {
        this.consola = consola;
    }
}
