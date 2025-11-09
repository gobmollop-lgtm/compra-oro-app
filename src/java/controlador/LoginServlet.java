package controlador;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import logica.UsuarioServicio;
import modelo.Usuario;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String usuario = request.getParameter("usuario");
        String contrasena = request.getParameter("contrasena");

        UsuarioServicio servicio = new UsuarioServicio();
        Usuario u = servicio.autenticar(usuario, contrasena);

        if (u != null) {
            
            HttpSession sesion = request.getSession();
            sesion.setAttribute("usuarioId", u.getId());
            sesion.setAttribute("nombreUsuario", u.getNombre());
            sesion.setAttribute("rol", u.getRol());
            // En LoginServlet.java, dentro del if (u != null)
            response.sendRedirect("menu-principal.jsp"); // ← cambia esto
            //response.sendRedirect("registrar-compra.jsp");
        } else {
            response.sendRedirect("login.jsp?error=1");
        }
    }
}