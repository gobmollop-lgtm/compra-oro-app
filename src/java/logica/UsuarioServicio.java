package logica;

import datos.UsuarioDAO;
import modelo.Usuario;
import java.util.List;

public class UsuarioServicio {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    // Método para autenticación (¡ESPECIALMENTE PARA LOGIN!)
    public Usuario autenticar(String usuario, String contrasena) {
        return usuarioDAO.buscarPorCredenciales(usuario, contrasena);
    }

    // Registro de nuevos usuarios
    public void registrar(Usuario usuario) {
        usuarioDAO.crear(usuario);
    }

    // Listar todos los usuarios
    public List<Usuario> listarTodos() {
        return usuarioDAO.listarTodos();
    }

    // Obtener usuario por ID
    public Usuario obtenerPorId(int id) {
        return usuarioDAO.buscarPorId(id);
    }

    // Actualizar usuario
    public void actualizar(Usuario usuario) {
        usuarioDAO.actualizar(usuario);
    }

    // Eliminar usuario
    public void eliminar(int id) {
        usuarioDAO.eliminar(id);
    }
    public void actualizarConContrasena(Usuario usuario) {
    usuarioDAO.actualizarConContrasena(usuario);
}
}