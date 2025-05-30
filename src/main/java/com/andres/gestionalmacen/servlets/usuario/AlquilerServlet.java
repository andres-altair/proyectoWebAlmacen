package com.andres.gestionalmacen.servlets.usuario;

import com.andres.gestionalmacen.servicios.AlquilerServicio;
import com.andres.gestionalmacen.dtos.SectoresAlquilerDto;
import com.andres.gestionalmacen.dtos.UsuarioDto;
import com.andres.gestionalmacen.dtos.SectorDto;
import com.andres.gestionalmacen.servicios.SectorServicio;
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
 * Servlet que gestiona los alquileres del usuario.
 *
 * @author andres
 */
@WebServlet("/usuario/alquiler")
public class AlquilerServlet extends HttpServlet {
    private final AlquilerServicio alquilerServicio = new AlquilerServicio();
    private final SectorServicio sectorServicio = new SectorServicio();
    /**
     * Maneja las peticiones GET para mostrar los alquileres y sectores disponibles del usuario autenticado.
     * @param peticion  Petición HTTP recibida
     * @param respuesta Respuesta HTTP a enviar
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
        // Log de inicio de la petición GET
        GestorRegistros.sistemaInfo("AlquilerServlet: Iniciando procesamiento de petición GET");
        GestorRegistros.sistemaInfo("AlquilerServlet: URI solicitada: " + peticion.getRequestURI());

        // Obtener la sesión sin crear una nueva si no existe
        HttpSession sesion = peticion.getSession(false);
        if (sesion == null) {
            GestorRegistros.sistemaWarning("AlquilerServlet: No hay sesión activa");
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }
        // Obtener el usuario de la sesión
        Object usuarioObj = sesion.getAttribute("usuario");
        if (usuarioObj == null) {
            GestorRegistros.sistemaWarning("AlquilerServlet: No hay usuario en la sesión");
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }
        // Obtener el id del usuario
        Long usuarioId = null;
        try {
            usuarioId = (Long) sesion.getAttribute("usuarioId");
        } catch (Exception e) {
            GestorRegistros.sistemaWarning("AlquilerServlet: Error casteando usuarioId de sesión: " + e.getMessage());
        }
        if (usuarioId == null) {
            GestorRegistros.sistemaWarning("AlquilerServlet: usuarioId es null en sesión");
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }
        // Obtener el usuario logueado
        UsuarioDto usuario = (UsuarioDto) sesion.getAttribute("usuario");
        // Verificar si el usuario es operario
        if (usuario.getRolId() != 4) { 
            GestorRegistros.sistemaWarning("AlquilerServlet: Intento no autorizado de acceso al panel de operario. Usuario ID: " + usuario.getId() + ", Rol actual: " + usuario.getRolId());
            GestorRegistros.warning(usuario.getId(), "Intento no autorizado de acceso al panel de operario. Rol actual: " + usuario.getRolId());
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }

        // Obtener los alquileres del usuario
        List<SectoresAlquilerDto> alquileres;
        try {
            alquileres = alquilerServicio.obtenerAlquileresUsuario(usuarioId);
        } catch (Exception e) {
            GestorRegistros.sistemaError("AlquilerServlet: Error al obtener alquileres del usuario: " + e.getMessage());
            peticion.setAttribute("error", "No se pudieron obtener los alquileres: " + e.getMessage());
            alquileres = java.util.Collections.emptyList();
        }
        // Obtener los sectores libres
        List<SectorDto> sectoresLibres;
        try {
            sectoresLibres = sectorServicio.obtenerSectoresLibres();
        } catch (Exception e) {
            GestorRegistros.sistemaError("AlquilerServlet: Error al obtener sectores libres: " + e.getMessage());
            peticion.setAttribute("error", "No se pudieron obtener los sectores libres: " + e.getMessage());
            sectoresLibres = java.util.Collections.emptyList();
        }

        // Conversión LocalDateTime a java.util.Date para JSTL
        for (SectoresAlquilerDto alquiler : alquileres) {
            if (alquiler.getFechaInicio() != null) {
                alquiler.setFechaInicioDate(java.util.Date.from(alquiler.getFechaInicio().atZone(java.time.ZoneId.systemDefault()).toInstant()));
            }
            if (alquiler.getFechaFin() != null) {
                alquiler.setFechaFinDate(java.util.Date.from(alquiler.getFechaFin().atZone(java.time.ZoneId.systemDefault()).toInstant()));
            }
        }
        // Establecer los atributos para la vista JSP
        peticion.setAttribute("alquileres", alquileres);
        peticion.setAttribute("sectoresLibres", sectoresLibres);
        GestorRegistros.sistemaInfo("AlquilerServlet: Forward a /usuario/alquiler.jsp");
        peticion.getRequestDispatcher("/usuario/alquiler.jsp").forward(peticion, respuesta);
    }
}
