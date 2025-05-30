<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.time.Year"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="context-path" content="${pageContext.request.contextPath}">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
    <title>Sistema de Gestión de Almacén - Registro</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- FontAwesome para iconos -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/registro.css">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <!-- Google Sign-In API -->
    <script src="https://accounts.google.com/gsi/client" async defer></script>
</head>
<body class="d-flex flex-column min-vh-100">
    <!-- Barra de Navegación -->
    <nav class="navbar navbar-expand-lg">
        <div class="container-fluid">
            <div class="navbar-logo">
                <img src="${pageContext.request.contextPath}/img/logo.svg" alt="EnvioGo" class="img-fluid">
            </div>
            <h1 class="navbar-title d-none d-sm-block">Registro de Usuario</h1>
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/acceso">Iniciar Sesión</a>
                </li>
            </ul>
        </div>
    </nav>
    <!-- Título solo visible en xs, debajo de la navbar -->
    <h1 class="navbar-title text-center d-block d-sm-none mt-3">Registro de Usuario</h1>
    <div class="container my-5">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card shadow">
                    <div class="card-body p-4">
                        
                        <c:if test="${not empty sessionScope.error}">
                            <div class="alert alert-danger" role="alert">
                                ${sessionScope.error}
                                <% session.removeAttribute("error"); %>
                            </div>
                        </c:if>
                        
                        <form action="registro" method="post" enctype="multipart/form-data" 
                              class="needs-validation" novalidate id="formularioRegistro">
                            <!-- Nombre Completo -->
                            <div class="form-group mb-3">
                                <label for="nombreCompleto" class="form-label">Nombre Completo:</label>
                                <div class="input-group">
                                    <span class="input-group-text">
                                        <i class="fas fa-user"></i>
                                    </span>
                                    <input type="text" 
                                           id="nombreCompleto"
                                           name="nombreCompleto"
                                           class="form-control" 
                                           required
                                           maxlength="50"
                                           placeholder="Ingresa tu nombre completo maximo 50 caracteres"
                                           value="${sessionScope.nombreCompleto}">
                                    <% session.removeAttribute("nombreCompleto"); %>
                                    <div class="invalid-feedback">
                                        Por favor, introduce un nombre (maximo 50 caracteres).
                                    </div>
                                </div>
                            </div>

                            <!-- Correo Electrónico -->
                            <div class="form-group mb-3">
                                <label for="correoElectronico" class="form-label">Correo Electrónico:</label>
                                <div class="input-group">
                                    <span class="input-group-text">
                                        <i class="fas fa-envelope"></i>
                                    </span>
                                    <input type="email" 
                                           id="correoElectronico"
                                           name="correoElectronico"
                                           class="form-control" 
                                           required
                                           maxlength="50"
                                           placeholder="Ingresa tu correo electrónico maximo 50 caracteres"
                                           autocomplete="off">
                                    <div class="invalid-feedback">
                                        Por favor, introduce un correo electrónico válido menor a 50 caracteres.
                                    </div>
                                </div>
                            </div>

                            <!-- Móvil -->
                            <div class="form-group mb-3">
                                <label for="movil" class="form-label">Teléfono Móvil:</label>
                                <div class="input-group">
                                    <span class="input-group-text">
                                        <i class="fas fa-mobile-alt"></i>
                                    </span>
                                    <input type="tel" 
                                           id="movil"
                                           name="movil"
                                           class="form-control" 
                                           required
                                           pattern="[0-9]{9}"
                                           minlength="9"
                                           maxlength="9"
                                           placeholder="Ingresa tu número móvil 9 exactos digitos"
                                           value="${sessionScope.movil}">
                                    <% session.removeAttribute("movil"); %>
                                    <div class="invalid-feedback">
                                        Por favor, introduce un número de teléfono móvil válido (9 dígitos).
                                    </div>
                                </div>
                            </div>

                            <!-- Contraseña -->
                            <div class="form-group mb-3">
                                <label for="contrasena" class="form-label">Contraseña:</label>
                                <div class="input-group">
                                    <span class="input-group-text">
                                        <i class="fas fa-lock"></i>
                                    </span>
                                    <input type="password" 
                                           id="contrasena"
                                           name="contrasena"
                                           class="form-control" 
                                           required
                                           minlength="8"
                                           placeholder="Ingresa tu contraseña maximo 255 caracteres">
                                    <button class="btn btn-outline-secondary" type="button" id="mostrarContrasena">
                                        <i class="fas fa-eye"></i>
                                    </button>
                                    <div class="invalid-feedback">
                                        La contraseña debe tener al menos 8 caracteres.
                                    </div>
                                </div>
                            </div>

                            <!-- Confirmar Contraseña -->
                            <div class="form-group mb-3">
                                <label for="confirmarContrasena" class="form-label">Confirmar Contraseña:</label>
                                <div class="input-group">
                                    <span class="input-group-text">
                                        <i class="fas fa-lock"></i>
                                    </span>
                                    <input type="password" 
                                           id="confirmarContrasena"
                                           class="form-control" 
                                           required
                                           minlength="8"
                                           placeholder="Confirma tu contraseña">
                                    <button class="btn btn-outline-secondary" type="button" id="mostrarConfirmarContrasena">
                                        <i class="fas fa-eye"></i>
                                    </button>
                                    <div class="invalid-feedback">
                                        Las contraseñas no coinciden.
                                    </div>
                                </div>
                            </div>

                            <!-- Foto -->
                            <div class="form-group mb-3">
                                <label for="foto" class="form-label">Foto de Perfil:</label>
                                <div class="input-group">
                                    <span class="input-group-text">
                                        <i class="fas fa-image"></i>
                                    </span>
                                    <input type="file" 
                                           id="foto"
                                           name="foto"
                                           class="form-control" 
                                           accept="image/*">
                                    <div class="invalid-feedback">
                                        Por favor, selecciona una imagen válida.
                                    </div>
                                </div>
                                <div id="previewFoto" class="mt-2 text-center d-none">
                                    <img src="" alt="Vista previa" class="img-thumbnail" style="max-width: 200px;">
                                </div>
                            </div>
                            
                            <!-- Botón de envío -->
                            <div class="text-center mb-3">
                                <button type="submit" class="btn btn-primary btn-block">
                                    <i class="fas fa-user-plus me-2"></i>Registrarse
                                </button>
                            </div>
                        </form>
                        <!-- Botón de Google centrado -->
                        <div class="d-flex justify-content-center my-3">
                            <div>
                                <div id="g_id_onload"
                                     data-client_id="${googleClientId}"
                                     data-context="signup"
                                     data-ux_mode="popup"
                                     data-callback="manejarRegistroGoogle"
                                     data-auto_prompt="false">
                                </div>
                                <div class="g_id_signin"
                                     data-type="standard"
                                     data-size="large"
                                     data-theme="outline"
                                     data-text="signup_with"
                                     data-shape="rectangular"
                                     data-logo_alignment="left"
                                     data-width="100%">
                                </div>
                            </div>
                        </div>

                        <div class="text-center mt-3">
                            ¿Ya tienes una cuenta? <a href="${pageContext.request.contextPath}/acceso">Inicia sesión aquí</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <footer class="container-fluid py-3 text-center mt-auto">
        <p>&copy; <%= Year.now() %> EnvioGo - Todos los derechos reservados</p>
    </footer>

    <!-- Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/registro.js"></script>
</body>
</html>