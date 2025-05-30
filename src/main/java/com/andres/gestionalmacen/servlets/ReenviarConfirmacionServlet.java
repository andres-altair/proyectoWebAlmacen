package com.andres.gestionalmacen.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.andres.gestionalmacen.dtos.UsuarioDto;
import com.andres.gestionalmacen.servicios.UsuarioServicio;
import com.andres.gestionalmacen.utilidades.EmailUtil;
import com.andres.gestionalmacen.utilidades.GestorRegistros;

/**
 * Servlet que maneja el reenvío de correos de confirmación.
 * Gestiona las solicitudes de reenvío y verifica el estado del usuario.
 * 
 * @author Andrés
 */
@WebServlet("/reenviarConfirmacion")
public class ReenviarConfirmacionServlet extends HttpServlet {
    
    private UsuarioServicio servicioUsuario;
    
    /**
     * Inicializa el servlet configurando el servicio de usuario.
     * 
     * @throws ServletException Si ocurre un error durante la inicialización
     */
    @Override
    public void init() throws ServletException {
        super.init();
        servicioUsuario = new UsuarioServicio();
        GestorRegistros.sistemaInfo("ReenviarConfirmacionServlet inicializado correctamente");
    }
    
    /**
     * Maneja las peticiones GET mostrando el formulario de reenvío.
     * 
     * @param peticion La petición HTTP del cliente
     * @param respuesta La respuesta HTTP al cliente
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de E/S
     */
    @Override
    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta) 
            throws ServletException, IOException {
        try {
            GestorRegistros.sistemaInfo("Acceso a formulario de reenvío de confirmación");
            peticion.getRequestDispatcher("/reenviarConfirmacion.jsp").forward(peticion, respuesta);
        } catch (Exception error) {
            GestorRegistros.sistemaError("Error al mostrar formulario de reenvío: " + error.getMessage());
            throw error;
        }
    }

    /**
     * Procesa las solicitudes de reenvío de confirmación.
     * Verifica el usuario y reenvía el correo de confirmación.
     * 
     * @param peticion La petición HTTP que contiene el correo electrónico
     * @param respuesta La respuesta HTTP al cliente
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de E/S
     */
    @Override
    protected void doPost(HttpServletRequest peticion, HttpServletResponse respuesta) 
            throws ServletException, IOException {
        String correoElectronico = peticion.getParameter("correoElectronico");
        
        GestorRegistros.sistemaInfo("Solicitud de reenvío para: " + correoElectronico);
        
        try {
            // Verificar si el usuario existe según el patrón establecido
            UsuarioDto correoUsuario = servicioUsuario.buscarPorCorreoRegistro(correoElectronico);
            
            if (correoUsuario == null) {
                GestorRegistros.sistemaWarning("Intento de reenvío para correo no existente: " + correoElectronico);
                peticion.getSession().setAttribute("error", 
                    "No existe una cuenta con ese correo electrónico.");
                respuesta.sendRedirect(peticion.getContextPath() + "/reenviarConfirmacion");
                return;
            }
            
            // Reenviar correo usando el método existente que maneja tokens
            EmailUtil.reenviarCorreoConfirmacion(correoElectronico);
            
            GestorRegistros.sistemaInfo("Correo de confirmación reenviado exitosamente a: " + correoElectronico);
            GestorRegistros.info(correoUsuario.getId(), "Solicitud de reenvío de confirmación procesada");
            
            peticion.getSession().setAttribute("mensaje", 
                "Se ha enviado un nuevo correo de confirmación. Por favor, revisa tu bandeja de entrada.");
            
        } catch (Exception error) {
            GestorRegistros.sistemaError("Error al reenviar confirmación para " + 
                correoElectronico + ": " + error.getMessage());
            peticion.getSession().setAttribute("error", 
                "No se pudo reenviar el correo de confirmación. Por favor, inténtalo más tarde.");
        }
        
        respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
    }
}