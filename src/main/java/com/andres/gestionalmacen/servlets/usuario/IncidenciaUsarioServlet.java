package com.andres.gestionalmacen.servlets.usuario;

import com.andres.gestionalmacen.dtos.IncidenciaDto;
import com.andres.gestionalmacen.dtos.UsuarioDto;
import com.andres.gestionalmacen.servicios.IncidenciaServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import com.andres.gestionalmacen.utilidades.GestorRegistros;

/**
 * Servlet que gestiona incidencias del usuario.
 *
 * @author andres
 */
@WebServlet("/usuario/incidencia")
public class IncidenciaUsarioServlet extends HttpServlet {
    private IncidenciaServicio incidenciaServicio = new IncidenciaServicio();
    /**
     * Maneja las peticiones GET para mostrar las incidencias del usuario autenticado.
     *
     * @param peticion  Petición HTTP recibida
     * @param respuesta Respuesta HTTP a enviar
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
        UsuarioDto usuario = (UsuarioDto) peticion.getSession().getAttribute("usuario");
        if (usuario == null || usuario.getRolId()!=4)  {
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }
        // Obtener incidencias solo del usuario actual
        try {
            List<IncidenciaDto> incidencias = incidenciaServicio.obtenerPorUsuario(usuario.getId());
            // Conversión LocalDateTime a java.util.Date para JSTL y mostrar fecha en jsp en dd/MM/yyyy HH:mm
            for (IncidenciaDto incidencia : incidencias) {
                if (incidencia.getFechaCreacion() != null) {
                    incidencia.setFechaCreacionDate(java.util.Date.from(incidencia.getFechaCreacion().atZone(java.time.ZoneId.systemDefault()).toInstant()));
                }
            }
            GestorRegistros.info(usuario.getId(), "Consulta de incidencias de usuario realizada correctamente");
            peticion.setAttribute("incidencias", incidencias);
            peticion.getRequestDispatcher("/usuario/incidenciasUsuario.jsp").forward(peticion, respuesta);
        } catch (Exception e) {
            // Manejo de error: devolver lista vacía y mostrar mensaje
            GestorRegistros.error(usuario != null ? usuario.getId() : null, "Error al obtener incidencias: " + e.getMessage());
            peticion.setAttribute("incidencias", java.util.Collections.emptyList());
            peticion.setAttribute("error", e.getMessage());
            peticion.getRequestDispatcher("/usuario/incidenciasUsuario.jsp").forward(peticion, respuesta);
        }
    }
    /**
     * Maneja las peticiones POST para crear o cambiar el estado de incidencias del usuario.
     *
     * @param peticion  Petición HTTP recibida
     * @param respuesta Respuesta HTTP a enviar
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de entrada/salida
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
            // Crear una nueva incidencia
            String descripcion = peticion.getParameter("descripcion");
            IncidenciaDto dto = new IncidenciaDto();
            dto.setUsuarioId(usuario.getId());
            dto.setDescripcion(descripcion);
            dto.setFechaCreacion(LocalDateTime.now());
            dto.setEstado(IncidenciaDto.Estado.pendiente);
            try {
                incidenciaServicio.crear(dto);
                GestorRegistros.info(usuario.getId(), "Incidencia creada: " + dto.getDescripcion());
                respuesta.sendRedirect(peticion.getContextPath() + "/usuario/incidencia");
            } catch (Exception e) {
                peticion.setAttribute("incidencias", java.util.Collections.emptyList());
                peticion.setAttribute("error", e.getMessage());
                peticion.getRequestDispatcher("/usuario/incidenciasUsuario.jsp").forward(peticion, respuesta);
            }
        } else if ("cambiarEstado".equals(accion)) {
            // Cambiar el estado de una incidencia existente
            Long id = Long.valueOf(peticion.getParameter("id"));
            String estado = peticion.getParameter("estado");
            try {
                incidenciaServicio.cambiarEstado(id, IncidenciaDto.Estado.valueOf(estado));
                GestorRegistros.info(usuario.getId(), "Cambio de estado de incidencia " + id + " a " + estado);
                respuesta.sendRedirect(peticion.getContextPath() + "/usuario/incidencia");
            } catch (Exception e) {
                peticion.setAttribute("incidencias", java.util.Collections.emptyList());
                peticion.setAttribute("error", e.getMessage());
                peticion.getRequestDispatcher("/usuario/incidenciasUsuario.jsp").forward(peticion, respuesta);
            }
        }
    }
}
