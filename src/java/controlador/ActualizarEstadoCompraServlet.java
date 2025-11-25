package controlador;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import logica.CompraServicio;

@WebServlet("/ActualizarEstadoCompraServlet")
public class ActualizarEstadoCompraServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int compraId = Integer.parseInt(request.getParameter("compraId"));
            if (compraId <= 0) {
                throw new IllegalArgumentException("ID de compra inválido");
            }

            int usuarioId = Integer.parseInt(request.getParameter("usuarioId"));
            String fechaInicio = request.getParameter("fechaInicio");
            String fechaFin = request.getParameter("fechaFin");
            String estadoFiltro = request.getParameter("estado");

            CompraServicio servicio = new CompraServicio();
            servicio.actualizarEstado(compraId, "Recibido");

            StringBuilder url = new StringBuilder("compras-detalle.jsp?usuarioId=");
            url.append(usuarioId);
            if (fechaInicio != null && !fechaInicio.isEmpty()) url.append("&fechaInicio=").append(fechaInicio);
            if (fechaFin != null && !fechaFin.isEmpty()) url.append("&fechaFin=").append(fechaFin);
            if (estadoFiltro != null && !estadoFiltro.isEmpty()) url.append("&estado=").append(estadoFiltro);
            url.append("&exito=1");

            response.sendRedirect(url.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("compras-detalle.jsp?usuarioId=" + request.getParameter("usuarioId") + "&error=Error: " + e.getMessage());
        }
    }
}