package logica;

import datos.CompraDAO;
import modelo.Compra;
import java.util.List;
import modelo.CompraResumen;

public class CompraServicio {
    private CompraDAO compraDAO = new CompraDAO();

    public void registrar(Compra compra) {
        compraDAO.crear(compra);
    }

    public List<Compra> obtenerUltimas(int limite) {
        return compraDAO.listarUltimas(limite);
    }

    public CompraResumen obtenerResumenPorUsuario(int usuarioId) {
        return compraDAO.obtenerResumenPorUsuario(usuarioId);
    }

    public List<Compra> obtenerPorUsuario(int usuarioId) {
        return compraDAO.obtenerPorUsuario(usuarioId);
    }

    // NUEVOS MÉTODOS
    public Compra obtenerPorId(int id) {
        return compraDAO.obtenerPorId(id);
    }

    public void eliminar(int id) {
        compraDAO.eliminar(id);
    }

 
// En logica/CompraServicio.java
public void actualizarEstado(int compraId, String estado) {
    compraDAO.actualizarEstado(compraId, estado);
}

}
