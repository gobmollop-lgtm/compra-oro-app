package controlador;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/eliminar-cliente")
public class EliminarClienteServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getSession().getAttribute("usuarioId") == null ||
            !"admin".equals(request.getSession().getAttribute("rol"))) {
            response.sendRedirect("login.jsp");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));

        logica.ClienteServicio servicio = new logica.ClienteServicio();
        servicio.eliminar(id);

        response.sendRedirect("clientes.jsp?msg=eliminado");
    }
}