package com.andres.gestionalmacen.servlets.operario;

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
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.time.ZoneId;
/**
 * Servlet que gestiona las incidencias del operario.
 * 
 * @author andres
 */
@WebServlet("/operario/incidencia")
public class IncidenciaOperarioServlet extends HttpServlet {
    private IncidenciaServicio incidenciaServicio = new IncidenciaServicio();
    /**
     * Maneja las peticiones GET para mostrar las incidencias del operario autenticado.
     * 
     * @param peticion objeto que contiene la petición HTTP
     * @param respuesta objeto que contiene la respuesta HTTP
     * @throws ServletException si ocurre un error en el servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
        UsuarioDto usuario = (UsuarioDto) peticion.getSession().getAttribute("usuario");
        if (usuario == null) {
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }HttpSession sesion = peticion.getSession(false);
        UsuarioDto operarioActual = (UsuarioDto) sesion.getAttribute("usuario");
        if (operarioActual.getRolId() != 3) {
            GestorRegistros.warning(operarioActual.getId(), 
                "Intento no autorizado de acceso a actividades de operario. Rol actual: " + operarioActual.getRolId());
            respuesta.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
            return;
        }
        // Obtener incidencias solo del operario actual
        try {
            List<IncidenciaDto> incidencias = incidenciaServicio.obtenerPorUsuario(usuario.getId());
            // Convertir LocalDateTime a java.util.Date para cada incidencia y mostrar fecha en jsp en dd/MM/yyyy HH:mm
            for (IncidenciaDto incidencia : incidencias) {
                incidencia.setFechaCreacionDate(Date.from(incidencia.getFechaCreacion().atZone(ZoneId.systemDefault()).toInstant()));
            }
            peticion.setAttribute("incidencias", incidencias);
            GestorRegistros.info(usuario.getId(), "Incidencias de operario mostradas correctamente.");
            peticion.getRequestDispatcher("/operario/incidenciasOperario.jsp").forward(peticion, respuesta);
        } catch (Exception e) {
            GestorRegistros.error(usuario.getId(), "Error al mostrar incidencias de operario: " + e.getMessage());
            // Devuelve lista vacía y muestra el error en la vista
            peticion.setAttribute("incidencias", java.util.Collections.emptyList());
            peticion.setAttribute("error", e.getMessage());
            peticion.getRequestDispatcher("/operario/incidenciasOperario.jsp").forward(peticion, respuesta);
        }
    }
    /**
     * Maneja las peticiones POST para crear o cambiar el estado de una incidencia del operario.
     * @param peticion objeto que contiene la petición HTTP
     * @param respuesta objeto que contiene la respuesta HTTP
     * @throws ServletException si ocurre un error en el servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doPost(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
        UsuarioDto usuario = (UsuarioDto) peticion.getSession().getAttribute("usuario");
        if (usuario == null) {
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }
        String accion = peticion.getParameter("accion");
        if ("crear".equals(accion)) {
            String descripcion = peticion.getParameter("descripcion");
            IncidenciaDto dto = new IncidenciaDto();
            dto.setUsuarioId(usuario.getId());
            dto.setDescripcion(descripcion);
            dto.setFechaCreacion(LocalDateTime.now());
            dto.setEstado(IncidenciaDto.Estado.pendiente);
            incidenciaServicio.crear(dto);
            GestorRegistros.info(usuario.getId(), "Incidencia creada por operario. Descripción: " + descripcion);
            respuesta.sendRedirect(peticion.getContextPath() + "/operario/incidencia");
        } else if ("cambiarEstado".equals(accion)) {
            Long id = Long.valueOf(peticion.getParameter("id"));
            String estado = peticion.getParameter("estado");
            try {
                incidenciaServicio.cambiarEstado(id, IncidenciaDto.Estado.valueOf(estado));
                GestorRegistros.info(usuario.getId(), "Estado de incidencia cambiado. Incidencia ID: " + id + ", Nuevo estado: " + estado);
            } catch (IllegalArgumentException e) {
                GestorRegistros.warning(usuario.getId(), "Valor de estado inválido recibido en cambio de incidencia: " + estado);
            }
            respuesta.sendRedirect(peticion.getContextPath() + "/operario/incidencia");
        }
    }
}
