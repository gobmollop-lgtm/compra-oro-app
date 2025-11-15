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
    // En logica/CompraServicio.java
public CompraResumen obtenerResumenPorUsuario(int usuarioId) {
    return compraDAO.obtenerResumenPorUsuario(usuarioId);
}
    
// En logica/CompraServicio.java
public List<Compra> obtenerPorUsuario(int usuarioId) {
    return compraDAO.obtenerPorUsuario(usuarioId);
}
}
