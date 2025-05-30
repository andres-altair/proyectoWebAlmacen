<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.time.Year" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Pedidos Operario</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/operario/pedidoOperario.css">
</head>
<body>
    <!-- Barra de Navegación Responsive (idéntica a panelOperario.jsp) -->
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
                                    <i class="fas fa-user-circle"></i> Pedidos
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
        <h2>Pedidos pendientes</h2>
      </div>
        <c:choose>
            <c:when test="${empty pedidosPendientes}">
                No hay pedidos pendientes en este momento.
            </c:when>
            <c:otherwise>
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Producto</th>
                            <th>Producto ID</th>
                            <th>Cantidad</th>
                            <th>Acción</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="pedido" items="${pedidosPendientes}">
                            <tr>
                                <td>${pedido.id}</td>
                                <td>${pedido.nombreProducto}</td>
                                <td>${pedido.productoId}</td>
                                <td>${pedido.cantidad}</td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/operario/pedidos" method="post">
                                        <input type="hidden" name="accion" value="asignarOperario">
                                        <input type="hidden" name="pedidoId" value="${pedido.id}">
                                        <input type="hidden" name="usuarioId" value="${sessionScope.usuario.id}">
                                        <button type="submit" class="btn btn-accion-azul btn-sm">Asignarme</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
        <hr class="my-4 hr-azul-marino">
        <h2>Mis pedidos en proceso</h2>
        <c:choose>
            <c:when test="${empty pedidosEnProceso}">
                No tienes pedidos en proceso actualmente.
            </c:when>
            <c:otherwise>
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Producto</th>
                            <th>Producto ID</th>
                            <th>Cantidad</th>
                            <th>Acción</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="pedido" items="${pedidosEnProceso}">
                            <tr>
                                <td>${pedido.id}</td>
                                <td>${pedido.nombreProducto}</td>
                                <td>${pedido.productoId}</td>
                                <td>${pedido.cantidad}</td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/operario/pedidos" method="post">
                                        <input type="hidden" name="accion" value="marcarProcesado">
                                        <input type="hidden" name="pedidoId" value="${pedido.id}">
                                        <input type="hidden" name="usuarioId" value="${sessionScope.usuario.id}">
                                        <button type="submit" class="btn btn-success btn-sm">Marcar como procesado</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
        <hr class="my-4 hr-azul-marino">
        <h2>Mis pedidos procesados</h2>
        <c:choose>
            <c:when test="${empty pedidosProcesados}">
                No tienes pedidos procesados actualmente.
            </c:when>
            <c:otherwise>
                <table class="table table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Producto</th>
                        <th>Producto ID</th>
                        <th>Cantidad</th>
                        <th>Estado</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="pedido" items="${pedidosProcesados}">
                        <tr>
                            <td>${pedido.id}</td>
                            <td>${pedido.nombreProducto}</td>
                            <td>${pedido.productoId}</td>
                            <td>${pedido.cantidad}</td>
                            <td>${pedido.estadoPedido}</td>
                        </tr>
                    </c:forEach>
                </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
    

    <!-- Footer -->
    <footer class="footer py-3" style="background-color: var(--azul-marino);">
        <div class="container-fluid">
            <div class="row">
                <div class="col-12 text-center">
                    <p class="mb-0 text-light">&copy; <%= Year.now() %> EnvioGo - Todos los derechos reservados</p>
                </div>
            </div>
        </div>
    </footer>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>