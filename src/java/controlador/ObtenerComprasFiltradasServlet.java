package controlador;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import logica.CompraServicio;
import logica.ConfiguracionServicio;
import modelo.Compra;
import modelo.Configuracion;

@WebServlet("/ObtenerComprasFiltradasServlet")
public class ObtenerComprasFiltradasServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int usuarioId = Integer.parseInt(request.getParameter("usuarioId"));
            String fechaInicioStr = request.getParameter("fechaInicio");
            String fechaFinStr = request.getParameter("fechaFin");
            String estadoFiltro = request.getParameter("estado");

            LocalDate fechaInicio = null, fechaFin = null;
            if (fechaInicioStr != null && !fechaInicioStr.isEmpty()) {
                fechaInicio = LocalDate.parse(fechaInicioStr);
            }
            if (fechaFinStr != null && !fechaFinStr.isEmpty()) {
                fechaFin = LocalDate.parse(fechaFinStr);
            }

            CompraServicio compraServicio = new CompraServicio();
            List<Compra> comprasTodas = compraServicio.obtenerPorUsuario(usuarioId);

            List<Compra> compras = new ArrayList<>();
            for (Compra c : comprasTodas) {
                if (c.getFecha() == null) continue;
                LocalDate fechaCompra = c.getFecha().toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate();
                if (fechaInicio != null && fechaCompra.isBefore(fechaInicio)) continue;
                if (fechaFin != null && fechaCompra.isAfter(fechaFin)) continue;

                
                compras.add(c);
            }

            // Calcular totales
            BigDecimal totalPeso = BigDecimal.ZERO;
            BigDecimal totalGeneral = BigDecimal.ZERO;
            for (Compra c : compras) {
                totalPeso = totalPeso.add(c.getPesoGramos());
                totalGeneral = totalGeneral.add(c.getTotal());
            }

            // Obtener símbolo de moneda
            ConfiguracionServicio configServicio = new ConfiguracionServicio();
            Configuracion config = configServicio.obtenerConfiguracion();
            String simboloMoneda = (config != null && config.getMonedaSimbolo() != null) 
                ? config.getMonedaSimbolo() : "C$";

            DecimalFormat df = new DecimalFormat("#,##0.00");
            df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));

            // Generar HTML de la tabla
            StringBuilder html = new StringBuilder();

            if (compras.isEmpty()) {
                html.append("<div class='text-center py-4'>");
                html.append("<i class='fas fa-shopping-cart fa-3x text-muted mb-3'></i>");
                html.append("<p class='text-muted'>No hay compras que coincidan con los filtros.</p>");
                html.append("</div>");
            } else {
                html.append("<div class='table-responsive'>");
                html.append("<table class='table table-striped'>");
                html.append("<thead class='table-light'>");
                html.append("<tr>");
                html.append("<th>Cliente</th>");
                html.append("<th>Peso</th>");
                html.append("<th>Kilate</th>");
                html.append("<th>Punto</th>");
                html.append("<th>Precio/g</th>");
                html.append("<th>Total</th>");
                html.append("<th>Fecha</th>");
                html.append("<th>Estado</th>");
                html.append("<th>Foto</th>");
                html.append("<th>Eliminar</th>");
                html.append("</tr>");
                html.append("</thead>");
                html.append("<tbody>");

                for (Compra c : compras) {
                    html.append("<tr>");
                    html.append("<td>").append(escapeHtml(c.getNombreCliente())).append("</td>");
                    html.append("<td>").append(c.getPesoGramos().setScale(3, RoundingMode.HALF_UP)).append("</td>");
                    html.append("<td>").append(c.getKilate().setScale(2, RoundingMode.HALF_UP)).append("</td>");
                    html.append("<td>").append(c.getPunto().setScale(2, RoundingMode.HALF_UP)).append("</td>");
                    html.append("<td>").append(simboloMoneda).append(df.format(c.getPrecioGramo().setScale(2, RoundingMode.HALF_UP))).append("</td>");
                    html.append("<td class='fw-bold'>").append(simboloMoneda).append(df.format(c.getTotal().setScale(2, RoundingMode.HALF_UP))).append("</td>");
                    html.append("<td>").append(c.getFecha()).append("</td>");
                    html.append("<td class='celda-estado' data-compra-id='").append(c.getId()).append("'>");
                    html.append("<select class='select-estado form-select form-select-sm'>");
                    
                    html.append("</select>");
                    html.append("</td>");
                    html.append("<td>");
                    if (c.getRutaFoto() != null && !c.getRutaFoto().trim().isEmpty()) {
                        html.append("<a href='").append(request.getContextPath()).append("/").append(escapeHtml(c.getRutaFoto())).append("' target='_blank' class='btn btn-sm btn-outline-primary btn-ver-foto'>Ver</a>");
                    } else {
                        html.append("<span class='text-muted'>-</span>");
                    }
                    html.append("</td>");
                    html.append("<td>");
                    html.append("<form action='EliminarCompraServlet' method='post' style='display:inline;' onsubmit='return confirm(\"¿Eliminar compra?\");'>");
                    html.append("<input type='hidden' name='compraId' value='").append(c.getId()).append("'>");
                    html.append("<input type='hidden' name='usuarioId' value='").append(usuarioId).append("'>");
                    html.append("<input type='hidden' name='fechaInicio' value='").append(escapeHtml(fechaInicioStr)).append("'>");
                    html.append("<input type='hidden' name='fechaFin' value='").append(escapeHtml(fechaFinStr)).append("'>");
                    html.append("<input type='hidden' name='estado' value='").append(escapeHtml(estadoFiltro)).append("'>");
                    html.append("<button type='submit' class='btn btn-sm btn-danger' title='Eliminar'><i class='fas fa-trash'></i></button>");
                    html.append("</form>");
                    html.append("</td>");
                    html.append("</tr>");
                }

                html.append("</tbody>");
                html.append("<tfoot>");
                html.append("<tr class='total-row'>");
                html.append("<td colspan='1' class='text-end'><strong>Totales:</strong></td>");
                html.append("<td>").append(totalPeso.setScale(3, RoundingMode.HALF_UP)).append("</td>");
                html.append("<td></td><td></td><td></td>");
                html.append("<td>").append(simboloMoneda).append(df.format(totalGeneral.setScale(2, RoundingMode.HALF_UP))).append("</td>");
                html.append("<td></td><td></td><td></td><td></td>");
                html.append("</tr>");
                html.append("</tfoot>");
                html.append("</table>");
                html.append("</div>");
            }

            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(html.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private String escapeHtml(String str) {
        if (str == null) return "";
        return str.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#x27;");
    }
}