package com.andres.gestionalmacen.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.andres.gestionalmacen.servicios.UsuarioServicio;
import com.andres.gestionalmacen.utilidades.EmailUtil;
import com.andres.gestionalmacen.utilidades.GestorRegistros;

/**
 * Servlet que maneja la confirmación de correo electrónico de usuarios.
 * Procesa el token de confirmación y actualiza el estado del usuario.
 * 
 * @author Andrés
 */
@WebServlet("/confirmarCorreoNuevo")    
public class ConfirmarCorreoNuevoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * Maneja las peticiones GET para confirmar el correo electrónico del usuario.
     * 
     * 
     * @param peticion La petición HTTP que contiene el token de confirmación
     * @param respuesta La respuesta HTTP al cliente
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de E/S
     */
    @Override
    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta) 
            throws ServletException, IOException {
        String tokenConfirmacion = peticion.getParameter("token");
        
        // Validar presencia del token
        if (tokenConfirmacion == null || tokenConfirmacion.isEmpty()) {
            GestorRegistros.sistemaWarning("Intento de confirmación sin token");
            peticion.getSession().setAttribute("error", "Token de confirmación inválido");
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }

        try {
            // Validar token
            if (!EmailUtil.validarToken(tokenConfirmacion)) {
                GestorRegistros.sistemaWarning("Token de confirmación expirado o inválido: " + tokenConfirmacion);
                peticion.getSession().setAttribute("error", 
                    "El enlace de confirmación ha expirado o no es válido. Por favor, solicita uno nuevo.");
                respuesta.sendRedirect(peticion.getContextPath() + "/reenviarConfirmacion");
                return;
            }

            // Procesar confirmación
            String correoElectronico = EmailUtil.obtenerCorreoDeToken(tokenConfirmacion);
            GestorRegistros.sistemaInfo("Procesando confirmación para: " + correoElectronico);
            
            UsuarioServicio servicioUsuario = new UsuarioServicio();
            servicioUsuario.confirmarCorreo(correoElectronico);

            // Registrar éxito
            GestorRegistros.sistemaInfo("Correo confirmado exitosamente: " + correoElectronico);
            peticion.getSession().setAttribute("mensaje", 
                "¡Correo confirmado exitosamente! Ya puedes iniciar sesión.");
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            
        } catch (Exception error) {
            // Registrar error detallado
            GestorRegistros.sistemaError("Error en confirmación de correo: " + error.getMessage());
            GestorRegistros.sistemaError("Detalles del error: " + error.toString());
            
            peticion.getSession().setAttribute("error", 
                "Error al confirmar el correo. Por favor, inténtalo más tarde.");
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
        }
    }
}