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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/gerente/inventario.css">
</head>
<body>
    <!-- Navbar idéntico al de perfilGerente.jsp -->
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
                                <a class="nav-link text-center" href="${pageContext.request.contextPath}/gerente/inventario">
                                    <i class="fas fa-boxes-stacked"></i> Inventario
                                </a>
                            </li>
                            <li class="nav-item d-none d-md-block d-lg-none mx-2">
                                <div class="dropdown">
                                    <a href="#" class="nav-link dropdown-toggle text-center" id="menuDropdownMd" data-bs-toggle="dropdown" aria-expanded="false">
                                        <i class="fas fa-list-ul"></i> Menú
                                    </a>
                                    <ul class="dropdown-menu text-center" aria-labelledby="menuDropdownMd">
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/gerente/perfil"><i class="fas fa-user-circle"></i> Mi Perfil</a></li>
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
                        <a href="${pageContext.request.contextPath}/gerente/inventario" class="nav-link">
                            <i class="fas fa-boxes-stacked"></i> Inventario
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
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/gerente/perfil"><i class="fas fa-user-circle"></i> Mi Perfil</a></li>
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

    <!-- Contenedor Principal -->
   <div class="panel-container">
    <body>
      <%-- Mostrar errores tanto del atributo como del parámetro URL --%>
      <% String errorMsg = request.getParameter("error");
         if (errorMsg == null) errorMsg = (String) request.getAttribute("error");
         if (errorMsg != null && !errorMsg.isEmpty()) { %>
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <%= errorMsg %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
        </div>
        <c:remove var="error" scope="session"/>
      <% } %>
      <div class="d-flex justify-content-between align-items-center mb-3">
        <h2>Inventario de Productos</h2>
        <!-- Botón para acción global si lo necesitas -->
      </div>
      <div class="table-responsive mt-4">
        <table class="table table-striped table-hover align-middle">
      <thead class="table-dark">
        <tr>
          <th>ID</th>
          <th>Nombre</th>
          <th>Descripción</th>
          <th>Cantidad</th>
          <th>Fecha creación</th>
          <th>Recuento</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="prod" items="${productos}">
          <tr>
            <td>${prod.id}</td>
            <td>${prod.nombre}</td>
            <td>${prod.descripcion}</td>
            <td>${prod.cantidad}</td>
            <td>${prod.fechaCreacionStr}</td>
            <td>
              <button type="button" class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#recuentoModal" data-producto-id="${prod.id}" data-producto-nombre="${prod.nombre}">
                <i class="fas fa-clipboard-check"></i> Recuento
              </button>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
     <!-- Modal para recuento -->
     <div class="modal fade" id="recuentoModal" tabindex="-1" aria-labelledby="recuentoModalLabel" aria-hidden="true">
       <div class="modal-dialog">
         <div class="modal-content">
           <form method="post" action="${pageContext.request.contextPath}/gerente/inventario">
             <div class="modal-header">
               <h5 class="modal-title" id="recuentoModalLabel">Registrar recuento de inventario</h5>
               <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
             </div>
             <div class="modal-body">
               <input type="hidden" name="producto_id" id="modalProductoId">
               <div class="mb-3">
                 <label for="cantidadContada" class="form-label">Cantidad contada</label>
                 <input type="number" class="form-control" id="cantidadContada" name="cantidad_contada" required min="0">
               </div>
               <div class="mb-3">
                 <label for="observaciones" class="form-label">Observaciones (opcional)</label>
                 <textarea class="form-control" id="observaciones" name="observaciones" maxlength="255" rows="2" placeholder="maximo 255 caracteres"></textarea>
               </div>
             </div>
             <div class="modal-footer">
               <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
               <button type="submit" class="btn btn-primary">Aceptar</button>
             </div>
           </form>
         </div>
       </div>
     </div>
   </div>
   <script>
     var recuentoModal = document.getElementById('recuentoModal');
     recuentoModal.addEventListener('show.bs.modal', function (event) {
       var button = event.relatedTarget;
       var productoId = button.getAttribute('data-producto-id');
       document.getElementById('modalProductoId').value = productoId;
     });
   </script>

   <!-- Historial de recuentos de inventario -->
   <div class="panel-container">
     <div class="d-flex justify-content-between align-items-center mb-3">
       <h2>Historial de Recuentos</h2>
     </div>
     <div class="table-responsive mt-4">
       <table class="table table-striped table-hover align-middle">
         <thead class="table-dark">
           <tr>
             <th>ID</th>
             <th>Producto</th>
             <th>Cantidad Contada</th>
             <th>Gerente</th>
             <th>Fecha Recuento</th>
             <th>Observaciones</th>
           </tr>
         </thead>
         <tbody>
           <c:forEach var="rec" items="${recuentos}">
             <tr>
               <td>${rec.id}</td>
               <td>${rec.nombreProducto}</td>
               <td>${rec.cantidadContada}</td>
               <td class="td-descripcion" title="${rec.nombreGerente}">
                <div class="descripcion-scroll">${rec.nombreGerente}</div>
               </td>
               <td>${rec.fechaRecuentoStr}</td>
               <td class="td-descripcion" title="${rec.observaciones}">
                 <div class="descripcion-scroll">${rec.observaciones}</div>
               </td>
             </tr>
           </c:forEach>
         </tbody>
       </table>
     </div>
   </div>

      <!-- Footer idéntico al de perfilGerente.jsp -->
    <footer class="footer">
        <p>&copy; <%= Year.now() %> EnvioGo - Todos los derechos reservados</p>
    </footer>


    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>