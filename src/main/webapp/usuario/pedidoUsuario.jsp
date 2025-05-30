<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.Year" %>
<%@ page import="com.andres.gestionalmacen.dtos.PedidoRespuestaDto" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    List<PedidoRespuestaDto> pedidosList = (List<PedidoRespuestaDto>) request.getAttribute("pedidosList");
    if (pedidosList == null) {
        pedidosList = new java.util.ArrayList<>();
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Mis Pedidos</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- FontAwesome para iconos -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <!-- Estilos propios -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/usuario/pedidoUsuario.css">
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
                            <a class="nav-link text-center" href="${pageContext.request.contextPath}/usuario/pedido">
                                <i class="fas fa-box"></i> Mis Pedidos
                            </a>
                        </li>
                        <li class="nav-item d-none d-md-block d-lg-none mx-2">
                            <div class="dropdown">
                                <a href="#" class="nav-link dropdown-toggle text-center" id="menuDropdownMd" data-bs-toggle="dropdown" aria-expanded="false">
                                    <i class="fas fa-list-ul"></i> Menú
                                </a>
                                <ul class="dropdown-menu text-center" aria-labelledby="menuDropdownMd">
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/usuario/alquiler"><i class="fas fa-warehouse"></i> Mis Alquileres</a></li>
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
                    <a href="${pageContext.request.contextPath}/usuario/pedido" class="nav-link">
                        <i class="fas fa-box"></i> Mis Pedidos
                    </a>
                </li>
                <li class="nav-item d-none d-lg-block text-center">
                    <a href="${pageContext.request.contextPath}/usuario/alquiler" class="nav-link">
                        <i class="fas fa-warehouse"></i> Mis Alquileres
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
        <h2>Mis Pedidos</h2>
    </div>
    <!-- Tabla de pedidos -->
    <div class="table-responsive">
        <table class="table table-striped table-hover">
        <%-- La alerta de "No tienes pedidos en envío" solo aparece si hayEnviando es falso --%>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Estado</th>
                    <th>Fecha</th>
                    <th>Cantidad</th>
                </tr>
            </thead>
            <tbody>
                <% for (PedidoRespuestaDto pedido : pedidosList) { %>
                    <tr>
                        <td><%= pedido.getId() %></td>
                        <td><span class="badge bg-secondary"><%= pedido.getEstadoPedido() %></span></td>
                        <td><%= pedido.getFechaPedidoStr() %></td>
                        <td><%= pedido.getCantidad() %></td>
                    </tr>
                <% } %>
            </tbody>
        </table>
        <c:if test="${!hayEnviando}">
    <div class="alert alert-info mt-3" role="alert">
        <i class="fas fa-info-circle"></i> No tienes pedidos en envío actualmente.
    </div>
</c:if>
    </div>
</div>

<!-- Footer -->

<footer class="footer">
    <p>&copy; <%= Year.now() %> EnvioGo - Todos los derechos reservados</p>
</footer>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>