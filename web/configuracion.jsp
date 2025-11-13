<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="modelo.Configuracion, logica.ConfiguracionServicio, java.util.Map, logica.PermisoServicio" %>
<%
// === PROTECCIÓN DINÁMICA DE PERMISOS ===
if (session.getAttribute("usuarioId") == null) {
    response.sendRedirect("login.jsp");
    return;
}

int usuarioId = Integer.parseInt(session.getAttribute("usuarioId").toString());
PermisoServicio permisoServicio = new PermisoServicio();
Map<String, Boolean> permisos = permisoServicio.obtenerPermisosPorUsuario(usuarioId);

// Solo permite acceso si tiene permiso para "configuracion"
if (!permisos.getOrDefault("configuracion", false)) {
    response.sendRedirect("menu-principal.jsp");
    return;
}
// ======================================

ConfiguracionServicio configServicio = new ConfiguracionServicio();
Configuracion configuracionMoneda = configServicio.obtenerConfiguracion();
if (configuracionMoneda == null) {
    response.sendError(500, "Configuración no encontrada");
    return;
}
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Configuración del Sistema</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="bg-light">
<div class="container mt-4">
    <div class="card shadow">
        <div class="card-header bg-warning text-dark">
            <h4 class="d-inline"><i class="fas fa-cog me-2"></i>Configuración del Sistema</h4>
            <div class="float-end">
                <small class="me-2">👤 <%= session.getAttribute("nombreUsuario") %> 
                    (<%= permisos.getOrDefault("asignacion_permisos", false) ? "Administrador" : "Usuario" %>)
                </small>
                <a href="menu-principal.jsp" class="btn btn-sm btn-outline-dark">Menú Principal</a>
            </div>
        </div>
        <div class="card-body">
            <% if ("1".equals(request.getParameter("exito"))) { %>
            <div class="alert alert-success">✅ Configuración guardada exitosamente.</div>
            <% } %>

            <form action="guardar-configuracion" method="post">
                <!-- Configuración del Negocio -->
                <h5><i class="fas fa-store me-2"></i>Datos del Negocio</h5>
                <div class="row mb-4">
                    <div class="col-md-6">
                        <label class="form-label">Nombre del Negocio</label>
                        <input type="text" name="nombreNegocio" class="form-control" value="<%= configuracionMoneda.getNombreNegocio() %>" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Teléfono</label>
                        <input type="text" name="telefonoNegocio" class="form-control" value="<%= configuracionMoneda.getTelefonoNegocio() != null ? configuracionMoneda.getTelefonoNegocio() : "" %>">
                    </div>
                    <div class="col-12 mt-2">
                        <label class="form-label">Dirección</label>
                        <textarea name="direccionNegocio" class="form-control"><%= configuracionMoneda.getDireccionNegocio() != null ? configuracionMoneda.getDireccionNegocio() : "" %></textarea>
                    </div>
                    <div class="col-12 mt-2">
                        <label class="form-label">URL del Logo (opcional)</label>
                        <input type="text" name="logoUrl" class="form-control" placeholder="Ej: https://ejemplo.com/logo.png" value="<%= configuracionMoneda.getLogoUrl() != null ? configuracionMoneda.getLogoUrl() : "" %>">
                        <% if (configuracionMoneda.getLogoUrl() != null && !configuracionMoneda.getLogoUrl().trim().isEmpty()) { %>
                        <div class="mt-2">
                            <img src="<%= configuracionMoneda.getLogoUrl() %>" alt="Logo" height="50">
                        </div>
                        <% } %>
                    </div>
                </div>

                <!-- Moneda -->
                <h5><i class="fas fa-money-bill-wave me-2"></i>Moneda</h5>
                <div class="mb-4">
                    <label class="form-label">Símbolo de Moneda</label>
                    <input type="text" name="monedaSimbolo" class="form-control" value="<%= configuracionMoneda.getMonedaSimbolo() %>" maxlength="10" placeholder="$, Q, €, Bs, etc." required>
                </div>

                <!-- Configuración de Red (NUEVO) -->
                <h5 class="mt-4"><i class="fas fa-network-wired me-2"></i>Configuración de Red</h5>
                <div class="mb-3">
                    <label class="form-label">IP del Servidor</label>
                    <input type="text" name="ipServidor" class="form-control" 
                           value="<%= configuracionMoneda.getIpServidor() != null ? configuracionMoneda.getIpServidor() : "localhost" %>"
                           placeholder="Ej: 192.168.1.35 o localhost" required>
                    <small class="text-muted">
    Usa tu IP local para acceder desde el celular. 
    <br>📍 Ejemplo de URL desde el celular: 
    <code>http://<%= configuracionMoneda.getIpServidor() != null ? configuracionMoneda.getIpServidor() : "tu-ip" %>:8080/CompraOroApp/pwa/</code>
</small>
                </div>

                <button type="submit" class="btn btn-warning"> Guardar Configuración </button>
                <a href="menu-principal.jsp" class="btn btn-secondary">Cancelar</a>
            </form>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>