package com.andres.gestionalmacen.servlets.gerente;

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
import java.util.List;
/**
 * Servlet que gestiona las incidencias del gerente.
 * 
 * @author andres
 */
@WebServlet("/gerente/incidencia")
public class IncidenciaGerenteServlet extends HttpServlet {
    private IncidenciaServicio incidenciaServicio = new IncidenciaServicio();
    /**
     * Maneja las peticiones GET para mostrar todas las incidencias del gerente autenticado.
     * @param peticion  Solicitud HTTP
     * @param respuesta  Respuesta HTTP
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest peticion , HttpServletResponse respuesta ) throws ServletException, IOException {
        UsuarioDto usuario = (UsuarioDto) peticion .getSession().getAttribute("usuario");
        if (usuario == null) {
            respuesta.sendRedirect(peticion .getContextPath() + "/acceso");
            return;
        }
        HttpSession sesion = peticion .getSession(false);
        UsuarioDto gerenteActual = (UsuarioDto) sesion.getAttribute("usuario");
        // Verificar si el usuario tiene permisos de administrador o no 
        if (gerenteActual.getRolId() != 2) {
            GestorRegistros.warning(((com.andres.gestionalmacen.dtos.UsuarioDto) usuario).getId(), 
                "Intento no autorizado de acceso al panel de incidencias. Rol actual: " + ((com.andres.gestionalmacen.dtos.UsuarioDto) usuario).getRolId());
                respuesta.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
            return;
        }
        // Log de acceso exitoso al panel de incidencias del gerente
        GestorRegistros.info(gerenteActual.getId(), "Acceso al panel de incidencias del gerente.");
        // Obtener incidencias solo del gerente actual
        try {
            List<IncidenciaDto> incidencias = incidenciaServicio.obtenerPorUsuario(usuario.getId());
            // Convertir LocalDateTime a java.util.Date para cada incidencia y mostrar fecha en jsp en dd/MM/yyyy HH:mm
            if (incidencias != null) {
                for (IncidenciaDto incidencia : incidencias) {
                    if (incidencia.getFechaCreacion() != null) {
                        incidencia.setFechaCreacionDate(java.util.Date.from(incidencia.getFechaCreacion().atZone(java.time.ZoneId.systemDefault()).toInstant()));
                    }
                }
            }
            peticion .setAttribute("incidencias", incidencias);
            peticion .getRequestDispatcher("/gerente/incidenciasGerente.jsp").forward(peticion, respuesta);
        } catch (Exception e) {
            if (gerenteActual != null) {
                GestorRegistros.error(gerenteActual.getId(), "Error al cargar incidencias: " + e.getMessage());
            } else {
                GestorRegistros.sistemaError("Error al cargar incidencias - IP: " + peticion.getRemoteAddr() + " - Error: " + e.getMessage());
            }
            peticion .setAttribute("incidencias", java.util.Collections.emptyList());
            peticion .setAttribute("error", e.getMessage());
            peticion .getRequestDispatcher("/gerente/incidenciasGerente.jsp").forward(peticion , respuesta);
        }
    }
    /**
     * Maneja las peticiones POST para crear una nueva incidencia.
     * @param peticion  Solicitud HTTP
     * @param respuesta Respuesta HTTP
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de entrada/salida
     */
    @Override
    protected void doPost(HttpServletRequest peticion , HttpServletResponse respuesta) throws ServletException, IOException {
        UsuarioDto usuario = (UsuarioDto) peticion .getSession().getAttribute("usuario");
        String accion = peticion .getParameter("accion");
        if ("crear".equals(accion)) {
            String descripcion = peticion .getParameter("descripcion");
            IncidenciaDto dto = new IncidenciaDto();
            dto.setUsuarioId(usuario.getId());
            dto.setDescripcion(descripcion);
            dto.setFechaCreacion(LocalDateTime.now());
            dto.setEstado(IncidenciaDto.Estado.pendiente);
            try {
                incidenciaServicio.crear(dto);
                GestorRegistros.info(usuario.getId(), "Incidencia creada por gerente. Descripción: " + descripcion);
                respuesta.sendRedirect(peticion .getContextPath() + "/gerente/incidencia");
            } catch (Exception e) {
                if (usuario != null) {
                    GestorRegistros.error(usuario.getId(), "Error al crear incidencia: " + e.getMessage());
                } else {
                    GestorRegistros.sistemaError("Error al crear incidencia - IP: " + peticion.getRemoteAddr() + " - Error: " + e.getMessage());
                }
                peticion .setAttribute("error", e.getMessage());
                // Recarga las incidencias para mostrar la lista y el error
                try {
                    List<IncidenciaDto> incidencias = incidenciaServicio.obtenerPorUsuario(usuario.getId());
                    peticion .setAttribute("incidencias", incidencias);
                } catch (Exception ex) {
                    peticion .setAttribute("incidencias", java.util.Collections.emptyList());
                }
                peticion .getRequestDispatcher("/gerente/incidenciasGerente.jsp").forward(peticion, respuesta);
            }
        }else if("cambiarEstado".equals(accion)){
            Long id = Long.valueOf(peticion.getParameter("id"));
            String estado = peticion.getParameter("estado");
            try {
                incidenciaServicio.cambiarEstado(id, IncidenciaDto.Estado.valueOf(estado));
                GestorRegistros.info(usuario.getId(), "Estado de incidencia cambiado. Incidencia ID: " + id + ", Nuevo estado: " + estado);
            } catch (IllegalArgumentException e) {
                GestorRegistros.warning(usuario.getId(), "Valor de estado inválido recibido en cambio de incidencia: " + estado);
            }
            respuesta.sendRedirect(peticion.getContextPath() + "/gerente/incidencia");
        }
    }
}
