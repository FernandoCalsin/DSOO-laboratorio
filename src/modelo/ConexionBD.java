package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/simulador";
    private static final String USER = "root";
    private static final String PASS = ""; // cambia si tu MySQL tiene contraseña

    public static Connection getConexion() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASS);
    }
    public static void main(String[] args) {
        try (Connection conn = getConexion()) {
            System.out.println("Conexión exitosa a MySQL!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
