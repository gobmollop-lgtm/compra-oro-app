<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%
if (session.getAttribute("usuarioId") == null) {
    response.sendRedirect("login.jsp");
    return;
}
String rol = (String) session.getAttribute("rol");
String nombreUsuario = (String) session.getAttribute("nombreUsuario");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Menú Principal - Compra de Oro</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .menu-card {
            transition: transform 0.2s;
        }
        .menu-card:hover {
            transform: translateY(-5px);
        }
        .role-badge {
            font-size: 0.9em;
        }
    </style>
</head>
<body class="bg-light">
<div class="container mt-4">
    <!-- Encabezado -->
    <div class="card mb-4 shadow">
        <div class="card-header bg-primary text-white">
            <div class="d-flex justify-content-between align-items-center">
                <h4 class="mb-0"><i class="fas fa-coins me-2"></i>Compra de Oro - Menú Principal</h4>
                <div class="text-end">
                    <div><i class="fas fa-user me-1"></i> <%= nombreUsuario %></div>
                    <span class="badge bg-<%= "admin".equals(rol) ? "success" : "info" %> role-badge">
                        <%= "admin".equals(rol) ? "Administrador" : "Comprador" %>
                    </span>
                </div>
            </div>
        </div>
        <div class="card-body">
            <div class="row g-4">
                <!-- Opción: Registrar Compra -->
                <div class="col-md-6 col-lg-4">
                    <a href="registrar-compra.jsp" class="text-decoration-none">
                        <div class="card menu-card h-100 border-primary">
                            <div class="card-body text-center">
                                <div class="bg-primary text-white rounded-circle p-3 d-inline-block mb-3">
                                    <i class="fas fa-shopping-cart fa-2x"></i>
                                </div>
                                <h5 class="card-title">Registrar Compra</h5>
                                <p class="card-text text-muted">Ingresa una nueva transacción de oro</p>
                            </div>
                        </div>
                    </a>
                </div>

                <!-- Opción: Historial de Compras -->
                <div class="col-md-6 col-lg-4">
                    <a href="lista-compras.jsp" class="text-decoration-none">
                        <div class="card menu-card h-100 border-success">
                            <div class="card-body text-center">
                                <div class="bg-success text-white rounded-circle p-3 d-inline-block mb-3">
                                    <i class="fas fa-list-alt fa-2x"></i>
                                </div>
                                <h5 class="card-title">Historial de Compras</h5>
                                <p class="card-text text-muted">Consulta todas las transacciones</p>
                            </div>
                        </div>
                    </a>
                </div>

                <!-- Opción: Gestión de Usuarios (solo admin) -->
                <%
                    if ("admin".equals(rol)) {
                %>
                <div class="col-md-6 col-lg-4">
                    <a href="gestion-usuarios.jsp" class="text-decoration-none">
                        <div class="card menu-card h-100 border-warning">
                            <div class="card-body text-center">
                                <div class="bg-warning text-dark rounded-circle p-3 d-inline-block mb-3">
                                    <i class="fas fa-users fa-2x"></i>
                                </div>
                                <h5 class="card-title">Gestión de Usuarios</h5>
                                <p class="card-text text-muted">Administra usuarios del sistema</p>
                            </div>
                        </div>
                    </a>
                </div>
                <%
                    }
                %>

                <!-- Opción: Clientes (solo admin) -->
                <%
                    if ("admin".equals(rol)) {
                %>
                <div class="col-md-6 col-lg-4">
                    <a href="lista-clientes.jsp" class="text-decoration-none">
                        <div class="card menu-card h-100 border-info">
                            <div class="card-body text-center">
                                <div class="bg-info text-white rounded-circle p-3 d-inline-block mb-3">
                                    <i class="fas fa-user-friends fa-2x"></i>
                                </div>
                                <h5 class="card-title">Clientes</h5>
                                <p class="card-text text-muted">Ver y gestionar clientes</p>
                            </div>
                        </div>
                    </a>
                </div>
                <%
                    }
                %>

                <!-- Opción: Configuración (solo admin) -->
                <%
                    if ("admin".equals(rol)) {
                %>
                <div class="col-md-6 col-lg-4">
                    <a href="configuracion.jsp" class="text-decoration-none">
                        <div class="card menu-card h-100 border-warning">
                            <div class="card-body text-center">
                                <div class="bg-warning text-dark rounded-circle p-3 d-inline-block mb-3">
                                    <i class="fas fa-cog fa-2x"></i>
                                </div>
                                <h5 class="card-title">Configuración</h5>
                                <p class="card-text text-muted">Personaliza el sistema</p>
                            </div>
                        </div>
                    </a>
                </div>
                <%
                    }
                %>
            </div>
        </div>
        <div class="card-footer text-center">
            <a href="logout" class="btn btn-outline-danger">
                <i class="fas fa-sign-out-alt me-1"></i> Cerrar Sesión
            </a>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>