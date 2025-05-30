package com.andres.gestionalmacen.servlets.gerente;

import com.andres.gestionalmacen.dtos.ActividadDto;
import com.andres.gestionalmacen.servicios.ActividadServicio;
import com.andres.gestionalmacen.utilidades.GestorRegistros;
import com.andres.gestionalmacen.dtos.UsuarioDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
/**
 * Servlet que maneja la actividad  de gerente
 * 
 * @author Andrés
 */
@WebServlet("/gerente/actividad")
public class ActividadGerenteServlet extends HttpServlet {
    private ActividadServicio actividadServicio = new ActividadServicio();

/**
 *Método que maneja la petición GET Obtiene y muestra todas las actividades creadas por el gerente
 *
 * @param peticion Solicitud HTTP
 * @param respuesta Respuesta HTTP
 * @throws ServletException Si ocurre un error en el servlet
 * @throws IOException Si ocurre un error de entrada/salida
 */
@Override
protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
        UsuarioDto usuario = (UsuarioDto) peticion.getSession().getAttribute("usuario");
        if (usuario == null) {
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }
        HttpSession sesion = peticion.getSession(false);
        UsuarioDto gerenteActual = (UsuarioDto) sesion.getAttribute("usuario");
        // Verificar si el usuario tiene permisos de administrador
        if (gerenteActual.getRolId() != 2) {
            GestorRegistros.warning(((com.andres.gestionalmacen.dtos.UsuarioDto) usuario).getId(), 
                "Intento no autorizado de acceso al panel de incidencias. Rol actual: " + ((com.andres.gestionalmacen.dtos.UsuarioDto) usuario).getRolId());
            respuesta.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
            return;
        }
        // Log de acceso exitoso al panel de actividades del gerente
        GestorRegistros.info(gerenteActual.getId(), "Acceso al panel de actividades del gerente.");
        try {
            List<ActividadDto> actividades = actividadServicio.obtenerPorGerente(usuario.getId());
            java.time.format.DateTimeFormatter formato = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            for (ActividadDto act : actividades) {
                if (act.getFechaCreacion() != null) {
                    act.setFechaCreacionStr(act.getFechaCreacion().format(formato));
                }
            }
            peticion.setAttribute("actividades", actividades);
        } catch (Exception ex) {
            if (gerenteActual != null) {
                GestorRegistros.error(gerenteActual.getId(), "Error al cargar actividades: " + ex.getMessage());
            } else {
                GestorRegistros.sistemaError("Error al cargar actividades - IP: " + peticion.getRemoteAddr() + " - Error: " + ex.getMessage());
            }
            peticion.setAttribute("error", ex.getMessage());
        }
        peticion.getRequestDispatcher("/gerente/actividadGerente.jsp").forward(peticion, respuesta);
    }

    /**
 * Método que maneja la petición POST Crea una nueva actividad para el gerente autenticado.
 * @author andres
 * @param peticion Solicitud HTTP
 * @param respuesta Respuesta HTTP
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
        String descripcion = peticion.getParameter("descripcion");
        ActividadDto dto = new ActividadDto();
        dto.setDescripcion(descripcion);
        dto.setGerenteId(usuario.getId());
        try {
            actividadServicio.crear(dto);
            GestorRegistros.info(usuario.getId(), "Actividad creada por gerente. Descripción: " + descripcion);
            respuesta.sendRedirect(peticion.getContextPath() + "/gerente/actividad");
        } catch (Exception ex) {
            if (usuario != null) {
                GestorRegistros.error(usuario.getId(), "Error al crear actividad: " + ex.getMessage());
            } else {
                GestorRegistros.sistemaError("Error al crear actividad - IP: " + peticion.getRemoteAddr() + " - Error: " + ex.getMessage());
            }
            peticion.setAttribute("error", ex.getMessage());
            peticion.getRequestDispatcher("/gerente/actividadGerente.jsp").forward(peticion, respuesta);
        }
    }
}
