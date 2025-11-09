package datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    
    public static Connection obtenerConexion() {
        try {
            // Lee las variables de Railway (o usa valores locales si no existen)
            String host = System.getenv("MYSQLHOST");
            String port = System.getenv("MYSQLPORT");
            String db = System.getenv("MYSQLDATABASE");
            String user = System.getenv("MYSQLUSER");
            String pass = System.getenv("MYSQLPASSWORD");

            // Si no hay variables (modo local), usa los valores locales
            if (host == null) {
                host = "localhost";
                port = "3306";
                db = "compra_oro";
                user = "root";
                pass = "123"; // ← tu contraseña local
            }

            String url = "jdbc:mysql://" + host + ":" + port + "/" + db +
                         "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=utf8";

            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, pass);

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("❌ Error de conexión: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}