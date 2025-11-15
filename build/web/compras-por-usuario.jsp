<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, modelo.Usuario, modelo.CompraResumen, logica.UsuarioServicio, logica.CompraServicio, logica.FondoServicio, java.util.Map, logica.PermisoServicio, java.math.BigDecimal, modelo.FondoComprador" %>
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

UsuarioServicio usuarioServicio = new UsuarioServicio();
List<Usuario> usuarios = usuarioServicio.obtenerTodosLosUsuarios();

CompraServicio compraServicio = new CompraServicio();
FondoServicio fondoServicio = new FondoServicio();
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Rendimiento de Compradores</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .card-header {
            background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
            color: white;
        }
        .badge-admin { background-color: #28a745; }
        .badge-comprador { background-color: #17a2b8; }
        .resumen-card {
            border-left: 4px solid #2575fc;
            transition: transform 0.2s;
        }
        .resumen-card:hover {
            transform: translateY(-3px);
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        .table th { font-weight: 600; }
        .btn-detalle {
            font-size: 0.85rem;
            padding: 0.3rem 0.6rem;
        }
    </style>
</head>
<body class="bg-light">
<div class="container mt-4">
    <div class="card shadow">
        <div class="card-header">
            <h4 class="mb-0"><i class="fas fa-chart-line me-2"></i>Rendimiento de Compradores</h4>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead class="table-light">
                        <tr>
                            <th>Usuario</th>
                            <th>Rol</th>
                            <th>Compras</th>
                            <th>Total Invertido</th>
                            <th>Monto Asignado</th>
                            <th>Saldo Disponible</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Usuario u : usuarios) { 
                            CompraResumen resumen = compraServicio.obtenerResumenPorUsuario(u.getId());
                            FondoComprador fondo = fondoServicio.obtenerPorUsuario(u.getId());
                            BigDecimal asignado = fondo != null ? fondo.getMontoAsignado() : BigDecimal.ZERO;
                            BigDecimal usado = fondo != null ? fondo.getMontoUsado() : BigDecimal.ZERO;
                            BigDecimal saldo = asignado.subtract(usado);
                        %>
                        <tr class="resumen-card">
                            <td>
                                <div class="d-flex align-items-center">
                                    <div class="bg-primary text-white rounded-circle p-2 me-2">
                                        <i class="fas fa-user"></i>
                                    </div>
                                    <div>
                                        <div><%= u.getNombre() %></div>
                                        <small class="text-muted">@<%= u.getUsuario() %></small>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <% if ("admin".equals(u.getRol())) { %>
                                    <span class="badge badge-admin">Administrador</span>
                                <% } else { %>
                                    <span class="badge badge-comprador">Comprador</span>
                                <% } %>
                            </td>
                            <td><%= resumen.getTotalCompras() %></td>
                            <td class="fw-bold text-success"><%= logica.FondoServicio.formatearMonto(resumen.getTotalInvertido()) %></td>
                            <td><%= logica.FondoServicio.formatearMonto(asignado) %></td>
                            <td><%= logica.FondoServicio.formatearMonto(saldo) %></td>
                            <td>
                                <a href="compras-detalle.jsp?usuarioId=<%= u.getId() %>" class="btn btn-sm btn-outline-primary btn-detalle">
                                    <i class="fas fa-eye me-1"></i>Ver
                                </a>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="card-footer text-end">
            <a href="menu-principal.jsp" class="btn btn-outline-secondary btn-sm">
                <i class="fas fa-arrow-left me-1"></i>Volver al Menú
            </a>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>