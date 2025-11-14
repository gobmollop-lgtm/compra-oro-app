package datos;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class PermisoDAO {
    // Obtiene permisos de un usuario como Map<modulo_nombre, puede_ver>
    public Map<String, Boolean> obtenerPermisosPorUsuario(int usuarioId) {
        Map<String, Boolean> permisos = new HashMap<>();
        String sql = "SELECT modulo_nombre, puede_ver FROM permisos WHERE usuario_id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                permisos.put(rs.getString("modulo_nombre"), rs.getBoolean("puede_ver"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return permisos;
    }

    public void guardarPermiso(int usuarioId, String modulo, boolean puedeVer) {
        String sql = "INSERT INTO permisos (usuario_id, modulo_nombre, puede_ver) " +
                     "VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE puede_ver = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setString(2, modulo);
            ps.setBoolean(3, puedeVer);
            ps.setBoolean(4, puedeVer);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}