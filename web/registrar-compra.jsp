<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map, logica.PermisoServicio, java.util.List, modelo.Cliente, logica.ClienteServicio, modelo.Configuracion, logica.ConfiguracionServicio, modelo.FondoComprador, logica.FondoServicio, java.math.BigDecimal" %>
<%
if (session.getAttribute("usuarioId") == null) {
    response.sendRedirect("login.jsp");
    return;
}
int usuarioId = Integer.parseInt(session.getAttribute("usuarioId").toString());

if (usuarioId <= 0) {
    request.setAttribute("error", "Error: Usuario no válido");
    request.getRequestDispatcher("registrar-compra.jsp").forward(request, response);
    return;
}

PermisoServicio permisoServicio = new PermisoServicio();
Map<String, Boolean> permisos = permisoServicio.obtenerPermisosPorUsuario(usuarioId);

if (!permisos.getOrDefault("compra", false)) {
    response.sendRedirect("menu-principal.jsp");
    return;
}

String rol = (String) session.getAttribute("rol");
ClienteServicio clienteServicio = new ClienteServicio();
List<Cliente> clientes = clienteServicio.obtenerTodos();

ConfiguracionServicio servicioMoneda = new ConfiguracionServicio();
Configuracion datosMoneda = servicioMoneda.obtenerConfiguracion();
String simboloMoneda = (datosMoneda != null && datosMoneda.getMonedaSimbolo() != null) 
    ? datosMoneda.getMonedaSimbolo() 
    : "$";

FondoServicio fondoServicio = new FondoServicio();
FondoComprador fondo = fondoServicio.obtenerPorUsuario(usuarioId);
BigDecimal saldoDisponible = fondo != null ? fondo.getSaldoDisponible() : BigDecimal.ZERO;
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrar Compra</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container mt-4">
    <div class="card shadow">
        <div class="card-header bg-primary text-white">
            <h4 class="d-inline">Registrar Compra de Oro</h4>
            <div class="float-end">
                <small class="me-2">👤 <%= session.getAttribute("nombreUsuario") %> 
                    (<%="admin".equals(rol) ? "Administrador" : "Comprador"%>)
                </small>
                <a href="menu-principal.jsp" class="btn btn-sm btn-outline-light">Menú Principal</a>
            </div>
        </div>
        <div class="card-body">
            <div class="alert alert-info text-center mb-3">
                <i class="fas fa-wallet me-1"></i> 
                Saldo disponible: <%= simboloMoneda %><%= saldoDisponible.setScale(2, BigDecimal.ROUND_HALF_UP) %>
                <% if (fondo != null) { %>
                    <br><small>Asignado: <%= simboloMoneda %><%= fondo.getMontoAsignado().setScale(2, BigDecimal.ROUND_HALF_UP) %></small>
                <% } %>
            </div>

            <% if ("cliente_ok".equals(request.getParameter("msg"))) { %>
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                Cliente registrado.
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <% } %>

            <form action="registrar-compra" method="post">
                <div class="mb-3">
                    <label class="form-label fw-bold">Cliente:</label>
                    <select name="clienteId" class="form-control" required>
                        <option value="">-- Elija un cliente --</option>
                        <% for (Cliente c : clientes) { %>
                        <option value="<%= c.getId() %>"><%= c.getNombre() %></option>
                        <% } %>
                    </select>
                </div>

                <%
                    if ("admin".equals(rol) || permisos.getOrDefault("clientes", false)) {
                %>
                <div class="text-end mb-2">
                    <button type="button" class="btn btn-sm btn-outline-primary" data-bs-toggle="modal" data-bs-target="#nuevoClienteModal">
                        + Nuevo Cliente
                    </button>
                </div>
                <%
                    }
                %>

                <div class="row">
                    <div class="col-md-3 mb-3">
                        <label class="form-label">Peso (gramos)</label>
                        <input type="number" step="0.001" id="peso" name="peso" class="form-control" required min="0.001" placeholder="10.500" oninput="calcular()">
                    </div>
                    <div class="col-md-3 mb-3">
                        <label class="form-label">Kilate</label>
                        <input type="number" step="0.01" id="kilate" name="kilate" class="form-control" required min="0.01" placeholder="120" oninput="calcular()">
                    </div>
                    <div class="col-md-3 mb-3">
                        <label class="form-label">Punto</label>
                        <input type="number" step="0.01" id="punto" name="punto" class="form-control" required min="0.01" placeholder="10" oninput="calcular()">
                    </div>
                    <div class="col-md-3 mb-3">
                        <label class="form-label">Precio por Gramo</label>
                        <input type="text" id="precioGramo" class="form-control" value="<%= simboloMoneda %>0.00" disabled>
                    </div>
                    <div class="col-md-3 mb-3">
                        <label class="form-label">Total</label>
                        <input type="text" id="total" class="form-control fw-bold" value="<%= simboloMoneda %>0.00" disabled>
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label">Observaciones</label>
                    <textarea name="observaciones" class="form-control" rows="2"></textarea>
                </div>

                <button type="submit" class="btn btn-success w-100">Registrar Compra</button>
                <small class="text-muted">
                    ⚠️ <strong>Precio por gramo</strong> = Kilate × Punto<br>
                    ⚠️ <strong>Total</strong> = Peso × Precio → ¡Calculado automáticamente!
                </small>
            </form>
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
</div>
<!-- Solo para prueba -->
<div class="alert alert-warning">
    Usuario ID actual: <%= usuarioId %>
</div>
<script>
function calcular() {
    const peso = parseFloat(document.getElementById('peso').value) || 0;
    const kilate = parseFloat(document.getElementById('kilate').value) || 0;
    const punto = parseFloat(document.getElementById('punto').value) || 0;
    const precioGramo = kilate * punto;
    const total = peso * precioGramo;
    const simbolo = '<%= simboloMoneda %>';
    document.getElementById('precioGramo').value = simbolo + precioGramo.toFixed(2);
    document.getElementById('total').value = simbolo + total.toFixed(2);
}
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>