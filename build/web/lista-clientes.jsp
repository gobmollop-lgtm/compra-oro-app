<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, modelo.Cliente, logica.ClienteServicio" %>
<%
if (session.getAttribute("usuarioId") == null || !"admin".equals(session.getAttribute("rol"))) {
    response.sendRedirect("login.jsp");
    return;
}

ClienteServicio clienteServicio = new ClienteServicio();
List<Cliente> clientes = clienteServicio.obtenerTodos();

String msg = request.getParameter("msg");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Clientes</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <script>
        function confirmarEliminacion() {
            return confirm("¿Estás seguro de que deseas eliminar este cliente?");
        }
    </script>
</head>
<body class="bg-light">
<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h3>Lista de Clientes</h3>
        <div>
            <!-- Botón para abrir el modal de nuevo cliente -->
            <button type="button" class="btn btn-success me-2" data-bs-toggle="modal" data-bs-target="#nuevoClienteModal">
                + Agregar Nuevo Cliente
            </button>
            <a href="menu-principal.jsp" class="btn btn-outline-secondary">Volver</a>
        </div>
    </div>

    <% if ("actualizado".equals(msg)) { %>
        <div class="alert alert-success alert-dismissible fade show">
            Cliente actualizado correctamente.
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    <% } else if ("eliminado".equals(msg)) { %>
        <div class="alert alert-success alert-dismissible fade show">
            Cliente eliminado correctamente.
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    <% } else if ("cliente_ok".equals(msg)) { %>
        <div class="alert alert-success alert-dismissible fade show">
            Cliente registrado correctamente.
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    <% } %>

    <div class="table-responsive">
        <table class="table table-striped table-bordered">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Teléfono</th>
                    <th>Email</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <% for (Cliente c : clientes) { %>
                <tr>
                    <td><%= c.getId() %></td>
                    <td><%= c.getNombre() %></td>
                    <td><%= c.getTelefono() != null ? c.getTelefono() : "-" %></td>
                    <td><%= c.getEmail() != null ? c.getEmail() : "-" %></td>
                    <td>
                        <a href="editar-cliente.jsp?id=<%= c.getId() %>" class="btn btn-sm btn-primary">Editar</a>
                        <a href="eliminar-cliente?id=<%= c.getId() %>" class="btn btn-sm btn-danger" onclick="return confirmarEliminacion()">Eliminar</a>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
</div>

<!-- Modal Nuevo Cliente -->
<div class="modal fade" id="nuevoClienteModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Nuevo Cliente</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="registrar-cliente" method="post">
                <div class="modal-body">
                    <div class="mb-3">
                        <input type="text" name="nombre" class="form-control" placeholder="Nombre completo" required>
                    </div>
                    <div class="mb-3">
                        <input type="text" name="telefono" class="form-control" placeholder="Teléfono (opcional)">
                    </div>
                    <div class="mb-3">
                        <input type="email" name="email" class="form-control" placeholder="Email (opcional)">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                    <button type="submit" class="btn btn-primary">Guardar</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>