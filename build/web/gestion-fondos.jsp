<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, modelo.Usuario, modelo.FondoComprador, logica.UsuarioServicio, logica.FondoServicio, java.util.Map, logica.PermisoServicio" %>
<%
// === PROTECCIÓN DINÁMICA DE PERMISOS ===
if (session.getAttribute("usuarioId") == null) {
    response.sendRedirect("login.jsp");
    return;
}

int usuarioId = Integer.parseInt(session.getAttribute("usuarioId").toString());
PermisoServicio permisoServicio = new PermisoServicio();
Map<String, Boolean> permisos = permisoServicio.obtenerPermisosPorUsuario(usuarioId);

if (!permisos.getOrDefault("asignacion_permisos", false)) {
    response.sendRedirect("menu-principal.jsp");
    return;
}
// ======================================

UsuarioServicio usuarioServicio = new UsuarioServicio();
List<Usuario> compradores = usuarioServicio.obtenerPorRol("comprador");

FondoServicio fondoServicio = new FondoServicio();
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Recarga de Fondos - Compradores</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .table th, .table td { vertical-align: middle; }
    </style>
</head>
<body class="bg-light">
<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h3><i class="fas fa-wallet me-2"></i>Recarga de Fondos - Compradores</h3>
        <a href="menu-principal.jsp" class="btn btn-outline-secondary btn-sm">Volver al Menú</a>
    </div>

    <% if ("1".equals(request.getParameter("exito"))) { %>
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        Fondo recargado correctamente.
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% } %>
    <% if ("1".equals(request.getParameter("error"))) { %>
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        Error al recargar fondo.
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% } %>

    <div class="card">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead class="table-dark">
                        <tr>
                            <th>Comprador</th>
                            <th>Monto Asignado</th>
                            <th>Monto Usado</th>
                            <th>Saldo Disponible</th>
                            <th>Recargar</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (compradores != null && !compradores.isEmpty()) { %>
                            <% for (Usuario u : compradores) {
                                FondoComprador fondo = fondoServicio.obtenerPorUsuario(u.getId());
                                java.math.BigDecimal asignado = fondo != null ? fondo.getMontoAsignado() : java.math.BigDecimal.ZERO;
                                java.math.BigDecimal usado = fondo != null ? fondo.getMontoUsado() : java.math.BigDecimal.ZERO;
                                java.math.BigDecimal saldo = asignado.subtract(usado);
                            %>
                            <tr>
                                <td>
                                    <strong><%= u.getNombre() %></strong><br>
                                    <small class="text-muted">@<%= u.getUsuario() %></small>
                                </td>
                                <td><%= logica.FondoServicio.formatearMonto(asignado) %></td>
                                <td><%= logica.FondoServicio.formatearMonto(usado) %></td>
                                <td><span class="text-success fw-bold"><%= logica.FondoServicio.formatearMonto(saldo) %></span></td>
                                <td>
                                    <form action="recargar-fondo" method="post" class="d-inline">
                                        <input type="hidden" name="compradorId" value="<%= u.getId() %>">
                                        <div class="input-group input-group-sm">
                                            <input type="number" step="0.01" name="monto" class="form-control" 
                                                   placeholder="Monto a recargar" 
                                                   value="0.00" min="0" required>
                                            <button class="btn btn-sm btn-success" type="submit" title="Recargar fondo">
                                                <i class="fas fa-plus"></i>
                                            </button>
                                        </div>
                                    </form>
                                </td>
                            </tr>
                            <% } %>
                        <% } else { %>
                            <tr>
                                <td colspan="5" class="text-center text-muted py-4">
                                    <i class="fas fa-user-friends fa-2x mb-2"></i><br>
                                    No hay compradores registrados.
                                </td>
                            </tr>
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