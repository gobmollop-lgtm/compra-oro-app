package datos;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import modelo.Compra;

public class CompraDAO {
    public void crear(Compra compra) {
        String sql = "INSERT INTO compras_oro (cliente_id, peso_gramos, kilate, punto, observaciones) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, compra.getClienteId());
            ps.setBigDecimal(2, compra.getPesoGramos());
            ps.setBigDecimal(3, compra.getKilate());
            ps.setBigDecimal(4, compra.getPunto());
            ps.setString(5, compra.getObservaciones());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Compra> listarUltimas(int limite) {
        List<Compra> lista = new ArrayList<>();
        String sql = "SELECT c.nombre, co.peso_gramos, co.kilate, co.punto, co.precio_gramo, co.total, co.fecha " +
                     "FROM compras_oro co " +
                     "JOIN clientes c ON co.cliente_id = c.id " +
                     "ORDER BY co.fecha DESC " +
                     "LIMIT ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Compra comp = new Compra();
                    comp.setNombreCliente(rs.getString("nombre"));
                    comp.setPesoGramos(rs.getBigDecimal("peso_gramos"));
                    comp.setKilate(rs.getBigDecimal("kilate"));
                    comp.setPunto(rs.getBigDecimal("punto"));
                    comp.setPrecioGramo(rs.getBigDecimal("precio_gramo"));
                    comp.setTotal(rs.getBigDecimal("total"));
                    comp.setFecha(rs.getTimestamp("fecha"));
                    lista.add(comp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}