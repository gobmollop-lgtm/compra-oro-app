package datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/compra_oro?"
            + "useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=utf8";
    private static final String USUARIO = "root";
    private static final String CONTRASEÑA = "123"; // ← Cambia si tu contraseña es otra

    public static Connection obtenerConexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("❌ Error de conexión: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}