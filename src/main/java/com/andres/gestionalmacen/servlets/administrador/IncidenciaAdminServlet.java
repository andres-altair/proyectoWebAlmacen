package com.andres.gestionalmacen.servlets.administrador;

import com.andres.gestionalmacen.dtos.IncidenciaDto;
import com.andres.gestionalmacen.dtos.UsuarioDto;
import com.andres.gestionalmacen.servicios.IncidenciaServicio;
import com.andres.gestionalmacen.utilidades.GestorRegistros;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
/**
 * Servlet que maneja las incidencias de usuarios.
 * @author Andrés
 */
@WebServlet("/admin/incidencia")
public class IncidenciaAdminServlet extends HttpServlet {
    private IncidenciaServicio incidenciaServicio = new IncidenciaServicio();
    /**
     * Método que maneja la petición GET para el panel de incidencia.
     * @author Andres
     * @param peticion La petición HTTP
     * @param respuesta La respuesta HTTP
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de entrada/salida
     */

    @Override
    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
        Object usuario = peticion.getSession().getAttribute("usuario");
        HttpSession sesion = peticion.getSession(false);
        if (usuario == null) {
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }
         UsuarioDto adminActual = (UsuarioDto) sesion.getAttribute("usuario");
        // Verificar si el usuario tiene permisos de administrador
        if (adminActual.getRolId() != 1) {
            GestorRegistros.warning(((com.andres.gestionalmacen.dtos.UsuarioDto) usuario).getId(), 
                "Intento no autorizado de acceso al panel de incidencias. Rol actual: " + ((com.andres.gestionalmacen.dtos.UsuarioDto) usuario).getRolId());
            respuesta.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
            return;
        }
        String filtro = peticion.getParameter("filtro");
        List<IncidenciaDto> incidencias;
        if ("todas".equals(filtro)) {
            try {
                incidencias = incidenciaServicio.obtenerTodas();
            } catch (Exception e) {
                peticion.setAttribute("error", e.getMessage());
                incidencias = java.util.Collections.emptyList();
            }
        }else if ("en_proceso".equals(filtro)) {
            incidencias = incidenciaServicio.obtenerEnProceso();
        } else if ("completadas".equals(filtro)) {
            incidencias = incidenciaServicio.obtenerCompletadas();
        } else if ("mias".equals(filtro)) {
            try {
                Long usuarioId = ((com.andres.gestionalmacen.dtos.UsuarioDto) usuario).getId();
                incidencias = incidenciaServicio.obtenerPorUsuario(usuarioId);
            } catch (Exception e) {
                peticion.setAttribute("error", e.getMessage());
                incidencias = java.util.Collections.emptyList();
            }
        } else {
            incidencias = incidenciaServicio.obtenerPendientes();
            filtro = "pendientes";
        }
        // Convertir LocalDateTime a java.util.Date para cada incidencia y mostrar fecha en jsp en dd/MM/yyyy HH:mm
        if (incidencias != null) {
            for (IncidenciaDto incidencia : incidencias) {
                if (incidencia.getFechaCreacion() != null) {
                    incidencia.setFechaCreacionDate(java.util.Date.from(incidencia.getFechaCreacion().atZone(java.time.ZoneId.systemDefault()).toInstant()));
                }
            }
        }
        peticion.setAttribute("incidencias", incidencias);
        peticion.setAttribute("filtroActual", filtro);
        peticion.getRequestDispatcher("/admin/incidenciasAdmin.jsp").forward(peticion, respuesta);
    }
    /**
    * Método que maneja las peticiones HTTP POST relacionadas con incidencias.
     * @param peticion  La solicitud HTTP del cliente.
     * @param respuesta La respuesta HTTP para el cliente.
     * @throws ServletException Si ocurre un error interno del servlet.
     * @throws IOException Si ocurre un error de entrada/salida.
    */
    @Override
    protected void doPost(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
        Object usuario = peticion.getSession().getAttribute("usuario");
        if (usuario == null) {
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }
        String accion = peticion.getParameter("accion");
        if ("crear".equals(accion)) {
    Long usuarioId = null;
    try {
        usuarioId = ((com.andres.gestionalmacen.dtos.UsuarioDto) peticion.getSession().getAttribute("usuario")).getId();
        String descripcion = peticion.getParameter("descripcion");
        IncidenciaDto dto = new IncidenciaDto();
        dto.setUsuarioId(usuarioId);
        dto.setDescripcion(descripcion);
        dto.setEstado(IncidenciaDto.Estado.pendiente);
        dto.setFechaCreacion(java.time.LocalDateTime.now());
        incidenciaServicio.crear(dto);
        GestorRegistros.info(usuarioId, "Incidencia creada por admin. Descripción: " + descripcion);
        respuesta.sendRedirect(peticion.getContextPath() + "/admin/incidencia");
        return;
    } catch (Exception e) {
        if (usuarioId != null) {
            GestorRegistros.error(usuarioId, "Error al crear incidencia: " + e.getMessage());
        } else {
            GestorRegistros.sistemaError("Error al crear incidencia (sin usuario) - IP: " + peticion.getRemoteAddr() + " - Error: " + e.getMessage());
        }
        peticion.setAttribute("error", e.getMessage());
        // Recargar incidencias para mostrar la vista correctamente
        String filtro = peticion.getParameter("filtro");
        List<IncidenciaDto> incidencias = obtenerIncidenciasPorFiltro(filtro, peticion);
        peticion.setAttribute("incidencias", incidencias);
        peticion.setAttribute("filtroActual", filtro != null ? filtro : "pendientes");
        peticion.getRequestDispatcher("/admin/incidenciasAdmin.jsp").forward(peticion, respuesta);
        return;
    }
} else if ("cambiarEstado".equals(accion)) {
    Long usuarioId = null;
    try {
        usuarioId = ((com.andres.gestionalmacen.dtos.UsuarioDto) peticion.getSession().getAttribute("usuario")).getId();
        Long id = Long.valueOf( peticion.getParameter("id"));
        String estado = peticion.getParameter("estado");
        incidenciaServicio.cambiarEstado(id, IncidenciaDto.Estado.valueOf(estado));
        GestorRegistros.info(usuarioId, "Estado de incidencia cambiado. Incidencia ID: " + id + ", nuevo estado: " + estado);
        respuesta.sendRedirect(peticion.getContextPath() + "/admin/incidencia");
        return;
    } catch (Exception e) {
        if (usuarioId != null) {
            GestorRegistros.error(usuarioId, "Error al cambiar estado de incidencia: " + e.getMessage());
        } else {
            GestorRegistros.sistemaError("Error al cambiar estado de incidencia (sin usuario) - IP: " + peticion.getRemoteAddr() + " - Error: " + e.getMessage());
        }
        peticion.setAttribute("error", e.getMessage());
        // Recargar incidencias para mostrar la vista correctamente
        String filtro = peticion.getParameter("filtro");
        List<IncidenciaDto> incidencias = obtenerIncidenciasPorFiltro(filtro, peticion);
        peticion.setAttribute("incidencias", incidencias);
        peticion.setAttribute("filtroActual", filtro != null ? filtro : "pendientes");
        peticion.getRequestDispatcher("/admin/incidenciasAdmin.jsp").forward(peticion, respuesta);
        return;
    }
} else {
            respuesta.sendRedirect(peticion.getContextPath() + "/admin/incidencia");
        }
    }

    /**
     * Método auxiliar que obtiene una lista de incidencias según un filtro proporcionado.
     * @author Andres
     * @param filtro    El filtro de incidencias a aplicar.
     * @param peticion  La solicitud HTTP, utilizada para obtener el usuario actual.
     * @return Una lista de objetos {@code IncidenciaDto} filtrados según el criterio dado.
     */
    private List<IncidenciaDto> obtenerIncidenciasPorFiltro(String filtro, HttpServletRequest peticion) {
        Object usuario = peticion.getSession().getAttribute("usuario");
        if ("todas".equals(filtro)) {
            try {
                return incidenciaServicio.obtenerTodas();
            } catch (Exception e) {
                // Puedes decidir qué hacer: devolver lista vacía o propagar el error
                // Aquí devuelvo lista vacía y podrías registrar el error si lo deseas
                return java.util.Collections.emptyList();
            }
        }else if ("en_proceso".equals(filtro)) {
            return incidenciaServicio.obtenerEnProceso();
        } else if ("completadas".equals(filtro)) {
            return incidenciaServicio.obtenerCompletadas();
        } else if ("mias".equals(filtro)) {
            try {
                Long usuarioId = ((com.andres.gestionalmacen.dtos.UsuarioDto) usuario).getId();
                return incidenciaServicio.obtenerPorUsuario(usuarioId);
            } catch (Exception e) {
                // Puedes decidir qué hacer: devolver lista vacía o propagar el error
                // Aquí devuelvo lista vacía y podrías registrar el error si lo deseas
                return java.util.Collections.emptyList();
            }
        } else {
            return incidenciaServicio.obtenerPendientes();
        }
    }
}

