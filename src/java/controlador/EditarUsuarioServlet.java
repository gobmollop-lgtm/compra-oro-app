package controlador;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import logica.UsuarioServicio;
import modelo.Usuario;

@WebServlet("/editar-usuario")
public class EditarUsuarioServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!"admin".equals(request.getSession().getAttribute("rol"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String nombre = request.getParameter("nombre");
            String usuario = request.getParameter("usuario");
            String rol = request.getParameter("rol");
            String contrasena = request.getParameter("contrasena");

            Usuario u = new Usuario();
            u.setId(id);
            u.setNombre(nombre);
            u.setUsuario(usuario);
            u.setRol(rol);

            UsuarioServicio servicio = new UsuarioServicio();

            if (contrasena != null && !contrasena.trim().isEmpty()) {
                // Actualizar con nueva contraseña
                u.setContrasena(contrasena);
                servicio.actualizarConContrasena(u);
            } else {
                // Actualizar sin cambiar contraseña
                servicio.actualizar(u);
            }

            response.sendRedirect("gestion-usuarios.jsp?exito=editado");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("gestion-usuarios.jsp?error=editar");
        }
    }
}