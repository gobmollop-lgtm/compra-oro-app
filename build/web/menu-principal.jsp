<!-- Nuevo diceño de Menu con 1x4-->
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map, logica.PermisoServicio" %>
<%
if (session.getAttribute("usuarioId") == null) {
    response.sendRedirect("login.jsp");
    return;
}

int usuarioId = Integer.parseInt(session.getAttribute("usuarioId").toString());
PermisoServicio permisoServicio = new PermisoServicio();
Map<String, Boolean> permisos = permisoServicio.obtenerPermisosPorUsuario(usuarioId);

String nombreUsuario = (String) session.getAttribute("nombreUsuario");
String rol = (String) session.getAttribute("rol");
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
            height: 100%;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            text-align: center;
            padding: 0.75rem;
            border-radius: 0.5rem;
            border: 2px solid transparent;
        }
        .menu-card:hover {
            transform: translateY(-3px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        .menu-card i {
            font-size: 1.75rem;
            margin-bottom: 0.5rem;
        }
        .menu-card h5 {
            font-size: 0.9rem;
            margin-bottom: 0.25rem;
            font-weight: 600;
        }
        .menu-card p {
            font-size: 0.75rem;
            margin: 0;
            color: #6c757d;
        }
        .role-badge {
            font-size: 0.75rem;
        }
        .card-header .btn-outline-danger {
            font-size: 0.85rem;
            padding: 0.3rem 0.6rem;
        }
        .card-body .row.g-4 > div {
            padding: 0.5rem;
        }
        /* Estilos para móviles */
        @media (max-width: 768px) {
            .menu-card i { font-size: 1.5rem; }
            .menu-card h5 { font-size: 0.85rem; }
            .menu-card p { font-size: 0.7rem; }
        }
    </style>
</head>
<body class="bg-light">
<div class="container mt-3">
    <div class="card mb-4 shadow">
        <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
            <h5 class="mb-0"><i class="fas fa-coins me-2"></i>Compra de Oro - Menú Principal</h5>
            <div class="text-end">
                <div><i class="fas fa-user me-1"></i> <%= nombreUsuario %></div>
                <span class="badge bg-<%= "admin".equals(rol) ? "success" : "info" %> role-badge">
                    <%= "admin".equals(rol) ? "Administrador" : "Comprador" %>
                </span>
            </div>
        </div>
        <div class="card-body">
            <div class="row g-3">
                <!-- Registrar Compra -->
                <div class="col-6 col-md-6 col-lg-3">
                    <% if (permisos.getOrDefault("compra", false)) { %>
                    <a href="registrar-compra.jsp" class="text-decoration-none">
                        <div class="card menu-card h-100 border-primary">
                            <div class="bg-primary text-white rounded-circle p-2 d-inline-block mb-2">
                                <i class="fas fa-shopping-cart"></i>
                            </div>
                            <h5>Registrar Compra</h5>
                            <p>Ingresa nueva transacción</p>
                        </div>
                    </a>
                    <% } else { %>
                    <div class="card menu-card h-100 border-secondary opacity-50">
                        <div class="bg-secondary text-white rounded-circle p-2 d-inline-block mb-2">
                            <i class="fas fa-shopping-cart"></i>
                        </div>
                        <h5>Registrar Compra</h5>
                        <p>Sin permiso</p>
                    </div>
                    <% } %>
                </div>

                <!-- Historial de Compras -->
                <div class="col-6 col-md-6 col-lg-3">
                    <% if (permisos.getOrDefault("historial", false)) { %>
                    <a href="lista-compras.jsp" class="text-decoration-none">
                        <div class="card menu-card h-100 border-success">
                            <div class="bg-success text-white rounded-circle p-2 d-inline-block mb-2">
                                <i class="fas fa-list-alt"></i>
                            </div>
                            <h5>Historial de Compras</h5>
                            <p>Consulta todas las transacciones</p>
                        </div>
                    </a>
                    <% } else { %>
                    <div class="card menu-card h-100 border-secondary opacity-50">
                        <div class="bg-secondary text-white rounded-circle p-2 d-inline-block mb-2">
                            <i class="fas fa-list-alt"></i>
                        </div>
                        <h5>Historial de Compras</h5>
                        <p>Sin permiso</p>
                    </div>
                    <% } %>
                </div>

                <!-- Clientes -->
                <div class="col-6 col-md-6 col-lg-3">
                    <% if (permisos.getOrDefault("clientes", false)) { %>
                    <a href="clientes.jsp" class="text-decoration-none">
                        <div class="card menu-card h-100 border-info">
                            <div class="bg-info text-white rounded-circle p-2 d-inline-block mb-2">
                                <i class="fas fa-user-friends"></i>
                            </div>
                            <h5>Clientes</h5>
                            <p>Ver y gestionar clientes</p>
                        </div>
                    </a>
                    <% } else { %>
                    <div class="card menu-card h-100 border-secondary opacity-50">
                        <div class="bg-secondary text-white rounded-circle p-2 d-inline-block mb-2">
                            <i class="fas fa-user-friends"></i>
                        </div>
                        <h5>Clientes</h5>
                        <p>Sin permiso</p>
                    </div>
                    <% } %>
                </div>

                <!-- Gestión de Usuarios -->
                <div class="col-6 col-md-6 col-lg-3">
                    <% if (permisos.getOrDefault("usuarios", false)) { %>
                    <a href="gestion-usuarios.jsp" class="text-decoration-none">
                        <div class="card menu-card h-100 border-warning">
                            <div class="bg-warning text-dark rounded-circle p-2 d-inline-block mb-2">
                                <i class="fas fa-users-gear"></i>
                            </div>
                            <h5>Gestión de Usuarios</h5>
                            <p>Administra usuarios del sistema</p>
                        </div>
                    </a>
                    <% } else { %>
                    <div class="card menu-card h-100 border-secondary opacity-50">
                        <div class="bg-secondary text-white rounded-circle p-2 d-inline-block mb-2">
                            <i class="fas fa-users-gear"></i>
                        </div>
                        <h5>Gestión de Usuarios</h5>
                        <p>Sin permiso</p>
                    </div>
                    <% } %>
                </div>

                <!-- Colaboradores -->
                <div class="col-6 col-md-6 col-lg-3">
                    <% if (permisos.getOrDefault("colaboradores", false)) { %>
                    <a href="colaboradores.jsp" class="text-decoration-none">
                        <div class="card menu-card h-100 border-primary">
                            <div class="bg-primary text-white rounded-circle p-2 d-inline-block mb-2">
                                <i class="fas fa-handshake"></i>
                            </div>
                            <h5>Colaboradores</h5>
                            <p>Gestiona tu equipo de trabajo</p>
                        </div>
                    </a>
                    <% } else { %>
                    <div class="card menu-card h-100 border-secondary opacity-50">
                        <div class="bg-secondary text-white rounded-circle p-2 d-inline-block mb-2">
                            <i class="fas fa-handshake"></i>
                        </div>
                        <h5>Colaboradores</h5>
                        <p>Sin permiso</p>
                    </div>
                    <% } %>
                </div>

                <!-- Configuración -->
                <div class="col-6 col-md-6 col-lg-3">
                    <% if (permisos.getOrDefault("configuracion", false)) { %>
                    <a href="configuracion.jsp" class="text-decoration-none">
                        <div class="card menu-card h-100 border-secondary">
                            <div class="bg-secondary text-white rounded-circle p-2 d-inline-block mb-2">
                                <i class="fas fa-cog"></i>
                            </div>
                            <h5>Configuración</h5>
                            <p>Personaliza el sistema</p>
                        </div>
                    </a>
                    <% } else { %>
                    <div class="card menu-card h-100 border-secondary opacity-50">
                        <div class="bg-secondary text-white rounded-circle p-2 d-inline-block mb-2">
                            <i class="fas fa-cog"></i>
                        </div>
                        <h5>Configuración</h5>
                        <p>Sin permiso</p>
                    </div>
                    <% } %>
                </div>

                <!-- Asignación de Permisos -->
                <div class="col-6 col-md-6 col-lg-3">
                    <% if (permisos.getOrDefault("asignacion_permisos", false)) { %>
                    <a href="asignacion-permisos.jsp" class="text-decoration-none">
                        <div class="card menu-card h-100 border-dark">
                            <div class="bg-dark text-white rounded-circle p-2 d-inline-block mb-2">
                                <i class="fas fa-shield-alt"></i>
                            </div>
                            <h5>Asignación de Permisos</h5>
                            <p>Gestiona acceso a módulos</p>
                        </div>
                    </a>
                    <% } else { %>
                    <div class="card menu-card h-100 border-secondary opacity-50">
                        <div class="bg-secondary text-white rounded-circle p-2 d-inline-block mb-2">
                            <i class="fas fa-shield-alt"></i>
                        </div>
                        <h5>Asignación de Permisos</h5>
                        <p>Sin permiso</p>
                    </div>
                    <% } %>
                </div>
            </div>
        </div>
        <div class="card-footer text-center">
            <a href="logout" class="btn btn-outline-danger btn-sm">
                <i class="fas fa-sign-out-alt me-1"></i> Cerrar Sesión
            </a>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>