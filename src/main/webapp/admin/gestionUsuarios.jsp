<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.Year" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>

<%@ page import="com.andres.gestionalmacen.dtos.UsuarioDto" %>


<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Gestión de Usuarios</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <!-- Estilos propios -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/gestionUsuarios.css">
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
                  <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/perfil"><i class="fas fa-user-circle"></i> Mi Perfil</a></li>
                  <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/usuarios"> <i class="fas fa-users"></i> Usuarios</a></li>
                  <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/RespaldoBBDD"><i class="fas fa-database"></i> RespaldoBBDD</a></li>
                  <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/incidencia"><i class="fas fa-exclamation-triangle"></i> Incidencias</a></li>
                  <li><a class="dropdown-item" href="${pageContext.request.contextPath}/cerrarSesion"><i class="fas fa-sign-out-alt"></i> Salir</a></li>
              </ul>
            </div>
        </div>
        <div class="collapse navbar-collapse justify-content-end" id="navbarOperario">
            <ul class="navbar-nav ms-auto align-items-center w-100">
                <div class="collapse navbar-collapse justify-content-center" id="navbarOperario">
                    <ul class="navbar-nav flex-row w-100 justify-content-center align-items-center">
                        <li class="nav-item d-none d-md-block d-lg-none mx-2">
                            <a class="nav-link text-center" href="${pageContext.request.contextPath}/admin/incidencia">
                              <i class="fas fa-exclamation-triangle"></i> Incidencias
                            </a>
                        </li>
                        <li class="nav-item d-none d-md-block d-lg-none mx-2">
                            <div class="dropdown">
                                <a href="#" class="nav-link dropdown-toggle text-center" id="menuDropdownMd" data-bs-toggle="dropdown" aria-expanded="false">
                                    <i class="fas fa-list-ul"></i> Menú
                                </a>
                                <ul class="dropdown-menu text-center" aria-labelledby="menuDropdownMd">
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/usuarios"><i class="fas fa-users"></i> Usuarios</a></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/RespaldoBBDD"><i class="fas fa-database"></i> RespaldoBBDD</a></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/perfil"><i class="fas fa-user-circle"></i> Mi Perfil</a></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/cerrarSesion"><i class="fas fa-sign-out-alt"></i> Salir</a></li>
                                </ul>
                            </div>
                        </li>
                    </ul>
                </div>
                <li class="nav-item d-none d-lg-block flex-grow-1"></li>
                <li class="nav-item d-none d-lg-block text-center">
                    <a href="${pageContext.request.contextPath}/admin/usuarios" class="nav-link">
                        <i class="fas fa-users"></i> Usuarios
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
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/RespaldoBBDD"><i class="fas fa-database"></i> RespaldoBBDD</a></li>
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
    <div class="panel-container container-fluid">
        <%-- Mensaje de error si existe --%>
        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-triangle"></i> ${error}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <c:remove var="error" scope="session"/>
        </c:if>
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h2>Gestión de Usuarios</h2>
            <button class="btn btn-nueva" data-bs-toggle="modal" data-bs-target="#modalCrearUsuario">
                <i class="fas fa-user-plus"></i> Nuevo Usuario
            </button>
        </div>

        <div class="card">
            <div class="card-body">
                <c:choose>
                    <c:when test="${empty paginacion.contenido}">
                        <div class="empty-state">
                            <i class='fas fa-users-slash'></i>
                            <h3>No hay usuarios registrados</h3>
                            <p>Actualmente no hay usuarios en el sistema.</p>
                            <button class="btn btn-nueva" data-bs-toggle="modal" data-bs-target="#modalCrearUsuario">
                                <i class="fas fa-user-plus"></i> Crear Primer Usuario
                            </button>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="table table-inci">
                                <thead>
                                    <tr>
                                        <th>Foto</th>
                                        <th>ID</th>
                                        <th>Nombre Completo</th>
                                        <th>Correo</th>
                                        <th>Móvil</th>
                                        <th>Rol</th>
                                        <th>Fecha Creación</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${paginacion.contenido}" var="usuario">
                                        <tr>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty usuario.fotoBase64}">
                                                        <img src="data:image/jpeg;base64,${usuario.fotoBase64}"
                                                             alt="Foto de ${usuario.nombreCompleto}"
                                                             class="rounded-circle"
                                                             style="width: 50px; height: 50px; object-fit: cover;"
                                                             loading="lazy">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img src="${pageContext.request.contextPath}/img/sinUsu.png"
                                                             alt="Foto por defecto"
                                                             class="rounded-circle"
                                                             style="width: 50px; height: 50px; object-fit: cover;">
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>${usuario.id}</td>
                                            <td class="td-descripcion" title="${usuario.nombreCompleto}">
                                                <div class="descripcion-scroll">${usuario.nombreCompleto}</div>
                                            </td>
                                            <td>${usuario.correoElectronico}</td>
                                            <td>${usuario.movil}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${usuario.rolId == 1}">Administrador</c:when>
                                                    <c:when test="${usuario.rolId == 2}">Gerente</c:when>
                                                    <c:when test="${usuario.rolId == 3}">Operario de almacén</c:when>
                                                    <c:when test="${usuario.rolId == 4}">Usuario</c:when>
                                                    <c:when test="${usuario.rolId == 5}">Transportista</c:when>
                                                    <c:otherwise>Rol desconocido</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td><fmt:formatDate value="${usuario.fechaCreacionDate}" pattern="dd-MM-yyyy HH:mm"/></td>
                                            <td>
                                                <button type="button" class="btn btn-accion-modificar"
                                                        data-bs-toggle="modal" 
                                                        data-bs-target="#modalModificarUsuario"
                                                        onclick="editarUsuario('${usuario.id}', '${usuario.nombreCompleto}', '${usuario.correoElectronico}', '${usuario.movil}', '${usuario.rolId}', '${usuario.fotoBase64}')">
                                                    <i class="fas fa-edit"></i> 
                                                </button>
                                                <button type="button" class="btn btn-accion-eliminar" 
                                                        data-bs-toggle="modal" 
                                                        data-bs-target="#modalEliminarUsuario" 
                                                        data-usuario-id="${usuario.id}">
                                                    <i class="fas fa-trash"></i> 
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                        <!-- Controles de paginación -->
                        <c:if test="${not empty paginacion && paginacion.totalPaginas > 1}">
                            <nav aria-label="Paginación de usuarios">
                                <ul class="pagination justify-content-center">
                                    <c:if test="${paginacion.numero > 0}">
                                        <li class="page-item">
                                            <a class="page-link" href="?page=${paginacion.numero - 1}" aria-label="Anterior">
                                                <span aria-hidden="true">&laquo;</span>
                                            </a>
                                        </li>
                                    </c:if>
                                    <c:forEach var="i" begin="0" end="${paginacion.totalPaginas - 1}">
                                        <li class="page-item ${i == paginacion.numero ? 'active' : ''}">
                                            <a class="page-link" href="?page=${i}">${i + 1}</a>
                                        </li>
                                    </c:forEach>
                                    <c:if test="${paginacion.numero < paginacion.totalPaginas - 1}">
                                        <li class="page-item">
                                            <a class="page-link" href="?page=${paginacion.numero + 1}" aria-label="Siguiente">
                                                <span aria-hidden="true">&raquo;</span>
                                            </a>
                                        </li>
                                    </c:if>
                                </ul>
                            </nav>
                        </c:if>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- Modal para crear usuario -->
    <div class="modal fade" id="modalCrearUsuario" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Crear Nuevo Usuario</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form action="${pageContext.request.contextPath}/admin/usuarios/crear" method="POST" enctype="multipart/form-data">
                        <input type="hidden" name="action" value="crear">
                        <div class="mb-3">
                            <label for="nombreCompleto" class="form-label">Nombre Completo</label>
                            <input type="text" class="form-control" name="nombreCompleto" required maxlength="50">
                            <div class="form-text">Máximo 50 caracteres</div>
                        </div>
                        <div class="mb-3">
                            <label for="movil" class="form-label">Móvil</label>
                            <input type="tel" class="form-control" name="movil" required pattern="[0-9]{9}" maxlength="9" placeholder="Ej: 612345678" title="Introduce un número de móvil válido de 9 dígitos">
                            <div class="form-text">Formato: exactamente 9 dígitos (Ejemplo: 612345678)</div>
                        </div>
                        <div class="mb-3">
                            <label for="correoElectronico" class="form-label">Correo Electrónico</label>
                            <input type="email" class="form-control" name="correoElectronico" required maxlength="50">
                            <div class="form-text">Máximo 50 caracteres</div>
                        </div>
                        <div class="mb-3">
                            <label for="contrasena" class="form-label">Contraseña</label>
                            <div class="input-group">
                                <input type="password" class="form-control" name="contrasena" id="contrasena" required>
                                <button class="btn btn-outline-secondary" type="button" id="botonMostrarContrasena" onclick="mostrarContrasena('contrasena', this)">
                                    <i class="fas fa-eye" id="iconoOjo"></i>
                                </button>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label for="rolId" class="form-label">Rol</label>
                            <select class="form-select" name="rolId" required>
                                <option value="" selected disabled>Seleccione un rol</option>
                                <option value="1">Administrador</option>
                                <option value="2">Gerente</option>
                                <option value="3">Operario de almacén</option>
                                <option value="5">Transportista</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="foto" class="form-label">Foto</label>
                            <input type="file" class="form-control" name="foto" id="foto" accept="image/*" onchange="mostrarVistaPrevia(event)">
                            <!-- Vista previa de la imagen -->
                            <div class="mt-3 text-center">
                                <img id="vistaPrevia" src="#" alt="Vista previa de la imagen" style="max-width: 100%; max-height: 200px; display: none;">
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-accion-cancelar" data-bs-dismiss="modal">Cancelar</button>
                            <button type="submit" class="btn btn-accion-aceptar">Crear Usuario</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal Modificar Usuario -->
    <div class="modal fade" id="modalModificarUsuario" tabindex="-1" aria-labelledby="modalModificarUsuarioLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="modalModificarUsuarioLabel">Modificar Usuario</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="formModificarUsuario" action="${pageContext.request.contextPath}/admin/usuarios/modificar" method="POST" enctype="multipart/form-data">
                        <input type="hidden" id="usuarioId" name="id">
                        <input type="hidden" name="JSESSIONID" value="${pageContext.session.id}">
                        
                        <div class="mb-3">
                            <label for="nombreCompleto" class="form-label">Nombre Completo</label>
                            <input type="text" class="form-control" id="nombreCompleto" name="nombreCompleto" required maxlength="50">
                            <div class="form-text">Máximo 50 caracteres</div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="correoElectronico" class="form-label">Correo Electrónico</label>
                            <input type="email" class="form-control" id="correoElectronico" name="correoElectronico" required maxlength="50">
                            <div class="form-text">Máximo 50 caracteres</div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="movil" class="form-label">Móvil</label>
                            <input type="tel" class="form-control" id="movil" name="movil" 
                                required 
                                pattern="[0-9]{9}"
                                maxlength="9"
                                placeholder="Ej: 612345678"
                                title="Introduce un número de móvil válido de 9 dígitos">
                            <div class="form-text">Formato: exactamente 9 dígitos (Ejemplo: 612345678)</div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="rolId" class="form-label">Rol</label>
                            <select class="form-select" id="rolId" name="rolId" required>
                                <option value="" selected disabled>Seleccione un rol</option>
                                <option value="1">Administrador</option>
                                <option value="2">Gerente</option>
                                 <option value="3">Operario de almacén</option>
                                 <option value="4">Usuario</option>
                                 <option value="5">Transportista</option>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label for="foto" class="form-label">Foto</label>
                            <input type="file" class="form-control" id="foto" name="foto" accept="image/*" onchange="mostrarVistaPreviaModificar(event)">
                            <!-- Vista previa de la imagen -->
                            <div id="contenedorFoto" class="mt-2">
                                <img id="imagenActual" src="" alt="Foto actual" style="max-width: 100px; display: none;" class="img-thumbnail">
                                <img id="vistaPreviaModificar" src="#" alt="Vista previa" style="max-width: 100px; display: none;" class="img-thumbnail">
                            </div>
                        </div>
                        
                        <div class="modal-footer">
                            <button type="button" class="btn btn-accion-cancelar" data-bs-dismiss="modal">Cancelar</button>
                            <button type="submit" class="btn btn-accion-aceptar">Guardar Cambios</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal de Confirmación de Eliminación -->
    <div class="modal fade" id="modalEliminarUsuario" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header  ">
                    <h5 class="modal-title">Confirmar Eliminación de Usuario</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="formEliminarUsuario" action="${pageContext.request.contextPath}/admin/usuarios/eliminar" method="POST">
                        <input type="hidden" id="usuarioIdEliminar" name="id">
                        
                        <div class="mb-3">
                            <div class="text-center mb-2">
                                <i class='bx bx-error-circle' style="color:var(--rojo-coral); font-size:2em;"></i><br>
                                <span style="color:var(--rojo-coral); font-size:1.2em; font-weight:bold;">Advertencia: Esta acción no se puede deshacer.</span>
                            </div>
                            <label for="confirmacionId" class="form-label">
                                Para confirmar, escriba el ID del usuario seguido de la palabra <b>eliminar</b>: <span id="idUsuarioAEliminar" class="fw-bold"></span>
                            </label>
                            <input type="text" class="form-control" id="confirmacionId" name="confirmacionId" 
                                required 
                                pattern="[0-9]+ eliminar"
                                placeholder="Ej: 83 eliminar"
                                title="Debe escribir el ID seguido de la palabra eliminar. Ejemplo: 83 eliminar">
                            <div class="invalid-feedback">
                                Debe escribir el ID seguido de la palabra eliminar (Ejemplo: 83 eliminar).
                            </div>
                        </div>
                        
                        <div class="modal-footer">
                            <button type="button" class="btn btn-accion-cancelar" data-bs-dismiss="modal">Cancelar</button>
                            <button type="submit" class="btn btn-accion-aceptar" id="btnConfirmarEliminar" disabled>
                                <i class='bx bx-trash'></i> Eliminar Usuario
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <footer>
        <p>&copy; <%= Year.now() %> EnvioGo - Todos los derechos reservados</p>
    </footer>

    <!-- Bootstrap y Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Custom JS -->
    <script src="${pageContext.request.contextPath}/js/gestionUsuarios.js"></script>
</body>
</html>