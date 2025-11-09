package controlador;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import logica.ConfiguracionServicio;
import modelo.Configuracion;

@WebServlet("/guardar-configuracion")
public class GuardarConfiguracionServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!"admin".equals(request.getSession().getAttribute("rol"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Configuracion c = new Configuracion();
        c.setId(1);
        c.setMonedaSimbolo(request.getParameter("monedaSimbolo"));
        c.setNombreNegocio(request.getParameter("nombreNegocio"));
        c.setTelefonoNegocio(request.getParameter("telefonoNegocio"));
        c.setDireccionNegocio(request.getParameter("direccionNegocio"));
        c.setLogoUrl(request.getParameter("logoUrl"));
        c.setCompradorVeHistorialCompleto("on".equals(request.getParameter("compradorVeHistorialCompleto")));
        c.setCompradorPuedeRegistrarCliente("on".equals(request.getParameter("compradorPuedeRegistrarCliente")));
        c.setIpServidor(request.getParameter("ipServidor")); // ← NUEVO

        ConfiguracionServicio servicio = new ConfiguracionServicio();
        servicio.guardarConfiguracion(c);

        response.sendRedirect("configuracion.jsp?exito=1");
    }
}