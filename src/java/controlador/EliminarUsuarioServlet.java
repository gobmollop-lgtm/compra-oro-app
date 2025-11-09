package controlador;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/eliminar-usuario")
public class EliminarUsuarioServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!"admin".equals(request.getSession().getAttribute("rol"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Integer usuarioIdSesion = (Integer) request.getSession().getAttribute("usuarioId");

            // No permitir eliminar al usuario actual
            if (usuarioIdSesion != null && usuarioIdSesion == id) {
                response.sendRedirect("gestion-usuarios.jsp?error=propio");
                return;
            }

            logica.UsuarioServicio servicio = new logica.UsuarioServicio();
            servicio.eliminar(id);

            response.sendRedirect("gestion-usuarios.jsp?exito=eliminado");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("gestion-usuarios.jsp?error=eliminar");
        }
    }
}