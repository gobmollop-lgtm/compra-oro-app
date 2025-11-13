package logica;

import datos.ModuloDAO;
import java.util.List;
import modelo.Modulo;

public class ModuloServicio {
    private ModuloDAO moduloDAO = new ModuloDAO();

    public List<Modulo> obtenerTodosModulos() {
        return moduloDAO.listarTodos();
    }
}