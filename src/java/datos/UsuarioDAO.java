package datos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Usuario;

public class UsuarioDAO {
    public void crear(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, usuario, contrasena, rol) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getUsuario());
            ps.setString(3, usuario.getContrasena());
            ps.setString(4, usuario.getRol());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, usuario, rol FROM usuarios ORDER BY nombre";
        try (Connection con = ConexionDB.obtenerConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setUsuario(rs.getString("usuario"));
                u.setRol(rs.getString("rol"));
                lista.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Usuario buscarPorId(int id) {
        String sql = "SELECT id, nombre, usuario, rol FROM usuarios WHERE id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id"));
                    u.setNombre(rs.getString("nombre"));
                    u.setUsuario(rs.getString("usuario"));
                    u.setRol(rs.getString("rol"));
                    return u;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void actualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, usuario = ?, rol = ? WHERE id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getUsuario());
            ps.setString(3, usuario.getRol());
            ps.setInt(4, usuario.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Añade este método a tu UsuarioDAO.java (si no está)
public Usuario buscarPorCredenciales(String usuario, String contrasena) {
    String sql = "SELECT id, nombre, usuario, rol FROM usuarios WHERE usuario = ? AND contrasena = ?";
    try (Connection con = ConexionDB.obtenerConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, usuario);
        ps.setString(2, contrasena);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setUsuario(rs.getString("usuario"));
                u.setRol(rs.getString("rol"));
                return u;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
public void actualizarConContrasena(Usuario usuario) {
    String sql = "UPDATE usuarios SET nombre = ?, usuario = ?, rol = ?, contrasena = ? WHERE id = ?";
    try (Connection con = ConexionDB.obtenerConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, usuario.getNombre());
        ps.setString(2, usuario.getUsuario());
        ps.setString(3, usuario.getRol());
        ps.setString(4, usuario.getContrasena());
        ps.setInt(5, usuario.getId());
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}