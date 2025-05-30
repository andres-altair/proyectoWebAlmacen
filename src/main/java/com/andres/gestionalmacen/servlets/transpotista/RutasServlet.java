package com.andres.gestionalmacen.servlets.transpotista;

import com.andres.gestionalmacen.dtos.RutaRespuestaDto;
import com.andres.gestionalmacen.dtos.UsuarioDto;
import com.andres.gestionalmacen.servicios.RutaServicio;
import com.andres.gestionalmacen.utilidades.GestorRegistros;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
/**
 * Servlet que gestiona rutas del transportista.
 * @author andres
 */
@WebServlet("/transportista/rutas")
public class RutasServlet extends HttpServlet {
    private RutaServicio rutaServicio = new RutaServicio();
   /**
     * Maneja las peticiones GET para rutas del transportista autenticado.
     * 
     * @param peticion objeto que contiene la petición HTTP
     * @param respuesta objeto que contiene la respuesta HTTP
     * @throws ServletException si ocurre un error en el servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
        // Obtener la sesión actual
        HttpSession sesion = peticion.getSession(false);
        
        // Verificar si la sesión es válida
        if (sesion == null || sesion.getAttribute("usuario") == null) {
            GestorRegistros.sistemaWarning("Intento de acceso al panel de transportista sin sesión válida desde IP: " 
                + peticion.getRemoteAddr());
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }

        // Obtener el usuario logueado
        UsuarioDto usuario = (UsuarioDto) sesion.getAttribute("usuario");
        
        // Verificar si el usuario es operario
        if (usuario.getRolId() != 5) { 
            GestorRegistros.warning(usuario.getId(), 
                "Intento no autorizado de acceso al panel de transportista. Rol actual: " + usuario.getRolId());
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }
        // Obtener rutas usando el servicio y manejar errores
        try {
            List<RutaRespuestaDto> rutas = rutaServicio.obtenerRutasActivasPorTransportista(usuario.getId());
            peticion.setAttribute("rutas", rutas);
            GestorRegistros.info(usuario.getId(), "Acceso exitoso a rutas del transportista.");
        } catch (IOException e) {
            GestorRegistros.error(usuario.getId(), "Error al obtener rutas del transportista: " + e.getMessage());
            peticion.setAttribute("errorRutas", e.getMessage());
        }
        String googleMapsApiKey = com.andres.gestionalmacen.configuracion.Configuracion.obtenerPropiedad("google.maps.api.key", "");
        peticion.setAttribute("googleMapsApiKey", googleMapsApiKey);
        peticion.getRequestDispatcher("/transportista/rutas.jsp").forward(peticion, respuesta);
    

    }   
    /**
    * Maneja las peticiones POST para rutas del transportista autenticado.
    * 
    * @param peticion objeto que contiene la petición HTTP
    * @param respuesta objeto que contiene la respuesta HTTP
    * @throws ServletException si ocurre un error en el servlet
    * @throws IOException si ocurre un error de entrada/salida
    */
    @Override
    protected void doPost(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
        
        respuesta.sendRedirect(peticion.getContextPath() + "/transportista/rutas");
    }
}