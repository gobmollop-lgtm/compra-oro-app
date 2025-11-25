package controlador;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import logica.CompraServicio;
import logica.FondoServicio;
import modelo.Compra;

@WebServlet("/EliminarCompraServlet")
public class EliminarCompraServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int compraId = Integer.parseInt(request.getParameter("compraId"));
            int usuarioIdDetalle = Integer.parseInt(request.getParameter("usuarioId"));
            String fechaInicio = request.getParameter("fechaInicio");
            String fechaFin = request.getParameter("fechaFin");
            String estado = request.getParameter("estado");

            CompraServicio compraServicio = new CompraServicio();
            Compra compra = compraServicio.obtenerPorId(compraId);
            if (compra == null) {
                response.sendRedirect("compras-detalle.jsp?usuarioId=" + usuarioIdDetalle + "&error=Compra no encontrada");
                return;
            }

            // Devolver fondos
            FondoServicio fondoServicio = new FondoServicio();
            fondoServicio.devolverMonto(compra.getUsuarioId(), compra.getTotal());

            // Eliminar
            compraServicio.eliminar(compraId);

            // Redirigir
            StringBuilder url = new StringBuilder("compras-detalle.jsp?usuarioId=");
            url.append(usuarioIdDetalle);
            if (fechaInicio != null && !fechaInicio.isEmpty()) url.append("&fechaInicio=").append(fechaInicio);
            if (fechaFin != null && !fechaFin.isEmpty()) url.append("&fechaFin=").append(fechaFin);
            if (estado != null && !estado.isEmpty()) url.append("&estado=").append(estado);
            url.append("&exito=2");

            response.sendRedirect(url.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("compras-detalle.jsp?usuarioId=" + request.getParameter("usuarioId") + "&error=Error al eliminar");
        }
    }
}