package datos;

import java.math.BigDecimal;
import java.sql.*;
import modelo.FondoComprador;

public class FondoDAO {
    public FondoComprador obtenerPorUsuario(int usuarioId) {
        String sql = "SELECT * FROM fondos_comprador WHERE usuario_id = ? AND activo = TRUE";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                FondoComprador fondo = new FondoComprador();
                fondo.setId(rs.getInt("id"));
                fondo.setUsuarioId(rs.getInt("usuario_id"));
                fondo.setMontoAsignado(rs.getBigDecimal("monto_asignado"));
                fondo.setMontoUsado(rs.getBigDecimal("monto_usado"));
                fondo.setFechaAsignacion(rs.getTimestamp("fecha_asignacion"));
                fondo.setActivo(rs.getBoolean("activo"));
                return fondo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void crear(FondoComprador fondo) {
        String sql = "INSERT INTO fondos_comprador (usuario_id, monto_asignado, monto_usado, activo) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, fondo.getUsuarioId());
            ps.setBigDecimal(2, fondo.getMontoAsignado());
            ps.setBigDecimal(3, fondo.getMontoUsado());
            ps.setBoolean(4, fondo.isActivo());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizarMontoAsignado(int usuarioId, BigDecimal nuevoMonto) {
        String sql = "UPDATE fondos_comprador SET monto_asignado = ? WHERE usuario_id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBigDecimal(1, nuevoMonto);
            ps.setInt(2, usuarioId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deducirMonto(int usuarioId, BigDecimal montoCompra) {
        String sql = "UPDATE fondos_comprador SET monto_usado = monto_usado + ? WHERE usuario_id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBigDecimal(1, montoCompra);
            ps.setInt(2, usuarioId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}