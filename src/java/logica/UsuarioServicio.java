package logica;

import datos.UsuarioDAO;
import modelo.Usuario;
import java.util.List;

public class UsuarioServicio {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    // Método de autenticación: busca por usuario y compara contraseña en Java (texto plano)
    public Usuario autenticar(String usuario, String contrasena) {
        Usuario u = usuarioDAO.buscarPorUsuario(usuario);
        if (u != null && contrasena != null && contrasena.equals(u.getContrasena())) {
            return u;
        }
        return null;
    }

    public void registrar(Usuario usuario) {
        usuarioDAO.crear(usuario);
    }

    public List<Usuario> listarTodos() {
        return usuarioDAO.listarTodos();
    }

    public Usuario obtenerPorId(int id) {
        return usuarioDAO.buscarPorId(id);
    }

    public void actualizar(Usuario usuario) {
        usuarioDAO.actualizar(usuario);
    }

    public void eliminar(int id) {
        usuarioDAO.eliminar(id);
    }

    public void actualizarConContrasena(Usuario usuario) {
        usuarioDAO.actualizarConContrasena(usuario);
    }
    public List<Usuario> obtenerTodos() {
        return usuarioDAO.listarTodos();
    }
}