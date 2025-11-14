package logica;

import datos.ModuloDAO;
import java.util.List;
import modelo.Modulo;

public class ModuloServicio {
    private ModuloDAO moduloDAO = new ModuloDAO();

    public List<Modulo> obtenerTodosModulos() {
        return moduloDAO.listarTodos();
    }

    // === NUEVO MÉTODO: AGREGAR MODULO ===
    public boolean agregarModulo(Modulo modulo) {
        if (modulo == null || modulo.getNombre() == null || modulo.getEtiqueta() == null) {
            return false;
        }
        return moduloDAO.agregar(modulo.getNombre().trim(), modulo.getEtiqueta().trim());
    }
}