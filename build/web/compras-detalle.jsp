<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, modelo.Compra, logica.CompraServicio, modelo.Usuario, logica.UsuarioServicio, modelo.Configuracion, logica.ConfiguracionServicio, java.util.Map, logica.PermisoServicio, java.math.BigDecimal, java.math.RoundingMode, java.text.DecimalFormat, java.text.DecimalFormatSymbols" %>
<%
if (session.getAttribute("usuarioId") == null) {
    response.sendRedirect("login.jsp");
    return;
}

int usuarioIdSesion = Integer.parseInt(session.getAttribute("usuarioId").toString());
PermisoServicio permisoServicio = new PermisoServicio();
Map<String, Boolean> permisos = permisoServicio.obtenerPermisosPorUsuario(usuarioIdSesion);

if (!permisos.getOrDefault("colaboradores", false)) {
    response.sendRedirect("menu-principal.jsp");
    return;
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

CompraServicio compraServicio = new CompraServicio();
List<Compra> compras = compraServicio.obtenerPorUsuario(usuarioIdDetalle);

ConfiguracionServicio configServicioMoneda = new ConfiguracionServicio();
Configuracion configuracionMoneda = configServicioMoneda.obtenerConfiguracion();
String simboloMoneda = (configuracionMoneda != null && configuracionMoneda.getMonedaSimbolo() != null) 
    ? configuracionMoneda.getMonedaSimbolo() 
    : "C$";

// Calcular totales generales
BigDecimal totalPeso = BigDecimal.ZERO;
BigDecimal totalGeneral = BigDecimal.ZERO;

for (Compra c : compras) {
    totalPeso = totalPeso.add(c.getPesoGramos());
    totalGeneral = totalGeneral.add(c.getTotal());
}

// Formato con separador de miles
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
        .btn-imprimir {
            background-color: #6c757d;
            color: white;
            border: none;
        }
        .btn-imprimir:hover {
            background-color: #5a6268;
        }

        /* Estilos para impresión */
        @media print {
            body * {
                visibility: hidden;
            }
            #contenido-imprimible, #contenido-imprimible * {
                visibility: visible;
            }
            #contenido-imprimible {
                position: absolute;
                left: 0;
                top: 0;
                width: 100%;
            }
            .no-print {
                display: none !important;
            }
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
            <!-- Botones: Imprimir + Volver al Menú Principal + Volver al Resumen -->
            <div class="d-flex justify-content-between mb-3 no-print">
                <div>
                    <button class="btn btn-sm btn-imprimir me-2" onclick="window.print()">
                        <i class="fas fa-print me-1"></i>Imprimir Reporte
                    </button>
                    <a href="menu-principal.jsp" class="btn btn-sm btn-outline-secondary">
                        <i class="fas fa-home me-1"></i>Volver al Menú Principal
                    </a>
                </div>
                <a href="compras-por-usuario.jsp" class="btn btn-outline-primary btn-sm">
                    <i class="fas fa-arrow-left me-1"></i>Volver al Resumen
                </a>
            </div>

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
                                </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            <% } else { %>
                <div class="text-center py-4">
                    <i class="fas fa-shopping-cart fa-3x text-muted mb-3"></i>
                    <p class="text-muted">No hay compras registradas para este usuario.</p>
                </div>
            <% } %>
        </div>
        <!-- Eliminado el botón "Volver al Resumen" del footer -->
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>