<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, modelo.Cliente, logica.ClienteServicio" %>
<%
if (session.getAttribute("usuarioId") == null || !"admin".equals(session.getAttribute("rol"))) {
    response.sendRedirect("login.jsp");
    return;
}
ClienteServicio clienteServicio = new ClienteServicio();
List<Cliente> clientes = clienteServicio.obtenerTodos();
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Clientes</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h3><i class="fas fa-user-friends me-2"></i>Lista de Clientes</h3>
        <a href="menu-principal.jsp" class="btn btn-outline-secondary">Volver</a>
    </div>

    <div class="table-responsive">
        <table class="table table-striped table-bordered">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Teléfono</th>
                    <th>Email</th>
                </tr>
            </thead>
            <tbody>
                <% for (Cliente c : clientes) { %>
                <tr>
                    <td><%= c.getId() %></td>
                    <td><%= c.getNombre() %></td>
                    <td><%= c.getTelefono() != null ? c.getTelefono() : "-" %></td>
                    <td><%= c.getEmail() != null ? c.getEmail() : "-" %></td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>