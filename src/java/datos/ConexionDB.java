package datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    public static Connection obtenerConexion() throws SQLException {
        try {
            // Variables de Railway (si están definidas)
            String host = System.getenv("MYSQLHOST");
            String port = System.getenv("MYSQLPORT");
            String db = System.getenv("MYSQLDATABASE");
            String user = System.getenv("MYSQLUSER");
            String pass = System.getenv("MYSQLPASSWORD");

            // Si no hay variables, usar valores fijos de Railway
            if (host == null || host.isEmpty()) {
                host = "mainline.proxy.rlwy.net";
                port = "15337";
                db = "railway"; // ✅ ¡Este es el nombre real en Railway!
                user = "root";
                pass = "POyDyGXWHQdRuKUAJZcUczjDSPshDfWA"; // ✅ Copiada exactamente de "Connect"
            }

            String url = "jdbc:mysql://" + host + ":" + port + "/" + db +
                         "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, pass);

        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver de MySQL no encontrado", e);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}