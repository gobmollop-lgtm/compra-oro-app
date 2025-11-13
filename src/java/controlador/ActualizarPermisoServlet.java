package controlador;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import logica.PermisoServicio;

@WebServlet("/actualizar-permiso")
public class ActualizarPermisoServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int usuarioId = Integer.parseInt(request.getParameter("usuarioId"));
        String modulo = request.getParameter("modulo");
        boolean puedeVer = Boolean.parseBoolean(request.getParameter("puedeVer"));

        PermisoServicio servicio = new PermisoServicio();
        servicio.guardarPermiso(usuarioId, modulo, puedeVer);

        response.getWriter().write("OK");
    }
}