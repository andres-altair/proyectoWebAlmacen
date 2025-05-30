<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.Year" %>

<html>
<head>
    <title>Actividades para Operario</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/operario/actividadOperario.css">
</head>
<body>
    <!-- Barra de Navegación Responsive (idéntica a incidenciasOperario.jsp) -->
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
            <div class="collapse navbar-collapse justify-content-end" id="navbarOperario">
                <ul class="navbar-nav ms-auto align-items-center w-100">
                    <div class="collapse navbar-collapse justify-content-center" id="navbarOperario">
                        <ul class="navbar-nav flex-row w-100 justify-content-center align-items-center">
                            <li class="nav-item d-none d-md-block d-lg-none mx-2">
                                <a class="nav-link text-center" href="${pageContext.request.contextPath}/operario/actividad">
                                  <i class="fas fa-tasks"></i> Actividades
                                </a>
                            </li>
                            <li class="nav-item d-none d-md-block d-lg-none mx-2">
                                <div class="dropdown">
                                    <a href="#" class="nav-link dropdown-toggle text-center" id="menuDropdownMd" data-bs-toggle="dropdown" aria-expanded="false">
                                        <i class="fas fa-list-ul"></i> Menú
                                    </a>
                                    <ul class="dropdown-menu text-center" aria-labelledby="menuDropdownMd">
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/operario/pedidos"><i class="fas fa-box"></i> Pedidos</a></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/operario/incidencia"><i class="fas fa-exclamation-triangle"></i> Incidencias</a></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/operario/perfil"><i class="fas fa-user-circle"></i> Mi Perfil</a></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/cerrarSesion"><i class="fas fa-sign-out-alt"></i> Salir</a></li>
                                    </ul>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <li class="nav-item d-none d-lg-block flex-grow-1"></li>
                    <li class="nav-item d-none d-lg-block text-center">
                        <a href="${pageContext.request.contextPath}/operario/actividad" class="nav-link">
                            <i class="fas fa-tasks"></i> Actividades
                        </a>
                    </li>
                    <li class="nav-item d-none d-lg-block text-center">
                      <a href="${pageContext.request.contextPath}/operario/pedidos" class="nav-link">
                          <i class="fas fa-box"></i> Pedidos
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

      
      <!-- Bloque de error con botón de quitar -->
      <c:if test="${not empty error}">
        <div id="error-alert" class="alert alert-danger alert-dismissible fade show" role="alert">
          <i class="fas fa-exclamation-circle"></i> ${error}
          <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
        </div>
        <c:remove var="error" scope="session"/>
      </c:if>
      <div class="d-flex justify-content-between align-items-center mb-3">
        <h2>Actividades disponibles</h2>
        <button class="btn btn-nueva d-none"><i class="fas fa-plus"></i> Nueva Actividad</button>
      </div>
      <div class="table-responsive">
        <table class="table table-striped table-hover">
          <thead>
            <tr>
              <th>ID</th>
              <th>Descripción</th>
              <th>Estado</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="actividad" items="${actividades}">
              <tr>
                <td>${actividad.id}</td>
                <td class="td-descripcion" title="${actividad.descripcion}">
                  <div class="descripcion-scroll">${actividad.descripcion}</div>
                </td>
                <td>
                  <span class="badge badge-estado ${actividad.estado}">${actividad.estado}</span>
                </td>
                <td>
                  <button class="btn btn-sm btn-warning" data-bs-toggle="modal" data-bs-target="#modalEstadoActividad${actividad.id}">
                    <i class="fas fa-edit"></i> Cambiar Estado
                  </button>
                  <!-- Modal para cambiar estado de actividad -->
                  <div class="modal fade" id="modalEstadoActividad${actividad.id}" tabindex="-1" aria-labelledby="modalEstadoActividadLabel${actividad.id}" aria-hidden="true">
                    <div class="modal-dialog">
                      <div class="modal-content">
                        <form method="post" action="${pageContext.request.contextPath}/operario/actividad">
                          <input type="hidden" name="actividadId" value="${actividad.id}" />
                          <div class="modal-header">
                            <h5 class="modal-title" id="modalEstadoActividadLabel${actividad.id}">Cambiar Estado de Actividad</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                          </div>
                          <div class="modal-body">
                            <div class="mb-3">
                              <label for="estado${actividad.id}" class="form-label">Nuevo Estado</label>
                              <select class="form-select" id="estado${actividad.id}" name="estado" required>
  <option value="" selected disabled>Seleccione un estado</option>
  <option value="en_proceso">En Proceso</option>
  <option value="completado">Completado</option>
</select>
                            </div>
                          </div>
                          <div class="modal-footer">
                            <button type="button" class="btn btn-accion-gris" data-bs-dismiss="modal">Cancelar</button>
                            <button type="submit" class="btn btn-accion-azul">Guardar Cambios</button>
                          </div>
                        </form>
                      </div>
                    </div>
                  </div>
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>
    </div>
    <!-- Footer -->
    <footer class="footer">
        <p>&copy; <%= java.time.Year.now() %> EnvioGo - Todos los derechos reservados</p>
    </footer>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
