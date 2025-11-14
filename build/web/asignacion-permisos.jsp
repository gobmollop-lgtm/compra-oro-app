<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, modelo.Usuario, modelo.Modulo, logica.UsuarioServicio, logica.ModuloServicio, logica.PermisoServicio" %>
<%
// === PROTECCIÓN DINÁMICA: solo usuarios con permiso "asignacion_permisos" ===
if (session.getAttribute("usuarioId") == null) {
    response.sendRedirect("login.jsp");
    return;
}

int usuarioIdSesion = Integer.parseInt(session.getAttribute("usuarioId").toString());
PermisoServicio permisoServicioProteccion = new PermisoServicio();
Map<String, Boolean> permisosSesion = permisoServicioProteccion.obtenerPermisosPorUsuario(usuarioIdSesion);

if (!permisosSesion.getOrDefault("asignacion_permisos", false)) {
    response.sendRedirect("menu-principal.jsp");
    return;
}
// ========================================================================

// === Carga de datos para la tabla ===
UsuarioServicio usuarioServicio = new UsuarioServicio();
List<Usuario> usuarios = usuarioServicio.obtenerTodos();

ModuloServicio moduloServicio = new ModuloServicio();
List<Modulo> modulos = moduloServicio.obtenerTodosModulos();

PermisoServicio permisoServicio = new PermisoServicio(); // Usado en el bucle
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Asignación de Permisos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .table th, .table td { 
            vertical-align: middle; 
            padding: 0.25rem 0.5rem; 
            font-size: 0.85rem;
        }
        .table td { text-align: center; }
        .badge { font-size: 0.75rem; padding: 0.2em 0.4em; }
        .btn-sm { padding: 0.2rem 0.4rem; font-size: 0.75rem; }
        .section-title { font-size: 1rem; margin-bottom: 0.5rem; }
        .compact-table { margin-top: 0.5rem; margin-bottom: 0.5rem; }
        .btn-add-module { background-color: #28a745; color: white; border: none; }
        .btn-add-module:hover { background-color: #218838; }
        .card-header .btn-add-module { font-size: 0.9rem; padding: 0.25rem 0.75rem; }
    </style>
</head>
<body class="bg-light">
<div class="container mt-3">

    <!-- Botón Volver -->
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h4><i class="fas fa-shield-alt me-2"></i>Asignación de Permisos</h4>
        <a href="menu-principal.jsp" class="btn btn-outline-secondary btn-sm">Volver al Menú</a>
    </div>

    <!-- Sección: Módulos Registrados + Botón Agregar -->
    <div class="card shadow-sm mb-3">
        <div class="card-header py-1 bg-dark text-white d-flex justify-content-between align-items-center">
            <div class="d-flex align-items-center">
                <i class="fas fa-list me-2"></i>
                <span class="section-title">Módulos Registrados</span>
            </div>
            <button type="button" class="btn btn-add-module btn-sm" data-bs-toggle="modal" data-bs-target="#agregarModuloModal">
                <i class="fas fa-plus me-1"></i>Agregar Módulo
            </button>
        </div>
        <div class="card-body p-2">
            <div class="table-responsive compact-table">
                <table class="table table-sm table-striped">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Nombre</th>
                            <th>Etiqueta</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (modulos != null && !modulos.isEmpty()) { %>
                            <% for (Modulo m : modulos) { %>
                                <tr>
                                    <td><%= m.getId() %></td>
                                    <td><code><%= m.getNombre() %></code></td>
                                    <td><%= m.getEtiqueta() %></td>
                                </tr>
                            <% } %>
                        <% } else { %>
                            <tr><td colspan="3" class="text-center text-muted">Sin módulos</td></tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Modal: Agregar Nuevo Módulo -->
    <div class="modal fade" id="agregarModuloModal" tabindex="-1" aria-labelledby="agregarModuloModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="agregarModuloModalLabel"><i class="fas fa-plus-circle me-2 text-success"></i>Agregar Nuevo Módulo</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form action="AgregarModuloServlet" method="POST" id="formularioAgregarModulo">
                        <div class="mb-3">
                            <label for="nombre" class="form-label">Nombre del Módulo (clave)</label>
                            <input type="text" class="form-control form-control-sm" id="nombre" name="nombre" placeholder="ej: colaboradores" required>
                            <small class="text-muted">Usa solo letras minúsculas y guiones bajos. Ej: "clientes", "configuracion"</small>
                        </div>
                        <div class="mb-3">
                            <label for="etiqueta" class="form-label">Etiqueta para mostrar</label>
                            <input type="text" class="form-control form-control-sm" id="etiqueta" name="etiqueta" placeholder="ej: Gestión de Colaboradores" required>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Cerrar</button>
                    <button type="submit" form="formularioAgregarModulo" class="btn btn-success btn-sm">
                        <i class="fas fa-plus me-1"></i> Agregar Módulo
                    </button>
                </div>
            </div>
        </div>
    </div>

    <!-- Tabla: Asignación de Permisos (Original) -->
    <div class="table-responsive">
        <table class="table table-striped table-bordered">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Usuario</th>
                    <th>Rol</th>
                    <% for (Modulo m : modulos) { %>
                    <th><%= m.getEtiqueta() %></th>
                    <% } %>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <% for (Usuario u : usuarios) {
                    Map<String, Boolean> permisosUsuario = permisoServicio.obtenerPermisosPorUsuario(u.getId());
                %>
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
                    <% for (Modulo m : modulos) {
                        boolean puedeVer = permisosUsuario.getOrDefault(m.getNombre(), false);
                    %>
                    <td>
                        <input type="checkbox" 
                               <%= puedeVer ? "checked" : "" %> 
                               onchange="actualizarPermiso(<%= u.getId() %>, '<%= m.getNombre() %>', this.checked)">
                    </td>
                    <% } %>
                    <td>
                        <% if (usuarioIdSesion != u.getId()) { %>
                        <a href="eliminar-usuario?id=<%= u.getId() %>" 
                           class="btn btn-sm btn-danger"
                           onclick="return confirm('¿Eliminar usuario <%= u.getUsuario() %>?')">
                            <i class="fas fa-trash"></i>
                        </a>
                        <% } else { %>
                        <button class="btn btn-sm btn-secondary" disabled>
                            <i class="fas fa-trash"></i>
                        </button>
                        <% } %>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
</div>

<script>
function actualizarPermiso(usuarioId, modulo, puedeVer) {
    fetch('actualizar-permiso?usuarioId=' + usuarioId + '&modulo=' + encodeURIComponent(modulo) + '&puedeVer=' + puedeVer)
        .then(response => {
            if (response.ok) {
                console.log("Permiso actualizado: " + modulo + " → " + puedeVer);
            } else {
                alert("Error al actualizar permiso");
            }
        })
        .catch(err => {
            console.error("Error:", err);
            alert("Error de red");
        });
}
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>