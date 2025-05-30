package com.andres.gestionalmacen.servlets.operario;

import com.andres.gestionalmacen.dtos.ActividadDto;
import com.andres.gestionalmacen.dtos.UsuarioDto;
import com.andres.gestionalmacen.servicios.ActividadServicio;
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
 * Servlet que gestiona las actividades del operario.
 * Permite visualizar y actualizar el estado de las actividades asignadas al operario autenticado.
 * 
 * @author andres
 */
@WebServlet("/operario/actividad")
public class ActividadOperarioServlet extends HttpServlet {
    private ActividadServicio actividadServicio = new ActividadServicio();

    /**
     * Maneja las peticiones GET para mostrar las actividades del operario autenticado.
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
        }
        HttpSession sesion = peticion.getSession(false);
        UsuarioDto operarioActual = (UsuarioDto) sesion.getAttribute("usuario");
        if (operarioActual.getRolId() != 3) {
            GestorRegistros.warning(operarioActual.getId(), 
                "Intento no autorizado de acceso a actividades de operario. Rol actual: " + operarioActual.getRolId());
            respuesta.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
            return;
        }
        try {
            List<ActividadDto> actividades = actividadServicio.obtenerParaOperario(usuario.getId());
            peticion.setAttribute("actividades", actividades);
            GestorRegistros.info(usuario.getId(), "Actividades de operario mostradas correctamente.");
        } catch (Exception ex) {
            GestorRegistros.error(usuario.getId(), "Error al mostrar actividades de operario: " + ex.getMessage());
            peticion.setAttribute("error", ex.getMessage());
        }
        peticion.getRequestDispatcher("/operario/actividadOperario.jsp").forward(peticion, respuesta);
    }

    /**
     * Maneja las peticiones POST para actualizar el estado de una actividad del operario.
     * 
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
        Long actividadId = Long.valueOf(peticion.getParameter("actividadId"));
        String nuevoEstado = peticion.getParameter("estado");
        try {
            // Asignar operario y cambiar estado
            actividadServicio.asignarOperarioYCambiarEstado(actividadId, usuario.getId(), nuevoEstado);
            GestorRegistros.info(usuario.getId(), "Estado de actividad actualizado. Actividad ID: " + actividadId + ", Nuevo estado: " + nuevoEstado);
            respuesta.sendRedirect(peticion.getContextPath() + "/operario/actividad");
        } catch (Exception ex) {
            GestorRegistros.error(usuario.getId(), "Error al actualizar estado de actividad: " + ex.getMessage());
            peticion.setAttribute("error", ex.getMessage());
            // Reenviar a la misma JSP de actividades para mostrar el error
            peticion.getRequestDispatcher("/operario/actividadOperario.jsp").forward(peticion, respuesta);
        }
    }
}