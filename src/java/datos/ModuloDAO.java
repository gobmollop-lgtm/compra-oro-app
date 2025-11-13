package datos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Modulo;

public class ModuloDAO {
    public List<Modulo> listarTodos() {
        List<Modulo> lista = new ArrayList<>();
        String sql = "SELECT nombre, etiqueta FROM modulos ORDER BY etiqueta";
        try (Connection con = ConexionDB.obtenerConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Modulo m = new Modulo();
                m.setNombre(rs.getString("nombre"));
                m.setEtiqueta(rs.getString("etiqueta"));
                lista.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}