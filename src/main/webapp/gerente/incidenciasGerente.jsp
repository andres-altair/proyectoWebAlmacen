<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.andres.gestionalmacen.dtos.UsuarioDto" %>
<%@ page import="com.andres.gestionalmacen.utilidades.ImagenUtil" %>
<%@ page import="java.time.Year" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Panel de Gerente</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- FontAwesome para iconos -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <!-- Estilos propios -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/gerente/incidenciaGerente.css">
</head>
<body>
<!-- Barra de Navegación Responsive (idéntica a incidenciasUsuario.jsp, enlaces de gerente) -->
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
                  <li><a class="dropdown-item" href="${pageContext.request.contextPath}/gerente/inventario"><i class="fas fa-boxes-stacked"></i> Inventario</a></li>
                  <li><a class="dropdown-item" href="${pageContext.request.contextPath}/gerente/incidencia"><i class="fas fa-exclamation-triangle"></i> Incidencias</a></li>
                  <li><a class="dropdown-item" href="${pageContext.request.contextPath}/gerente/actividad"><i class="fas fa-tasks"></i> Actividades</a></li>
                  <li><a class="dropdown-item" href="${pageContext.request.contextPath}/gerente/perfil"><i class="fas fa-user-circle"></i> Mi Perfil</a></li>
                  <li><a class="dropdown-item" href="${pageContext.request.contextPath}/cerrarSesion"><i class="fas fa-sign-out-alt"></i> Salir</a></li>
              </ul>
          </div>
      </div>
      <div class="collapse navbar-collapse justify-content-end" id="navbarUser">
          <ul class="navbar-nav ms-auto align-items-center w-100">
              <div class="collapse navbar-collapse justify-content-center" id="navbarUser">
                  <ul class="navbar-nav flex-row w-100 justify-content-center align-items-center">
                      <li class="nav-item d-none d-md-block d-lg-none mx-2">
                          <a class="nav-link text-center" href="${pageContext.request.contextPath}/gerente/incidencia">
                            <i class="fas fa-exclamation-triangle"></i> Incidencias
                          </a>
                      </li>
                      <li class="nav-item d-none d-md-block d-lg-none mx-2">
                          <div class="dropdown">
                              <a href="#" class="nav-link dropdown-toggle text-center" id="menuDropdownMd" data-bs-toggle="dropdown" aria-expanded="false">
                                  <i class="fas fa-list-ul"></i> Menú
                              </a>
                              <ul class="dropdown-menu text-center" aria-labelledby="menuDropdownMd">
                                  <li><a class="dropdown-item" href="${pageContext.request.contextPath}/gerente/inventario"><i class="fas fa-boxes-stacked"></i> Inventario</a></li>
                                  <li><a class="dropdown-item" href="${pageContext.request.contextPath}/gerente/actividad"><i class="fas fa-tasks"></i> Actividades</a></li>
                                  <li><a class="dropdown-item" href="${pageContext.request.contextPath}/gerente/perfil"><i class="fas fa-user-circle"></i> Mi Perfil</a></li>
                                  <li><a class="dropdown-item" href="${pageContext.request.contextPath}/cerrarSesion"><i class="fas fa-sign-out-alt"></i> Salir</a></li>
                              </ul>
                          </div>
                      </li>
                  </ul>
              </div>
              <li class="nav-item d-none d-lg-block flex-grow-1"></li>
              <li class="nav-item d-none d-lg-block text-center">
                  <a href="${pageContext.request.contextPath}/gerente/incidencia" class="nav-link">
                    <i class="fas fa-exclamation-triangle"></i> Incidencias
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
                          <li><a class="dropdown-item" href="${pageContext.request.contextPath}/gerente/perfil"><i class="fas fa-user-circle"></i> Mi Perfil</a></li>
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
<c:if test="${not empty error}">
  <div class="alert alert-danger alert-dismissible fade show" role="alert">
    ${error}
    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
  </div>
  <c:remove var="error" scope="session"/>
</c:if>
    <!-- Contenedor Principal -->
   <div class="panel-container container-fluid">
     <div class="d-flex justify-content-between align-items-center mb-3">
        <h2>Incidencias del Gerente</h2>
        <button class="btn btn-nueva" data-bs-toggle="modal" data-bs-target="#modalNuevaIncidencia">
  <i class="fas fa-plus"></i> Nueva Incidencia
</button>
     </div>
     <!-- Tabla de incidencias -->
     <div class="table-responsive">
        <table class="table table-striped table-hover">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Descripción</th>
                    <th>Fecha</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="incidencia" items="${incidencias}">
                    <tr>
                        <td>${incidencia.id}</td>
                        <td class="td-descripcion" title="${incidencia.descripcion}">
                          <div class="descripcion-scroll">${incidencia.descripcion}</div></td>
                        <td><fmt:formatDate value="${incidencia.fechaCreacionDate}" pattern="dd-MM-yyyy HH:mm"/></td>
                        <td>
                            <span class="badge bg-secondary">${incidencia.estado}</span>
                        </td>
                        <td>
                            <button class="btn btn-sm btn-warning" data-bs-toggle="modal" data-bs-target="#modalEstadoIncidencia${incidencia.id}">
                                <i class="fas fa-edit"></i> Cambiar Estado
                            </button>
                        </td>
                    </tr>
                    <!-- Modal para cambiar estado -->
                   <div class="modal fade" id="modalEstadoIncidencia${incidencia.id}" tabindex="-1" aria-labelledby="modalEstadoIncidenciaLabel${incidencia.id}" aria-hidden="true">
                    <div class="modal-dialog">
                      <div class="modal-content">
                        <form method="post" action="${pageContext.request.contextPath}/gerente/incidencia">
                          <input type="hidden" name="accion" value="cambiarEstado" />
                          <div class="modal-header">
                            <h5 class="modal-title" id="modalEstadoIncidenciaLabel${incidencia.id}">Cambiar Estado de Incidencia</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                          </div>
                          <div class="modal-body">
                            <input type="hidden" name="id" value="${incidencia.id}" />
                            <div class="mb-3">
                              <label for="estado" class="form-label">Nuevo Estado</label>
                              <select class="form-select" name="estado" required>
                                <option value="" selected disabled>Seleccione un estado</option>
                                <option value="pendiente">Pendiente</option>
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
                </c:forEach>
            </tbody>
        </table>
     </div>
   </div>

   <!-- Modal Nueva Incidencia -->
   <div class="modal fade" id="modalNuevaIncidencia" tabindex="-1" aria-labelledby="modalNuevaIncidenciaLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <form method="post" action="${pageContext.request.contextPath}/gerente/incidencia">
            <input type="hidden" name="accion" value="crear" />
            <div class="modal-header">
              <h5 class="modal-title" id="modalNuevaIncidenciaLabel">Nueva Incidencia</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
              <div class="mb-3">
                <label for="descripcion" class="form-label">Descripción</label>
                <textarea class="form-control" name="descripcion" required></textarea>
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-accion-gris" data-bs-dismiss="modal">Cancelar</button>
              <button type="submit" class="btn btn-nueva">Crear Incidencia</button>
            </div>
          </form>
        </div>
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
