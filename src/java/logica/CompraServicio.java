package logica;

import datos.CompraDAO;
import modelo.Compra;
import java.util.List;

public class CompraServicio {
    private CompraDAO compraDAO = new CompraDAO();

    public void registrar(Compra compra) {
        compraDAO.crear(compra);
    }

    public List<Compra> obtenerUltimas(int limite) {
        return compraDAO.listarUltimas(limite);
    }
    

}
