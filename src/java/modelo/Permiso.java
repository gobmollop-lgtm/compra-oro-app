package modelo;

public class Permiso {
    private int id;
    private int usuarioId;
    private String modulo;
    private String permiso; // "ver", "editar", "todos"

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public String getModulo() { return modulo; }
    public void setModulo(String modulo) { this.modulo = modulo; }

    public String getPermiso() { return permiso; }
    public void setPermiso(String permiso) { this.permiso = permiso; }
}
