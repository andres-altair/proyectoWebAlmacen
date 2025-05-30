package com.andres.gestionalmacen.servlets.administrador;

import com.andres.gestionalmacen.dtos.UsuarioDto;
import com.andres.gestionalmacen.servicios.RespaldoBBDDServicio;
import com.andres.gestionalmacen.utilidades.GestorRegistros;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
/**
 * Servlet que gestiona las operaciones de respaldo y exportación de la base de datos
 * @author Andres
 * 
 */
@WebServlet("/admin/RespaldoBBDD")
public class RespaldoBBDDServlet extends HttpServlet {

    private final RespaldoBBDDServicio servicio = new RespaldoBBDDServicio();
    /**
     * Maneja las peticiones GET para mostrar la página de respaldo de la base de datos.
     * @param peticion  La petición HTTP recibida
     * @param respuesta La respuesta HTTP a enviar
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException      Si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta) 
            throws ServletException, IOException {
        HttpSession sesion = peticion.getSession(false);
        
        if (sesion != null && sesion.getAttribute("usuario") != null) {
            UsuarioDto usuario = (UsuarioDto) sesion.getAttribute("usuario");
            
            if (usuario.getRolId() == 1) { // Verificar si es admin
                GestorRegistros.info(usuario.getId(), "Acceso al panel de administración");
                
                peticion.getRequestDispatcher("/admin/RespaldoBBDD.jsp").forward(peticion, respuesta);
            } else {
                // Intento de acceso no autorizado
                if (usuario.getId() != null) {
                    GestorRegistros.warning(usuario.getId(), 
                        "Intento de acceso no autorizado al panel de administración. Rol actual: " + usuario.getRolId());
                }
                GestorRegistros.sistemaWarning("Intento de acceso no autorizado al panel de administración desde IP: " 
                    + peticion.getRemoteAddr());
                respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            }
        } else {
            // Intento de acceso sin sesión
            GestorRegistros.sistemaWarning("Intento de acceso al panel de administración sin sesión válida desde IP: " 
                + peticion.getRemoteAddr());
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
        }
    }
    /**
     * Maneja las peticiones POST para ejecutar acciones de respaldo/exportación de la base de datos.
     * @author andres
     * @param peticion  La petición HTTP recibida
     * @param respuesta La respuesta HTTP a enviar
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException      Si ocurre un error de entrada/salida
     */
    @Override
    protected void doPost(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
        HttpSession sesion = peticion.getSession(false);
        UsuarioDto usuario = (sesion != null) ? (UsuarioDto) sesion.getAttribute("usuario") : null;
        Long usuarioId = (usuario != null) ? usuario.getId() : null;
        String accion = peticion.getParameter("accion");
        if (accion == null) {
            respuesta.sendRedirect("RespaldoBBDD.jsp?error=Acción inválida");
            return;
        }

        // Obtiene los parámetros comunes para las acciones
        String nombreBBDD = peticion.getParameter("nombreBBDD");
        if (nombreBBDD == null || nombreBBDD.isBlank()) {
            nombreBBDD = "gestion_usuarios"; // valor por defecto
        }

        switch (accion) {
            case "exportar":
                try {
                    servicio.descargarExportacion(respuesta, nombreBBDD);
                    if (usuarioId != null) {
                        GestorRegistros.info(usuarioId, "Exportación completa de la base de datos realizada sobre BBDD: " + nombreBBDD);
                    }
                } catch (Exception ex) {
                    if (usuarioId != null) {
                        GestorRegistros.error(usuarioId, "Error en exportación completa de la base de datos: " + ex.getMessage());
                    } else {
                        GestorRegistros.sistemaError("Error en exportación completa de la base de datos (sin usuario) - IP: " + peticion.getRemoteAddr() + " - Error: " + ex.getMessage());
                    }
                    peticion.setAttribute("error", ex.getMessage());
                    peticion.getRequestDispatcher("/admin/RespaldoBBDD.jsp").forward(peticion, respuesta);
                }
                break;
            case "exportar_datos":
                try {
                    servicio.descargarExportacionDatos(respuesta, nombreBBDD);
                    if (usuarioId != null) {
                        GestorRegistros.info(usuarioId, "Exportación de datos realizada sobre BBDD: " + nombreBBDD);
                    }
                } catch (Exception ex) {
                    if (usuarioId != null) {
                        GestorRegistros.error(usuarioId, "Error en exportación de datos de la base de datos: " + ex.getMessage());
                    } else {
                        GestorRegistros.sistemaError("Error en exportación de datos de la base de datos (sin usuario) - IP: " + peticion.getRemoteAddr() + " - Error: " + ex.getMessage());
                    }
                    peticion.setAttribute("error", ex.getMessage());
                    peticion.getRequestDispatcher("/admin/RespaldoBBDD.jsp").forward(peticion, respuesta);
                }
                break;
            case "respaldo_completo_ambas":
                try {
                    String mensaje = servicio.respaldoCompletoAmbas();
                    if (mensaje != null && mensaje.toLowerCase().contains("error")) {
                        peticion.setAttribute("error", mensaje);
                        if (usuarioId != null) {
                            GestorRegistros.error(usuarioId, "Error en respaldo completo ambas: " + mensaje);
                        } else {
                            GestorRegistros.sistemaError("Error en respaldo completo ambas (sin usuario) - IP: " + peticion.getRemoteAddr() + " - Error: " + mensaje);
                        }
                    } else {
                        peticion.setAttribute("mensaje", mensaje);
                        if (usuarioId != null) {
                            GestorRegistros.info(usuarioId, "Respaldo completo ambas realizado correctamente sobre BBDD: " + nombreBBDD);
                        }
                    }
                } catch (Exception ex) {
                    peticion.setAttribute("error", "Error al generar el respaldo: " + ex.getMessage());
                    if (usuarioId != null) {
                        GestorRegistros.error(usuarioId, "Error crítico en respaldo completo ambas: " + ex.getMessage());
                    } else {
                        GestorRegistros.sistemaError("Error crítico en respaldo completo ambas (sin usuario) - IP: " + peticion.getRemoteAddr() + " - Error: " + ex.getMessage());
                    }
                }
                peticion.getRequestDispatcher("/admin/RespaldoBBDD.jsp").forward(peticion, respuesta);
                break;
            default:
                respuesta.sendRedirect("/admin/RespaldoBBDD");
        }
    }
}