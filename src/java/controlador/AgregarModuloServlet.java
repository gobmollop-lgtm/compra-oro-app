package controlador;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logica.ModuloServicio;
import modelo.Modulo;

@WebServlet("/AgregarModuloServlet")
public class AgregarModuloServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String nombre = request.getParameter("nombre");
        String etiqueta = request.getParameter("etiqueta");
        
        if (nombre == null || nombre.trim().isEmpty() || 
            etiqueta == null || etiqueta.trim().isEmpty()) {
            request.setAttribute("error", "Todos los campos son obligatorios.");
            request.getRequestDispatcher("/asignacion-permisos.jsp").forward(request, response);
            return;
        }

        // Normaliza el nombre (solo letras, números, guiones bajos)
        nombre = nombre.trim().toLowerCase().replaceAll("[^a-z0-9_]", "_");

        try {
            ModuloServicio moduloServicio = new ModuloServicio();
            Modulo nuevoModulo = new Modulo();
            nuevoModulo.setNombre(nombre);
            nuevoModulo.setEtiqueta(etiqueta.trim());

            boolean exito = moduloServicio.agregarModulo(nuevoModulo);

            if (exito) {
                request.setAttribute("mensaje", "Módulo '" + nombre + "' agregado correctamente.");
            } else {
                request.setAttribute("error", "No se pudo agregar el módulo. Posiblemente ya exista.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error interno al guardar el módulo.");
        }

        request.getRequestDispatcher("/asignacion-permisos.jsp").forward(request, response);
    }
}