package logica;

import datos.PermisoDAO;
import java.util.List;
import java.util.Map;
import modelo.Modulo;

public class PermisoServicio {
    private PermisoDAO permisoDAO = new PermisoDAO();

    public Map<String, Boolean> obtenerPermisosPorUsuario(int usuarioId) {
        return permisoDAO.obtenerPermisosPorUsuario(usuarioId);
    }

    public void guardarPermiso(int usuarioId, String modulo, boolean puedeVer) {
        permisoDAO.guardarPermiso(usuarioId, modulo, puedeVer);
    }
    // En logica/PermisoServicio.java
    public void asignarTodosPermisos(int usuarioId) {
    List<Modulo> modulos = new ModuloServicio().obtenerTodosModulos();
    for (Modulo m : modulos) {
        guardarPermiso(usuarioId, m.getNombre(), true); // Puede ver
    }
}
}