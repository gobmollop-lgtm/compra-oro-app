<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, modelo.Compra, logica.CompraServicio, modelo.Usuario, logica.UsuarioServicio, modelo.Configuracion, logica.ConfiguracionServicio, java.util.Map, logica.PermisoServicio, java.math.BigDecimal, java.math.RoundingMode, java.text.DecimalFormat, java.text.DecimalFormatSymbols, java.time.LocalDate, java.util.ArrayList" %>
<%
if (session.getAttribute("usuarioId") == null) {
    response.sendRedirect("login.jsp");
    return;
}

int usuarioIdSesion = Integer.parseInt(session.getAttribute("usuarioId").toString());
String rolSesion = (String) session.getAttribute("rol");

// Si es admin, dar todos los permisos
boolean esAdmin = "admin".equals(rolSesion);
Map<String, Boolean> permisos = new java.util.HashMap<>();
if (esAdmin) {
    permisos.put("colaboradores", true);
    permisos.put("eliminar_compras", true);
} else {
    PermisoServicio permisoServicio = new PermisoServicio();
    permisos = permisoServicio.obtenerPermisosPorUsuario(usuarioIdSesion);
    if (!permisos.getOrDefault("colaboradores", false)) {
        response.sendRedirect("menu-principal.jsp");
        return;
    }
}

String usuarioIdParam = request.getParameter("usuarioId");
if (usuarioIdParam == null || usuarioIdParam.isEmpty()) {
    response.sendRedirect("compras-por-usuario.jsp");
    return;
}

int usuarioIdDetalle = Integer.parseInt(usuarioIdParam);

UsuarioServicio usuarioServicio = new UsuarioServicio();
Usuario usuarioDetalle = usuarioServicio.obtenerPorId(usuarioIdDetalle);
if (usuarioDetalle == null) {
    response.sendRedirect("compras-por-usuario.jsp");
    return;
}

// Obtener parámetros de filtro
String fechaInicioStr = request.getParameter("fechaInicio");
String fechaFinStr = request.getParameter("fechaFin");
String estadoFiltro = request.getParameter("estado");

// Determinar si es la primera carga
boolean esPrimeraCarga = (fechaInicioStr == null || fechaInicioStr.isEmpty()) && 
                         (fechaFinStr == null || fechaFinStr.isEmpty()) && 
                         (estadoFiltro == null);

// Si es la primera carga, usar "Pendiente" por defecto
if (esPrimeraCarga) {
    estadoFiltro = "Pendiente";
}

LocalDate fechaInicio = null, fechaFin = null;
if (fechaInicioStr != null && !fechaInicioStr.trim().isEmpty()) {
    fechaInicio = LocalDate.parse(fechaInicioStr);
}
if (fechaFinStr != null && !fechaFinStr.trim().isEmpty()) {
    fechaFin = LocalDate.parse(fechaFinStr);
}

CompraServicio compraServicio = new CompraServicio();
List<Compra> comprasTodas = compraServicio.obtenerPorUsuario(usuarioIdDetalle);

List<Compra> compras = new ArrayList<>();
for (Compra c : comprasTodas) {
    if (c.getFecha() == null) continue;
    LocalDate fechaCompra = c.getFecha().toInstant()
        .atZone(java.time.ZoneId.systemDefault())
        .toLocalDate();
    if (fechaInicio != null && fechaCompra.isBefore(fechaInicio)) continue;
    if (fechaFin != null && fechaCompra.isAfter(fechaFin)) continue;

    // Filtrar por estado
    if ("Pendiente".equals(estadoFiltro)) {
        if (!"Pendiente".equals(c.getEstado())) continue;
    } else if ("Recibido".equals(estadoFiltro)) {
        if (!"Recibido".equals(c.getEstado())) continue;
    }
    compras.add(c);
}

ConfiguracionServicio configServicioMoneda = new ConfiguracionServicio();
Configuracion configuracionMoneda = configServicioMoneda.obtenerConfiguracion();
String simboloMoneda = (configuracionMoneda != null && configuracionMoneda.getMonedaSimbolo() != null) 
    ? configuracionMoneda.getMonedaSimbolo() 
    : "C$";

BigDecimal totalPeso = BigDecimal.ZERO;
BigDecimal totalGeneral = BigDecimal.ZERO;
for (Compra c : compras) {
    totalPeso = totalPeso.add(c.getPesoGramos());
    totalGeneral = totalGeneral.add(c.getTotal());
}

DecimalFormat df = new DecimalFormat("#,##0.00");
df.setDecimalFormatSymbols(new DecimalFormatSymbols(java.util.Locale.US));
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Compras de <%= usuarioDetalle.getNombre() %></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .detalle-header {
            background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
            color: white;
        }
        .table th { font-weight: 600; }
        .total-row {
            font-weight: bold;
            background-color: #f8f9fa;
        }
        .btn-imprimir { background-color: #6c757d; color: white; border: none; padding: 2px 6px; font-size: 0.75rem; }
        .btn-imprimir:hover { background-color: #5a6268; }
        .btn-excel { background-color: #28a745; color: white; border: none; padding: 2px 6px; font-size: 0.75rem; }
        .btn-excel:hover { background-color: #218838; }
        .btn-ver-foto { padding: 2px 6px; font-size: 0.75rem; min-width: 50px; }
        .btn-recibido { padding: 2px 6px; font-size: 0.75rem; }
        .btn-eliminar { padding: 2px 6px; font-size: 0.75rem; }
        .form-control-sm { font-size: 0.75rem; padding: 0.25rem 0.5rem; height: calc(1.5em + 0.5rem + 2px); }
        .form-select-sm { font-size: 0.75rem; padding: 0.25rem 0.5rem; height: calc(1.5em + 0.5rem + 2px); }
        .input-group-sm > .form-control,
        .input-group-sm > .form-select,
        .input-group-sm > .input-group-text {
            font-size: 0.75rem;
            padding: 0.25rem 0.5rem;
            height: calc(1.5em + 0.5rem + 2px);
        }
        @media print {
            body * { visibility: hidden; }
            #contenido-imprimible, #contenido-imprimible * { visibility: visible; }
            #contenido-imprimible { position: absolute; left: 0; top: 0; width: 100%; }
            .no-print { display: none !important; }
        }
    </style>
</head>
<body class="bg-light">
<div class="container mt-4">
    <div class="card shadow">
        <div class="card-header detalle-header">
            <h4 class="mb-0">
                <i class="fas fa-shopping-cart me-2"></i>
                Compras de <%= usuarioDetalle.getNombre() %>
                <% if ("admin".equals(usuarioDetalle.getRol())) { %>
                    <span class="badge bg-success ms-2">Administrador</span>
                <% } else { %>
                    <span class="badge bg-info ms-2">Comprador</span>
                <% } %>
            </h4>
        </div>
        <div class="card-body">

            <!-- Formulario de filtros -->
            <form method="get" class="row g-1 mb-3 no-print align-items-center">
                <input type="hidden" name="usuarioId" value="<%= usuarioIdDetalle %>">
                <div class="col-auto">
                    <label class="form-label mb-0">Fecha Inicio</label>
                </div>
                <div class="col-auto">
                    <input type="date" class="form-control form-control-sm" name="fechaInicio" value="<%= (fechaInicioStr != null ? fechaInicioStr : "") %>">
                </div>
                <div class="col-auto">
                    <label class="form-label mb-0">Fecha Fin</label>
                </div>
                <div class="col-auto">
                    <input type="date" class="form-control form-control-sm" name="fechaFin" value="<%= (fechaFinStr != null ? fechaFinStr : "") %>">
                </div>
                <div class="col-auto">
                    <label class="form-label mb-0">Estado</label>
                </div>
                <div class="col-auto">
                    <select class="form-select form-select-sm" name="estado" onchange="this.form.submit()">
                        <option value="Pendiente" <%= "Pendiente".equals(estadoFiltro) ? "selected" : "" %>>Pendiente</option>
                        <option value="Recibido" <%= "Recibido".equals(estadoFiltro) ? "selected" : "" %>>Recibido</option>
                        <option value="" <%= (estadoFiltro == null || "".equals(estadoFiltro)) ? "selected" : "" %>>Todos</option>
                    </select>
                </div>
                <div class="col-auto">
                    <button type="submit" class="btn btn-primary btn-sm">
                        <i class="fas fa-filter me-1"></i>Filtrar
                    </button>
                </div>
                <div class="col-auto">
                    <a href="?usuarioId=<%= usuarioIdDetalle %>" class="btn btn-outline-secondary btn-sm px-2">
                        Limpiar
                    </a>
                </div>
            </form>

            <!-- Botones de acción (fuera del formulario de filtros) -->
            <div class="d-flex flex-wrap gap-2 mb-3 no-print">
                <button class="btn btn-sm btn-secondary" onclick="window.print()">
                    <i class="fas fa-print me-1"></i>Imprimir
                </button>
                <button class="btn btn-sm btn-success" onclick="exportarAExcel()">
                    <i class="fas fa-file-excel me-1"></i>Exportar
                </button>
                <a href="menu-principal.jsp" class="btn btn-sm btn-outline-secondary">
                    <i class="fas fa-home me-1"></i>Menú
                </a>
                <a href="compras-por-usuario.jsp" class="btn btn-sm btn-outline-primary">
                    <i class="fas fa-arrow-left me-1"></i>Resumen
                </a>
            </div>

            <!-- Formulario oculto para exportar Excel -->
            <form id="formExportarExcel" method="post" action="ExportarComprasExcelServlet" style="display:none;">
                <input type="hidden" name="tablaHTML" id="tablaHTML">
                <input type="hidden" name="nombreUsuario" id="nombreUsuario" value="<%= usuarioDetalle.getNombre() %>">
            </form>

            <% 
            String error = request.getParameter("error");
            String exito = request.getParameter("exito");
            if (error != null) { %>
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <%= error %>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            <% } else if ("1".equals(exito)) { %>
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    Estado actualizado correctamente.
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            <% } else if ("2".equals(exito)) { %>
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    Compra eliminada correctamente.
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            <% } %>

            <% if (compras != null && !compras.isEmpty()) { %>
                <div id="contenido-imprimible">
                    <div class="table-responsive">
                        <table class="table table-striped">
                            <thead class="table-light">
                                <tr>
                                    <th>Cliente</th>
                                    <th>Peso</th>
                                    <th>Kilate</th>
                                    <th>Punto</th>
                                    <th>Precio/g</th>
                                    <th>Total</th>
                                    <th>Fecha</th>
                                    <th>Estado</th>
                                    <th>Foto</th>
                                    <% if (esAdmin || permisos.getOrDefault("eliminar_compras", false)) { %>
                                        <th>Acciones</th>
                                    <% } %>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (Compra c : compras) { %>
                                <tr>
                                    <td><%= c.getNombreCliente() %></td>
                                    <td><%= c.getPesoGramos().setScale(3, RoundingMode.HALF_UP) %></td>
                                    <td><%= c.getKilate().setScale(2, RoundingMode.HALF_UP) %></td>
                                    <td><%= c.getPunto().setScale(2, RoundingMode.HALF_UP) %></td>
                                    <td><%= simboloMoneda %><%= df.format(c.getPrecioGramo().setScale(2, RoundingMode.HALF_UP)) %></td>
                                    <td class="fw-bold"><%= simboloMoneda %><%= df.format(c.getTotal().setScale(2, RoundingMode.HALF_UP)) %></td>
                                    <td><%= c.getFecha() %></td>
                                    <td>
                                        <%= c.getEstado() %>
                                        <% if ("Pendiente".equals(c.getEstado())) { %>
                                            <form action="ActualizarEstadoCompraServlet" method="post" style="display:inline; margin-left:2px;" onsubmit="return confirm('¿Marcar como recibido esta compra?');">
                                                <input type="hidden" name="compraId" value="<%= c.getId() %>" />
                                                <input type="hidden" name="usuarioId" value="<%= usuarioIdDetalle %>" />
                                                <input type="hidden" name="fechaInicio" value="<%= (fechaInicioStr != null ? fechaInicioStr : "") %>" />
                                                <input type="hidden" name="fechaFin" value="<%= (fechaFinStr != null ? fechaFinStr : "") %>" />
                                                <input type="hidden" name="estado" value="<%= (estadoFiltro != null ? estadoFiltro : "") %>" />
                                                <button type="submit" class="btn btn-xs btn-success btn-recibido">
                                                    ✓ Recibido
                                                </button>
                                            </form>
                                        <% } %>
                                    </td>
                                    <td>
                                        <% if (c.getRutaFoto() != null && !c.getRutaFoto().trim().isEmpty()) { %>
                                            <a href="<%= request.getContextPath() + "/" + c.getRutaFoto() %>" target="_blank" class="btn btn-xs btn-outline-primary btn-ver-foto">Ver</a>
                                        <% } else { %>
                                            <span class="text-muted">-</span>
                                        <% } %>
                                    </td>
                                    <% if (esAdmin || permisos.getOrDefault("eliminar_compras", false)) { %>
                                        <td>
                                            <form action="EliminarCompraServlet" method="post" style="display:inline;" onsubmit="return confirm('¿Eliminar compra de <%= c.getNombreCliente() %> del <%= c.getFecha() %>? Esta acción es irreversible.');">
                                                <input type="hidden" name="compraId" value="<%= c.getId() %>" />
                                                <input type="hidden" name="usuarioId" value="<%= usuarioIdDetalle %>" />
                                                <input type="hidden" name="fechaInicio" value="<%= (fechaInicioStr != null ? fechaInicioStr : "") %>" />
                                                <input type="hidden" name="fechaFin" value="<%= (fechaFinStr != null ? fechaFinStr : "") %>" />
                                                <input type="hidden" name="estado" value="<%= (estadoFiltro != null ? estadoFiltro : "") %>" />
                                                <button type="submit" class="btn btn-xs btn-danger btn-eliminar">
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </form>
                                        </td>
                                    <% } %>
                                </tr>
                                <% } %>
                            </tbody>
                            <tfoot>
                                <tr class="total-row">
                                    <td colspan="1" class="text-end"><strong>Totales:</strong></td>
                                    <td><%= totalPeso.setScale(3, RoundingMode.HALF_UP) %></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td><%= simboloMoneda %><%= df.format(totalGeneral.setScale(2, RoundingMode.HALF_UP)) %></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <% if (esAdmin || permisos.getOrDefault("eliminar_compras", false)) { %>
                                        <td></td>
                                    <% } %>
                                </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            <% } else { %>
                <div class="text-center py-4">
                    <i class="fas fa-shopping-cart fa-3x text-muted mb-3"></i>
                    <p class="text-muted">
                        <% if ("Pendiente".equals(estadoFiltro)) { %>
                            No hay compras pendientes.
                        <% } else if ("Recibido".equals(estadoFiltro)) { %>
                            No hay compras recibidas.
                        <% } else { %>
                            No hay compras.
                        <% } %>
                    </p>
                </div>
            <% } %>
        </div>
    </div>
</div>

<script>
function exportarAExcel() {
    const table = document.querySelector('#contenido-imprimible table');
    if (!table) {
        alert('No se encontró la tabla para exportar.');
        return;
    }
    document.getElementById('tablaHTML').value = table.outerHTML;
    document.getElementById('formExportarExcel').submit();
}
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>                                