package datos;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import modelo.Compra;
import modelo.CompraResumen;

public class CompraDAO {
    public void crear(Compra compra) {
        String sql = "INSERT INTO compras_oro (cliente_id, usuario_id, peso_gramos, kilate, punto, precio_gramo, total, ruta_foto) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, compra.getClienteId());
            ps.setInt(2, compra.getUsuarioId());
            ps.setBigDecimal(3, compra.getPesoGramos());
            ps.setBigDecimal(4, compra.getKilate());
            ps.setBigDecimal(5, compra.getPunto());
            ps.setBigDecimal(6, compra.getPrecioGramo());
            ps.setBigDecimal(7, compra.getTotal());
            ps.setString(8, compra.getRutaFoto()); // <<<< GUARDAR LA RUTA
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al registrar compra", e);
        }
    }

    public List<Compra> listarUltimas(int limite) {
    List<Compra> lista = new ArrayList<>();
    String sql = "SELECT c.nombre AS nombre_cliente, u.nombre AS nombre_usuario, co.peso_gramos, co.kilate, co.punto, co.precio_gramo, co.total, co.fecha " +
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
            lista.add(comp);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return lista;
}
    // En datos/CompraDAO.java
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
    // En datos/CompraDAO.java
public List<Compra> obtenerPorUsuario(int usuarioId) {
        List<Compra> lista = new ArrayList<>();
        String sql = "SELECT c.nombre AS nombre_cliente, co.peso_gramos, co.kilate, co.punto, co.precio_gramo, co.total, co.fecha, co.ruta_foto " + // <<<< INCLUIR ruta_foto
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
                comp.setNombreCliente(rs.getString("nombre_cliente"));
                comp.setPesoGramos(rs.getBigDecimal("peso_gramos"));
                comp.setKilate(rs.getBigDecimal("kilate"));
                comp.setPunto(rs.getBigDecimal("punto"));
                comp.setPrecioGramo(rs.getBigDecimal("precio_gramo"));
                comp.setTotal(rs.getBigDecimal("total"));
                comp.setFecha(rs.getTimestamp("fecha"));
                comp.setRutaFoto(rs.getString("ruta_foto")); // <<<< LEER LA RUTA
                lista.add(comp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
 
}