package datos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Modulo;

public class ModuloDAO {
    
    public List<Modulo> listarTodos() {
        List<Modulo> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, etiqueta FROM modulos ORDER BY etiqueta";
        try (Connection con = ConexionDB.obtenerConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Modulo m = new Modulo();
                m.setId(rs.getInt("id"));
                m.setNombre(rs.getString("nombre"));
                m.setEtiqueta(rs.getString("etiqueta"));
                lista.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // === NUEVO MÉTODO: AGREGAR MODULO ===
    public boolean agregar(String nombre, String etiqueta) {
        String sql = "INSERT INTO modulos (nombre, etiqueta) VALUES (?, ?)";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, etiqueta);
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}