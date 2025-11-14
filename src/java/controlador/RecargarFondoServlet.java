package controlador;

import java.io.IOException;
import java.math.BigDecimal;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import logica.FondoServicio;
import logica.PermisoServicio;

@WebServlet("/recargar-fondo")
public class RecargarFondoServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        if (session.getAttribute("usuarioId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int usuarioIdSesion = Integer.parseInt(session.getAttribute("usuarioId").toString());
        PermisoServicio permisoServicio = new PermisoServicio();
        if (!permisoServicio.obtenerPermisosPorUsuario(usuarioIdSesion).getOrDefault("asignacion_permisos", false)) {
            response.sendRedirect("menu-principal.jsp");
            return;
        }

        try {
            int compradorId = Integer.parseInt(request.getParameter("compradorId"));
            BigDecimal montoRecarga = new BigDecimal(request.getParameter("monto"));

            FondoServicio fondoServicio = new FondoServicio();
            fondoServicio.recargarFondo(compradorId, montoRecarga);

            response.sendRedirect("gestion-fondos.jsp?exito=1");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("gestion-fondos.jsp?error=1");
        }
    }
}