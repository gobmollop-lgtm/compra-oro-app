<%-- 
    Document   : login
    Created on : 11-07-2025, 10:42:18 PM
    Author     : EZEQUIAS
--%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Compra de Oro</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style> body { background: #f8f9fa; } </style>
</head>
<body>
<div class="container d-flex align-items-center min-vh-100">
    <div class="row w-100 justify-content-center">
        <div class="col-md-5">
            <div class="card shadow">
                <div class="card-header bg-primary text-white text-center">
                    <h4>Iniciar Sesión</h4>
                </div>
                <div class="card-body">
                    <% if (request.getParameter("error") != null) { %>
                    <div class="alert alert-danger">Usuario o contraseña incorrectos</div>
                    <% } %>
                    <form action="login" method="post">
                        <div class="mb-3">
                            <input type="text" name="usuario" class="form-control" placeholder="Usuario" required autofocus>
                        </div>
                        <div class="mb-3">
                            <input type="password" name="contrasena" class="form-control" placeholder="Contraseña" required>
                        </div>
                        <button type="submit" class="btn btn-primary w-100">Entrar</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>