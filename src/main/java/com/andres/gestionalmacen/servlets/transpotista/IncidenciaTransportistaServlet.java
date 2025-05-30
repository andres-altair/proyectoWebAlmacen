package com.andres.gestionalmacen.servlets.transpotista;

import com.andres.gestionalmacen.dtos.IncidenciaDto;
import com.andres.gestionalmacen.dtos.UsuarioDto;
import com.andres.gestionalmacen.servicios.IncidenciaServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.andres.gestionalmacen.utilidades.GestorRegistros;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.time.ZoneId;
/**
 * Servlet que gestiona incidencia transportista.
 * 
 * @author andres
 */
@WebServlet("/transportista/incidencia")
public class IncidenciaTransportistaServlet extends HttpServlet {
    private IncidenciaServicio incidenciaServicio = new IncidenciaServicio();
    /**
     * Maneja las peticiones GET para incidencia transportista.
     * 
     * @param pet objeto que contiene la petición HTTP
     * @param resp objeto que contiene la respuesta HTTP
     * @throws ServletException si ocurre un error en el servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest pet, HttpServletResponse resp) throws ServletException, IOException {
        UsuarioDto usuario = (UsuarioDto) pet.getSession().getAttribute("usuario");
        if (usuario == null) {
            resp.sendRedirect(pet.getContextPath() + "/acceso");
            return;
        }
        // Solo transportista 
        if (usuario.getRolId() != 5) {
            GestorRegistros.warning(usuario.getId(), "Intento no autorizado de acceso a incidencias de transportista. Rol actual: " + usuario.getRolId());
            resp.sendRedirect(pet.getContextPath() + "/acceso");
            return;
        }
        // Obtener incidencias solo del transportista actual
        try {
            List<IncidenciaDto> incidencias = incidenciaServicio.obtenerPorUsuario(usuario.getId());
            // Convertir LocalDateTime a java.util.Date para cada incidencia y mostrar fecha en jsp en dd/MM/yyyy HH:mm
            for (IncidenciaDto incidencia : incidencias) {
                incidencia.setFechaCreacionDate(Date.from(incidencia.getFechaCreacion().atZone(ZoneId.systemDefault()).toInstant()));
            }
            pet.setAttribute("incidencias", incidencias);
            GestorRegistros.info(usuario.getId(), "Incidencias de transportista mostradas correctamente.");
            pet.getRequestDispatcher("/transportista/incidenciasTransportista.jsp").forward(pet, resp);
        } catch (Exception e) {
            GestorRegistros.error(usuario.getId(), "Error al mostrar incidencias de transportista: " + e.getMessage());
            pet.setAttribute("incidencias", java.util.Collections.emptyList());
            pet.setAttribute("error", e.getMessage());
            pet.getRequestDispatcher("/transportista/incidenciasTransportista.jsp").forward(pet, resp);
        }
    }
    /**
     * Maneja las peticiones POST para incidencia transportista.
     * 
     * @param pet objeto que contiene la petición HTTP
     * @param resp objeto que contiene la respuesta HTTP
     * @throws ServletException si ocurre un error en el servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doPost(HttpServletRequest pet, HttpServletResponse resp) throws ServletException, IOException {
        UsuarioDto usuario = (UsuarioDto) pet.getSession().getAttribute("usuario");
        if (usuario == null) {
            resp.sendRedirect(pet.getContextPath() + "/acceso");
            return;
        }
        String accion = pet.getParameter("accion");
        if ("crear".equals(accion)) {
            try {
                String descripcion = pet.getParameter("descripcion");
                IncidenciaDto dto = new IncidenciaDto();
                dto.setUsuarioId(usuario.getId());
                dto.setDescripcion(descripcion);
                dto.setFechaCreacion(LocalDateTime.now());
                dto.setEstado(IncidenciaDto.Estado.pendiente);
                incidenciaServicio.crear(dto);
                GestorRegistros.info(usuario.getId(), "Incidencia creada por transportista. Descripción: " + descripcion);
                resp.sendRedirect(pet.getContextPath() + "/transportista/incidencia");
            } catch (Exception e) {
                GestorRegistros.error(usuario.getId(), "Error al crear incidencia de transportista: " + e.getMessage());
                pet.setAttribute("error", e.getMessage());
                pet.getRequestDispatcher("/transportista/incidenciasTransportista.jsp").forward(pet, resp);
            }
        } else if ("cambiarEstado".equals(accion)) {
            Long id = Long.valueOf(pet.getParameter("id"));
            String estado = pet.getParameter("estado");
            try {
                incidenciaServicio.cambiarEstado(id, IncidenciaDto.Estado.valueOf(estado));
                GestorRegistros.info(usuario.getId(), "Estado de incidencia cambiado. Incidencia ID: " + id + ", Nuevo estado: " + estado);
            } catch (IllegalArgumentException e) {
                GestorRegistros.warning(usuario.getId(), "Valor de estado inválido recibido en cambio de incidencia: " + estado);
                pet.setAttribute("error", e.getMessage());
                pet.getRequestDispatcher("/transportista/incidenciasTransportista.jsp").forward(pet, resp);
            }
            resp.sendRedirect(pet.getContextPath() + "/transportista/incidencia");
        }
    }
}