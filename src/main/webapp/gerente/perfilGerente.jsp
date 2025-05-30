<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.andres.gestionalmacen.dtos.SectoresAlquilerDto" %>
<%@ page import="com.andres.gestionalmacen.dtos.SectorDto" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.time.Year" %>
<%@ page import="com.andres.gestionalmacen.dtos.UsuarioDto" %>
<%@ page import="java.util.Base64" %>
<%
    UsuarioDto usuario = (UsuarioDto) request.getAttribute("usuario");
    String mensaje = (String) request.getAttribute("mensaje");
    String error = (String) request.getAttribute("error");
%>
<html>
<head>
    <title>Mi Perfil Gerente</title>
    <!-- Bootstrap CSS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- FontAwesome (iconos) -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" rel="stylesheet">
    <!-- Estilos propios -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/gerente/perfilGerente.css">
</head>
<body>
<!-- Barra de Navegación Responsive -->
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
                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/gerente/perfil"><i class="fas fa-user-circle"></i> Mi Perfil</a></li>
                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/gerente/inventario"><i class="fas fa-boxes-stacked"></i> Inventario</a></li>
                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/gerente/incidencia"><i class="fas fa-exclamation-triangle"></i> Incidencias</a></li>
                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/gerente/actividad"><i class="fas fa-tasks"></i> Actividades</a></li>
                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/cerrarSesion"><i class="fas fa-sign-out-alt"></i> Salir</a></li>
                </ul>
            </div>
        </div>
        <div class="collapse navbar-collapse justify-content-end" id="navbarUser">
            <ul class="navbar-nav ms-auto align-items-center w-100">
                <div class="collapse navbar-collapse justify-content-center" id="navbarUser">
                    <ul class="navbar-nav flex-row w-100 justify-content-center align-items-center">
                        <li class="nav-item d-none d-md-block d-lg-none mx-2">
                            <a class="nav-link text-center" href="${pageContext.request.contextPath}/gerente/perfil">
                                <i class="fas fa-user-circle"></i> Mi Perfil
                            </a>
                        </li>
                        <li class="nav-item d-none d-md-block d-lg-none mx-2">
                            <div class="dropdown">
                                <a href="#" class="nav-link dropdown-toggle text-center" id="menuDropdownMd" data-bs-toggle="dropdown" aria-expanded="false">
                                    <i class="fas fa-list-ul"></i> Menú
                                </a>
                                <ul class="dropdown-menu text-center" aria-labelledby="menuDropdownMd">
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/gerente/inventario"><i class="fas fa-boxes-stacked"></i> Inventario</a></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/gerente/incidencia"><i class="fas fa-exclamation-triangle"></i> Incidencias</a></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/gerente/actividad"><i class="fas fa-tasks"></i> Actividades</a></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/cerrarSesion"><i class="fas fa-sign-out-alt"></i> Salir</a></li>
                                </ul>
                            </div>
                        </li>
                    </ul>
                </div>
                <li class="nav-item d-none d-lg-block flex-grow-1"></li>
                <li class="nav-item d-none d-lg-block text-center">
                    <a href="${pageContext.request.contextPath}/gerente/perfil" class="nav-link">
                        <i class="fas fa-user-circle"></i> Mi Perfil
                    </a>
                </li>
                <li class="nav-item d-none d-lg-block text-center">
                    <a href="${pageContext.request.contextPath}/gerente/actividad" class="nav-link">
                        <i class="fas fa-tasks"></i> Actividades
                    </a>
                </li>
                <li class="nav-item d-none d-lg-block text-center">
                    <div class="dropdown d-inline-block">
                        <a href="#" class="nav-link dropdown-toggle" id="menuDropdownLg" data-bs-toggle="dropdown" aria-expanded="false">
                            <i class="fas fa-list-ul"></i> Menú
                        </a>
                        <ul class="dropdown-menu" aria-labelledby="menuDropdownLg">
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/gerente/inventario"><i class="fas fa-boxes-stacked"></i> Inventario</a></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/gerente/incidencia"><i class="fas fa-exclamation-triangle"></i> Incidencias</a></li>
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
<br><br>
<div class="perfil-container">
    <h2>Mi Perfil</h2>
    <% if (mensaje != null && !mensaje.isEmpty()) { %>
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <%= mensaje %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
        </div>
        <c:remove var="mensaje" scope="session"/>
    <% } %>
    <% if (error != null && !error.isEmpty()) { %>
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <%= error %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
        </div>
        <c:remove var="error" scope="session"/>
    <% } %>
    <form action="<%= request.getContextPath() %>/gerente/perfil" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <label for="nombreCompleto">Nombre completo</label>
            <input type="text" id="nombreCompleto" name="nombreCompleto" maxlength="50" value="<%= usuario != null ? usuario.getNombreCompleto() : "" %>" placeholder="Nombre completo" required>
        </div>
        <div class="form-group">
            <label for="movil">Móvil</label>
            <input type="text" id="movil" name="movil" maxlength="9" pattern="[0-9]{9}" value="<%= usuario != null ? usuario.getMovil() : "" %>" placeholder="Número de móvil" required>
        </div>
        <div class="form-group">
            <label for="foto">Foto de perfil</label>
        <% if (usuario != null && usuario.getFoto() != null && usuario.getFoto().length > 0) { %>
            <img class="foto-preview" src="data:image/jpeg;base64,<%= Base64.getEncoder().encodeToString(usuario.getFoto()) %>" alt="Foto de perfil" />
        <% } else { %>
            <img class="foto-preview" src="https://ui-avatars.com/api/?name=<%= usuario != null ? usuario.getNombreCompleto().replace(" ", "+") : "Gerente" %>&background=1976d2&color=fff" alt="Sin foto" />
        <% } %>
        <input type="file" id="foto" name="foto" accept="image/*">
        </div>
        <div class="form-group" style="text-align:center;">
            <button type="submit" class="btn">Guardar cambios</button>
        </div>
    </form>
    <hr>
    <h4>Datos completos del usuario</h4>
    <% if (usuario != null) { %>
        <pre style="font-size:13px; background:#f4f4f4; padding:1em; border-radius:6px; border:1px solid #ddd; overflow-x:auto;">
            ID: <%= usuario.getId() %>
            Nombre: <%= usuario.getNombreCompleto() %>
            Móvil: <%= usuario.getMovil() %>
            Correo: <%= usuario.getCorreoElectronico() %>
            Rol ID: <%= usuario.getRolId() %>
            Fecha creación: <%= usuario.getFechaCreacion() %>
            Correo confirmado: <%= usuario.isCorreoConfirmado() %>
            Google: <%= usuario.isGoogle() %>
        </pre>
    <% } %>
</div>
<!-- Footer -->
<footer class="footer">
    <p>&copy; <%= Year.now() %> EnvioGo - Todos los derechos reservados</p>
</footer>
</body>
</html>
