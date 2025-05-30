<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.andres.gestionalmacen.dtos.UsuarioDto" %>
<%@ page import="com.andres.gestionalmacen.utilidades.ImagenUtil" %>
<%@ page import="java.time.Year" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Panel de Administrador</title>
    <!-- Bootstrap CSS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- FontAwesome para iconos -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" rel="stylesheet">
    <!-- Estilos propios -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/RespaldoBBDD.css">
</head>
<body>
    <!-- Barra de Navegación Responsive -->
    <nav class="navbar navbar-expand-md navbar-light py-2" style="background-color: var(--azul-marino);">
        <div class="container-fluid">
            <!-- Logo SIEMPRE visible a la izquierda en lg+, arriba en md, oculto en sm -->
            <a class="navbar-brand d-none d-lg-block" href="#">
                <img src="${pageContext.request.contextPath}/img/logo.svg" alt="EnvioGo" class="logo-navbar" />
            </a>
            <a class="navbar-brand d-none d-md-block d-lg-none align-items-center" href="#" style="margin-right: 1.5rem;">
                <img src="${pageContext.request.contextPath}/img/logo.svg" alt="EnvioGo" class="logo-navbar" />
            </a>
            <!-- XS y SM: Logo a la izq y botón Menú a la derecha, ambos centrados verticalmente -->
            <div class="d-flex d-md-none align-items-center justify-content-between w-100 mb-2" style="min-height:56px;">
                <a class="navbar-brand m-0 p-0" href="#" style="height:56px;display:flex;align-items:center;">
                    <img src="${pageContext.request.contextPath}/img/logo.svg" alt="EnvioGo" class="logo-navbar" />
                </a>
                <div class="dropdown ms-2" style="flex-shrink:0;">
                    <a href="#" class="nav-link dropdown-toggle px-3 py-2" id="menuDropdownSm" data-bs-toggle="dropdown" aria-expanded="false" style="font-weight:500;background:var(--blanco);color:var(--azul-marino);border-radius:5px;border:2px solid var(--azul-marino);">
                        <i class="fas fa-list-ul"></i> Menú
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end text-center" aria-labelledby="menuDropdownSm">
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/perfil"><i class="fas fa-user-circle"></i> Mi Perfil</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/usuarios"> <i class="fas fa-users"></i> Usuarios</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/RespaldoBBDD"><i class="fas fa-database"></i> RespaldoBBDD</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/incidencia"><i class="fas fa-exclamation-triangle"></i> Incidencias</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/cerrarSesion"><i class="fas fa-sign-out-alt"></i> Salir</a></li>
                    </ul>
                </div>
            </div>
            <div class="collapse navbar-collapse justify-content-end" id="navbarUser">
                <ul class="navbar-nav ms-auto align-items-center w-100">
                    <!-- MD: Logo arriba, dos botones centrados abajo -->
                    <div class="collapse navbar-collapse justify-content-center" id="navbarUser">
                        <ul class="navbar-nav flex-row w-100 justify-content-center align-items-center">
                            <!-- Eliminamos el enlace de la vista LG -->
                            <li class="nav-item d-none d-md-block d-lg-none mx-2">
                                <a href="${pageContext.request.contextPath}/admin/RespaldoBBDD" class="nav-link">
                                    <i class="fas fa-database"></i> RespaldoBBDD
                                </a>
                            </li>
                            <li class="nav-item d-none d-md-block d-lg-none mx-2">
                                <div class="dropdown">
                                    <a href="#" class="nav-link dropdown-toggle text-center" id="menuDropdownMd" data-bs-toggle="dropdown" aria-expanded="false">
                                        <i class="fas fa-list-ul"></i> Menú
                                    </a>
                                    <ul class="dropdown-menu text-center" aria-labelledby="menuDropdownMd">
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/usuarios"><i class="fas fa-users"></i> Usuarios</a></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/perfil"><i class="fas fa-user-circle"></i> Mi Perfil</a></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/incidencia"><i class="fas fa-exclamation-triangle"></i> Incidencias</a></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/cerrarSesion"><i class="fas fa-sign-out-alt"></i> Salir</a></li>
                                    </ul>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <!-- LG+: Logo izq, todos los botones distribuidos -->
                    <li class="nav-item d-none d-lg-block flex-grow-1"></li>
                    <li class="nav-item d-none d-lg-block text-center">
                        <a href="${pageContext.request.contextPath}/admin/RespaldoBBDD" class="nav-link">
                            <i class="fas fa-database"></i> RespaldoBBDD
                        </a>
                    </li>
                    <li class="nav-item d-none d-lg-block text-center">
                        <a href="${pageContext.request.contextPath}/admin/incidencia" class="nav-link">
                          <i class="fas fa-exclamation-triangle"></i> Incidencias
                        </a>
                    </li>
                    <li class="nav-item d-none d-lg-block text-center">
                        <div class="dropdown d-inline-block">
                            <a href="#" class="nav-link dropdown-toggle" id="menuDropdownLg" data-bs-toggle="dropdown" aria-expanded="false">
                                <i class="fas fa-list-ul"></i> Menú
                            </a>
                            <ul class="dropdown-menu" aria-labelledby="menuDropdownLg">
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/usuarios"><i class="fas fa-users"></i> Usuarios</a></li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/perfil"><i class="fas fa-user-circle"></i> Mi Perfil</a></li>
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

    <!-- Contenedor Principal -->
    <div class="panel-container d-flex flex-column align-items-center justify-content-center" style="min-height: 60vh;">
        <div class="w-100" style="max-width: 420px;">
            <form method="post" action="${pageContext.request.contextPath}/admin/RespaldoBBDD" class="mb-3">
                <input type="hidden" name="accion" value="exportar">
                <div class="row g-2 align-items-center justify-content-center">
                    <div class="col">
                        <select name="nombreBBDD" class="form-select" required>
                            <option value="" selected disabled>Seleccione una base de datos</option>
                            <option value="gestion_usuarios">Usuarios</option>
                            <option value="gestion_almacenes">Almacenes</option>
                        </select>
                    </div>
                    <div class="col-auto">
                        <button type="submit" class="btn btn-azulmarino">Exportar Estructura</button>
                    </div>
                </div>
            </form>
            <form method="post" action="${pageContext.request.contextPath}/admin/RespaldoBBDD" class="mb-3">
                <input type="hidden" name="accion" value="exportar_datos">
                <div class="row g-2 align-items-center justify-content-center">
                    <div class="col">
                        <select name="nombreBBDD" class="form-select" required>
                            <option value="" selected disabled>Seleccione una base de datos</option>
                            <option value="gestion_usuarios">Usuarios</option>
                            <option value="gestion_almacenes">Almacenes</option>
                        </select>
                    </div>
                    <div class="col-auto">
                        <button type="submit" class="btn btn-azulmarino">Exportar Datos</button>
                    </div>
                </div>
            </form>
            <form method="post" action="${pageContext.request.contextPath}/admin/RespaldoBBDD" class="mb-3">
                <input type="hidden" name="accion" value="respaldo_completo_ambas">
                <button type="submit" class="btn btn-azulmarino">Respaldo Completo Ambas BBDD</button>
            </form>
            <c:if test="${not empty mensaje}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check"></i> ${mensaje}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <c:remove var="mensaje" scope="session"/>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-triangle"></i> ${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <c:remove var="error" scope="session"/>
            </c:if>
        </div>
    </div>
    
    <!-- Footer -->
    <footer class="footer">
        <p>&copy; <%= Year.now() %> EnvioGo - Todos los derechos reservados</p>
    </footer>

</body>
</html>