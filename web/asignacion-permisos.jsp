<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, modelo.Usuario, modelo.Modulo, logica.UsuarioServicio, logica.ModuloServicio, logica.PermisoServicio" %>
<%
// === PROTECCIÓN DE PERMISOS ===
if (session.getAttribute("usuarioId") == null) {
    response.sendRedirect("login.jsp");
    return;
}
int usuarioId = Integer.parseInt(session.getAttribute("usuarioId").toString());
PermisoServicio permisoServicioProteccion = new PermisoServicio();
Map<String, Boolean> permisos = permisoServicioProteccion.obtenerPermisosPorUsuario(usuarioId);

if (!permisos.getOrDefault("asignacion_permisos", false)) {
    response.sendRedirect("menu-principal.jsp");
    return;
}
// =============================

// === DECLARACIÓN DE SERVICIOS PARA LA TABLA ===
UsuarioServicio usuarioServicio = new UsuarioServicio();
List<Usuario> usuarios = usuarioServicio.obtenerTodos();

ModuloServicio moduloServicio = new ModuloServicio();
List<Modulo> modulos = moduloServicio.obtenerTodosModulos();

PermisoServicio permisoServicio = new PermisoServicio(); // ← ¡Declarado aquí!
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Asignación de Permisos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h3><i class="fas fa-shield-alt me-2"></i>Asignación de Permisos</h3>
        <a href="menu-principal.jsp" class="btn btn-outline-secondary">Volver</a>
    </div>

    <div class="table-responsive">
        <table class="table table-striped table-bordered">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Usuario</th>
                    <% for (Modulo m : modulos) { %>
                    <th><%= m.getEtiqueta() %></th>
                    <% } %>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <% for (Usuario u : usuarios) {
                    Map<String, Boolean> userPerms = permisoServicio.obtenerPermisosPorUsuario(u.getId());
                %>
                <tr>
                    <td><%= u.getId() %></td>
                    <td><%= u.getNombre() %></td>
                    <td><%= u.getUsuario() %></td>
                    <% for (Modulo m : modulos) {
                        boolean puedeVer = userPerms.getOrDefault(m.getNombre(), false);
                    %>
                    <td class="text-center">
                        <input type="checkbox" 
                               <%= puedeVer ? "checked" : "" %> 
                               onchange="actualizarPermiso(<%= u.getId() %>, '<%= m.getNombre() %>', this.checked)">
                    </td>
                    <% } %>
                    <td>
                        <a href="eliminar-usuario?id=<%= u.getId() %>" class="btn btn-sm btn-danger" onclick="return confirm('¿Eliminar usuario?')">Eliminar</a>
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
        .then(response => response.text())
        .then(data => {
            console.log("Permiso actualizado para " + modulo);
        })
        .catch(err => console.error("Error:", err));
}
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>