package controlador;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import logica.UsuarioServicio;
import logica.PermisoServicio;
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

            // === ASIGNAR PERMISOS POR DEFECTO ===
            PermisoServicio permisoServicio = new PermisoServicio();
            if ("admin".equals(u.getRol())) {
                // Admin: todos los permisos activados
                permisoServicio.asignarTodosPermisos(u.getId());
            }
            // Comprador: sin permisos (se asignan manualmente desde la interfaz)
            // ===================================

            response.sendRedirect("menu-principal.jsp");
        } else {
            response.sendRedirect("login.jsp?error=1");
        }
    }
}