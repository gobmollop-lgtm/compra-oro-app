<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, modelo.Compra, logica.CompraServicio, modelo.Configuracion, logica.ConfiguracionServicio, java.util.Map, logica.PermisoServicio" %>
<%
if (session.getAttribute("usuarioId") == null) {
    response.sendRedirect("login.jsp");
    return;
}
int usuarioId = Integer.parseInt(session.getAttribute("usuarioId").toString());
PermisoServicio permisoServicio = new PermisoServicio();
Map<String, Boolean> permisos = permisoServicio.obtenerPermisosPorUsuario(usuarioId);

if (!permisos.getOrDefault("historial", false)) {
    response.sendRedirect("menu-principal.jsp");
    return;
}

CompraServicio compraServicio = new CompraServicio();
List<Compra> compras = compraServicio.obtenerUltimas(20);

ConfiguracionServicio servicioConfigMoneda = new ConfiguracionServicio();
Configuracion configuracionMoneda = servicioConfigMoneda.obtenerConfiguracion();
String simboloMoneda = (configuracionMoneda != null && configuracionMoneda.getMonedaSimbolo() != null) 
    ? configuracionMoneda.getMonedaSimbolo() 
    : "$";
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Historial de Compras</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h3>Compras Recientes</h3>
        <div>
            <% if (permisos.getOrDefault("compra", false)) { %>
            <a href="registrar-compra.jsp" class="btn btn-primary me-2">Nueva Compra</a>
            <% } %>
            <a href="menu-principal.jsp" class="btn btn-outline-secondary">Menú Principal</a>
        </div>
    </div>

    <% if ("1".equals(request.getParameter("exito"))) { %>
    <div class="alert alert-success">¡Compra registrada exitosamente!</div>
    <% } %>

    <div class="table-responsive">
        <table class="table table-striped table-bordered">
            <thead class="table-dark">
                <tr>
                    <th>Usuario</th>
                    <th>Cliente</th>
                    <th>Peso</th>
                    <th>Kilate</th>
                    <th>Punto</th>
                    <th>Precio/g</th>
                    <th>Total</th>
                    <th>Fecha</th>
                </tr>
            </thead>
            <tbody>
                <% for (Compra c : compras) { %>
                <tr>
                    <td><%= c.getNombreUsuario() %></td>
                    <td><%= c.getNombreCliente() %></td>
                    <td><%= c.getPesoGramos() %></td>
                    <td><%= c.getKilate() %></td>
                    <td><%= c.getPunto() %></td>
                    <td><%= simboloMoneda %><%= c.getPrecioGramo() %></td>
                    <td class="fw-bold"><%= simboloMoneda %><%= c.getTotal() %></td>
                    <td><%= c.getFecha() %></td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>