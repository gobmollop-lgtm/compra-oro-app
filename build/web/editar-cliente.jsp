<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="modelo.Cliente, logica.ClienteServicio" %>
<%
if (session.getAttribute("usuarioId") == null || !"admin".equals(session.getAttribute("rol"))) {
    response.sendRedirect("login.jsp");
    return;
}
String idParam = request.getParameter("id");
if (idParam == null || idParam.trim().isEmpty()) {
    response.sendRedirect("clientes.jsp?error=id_faltante");
    return;
}
int id = Integer.parseInt(idParam);
ClienteServicio servicio = new ClienteServicio();
Cliente cliente = servicio.obtenerPorId(id);
if (cliente == null) {
    response.sendRedirect("clientes.jsp?error=cliente_no_existe");
    return;
}
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar Cliente</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container mt-4">
    <h3>Editar Cliente</h3>
    <form action="actualizar-cliente" method="post">
        <input type="hidden" name="id" value="<%= cliente.getId() %>">
        <div class="mb-3">
            <label>Nombre:</label>
            <input type="text" name="nombre" class="form-control" value="<%= cliente.getNombre() %>" required>
        </div>
        <div class="mb-3">
            <label>Teléfono:</label>
            <input type="text" name="telefono" class="form-control" value="<%= cliente.getTelefono() != null ? cliente.getTelefono() : "" %>">
        </div>
        <div class="mb-3">
            <label>Email:</label>
            <input type="email" name="email" class="form-control" value="<%= cliente.getEmail() != null ? cliente.getEmail() : "" %>">
        </div>
        <button type="submit" class="btn btn-success">Guardar Cambios</button>
        <a href="clientes.jsp" class="btn btn-secondary">Cancelar</a>
    </form>
</div>
</body>
</html>