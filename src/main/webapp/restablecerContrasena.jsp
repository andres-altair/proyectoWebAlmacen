<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.time.Year"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
    <title>Sistema de Gestión de Almacén - Restablecer Contraseña</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- FontAwesome para iconos -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/restablecerContrasena.css">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
</head>
<body class="d-flex flex-column min-vh-100">
    <nav class="navbar navbar-expand-lg">
        <div class="container-fluid">
            <div class="navbar-logo">
                <img src="${pageContext.request.contextPath}/img/logo.svg" alt="EnvioGo" class="img-fluid">
            </div>
            <h1 class="navbar-title d-none d-sm-block">Reestablecer Contraseña</h1>
            <!-- Menú normal para lg y superiores -->
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto d-none d-lg-flex">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/acceso">Iniciar Sesión</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/registro">Registrarse</a>
                    </li>
                </ul>
            </div>
            <!-- Dropdown personalizado solo visible en md y menores -->
            <div class="dropdown ms-auto d-lg-none" style="flex-shrink:0;">
                <a href="#" class="nav-link dropdown-toggle px-3 py-2" id="menuDropdownSm" data-bs-toggle="dropdown" aria-expanded="false" style="font-weight:500;background:var(--blanco);color:var(--azul-marino);border-radius:5px;border:2px solid var(--azul-marino);">
                    <i class="fas fa-list-ul"></i> Menú
                </a>
                <ul class="dropdown-menu dropdown-menu-end text-center" aria-labelledby="menuDropdownSm">
                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/acceso"><i class="fas fa-sign-in-alt"></i> Iniciar Sesión</a></li>
                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/registro"><i class="fas fa-user-plus"></i> Registrarse</a></li>
                </ul>
            </div>
        </div>
    </nav>
    <div class="container my-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow">
                    <div class="card-body p-4">
                        <h3 class="text-center mb-4">Restablecer Contraseña</h3>
                        
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger" role="alert">
                                ${error}
                            </div>
                        </c:if>
                        
                        <form action="restablecerContrasena" method="post" class="needs-validation" novalidate id="formularioRestablecer">
                            <input type="hidden" name="token" value="${token}">
                            
                            <!-- Nueva Contraseña -->
                            <div class="form-group mb-3">
                                <label for="nuevaContrasena" class="form-label">Nueva Contraseña:</label>
                                <div class="input-group">
                                    <span class="input-group-text">
                                        <i class="fas fa-lock"></i>
                                    </span>
                                    <input type="password" 
                                           id="nuevaContrasena"
                                           name="nuevaContrasena"
                                           class="form-control" 
                                           required
                                           minlength="8"
                                           pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$"
                                           placeholder="Ingresa tu nueva contraseña">
                                    <button class="btn btn-outline-secondary" type="button" id="mostrarContrasena">
                                        <i class="fas fa-eye"></i>
                                    </button>
                                    <div class="invalid-feedback">
                                        La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número.
                                    </div>
                                </div>
                            </div>

                            <!-- Confirmar Nueva Contraseña -->
                            <div class="form-group mb-3">
                                <label for="confirmarContrasena" class="form-label">Confirmar Nueva Contraseña:</label>
                                <div class="input-group">
                                    <span class="input-group-text">
                                        <i class="fas fa-lock"></i>
                                    </span>
                                    <input type="password" 
                                           id="confirmarContrasena"
                                           name="confirmarContrasena"
                                           class="form-control" 
                                           required
                                           minlength="8"
                                           pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$"
                                           placeholder="Confirma tu nueva contraseña">
                                    <button class="btn btn-outline-secondary" type="button" id="mostrarConfirmarContrasena">
                                        <i class="fas fa-eye"></i>
                                    </button>
                                    <div class="invalid-feedback">
                                        Las contraseñas no coinciden o no cumplen con los requisitos mínimos.
                                    </div>
                                </div>
                            </div>
                            
                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-key me-2"></i>Restablecer Contraseña
                                </button>
                                <a href="${pageContext.request.contextPath}/acceso" class="btn btn-outline-secondary">
                                    <i class="fas fa-arrow-left me-2"></i>Volver al Inicio de Sesión
                                </a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <footer class="container-fluid py-3 text-center mt-auto">
        <p>&copy; <%=Year.now()%> Sistema de Gestión de Almacén - Todos los derechos reservados</p>
    </footer>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Script de validación -->
    <script src="${pageContext.request.contextPath}/js/restablecerContrasena.js"></script>

</body>
</html>