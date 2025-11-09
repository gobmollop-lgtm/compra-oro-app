<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, modelo.Usuario, logica.UsuarioServicio" %>
<%
if (session.getAttribute("usuarioId") == null || !"admin".equals(session.getAttribute("rol"))) {
    response.sendRedirect("login.jsp");
    return;
}
UsuarioServicio servicio = new UsuarioServicio();
List<Usuario> usuarios = servicio.listarTodos();
Integer usuarioIdSesion = (Integer) session.getAttribute("usuarioId");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Usuarios</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="bg-light">
<div class="container mt-4">
    <div class="card shadow">
        <div class="card-header bg-warning text-dark">
            <h4 class="d-inline"><i class="fas fa-users-cog me-2"></i>Gestión de Usuarios</h4>
            <div class="float-end">
                <small class="me-2">👤 <%= session.getAttribute("nombreUsuario") %> (Administrador)</small>
                <a href="menu-principal.jsp" class="btn btn-sm btn-outline-dark">Menú Principal</a>
            </div>
        </div>
        <div class="card-body">
            <!-- Mensajes -->
            <% if ("1".equals(request.getParameter("exito"))) { %>
            <div class="alert alert-success">✅ Usuario creado exitosamente.</div>
            <% } else if ("editado".equals(request.getParameter("exito"))) { %>
            <div class="alert alert-success">✅ Usuario actualizado exitosamente.</div>
            <% } else if ("eliminado".equals(request.getParameter("exito"))) { %>
            <div class="alert alert-success">✅ Usuario eliminado exitosamente.</div>
            <% } %>
            
            <% if ("propio".equals(request.getParameter("error"))) { %>
            <div class="alert alert-warning">⚠️ No puedes eliminar tu propia cuenta.</div>
            <% } else if (request.getParameter("error") != null) { %>
            <div class="alert alert-danger">❌ Error al procesar la operación.</div>
            <% } %>

            <!-- Formulario de registro -->
            <div class="card mb-4">
                <div class="card-header">
                    <h5 class="mb-0">Nuevo Usuario</h5>
                </div>
                <div class="card-body">
                    <form action="registrar-usuario" method="post">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Nombre Completo</label>
                                <input type="text" name="nombre" class="form-control" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Usuario (login)</label>
                                <input type="text" name="usuario" class="form-control" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Contraseña</label>
                                <input type="password" name="contrasena" class="form-control" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Rol</label>
                                <select name="rol" class="form-control" required>
                                    <option value="admin">Administrador</option>
                                    <option value="comprador">Comprador</option>
                                </select>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-success">Registrar Usuario</button>
                    </form>
                </div>
            </div>

            <!-- Tabla de usuarios -->
            <h5 class="mb-3">Usuarios Registrados</h5>
            <div class="table-responsive">
                <table class="table table-striped table-bordered">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Nombre</th>
                            <th>Usuario</th>
                            <th>Rol</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Usuario u : usuarios) { %>
                        <tr>
                            <td><%= u.getId() %></td>
                            <td><%= u.getNombre() %></td>
                            <td><%= u.getUsuario() %></td>
                            <td>
                                <% if ("admin".equals(u.getRol())) { %>
                                <span class="badge bg-success">Administrador</span>
                                <% } else { %>
                                <span class="badge bg-info">Comprador</span>
                                <% } %>
                            </td>
                            <td>
                                <!-- Botón Editar -->
                                <button type="button" class="btn btn-sm btn-outline-primary" data-bs-toggle="modal" data-bs-target="#editarModal<%= u.getId() %>">
                                    <i class="fas fa-edit"></i>
                                </button>
                                
                                <!-- Botón Eliminar -->
                                <% if (usuarioIdSesion != null && usuarioIdSesion != u.getId()) { %>
                                <a href="eliminar-usuario?id=<%= u.getId() %>" 
                                   class="btn btn-sm btn-outline-danger"
                                   onclick="return confirm('¿Eliminar usuario <%= u.getUsuario() %>?')">
                                    <i class="fas fa-trash"></i>
                                </a>
                                <% } else { %>
                                <button class="btn btn-sm btn-outline-secondary" disabled>
                                    <i class="fas fa-trash"></i>
                                </button>
                                <% } %>
                            </td>
                        </tr>

                        <!-- Modal de Edición (ACTUALIZADO CON CONTRASEÑA) -->
<div class="modal fade" id="editarModal<%= u.getId() %>" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Editar Usuario: <%= u.getUsuario() %></h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="editar-usuario" method="post">
                <div class="modal-body">
                    <input type="hidden" name="id" value="<%= u.getId() %>">
                    <div class="mb-3">
                        <label>Nombre Completo</label>
                        <input type="text" name="nombre" class="form-control" value="<%= u.getNombre() %>" required>
                    </div>
                    <div class="mb-3">
                        <label>Usuario</label>
                        <input type="text" name="usuario" class="form-control" value="<%= u.getUsuario() %>" required>
                    </div>
                    <div class="mb-3">
                        <label>Rol</label>
                        <select name="rol" class="form-control" required>
                            <option value="admin" <%= "admin".equals(u.getRol()) ? "selected" : "" %>>Administrador</option>
                            <option value="comprador" <%= "comprador".equals(u.getRol()) ? "selected" : "" %>>Comprador</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label>Nueva Contraseña (opcional)</label>
                        <input type="password" name="contrasena" class="form-control" placeholder="Dejar vacío para no cambiar">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                    <button type="submit" class="btn btn-primary">Guardar Cambios</button>
                </div>
            </form>
        </div>
    </div>
</div>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>