package datos;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionDB {
    
    public static Connection obtenerConexion() {
        try {
            // Usa variables de entorno de Railway
            String host = System.getenv("MYSQLHOST");
            String port = System.getenv("MYSQLPORT");
            String db   = System.getenv("MYSQLDATABASE");
            if (db == null || db.isEmpty()) {
                db = System.getenv("MYSQL_DATABASE"); // Soporta ambas variantes
            }
            String user = System.getenv("MYSQLUSER");
            String pass = System.getenv("MYSQLPASSWORD");

            // Si no hay variables (modo local), usa valores locales
            if (host == null || host.isEmpty()) {
                host = "localhost";
                port = "3306";
                db   = "compra_oro";
                user = "root";
                pass = "123"; // ← tu contraseña local
            }

            // Construye la URL para MySQL
            String baseUrl = "jdbc:mysql://" + host + ":" + port + "/" + db;

            String url;
            if (host != null && !host.isEmpty() && !host.equalsIgnoreCase("localhost")) {
                // Modo nube (Railway) → SSL estricto
                url = baseUrl +
                      "?sslMode=VERIFY_IDENTITY" +
                      "&enabledTLSProtocols=TLSv1.2,TLSv1.3" +
                      "&serverTimezone=UTC";
            } else {
                // Modo local → sin SSL (o con SSL si lo tienes)
                url = baseUrl +
                      "?allowPublicKeyRetrieval=true" +
                      "&useSSL=false" +
                      "&serverTimezone=UTC" +
                      "&useUnicode=true" +
                      "&characterEncoding=utf8";
            }

            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}