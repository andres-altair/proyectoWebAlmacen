package com.andres.gestionalmacen.servlets.administrador;

import com.andres.gestionalmacen.dtos.UsuarioDto;
import com.andres.gestionalmacen.servicios.UsuarioServicio;
import com.andres.gestionalmacen.utilidades.GestorRegistros;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet que maneja la eliminación de usuarios.
 * @author Andrés
 */
@WebServlet("/admin/usuarios/eliminar")
public class EliminarUsuarioServlet extends HttpServlet {

    private final UsuarioServicio servicioUsuario;

    /**
     * Constructor que inicializa el servicio de usuarios.
     */
    public EliminarUsuarioServlet() {
        this.servicioUsuario = new UsuarioServicio();
    }

    /**
     * Método que maneja la petición POST para eliminar un usuario.
     * 
     * @param peticion  objeto que contiene la petición HTTP
     * @param respuesta objeto que contiene la respuesta HTTP
     * @throws ServletException si ocurre un error en la ejecución del servlet
     * @throws IOException      si ocurre un error en la lectura o escritura de la petición o respuesta
     */
    @Override
    protected void doPost(HttpServletRequest peticion, HttpServletResponse respuesta) 
            throws ServletException, IOException {
        
        // Obtener la sesión del usuario
        HttpSession sesion = peticion.getSession(false);
        
        // Verificar si la sesión es válida
        if (sesion == null || sesion.getAttribute("usuario") == null) {
            GestorRegistros.sistemaWarning("Intento de eliminar usuario sin sesión válida desde IP: " 
                + peticion.getRemoteAddr());
            respuesta.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sesión no válida");
            return;
        }

        // Obtener el usuario actual
        UsuarioDto adminActual = (UsuarioDto) sesion.getAttribute("usuario");
        
        // Verificar si el usuario tiene permisos de administrador
        if (adminActual.getRolId() != 1) {
            GestorRegistros.warning(adminActual.getId(), 
                "Intento no autorizado de eliminar usuario. Rol actual: " + adminActual.getRolId());
            respuesta.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
            return;
        }

        try {
            // Obtener y validar el ID del usuario a eliminar
            String idCadena = peticion.getParameter("id");
            String idConfirmacion = peticion.getParameter("confirmacionId");
            
            if (idCadena == null || idCadena.trim().isEmpty()) {
                GestorRegistros.warning(adminActual.getId(), "Intento de eliminación sin proporcionar ID");
                throw new Exception("ID de usuario no proporcionado");
            }
            
            if (idConfirmacion == null || !idConfirmacion.equals(idCadena)) {
                GestorRegistros.warning(adminActual.getId(), 
                    "ID de confirmación no coincide para eliminación. ID: " + idCadena);
                throw new Exception("El ID de confirmación no coincide");
            }

            // Convertir el ID a Long
            Long id = Long.parseLong(idCadena);
            
            // Registrar el inicio de la eliminación del usuario
            GestorRegistros.info(adminActual.getId(), "Iniciando eliminación del usuario con ID: " + id);

            // Verificar que no se está intentando eliminar al usuario actual
            if (adminActual.getId().equals(id)) {
                GestorRegistros.warning(adminActual.getId(), 
                    "Intento de eliminar la propia cuenta del administrador");
                throw new Exception("No puedes eliminar tu propia cuenta");
            }else if(id == 1 || id == 4) {
                GestorRegistros.warning(adminActual.getId(), 
                    "Intento de eliminar usuario protegido. ID: " + id);
                throw new Exception("Este usuario no puede ser eliminado por razones de seguridad");
            }

            // Intentar eliminar el usuario
            servicioUsuario.eliminarUsuario(id);
            
            // Registrar la eliminación exitosa del usuario
            GestorRegistros.info(adminActual.getId(), "Usuario eliminado exitosamente. ID: " + id);

            // Redirigir a la página de gestión de usuarios
            respuesta.sendRedirect(peticion.getContextPath() + "/admin/usuarios");
            
        } catch (NumberFormatException e) {
            // Registrar el error al parsear el ID del usuario
            GestorRegistros.error(adminActual.getId(), 
                "Error al parsear ID de usuario para eliminación: " + e.getMessage());
            respuesta.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de usuario inválido");
            
        } catch (Exception e) {
            try {
                // Registrar el error al eliminar el usuario
                GestorRegistros.error(adminActual.getId(), 
                    "Error al eliminar usuario: " + e.getMessage());
            } catch (Exception ex) {
                // Registrar el error en el sistema
                GestorRegistros.sistemaError("Error al eliminar usuario - IP: " + peticion.getRemoteAddr() 
                    + " - Error: " + e.getMessage());
            }
            String mensajeError = e.getMessage();
            // Si el mensaje es conocido y útil, mostrarlo al usuario; si no, uno genérico
            if (mensajeError != null && !mensajeError.isBlank() && !mensajeError.toLowerCase().contains("error al eliminar usuario")) {
                peticion.getSession().setAttribute("error", mensajeError);
            } else {
                peticion.getSession().setAttribute("error", "Ha ocurrido un error al eliminar el usuario. Por favor, inténtalo de nuevo.");
            }
            respuesta.sendRedirect(peticion.getContextPath() + "/admin/usuarios");
        }
    }
}