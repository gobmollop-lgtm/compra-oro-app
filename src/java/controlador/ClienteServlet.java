package controlador;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import logica.ClienteServicio;
import modelo.Cliente;

@WebServlet("/registrar-cliente")
public class ClienteServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");

        Cliente cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setTelefono(telefono);
        cliente.setEmail(email);

        ClienteServicio servicio = new ClienteServicio();
        servicio.registrar(cliente);

        response.sendRedirect("registrar-compra.jsp?msg=cliente_ok");
    }
}