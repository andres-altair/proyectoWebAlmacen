<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.time.Year" %>
<%@ page import="java.util.List" %>
<%@ page import="com.andres.gestionalmacen.dtos.RutaRespuestaDto" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Pedidos Transportista</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/transportista/ruta.css">
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
            <!-- XS y SM: Logo a la izq y botón Menú a la derecha -->
            <div class="d-flex d-md-none align-items-center justify-content-between w-100 mb-2" style="min-height:56px;">
                <a class="navbar-brand m-0 p-0" href="#" style="height:56px;display:flex;align-items:center;">
                    <img src="${pageContext.request.contextPath}/img/logo.svg" alt="EnvioGo" class="logo-navbar" />
                </a>
                <div class="dropdown ms-2" style="flex-shrink:0;">
                    <a href="#" class="nav-link dropdown-toggle px-3 py-2" id="menuDropdownSm" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="fas fa-list-ul"></i> Menú
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end text-center" aria-labelledby="menuDropdownSm">
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/transportista/pedidos"><i class="fas fa-box"></i> Pedidos</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/transportista/rutas"><i class="fas fa-route"></i> Rutas</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/transportista/incidencia"><i class="fas fa-exclamation-triangle"></i> Incidencias</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/transportista/perfil"><i class="fas fa-user-circle"></i> Mi Perfil</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/cerrarSesion"><i class="fas fa-sign-out-alt"></i> Salir</a></li>
                    </ul>
                </div>
            </div>
            <div class="collapse navbar-collapse justify-content-end" id="navbarTransportista">
                <ul class="navbar-nav ms-auto align-items-center w-100">
                    <div class="collapse navbar-collapse justify-content-center" id="navbarTransportista">
                        <ul class="navbar-nav flex-row w-100 justify-content-center align-items-center">
                            <li class="nav-item d-none d-md-block d-lg-none mx-2">
                                <a class="nav-link text-center" href="${pageContext.request.contextPath}/transportista/rutas">
                                    <i class="fas fa-route"></i> Rutas
                                </a>
                            </li>
                            <li class="nav-item d-none d-md-block d-lg-none mx-2">
                                <div class="dropdown">
                                    <a href="#" class="nav-link dropdown-toggle text-center" id="menuDropdownMd" data-bs-toggle="dropdown" aria-expanded="false">
                                        <i class="fas fa-list-ul"></i> Menú
                                    </a>
                                    <ul class="dropdown-menu text-center" aria-labelledby="menuDropdownMd">
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/transportista/pedidos"><i class="fas fa-box"></i> Pedidos</a></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/transportista/incidencia"><i class="fas fa-exclamation-triangle"></i> Incidencias</a></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/transportista/perfil"><i class="fas fa-user-circle"></i> Mi Perfil</a></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/cerrarSesion"><i class="fas fa-sign-out-alt"></i> Salir</a></li>
                                    </ul>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <li class="nav-item d-none d-lg-block flex-grow-1"></li>
                    <li class="nav-item d-none d-lg-block text-center">
                        <a href="${pageContext.request.contextPath}/transportista/rutas" class="nav-link">
                            <i class="fas fa-route"></i> Rutas
                        </a>
                    </li>
                    <li class="nav-item d-none d-lg-block text-center">
                        <a href="${pageContext.request.contextPath}/transportista/pedidos" class="nav-link">
                            <i class="fas fa-box"></i> Pedidos
                        </a>
                    </li>
                    <li class="nav-item d-none d-lg-block text-center">
                        <div class="dropdown d-inline-block">
                            <a href="#" class="nav-link dropdown-toggle" id="menuDropdownLg" data-bs-toggle="dropdown" aria-expanded="false">
                                <i class="fas fa-list-ul"></i> Menú
                            </a>
                            <ul class="dropdown-menu" aria-labelledby="menuDropdownLg">
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/transportista/incidencia"><i class="fas fa-exclamation-triangle"></i> Incidencias</a></li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/transportista/perfil"><i class="fas fa-user-circle"></i> Mi Perfil</a></li>
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
    <div class="panel-container container-fluid">
        <c:if test="${not empty error}">
            <div id="error-alert" class="alert alert-danger alert-dismissible fade show" role="alert">
              <i class="fas fa-exclamation-circle"></i> ${error}
              <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
            </div>
            <c:remove var="error" scope="session"/>
          </c:if>
      <div class="d-flex justify-content-between align-items-center mb-3">
        <h2>Mis Rutas Activas</h2>
      </div>
      <c:choose>
        <c:when test="${empty rutas}">
          No tienes rutas activas.
        </c:when>
        <c:otherwise>
          <table class="table table-striped table-rutas">
            <thead>
              <tr>
                <th>Origen</th>
                <th>Destino</th>
                <th>Ruta Google Maps</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="ruta" items="${rutas}">
                <tr>
                  <td>${ruta.origen}</td>
                  <td>${ruta.destino}</td>
                  <td>
                    <a href="#" onclick="mostrarMapaEnFila(this, '${ruta.origen}', '${ruta.destino}'); return false;" 
                        class="btn btn-accion-azul btn-sm">
                        <i class="fas fa-map-marked-alt"></i> Ver ruta
                    </a>
                    <a target="_blank"
                        href="https://www.google.com/maps/dir/?api=1&origin=${ruta.origen}&destination=${ruta.destino}&travelmode=driving"
                        class="btn btn-accion-azul btn-sm">
                        <i class="fas fa-location-arrow"></i> En Google Maps
                    </a>
                </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
          <!-- Script de Google Maps con tu API Key -->
          <script src="https://maps.googleapis.com/maps/api/js?key=${googleMapsApiKey}"></script>
        </c:otherwise>
      </c:choose>
    </div>

    <!-- Footer -->
    <footer>
        <p>&copy; <%= Year.now() %> EnvioGo - Todos los derechos reservados</p>
    </footer>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/pedidoTransportista.js"></script>
</body>
</html>