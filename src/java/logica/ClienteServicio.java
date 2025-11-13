package logica;

import datos.ClienteDAO;
import modelo.Cliente;
import java.util.List;

public class ClienteServicio {
    private ClienteDAO clienteDAO = new ClienteDAO();

    public void registrar(Cliente cliente) {
        clienteDAO.crear(cliente);
    }

    public List<Cliente> obtenerTodos() {
        return clienteDAO.listarTodos();
    }

    public Cliente obtenerPorId(int id) {
        return clienteDAO.obtenerPorId(id);
    }

    public void actualizar(Cliente cliente) {
        clienteDAO.actualizar(cliente);
    }

    public void eliminar(int id) {
        clienteDAO.eliminar(id);
    }
}
