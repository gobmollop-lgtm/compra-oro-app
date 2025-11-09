package datos;

import java.sql.*;
import modelo.Configuracion;

public class ConfiguracionDAO {
    public Configuracion obtener() {
        String sql = "SELECT * FROM configuracion WHERE id = 1";
        try (Connection con = ConexionDB.obtenerConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                Configuracion c = new Configuracion();
                c.setId(rs.getInt("id"));
                c.setMonedaSimbolo(rs.getString("moneda_simbolo"));
                c.setNombreNegocio(rs.getString("nombre_negocio"));
                c.setTelefonoNegocio(rs.getString("telefono_negocio"));
                c.setDireccionNegocio(rs.getString("direccion_negocio"));
                c.setLogoUrl(rs.getString("logo_url"));
                c.setCompradorVeHistorialCompleto(rs.getBoolean("comprador_ve_historial_completo"));
                c.setCompradorPuedeRegistrarCliente(rs.getBoolean("comprador_puede_registrar_cliente"));
                c.setIpServidor(rs.getString("ip_servidor")); // ← NUEVO
                return c;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void actualizar(Configuracion c) {
        String sql = "UPDATE configuracion SET " +
                     "moneda_simbolo = ?, " +
                     "nombre_negocio = ?, " +
                     "telefono_negocio = ?, " +
                     "direccion_negocio = ?, " +
                     "logo_url = ?, " +
                     "comprador_ve_historial_completo = ?, " +
                     "comprador_puede_registrar_cliente = ?, " +
                     "ip_servidor = ? " + // ← NUEVO
                     "WHERE id = 1";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getMonedaSimbolo());
            ps.setString(2, c.getNombreNegocio());
            ps.setString(3, c.getTelefonoNegocio());
            ps.setString(4, c.getDireccionNegocio());
            ps.setString(5, c.getLogoUrl());
            ps.setBoolean(6, c.isCompradorVeHistorialCompleto());
            ps.setBoolean(7, c.isCompradorPuedeRegistrarCliente());
            ps.setString(8, c.getIpServidor()); // ← NUEVO
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}