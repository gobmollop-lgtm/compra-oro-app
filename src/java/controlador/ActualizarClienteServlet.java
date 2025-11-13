package controlador;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import logica.ClienteServicio;
import modelo.Cliente;

@WebServlet("/actualizar-cliente")
public class ActualizarClienteServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getSession().getAttribute("usuarioId") == null ||
            !"admin".equals(request.getSession().getAttribute("rol"))) {
            response.sendRedirect("login.jsp");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");

        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNombre(nombre);
        cliente.setTelefono(telefono);
        cliente.setEmail(email);

        ClienteServicio servicio = new ClienteServicio();
        servicio.actualizar(cliente);

        response.sendRedirect("clientes.jsp?msg=actualizado");
    }
}