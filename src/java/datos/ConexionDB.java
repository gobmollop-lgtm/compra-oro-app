package datos;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionDB {

    public static Connection obtenerConexion(int prueba) {
        try {
            // Detectar si estamos en Railway (las variables deben estar definidas)
            String host = System.getenv("MYSQLHOST");
            String port = System.getenv("MYSQLPORT");
            String db = System.getenv("MYSQLDATABASE");
            String user = System.getenv("MYSQLUSER");
            String pass = System.getenv("MYSQLPASSWORD");

            // Si alguna variable crítica falta, usar modo local
            boolean esRailway = (host != null && !host.isEmpty()
                    && db != null && !db.isEmpty()
                    && user != null && !user.isEmpty());

            if (!esRailway) {
                // Modo local
                host = "localhost";
                port = "3306";
                db = "compra_oro";
                user = "root";
                pass = "123";
            }

            String baseUrl = "jdbc:mysql://" + host + ":" + port + "/" + db;
            String url;
            if (esRailway) {
                // Modo Railway: con SSL estricto
                url = baseUrl
                        + "?sslMode=VERIFY_IDENTITY"
                        + "&enabledTLSProtocols=TLSv1.2,TLSv1.3"
                        + "&serverTimezone=UTC";
            } else {
                // Modo local: sin SSL
                url = baseUrl
                        + "?allowPublicKeyRetrieval=true"
                        + "&useSSL=false"
                        + "&serverTimezone=UTC"
                        + "&useUnicode=true"
                        + "&characterEncoding=utf8";
            }

            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Connection obtenerConexion() {
        try {
            String host = System.getenv("MYSQLHOST");
            String port = System.getenv("MYSQLPORT");
            String db = System.getenv("MYSQLDATABASE");
            String user = System.getenv("MYSQLUSER");
            String pass = System.getenv("MYSQLPASSWORD");

            boolean esRailway = host != null && !host.isEmpty();

            if (!esRailway) {
                host = "localhost";
                port = "3306";
                db = "compra_oro";
                user = "root";
                pass = "123";
            }

            String baseUrl = "jdbc:mysql://" + host + ":" + port + "/" + db;

            String url;
            if (esRailway) {
                url = baseUrl
                        + "?sslMode=REQUIRED"
                        + "&serverTimezone=UTC";
            } else {
                url = baseUrl
                        + "?allowPublicKeyRetrieval=true"
                        + "&useSSL=false"
                        + "&serverTimezone=UTC"
                        + "&useUnicode=true"
                        + "&characterEncoding=utf8";
            }

            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, pass);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
