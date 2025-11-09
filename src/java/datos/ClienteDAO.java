package datos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Cliente;

public class ClienteDAO {
    public void crear(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre, telefono, email) VALUES (?, ?, ?)";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getTelefono());
            ps.setString(3, cliente.getEmail());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT id, nombre FROM clientes ORDER BY nombre";
        try (Connection con = ConexionDB.obtenerConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                lista.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
