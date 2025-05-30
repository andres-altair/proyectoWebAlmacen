<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.andres.gestionalmacen.dtos.SectoresAlquilerDto" %>
<%@ page import="com.andres.gestionalmacen.dtos.SectorDto" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.time.Year" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Mis Alquileres</title>
    <!-- Bootstrap CSS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- FontAwesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" rel="stylesheet">
    <!-- Estilos propios -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/usuario/alquiler.css">
</head>
<body>
    <!-- Barra de Navegación Responsive -->
<nav class="navbar navbar-expand-md navbar-light py-2" style="background-color: var(--azul-marino);">
    <div class="container-fluid">
        <!-- Logo SIEMPRE visible a la izquierda en lg+, arriba en md, oculto en sm -->
        <a class="navbar-brand d-none d-lg-block" href="#">
            <img src="${pageContext.request.contextPath}/img/logo.svg" alt="EnvioGo" class=" logo-navbar" />
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
                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/usuario/alquiler"><i class="fas fa-warehouse"></i> Mis Alquileres</a></li>
                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/usuario/perfil"><i class="fas fa-user-circle"></i> Mi Perfil</a></li>
                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/usuario/incidencia"><i class="fas fa-exclamation-triangle"></i> Incidencias</a></li>
                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/usuario/pedido"><i class="fas fa-box"></i> Mis Pedidos</a></li>
                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/cerrarSesion"><i class="fas fa-sign-out-alt"></i> Salir</a></li>
                </ul>
            </div>
        </div>
        <div class="collapse navbar-collapse justify-content-end" id="navbarUser">
            <ul class="navbar-nav ms-auto align-items-center w-100">
                <!-- MD: Logo arriba, dos botones centrados abajo -->
                <div class="collapse navbar-collapse justify-content-center" id="navbarUser">
                    <ul class="navbar-nav flex-row w-100 justify-content-center align-items-center">
                        <li class="nav-item d-none d-md-block d-lg-none mx-2">
                            <a class="nav-link text-center" href="${pageContext.request.contextPath}/usuario/alquiler">
                                <i class="fas fa-warehouse"></i> Mis Alquileres
                            </a>
                        </li>
                        <li class="nav-item d-none d-md-block d-lg-none mx-2">
                            <div class="dropdown">
                                <a href="#" class="nav-link dropdown-toggle text-center" id="menuDropdownMd" data-bs-toggle="dropdown" aria-expanded="false">
                                    <i class="fas fa-list-ul"></i> Menú
                                </a>
                                <ul class="dropdown-menu text-center" aria-labelledby="menuDropdownMd">
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/usuario/pedido"><i class="fas fa-box"></i> Mis Pedidos</a></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/usuario/perfil"><i class="fas fa-user-circle"></i> Mi Perfil</a></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/usuario/incidencia"><i class="fas fa-exclamation-triangle"></i> Incidencias</a></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/cerrarSesion"><i class="fas fa-sign-out-alt"></i> Salir</a></li>
                                </ul>
                            </div>
                        </li>
                    </ul>
                </div>
                <!-- LG+: Logo izq, todos los botones distribuidos -->
                <li class="nav-item d-none d-lg-block flex-grow-1"></li>
                <li class="nav-item d-none d-lg-block text-center">
                    <a href="${pageContext.request.contextPath}/usuario/alquiler" class="nav-link">
                        <i class="fas fa-warehouse"></i> Mis Alquileres
                    </a>
                </li>
                <li class="nav-item d-none d-lg-block text-center">
                    <a href="${pageContext.request.contextPath}/usuario/pedido" class="nav-link">
                        <i class="fas fa-box"></i> Mis Pedidos
                    </a>
                </li>
                <li class="nav-item d-none d-lg-block text-center">
                    <div class="dropdown d-inline-block">
                        <a href="#" class="nav-link dropdown-toggle" id="menuDropdownLg" data-bs-toggle="dropdown" aria-expanded="false">
                            <i class="fas fa-list-ul"></i> Menú
                        </a>
                        <ul class="dropdown-menu" aria-labelledby="menuDropdownLg">
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/usuario/perfil"><i class="fas fa-user-circle"></i> Mi Perfil</a></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/usuario/incidencia"><i class="fas fa-exclamation-triangle"></i> Incidencias</a></li>
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
<c:if test="${not empty error}">
            <div id="error-alert" class="alert alert-danger alert-dismissible fade show" role="alert">
              <i class="fas fa-exclamation-circle"></i> ${error}
              <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
            </div>
            <c:remove var="error" scope="session"/>
          </c:if>
<h2 class="text-center">Mis Alquileres</h2>
<c:choose>
   <c:when test="${empty alquileres}">
    <div class="container mt-4 mb-4 d-flex justify-content-center">
        <p class="text-center text-center-empty-message">No tienes alquileres activos.</p>
    </div>
</c:when>

    <c:otherwise>
        <div class="container mt-4 mb-4">
            <table class="alquiler-table table table-bordered">
                <tr>
                    <th>ID</th>
                    <th>Sector</th>
                    <th>Fecha Inicio</th>
                    <th>Fecha Fin</th>
                    <th>Precio</th>
                </tr>
                <c:forEach var="alquiler" items="${alquileres}">
                    <c:if test="${alquiler.estado == 1}">
                    <tr>
                        <td>${alquiler.id}</td>
                        <td>${alquiler.sectorId}</td>
                        <td><fmt:formatDate value="${alquiler.fechaInicioDate}" pattern="dd-MM-yyyy HH:mm"/></td>
                        <td><fmt:formatDate value="${alquiler.fechaFinDate}" pattern="dd-MM-yyyy HH:mm"/></td>
                        <td>
                            <c:forEach var="sector" items="${sectoresLibres}">
                                <c:if test="${sector.id == alquiler.sectorId}">
                                    €${sector.precio}
                                </c:if>
                            </c:forEach>
                        </td>
                    </tr>
                    </c:if>
                </c:forEach>
            </table>
        </div>
    </c:otherwise>
</c:choose>
<br>
<h2 class="text-center">Sectores Libres para Alquilar</h2>
<c:choose>
    <c:when test="${empty sectoresLibres}">
        <div class="container mt-4 mb-4">
            <p class="text-center text-center-empty-message">No tienes alquileres activos.</p>
        </div>
        
    </c:when>
    <c:otherwise>
        <div class="container mt-4 mb-4">
            <table class="sectores-table table table-bordered">
                <tr>
                    <th>ID</th>
                    <th>Precio</th>
                    <th>Estado</th>
                    <th>Acción</th>
                </tr>
                <c:forEach var="sector" items="${sectoresLibres}">
                    <c:if test="${sector.estado eq 'libre'}">
                    <tr>
                        <td>${sector.id}</td>
                        <td>${sector.precio}</td>
                        <td>${sector.estado}</td>
                        <td>
                            <form action="${pageContext.request.contextPath}/usuario/pagoSector" method="get">
                                <input type="hidden" name="sectorId" value="${sector.id}"/>
                                <button type="submit" class="payment-button d-flex align-items-center justify-content-center">
                                    <i class="fab fa-paypal me-2"></i> Pagar con PayPal
                                </button>
                            </form>
                        </td>
                    </tr>
                    </c:if>
                </c:forEach>
            </table>
        </div>
    </c:otherwise>
</c:choose>
    <!-- Footer -->
    <footer class="footer">
        <p>&copy; <%= Year.now() %> EnvioGo - Todos los derechos reservados</p>
    </footer>
</body>
</html>