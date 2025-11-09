package controlador;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import logica.UsuarioServicio;
import modelo.Usuario;

@WebServlet("/registrar-usuario")
public class RegistrarUsuarioServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Verificación de rol
        HttpSession sesion = request.getSession(false);
        if (sesion == null || !"admin".equals(sesion.getAttribute("rol"))) {
            System.out.println("⚠️ Acceso denegado: no es admin");
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            String nombre = request.getParameter("nombre");
            String usuario = request.getParameter("usuario");
            String contrasena = request.getParameter("contrasena");
            String rol = request.getParameter("rol");

            System.out.println("📝 Recibido: nombre=" + nombre + ", usuario=" + usuario + ", rol=" + rol);

            Usuario u = new Usuario();
            u.setNombre(nombre);
            u.setUsuario(usuario);
            u.setContrasena(contrasena);
            u.setRol(rol);

            UsuarioServicio servicio = new UsuarioServicio();
            servicio.registrar(u);

            System.out.println("✅ Usuario registrado exitosamente");
            response.sendRedirect("gestion-usuarios.jsp?exito=1");
        } catch (Exception e) {
            System.err.println("❌ Error al registrar usuario:");
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("gestion-usuarios.jsp").forward(request, response);
        }
    }
}