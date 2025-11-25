package datos;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import modelo.Compra;
import modelo.CompraResumen;

public class CompraDAO {
    
    public void crear(Compra compra) {
        String sql = "INSERT INTO compras_oro (cliente_id, usuario_id, peso_gramos, kilate, punto, precio_gramo, total, ruta_foto, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, compra.getClienteId());
            ps.setInt(2, compra.getUsuarioId());
            ps.setBigDecimal(3, compra.getPesoGramos());
            ps.setBigDecimal(4, compra.getKilate());
            ps.setBigDecimal(5, compra.getPunto());
            ps.setBigDecimal(6, compra.getPrecioGramo());
            ps.setBigDecimal(7, compra.getTotal());
            ps.setString(8, compra.getRutaFoto());
            ps.setString(9, compra.getEstado()); // ✅ "Pendiente" o "Entregado"
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al registrar compra", e);
        }
    }

    public List<Compra> listarUltimas(int limite) {
        List<Compra> lista = new ArrayList<>();
        String sql = "SELECT c.nombre AS nombre_cliente, u.nombre AS nombre_usuario, co.peso_gramos, co.kilate, co.punto, co.precio_gramo, co.total, co.fecha, co.estado " +
                     "FROM compras_oro co " +
                     "JOIN clientes c ON co.cliente_id = c.id " +
                     "JOIN usuarios u ON co.usuario_id = u.id " +
                     "ORDER BY co.fecha DESC " +
                     "LIMIT ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limite);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Compra comp = new Compra();
                comp.setNombreCliente(rs.getString("nombre_cliente"));
                comp.setNombreUsuario(rs.getString("nombre_usuario"));
                comp.setPesoGramos(rs.getBigDecimal("peso_gramos"));
                comp.setKilate(rs.getBigDecimal("kilate"));
                comp.setPunto(rs.getBigDecimal("punto"));
                comp.setPrecioGramo(rs.getBigDecimal("precio_gramo"));
                comp.setTotal(rs.getBigDecimal("total"));
                comp.setFecha(rs.getTimestamp("fecha"));
                comp.setEstado(rs.getString("estado")); // ✅ Incluir estado
                lista.add(comp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public CompraResumen obtenerResumenPorUsuario(int usuarioId) {
        String sql = "SELECT COUNT(*) as total_compras, COALESCE(SUM(total), 0) as total_invertido FROM compras_oro WHERE usuario_id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int total = rs.getInt("total_compras");
                BigDecimal invertido = rs.getBigDecimal("total_invertido");
                return new CompraResumen(total, invertido != null ? invertido : BigDecimal.ZERO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CompraResumen(0, BigDecimal.ZERO);
    }

    public List<Compra> obtenerPorUsuario(int usuarioId) {
    List<Compra> lista = new ArrayList<>();
    String sql = "SELECT co.id, c.nombre AS nombre_cliente, co.peso_gramos, co.kilate, co.punto, co.precio_gramo, co.total, co.fecha, co.ruta_foto, co.estado " +
                 "FROM compras_oro co " +
                 "JOIN clientes c ON co.cliente_id = c.id " +
                 "WHERE co.usuario_id = ? " +
                 "ORDER BY co.fecha DESC";
    try (Connection con = ConexionDB.obtenerConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, usuarioId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Compra comp = new Compra();
            comp.setId(rs.getInt("id")); // ✅ ¡CLAVE!
            comp.setNombreCliente(rs.getString("nombre_cliente"));
            comp.setPesoGramos(rs.getBigDecimal("peso_gramos"));
            comp.setKilate(rs.getBigDecimal("kilate"));
            comp.setPunto(rs.getBigDecimal("punto"));
            comp.setPrecioGramo(rs.getBigDecimal("precio_gramo"));
            comp.setTotal(rs.getBigDecimal("total"));
            comp.setFecha(rs.getTimestamp("fecha"));
            comp.setRutaFoto(rs.getString("ruta_foto"));
            comp.setEstado(rs.getString("estado"));
            lista.add(comp);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return lista;
}

    // === MÉTODOS ACTUALIZADOS ===

    public Compra obtenerPorId(int id) {
        String sql = "SELECT co.id, co.cliente_id, co.usuario_id, c.nombre AS nombre_cliente, co.peso_gramos, co.kilate, co.punto, " +
                     "co.precio_gramo, co.total, co.ruta_foto, co.fecha, co.estado " + // ✅ usar 'estado', no 'justificado'
                     "FROM compras_oro co " +
                     "JOIN clientes c ON co.cliente_id = c.id " +
                     "WHERE co.id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Compra c = new Compra();
                c.setId(rs.getInt("id"));
                c.setClienteId(rs.getInt("cliente_id"));
                c.setUsuarioId(rs.getInt("usuario_id"));
                c.setNombreCliente(rs.getString("nombre_cliente"));
                c.setPesoGramos(rs.getBigDecimal("peso_gramos"));
                c.setKilate(rs.getBigDecimal("kilate"));
                c.setPunto(rs.getBigDecimal("punto"));
                c.setPrecioGramo(rs.getBigDecimal("precio_gramo"));
                c.setTotal(rs.getBigDecimal("total"));
                c.setRutaFoto(rs.getString("ruta_foto"));
                c.setFecha(rs.getTimestamp("fecha"));
                c.setEstado(rs.getString("estado")); // ✅ Leer estado
                return c;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM compras_oro WHERE id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Eliminado: marcarComoJustificado (obsoleto)
    // ✅ Eliminado: actualizarJustificado (obsoleto)

    // === NUEVO MÉTODO ===
    public void actualizarEstado(int id, String estado) {
    String sql = "UPDATE compras_oro SET estado = ? WHERE id = ?";
    try (Connection con = ConexionDB.obtenerConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, estado);
        ps.setInt(2, id);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Error al actualizar estado", e);
    }
}
  
}