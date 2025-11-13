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
        // === Protección: solo usuarios con permiso pueden guardar ===
        if (request.getSession().getAttribute("usuarioId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        // Ya no usamos "rol", porque el permiso lo gestiona el sistema dinámico
        // Pero si quieres mantener una capa extra, puedes verificar con PermisoServicio aquí

        Configuracion c = new Configuracion();
        c.setId(1);
        c.setMonedaSimbolo(request.getParameter("monedaSimbolo"));
        c.setNombreNegocio(request.getParameter("nombreNegocio"));
        c.setTelefonoNegocio(request.getParameter("telefonoNegocio"));
        c.setDireccionNegocio(request.getParameter("direccionNegocio"));
        c.setLogoUrl(request.getParameter("logoUrl"));
        // ✅ Eliminadas las líneas de permisos del comprador
        c.setIpServidor(request.getParameter("ipServidor"));

        ConfiguracionServicio servicio = new ConfiguracionServicio();
        servicio.guardarConfiguracion(c);

        response.sendRedirect("configuracion.jsp?exito=1");
    }
}