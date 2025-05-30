<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.andres.gestionalmacen.dtos.UsuarioDto" %>
<%@ page import="com.andres.gestionalmacen.utilidades.ImagenUtil" %>
<%@ page import="java.time.Year" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Panel de Operario</title>
    <!-- FontAwesome para iconos -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Estilos propios -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/operario/panelOperario.css">
</head>
<body>
<!-- Barra de Navegación Responsive (idéntica a panelGerente.jsp, enlaces de operario) -->
<nav class="navbar navbar-expand-md navbar-light py-2" style="background-color: var(--azul-marino);">
    <div class="container-fluid">
        <a class="navbar-brand d-none d-lg-block" href="#">
            <img src="${pageContext.request.contextPath}/img/logo.svg" alt="EnvioGo" class="logo-navbar" />
        </a>
        <a class="navbar-brand d-none d-md-block d-lg-none align-items-center" href="#" style="margin-right: 1.5rem;">
            <img src="${pageContext.request.contextPath}/img/logo.svg" alt="EnvioGo" class="logo-navbar" />
        </a>
        <div class="d-flex d-md-none align-items-center justify-content-between w-100 mb-2" style="min-height:56px;">
            <a class="navbar-brand m-0 p-0" href="#" style="height:56px;display:flex;align-items:center;">
                <img src="${pageContext.request.contextPath}/img/logo.svg" alt="EnvioGo" class="logo-navbar" />
            </a>
            <div class="dropdown ms-2" style="flex-shrink:0;">
                <a href="#" class="nav-link dropdown-toggle px-3 py-2" id="menuDropdownSm" data-bs-toggle="dropdown" aria-expanded="false" style="font-weight:500;background:var(--blanco);color:var(--azul-marino);border-radius:5px;border:2px solid var(--azul-marino);">
                    <i class="fas fa-list-ul"></i> Menú
                </a>
                <ul class="dropdown-menu dropdown-menu-end text-center" aria-labelledby="menuDropdownSm">
                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/operario/pedidos"><i class="fas fa-box"></i> Pedidos</a></li>
                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/operario/incidencia"><i class="fas fa-exclamation-triangle"></i> Incidencias</a></li>
                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/operario/actividad"><i class="fas fa-tasks"></i> Actividades</a></li>
                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/operario/perfil"><i class="fas fa-user-circle"></i> Mi Perfil</a></li>
                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/cerrarSesion"><i class="fas fa-sign-out-alt"></i> Salir</a></li>
                </ul>
            </div>
        </div>
        <div class="collapse navbar-collapse justify-content-end" id="navbarUser">
            <ul class="navbar-nav ms-auto align-items-center w-100">
                <div class="collapse navbar-collapse justify-content-center" id="navbarUser">
                    <ul class="navbar-nav flex-row w-100 justify-content-center align-items-center">
                        <li class="nav-item d-none d-md-block d-lg-none mx-2">
                            <a class="nav-link text-center" href="${pageContext.request.contextPath}/operario/pedidos">
                                <i class="fas fa-box"></i> Pedidos
                            </a>
                        </li>
                        <li class="nav-item d-none d-md-block d-lg-none mx-2">
                            <div class="dropdown">
                                <a href="#" class="nav-link dropdown-toggle text-center" id="menuDropdownMd" data-bs-toggle="dropdown" aria-expanded="false">
                                    <i class="fas fa-list-ul"></i> Menú
                                </a>
                                <ul class="dropdown-menu text-center" aria-labelledby="menuDropdownMd">
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/operario/incidencia"><i class="fas fa-exclamation-triangle"></i> Incidencias</a></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/operario/actividad"><i class="fas fa-tasks"></i> Actividades</a></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/operario/perfil"><i class="fas fa-user-circle"></i> Mi Perfil</a></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/cerrarSesion"><i class="fas fa-sign-out-alt"></i> Salir</a></li>
                                </ul>
                            </div>
                        </li>
                    </ul>
                </div>
                <li class="nav-item d-none d-lg-block flex-grow-1"></li>
                <li class="nav-item d-none d-lg-block text-center">
                    <a href="${pageContext.request.contextPath}/operario/pedidos" class="nav-link">
                        <i class="fas fa-box"></i> Pedidos
                    </a>
                </li>
                <li class="nav-item d-none d-lg-block text-center">
                    <a href="${pageContext.request.contextPath}/operario/actividad" class="nav-link">
                        <i class="fas fa-tasks"></i> Actividades
                    </a>
                </li>
                <li class="nav-item d-none d-lg-block text-center">
                    <div class="dropdown d-inline-block">
                        <a href="#" class="nav-link dropdown-toggle" id="menuDropdownLg" data-bs-toggle="dropdown" aria-expanded="false">
                            <i class="fas fa-list-ul"></i> Menú
                        </a>
                        <ul class="dropdown-menu" aria-labelledby="menuDropdownLg">
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/operario/incidencia"><i class="fas fa-exclamation-triangle"></i> Incidencias</a></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/operario/perfil"><i class="fas fa-user-circle"></i> Mi Perfil</a></li>
                        </ul>
                    </div>
                </li>
                <li class="nav-item d-none d-lg-block text-center">
                    <a href="${pageContext.request.contextPath}/cerrarSesion" class="nav-link">
                        <i class="fas fa-sign-out-alt"></i> Salir
                    </a>
                </li>
                <li class="nav-item d-none d-lg-block flex-grow-1"></li>
            </ul>
        </div>
    </div>
</nav>

<%
    String error = (String) request.getAttribute("error");
%>
    <!-- Contenedor Principal -->
    <div class="panel-container">
        <% if (error != null && !error.trim().isEmpty()) { %>
            <div id="error-alert" class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-circle"></i> <%= error %>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
            </div>
            <c:remove var="error" scope="session"/>
        <% } %>
        <!-- Sección Superior - Información de Usuario -->
        <section class="user-section">
            <div class="user-info">
                <img src="${usuarioFoto}" class="img-perfil" alt="Foto de perfil">
                <span class="welcome-text">Bienvenido <span class="nombre-usuario">${usuarioNombre}</span></span>
            </div>
        </section>      
    </div>

    <!-- Footer -->
    <footer class="footer">
        <p>&copy; <%= Year.now() %> EnvioGo - Todos los derechos reservados</p>
    </footer>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>