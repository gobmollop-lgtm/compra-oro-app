package controlador;

import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import datos.ConexionDB;

@WebServlet("/verImagenCompra")
public class VerImagenCompraServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de compra no proporcionado");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de compra inválido");
            return;
        }

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConexionDB.obtenerConexion();
            String sql = "SELECT imagen FROM compras_oro WHERE id = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                byte[] imagen = rs.getBytes("imagen");
                if (imagen != null && imagen.length > 0) {
                    // Detectar tipo sería ideal, pero asumimos JPEG/PNG → usamos genérico
                    response.setContentType("image/jpeg");
                    response.setContentLength(imagen.length);
                    response.getOutputStream().write(imagen);
                    return;
                }
            }

            // Si no hay imagen, devolver error o placeholder
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Imagen no disponible");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error en base de datos");
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}