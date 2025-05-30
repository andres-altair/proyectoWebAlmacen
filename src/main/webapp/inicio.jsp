<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.time.Year" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
    <title>Sistema de Gestión de Almacén</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- FontAwesome para iconos -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/inicio.css">
</head>
<body>
    <!-- Barra de Navegación -->
    <nav class="navbar navbar-expand-md">
        <div class="container-fluid">
            <div class="navbar-logo">
                <img src="${pageContext.request.contextPath}/img/logo.svg" alt="EnvioGo" class="img-fluid">
            </div>
            <h1 class="navbar-title d-none d-lg-block">Sistema de Gestión de Almacén</h1>
            <!-- Menú normal para md y superiores -->
            <ul class="navbar-nav ms-auto d-none d-md-flex">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/acceso">Iniciar Sesión</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/registro">Registrarse</a>
                </li>
            </ul>
            <!-- Dropdown personalizado solo visible en sm y xs -->
            <div class="dropdown ms-auto d-md-none" style="flex-shrink:0;">
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

    <!-- Contenedor Principal -->
    <div class="container my-4">
        <!-- Mostrar mensaje de error si existe -->
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <%= request.getAttribute("error") %>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        <% } %>
        
        
        <div class="row text-center">
            <div class="col-12">
                <h1 class="display-4">Sistema de Gestión de Almacén</h1>
            </div>
        </div>
    </div>

    <!-- Carrusel -->
    <div id="carouselAlmacen" class="carousel slide" data-bs-ride="carousel">
        <!-- Indicadores -->
        <div class="carousel-indicators">
            <button type="button" data-bs-target="#carouselAlmacen" data-bs-slide-to="0" class="active"></button>
            <button type="button" data-bs-target="#carouselAlmacen" data-bs-slide-to="1"></button>
            <button type="button" data-bs-target="#carouselAlmacen" data-bs-slide-to="2"></button>
        </div>

        <!-- Slides -->
        <div class="carousel-inner">
            <div class="carousel-item active">
                <img src="https://images.pexels.com/photos/4483610/pexels-photo-4483610.jpeg" class="d-block w-100" alt="">
                <div class="carousel-caption">
                    <h2 class="d-none d-sm-block">Gestión Moderna de Almacenes</h2>
                    <p class="d-none d-sm-block">Optimiza tus operaciones con nuestra solución integral</p>
                </div>
            </div>
            <div class="carousel-item">
                <img src="https://images.pexels.com/photos/4481259/pexels-photo-4481259.jpeg" class="d-block w-100" alt="">
                <div class="carousel-caption">
                    <h2 class="d-none d-sm-block">Servicios Logísticos Avanzados</h2>
                    <p class="d-none d-sm-block">Control total de tu inventario en tiempo real</p>
                </div>
            </div>
            <div class="carousel-item">
                <img src="https://images.pexels.com/photos/6169669/pexels-photo-6169669.jpeg" class="d-block w-100" alt="">
                <div class="carousel-caption">
                    <h2 class="d-none d-sm-block">Tecnología de Última Generación</h2>
                    <p class="d-none d-sm-block">Sistemas automatizados para mayor eficiencia</p>
                </div>
            </div>
        </div>

        <!-- Controles -->
        <button class="carousel-control-prev" type="button" data-bs-target="#carouselAlmacen" data-bs-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Anterior</span>
        </button>
        <button class="carousel-control-next" type="button" data-bs-target="#carouselAlmacen" data-bs-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Siguiente</span>
        </button>
    </div>

     <!-- Footer -->
     <footer class="container-fluid py-3 text-center mt-auto">
        <p>&copy; <%= Year.now() %> EnvioGo - Todos los derechos reservados</p>
        
    </footer>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>