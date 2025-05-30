package com.andres.gestionalmacen.servlets;

import java.io.IOException;
import java.util.Collections;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.andres.gestionalmacen.configuracion.Configuracion;
import com.andres.gestionalmacen.dtos.UsuarioDto;
import com.andres.gestionalmacen.servicios.UsuarioServicio;
import com.andres.gestionalmacen.utilidades.GestorRegistros;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import org.slf4j.MDC;
/**
 * Servlet que gestiona el acceso por google de usuarios al sistema.
 * 
 * @author Andrés
 */
@WebServlet("/GoogleAccesoServlet")
public class GoogleAccesoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static String ID_CLIENTE;
    
    private UsuarioServicio servicioUsuario;
    private GoogleIdTokenVerifier verificador;
    
    /**
     * Inicializa el servlet configurando el servicio de usuario y el verificador de Google.
     * 
     * @throws ServletException Si ocurre un error durante la inicialización
     */
    @Override
    public void init() throws ServletException {
        super.init();
        servicioUsuario = new UsuarioServicio();
        // Leer el ID de cliente desde config.properties/config-prod.properties
        ID_CLIENTE = Configuracion.obtenerPropiedad("google.client.id", "");
        verificador = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
            .setAudience(Collections.singletonList(ID_CLIENTE))
            .build();
        GestorRegistros.sistemaInfo("GoogleAccesoServlet inicializado correctamente con CLIENT_ID: " + ID_CLIENTE);
    }
    
    /**
     * Procesa las solicitudes POST para la autenticación con Google.
     * Verifica el token, valida el usuario y gestiona la sesión.
     * 
     * @param peticion La petición HTTP que contiene el token de Google
     * @param respuesta La respuesta HTTP al cliente
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de E/S
     */
    @Override
    protected void doPost(HttpServletRequest peticion, HttpServletResponse respuesta) 
            throws ServletException, IOException {
        
        respuesta.setContentType("text/plain;charset=UTF-8");
        
        String tokenId = peticion.getParameter("idToken");
        if (tokenId == null || tokenId.isEmpty()) {
            GestorRegistros.sistemaWarning("Intento de acceso con Google sin token");
            enviarError(respuesta, HttpServletResponse.SC_BAD_REQUEST, "Token de ID no proporcionado");
            return;
        }
        
        try {
            // Verificar el token de ID
            GoogleIdToken tokenGoogle = verificador.verify(tokenId);
            if (tokenGoogle == null) {
                GestorRegistros.sistemaWarning("Token de Google inválido");
                enviarError(respuesta, HttpServletResponse.SC_UNAUTHORIZED, "Token de Google inválido");
                return;
            }

            GoogleIdToken.Payload payload = tokenGoogle.getPayload();
            String correoElectronico = payload.getEmail();

            // Buscar usuario por correo
            UsuarioDto usuario = servicioUsuario.buscarPorCorreo(correoElectronico);
            
            if (usuario == null) {
                // Usuario no existe, redirigir a registro
                GestorRegistros.sistemaInfo("Usuario de Google no encontrado: " + correoElectronico);
                enviarError(respuesta, HttpServletResponse.SC_NOT_FOUND, "Usuario no encontrado");
                return;
            }

            // Verificar que el usuario sea de tipo Google y tenga rol de usuario (4)
            if (!usuario.isGoogle() || usuario.getRolId() != 4) {
                GestorRegistros.sistemaWarning("Intento de acceso con cuenta no Google o rol incorrecto: " + correoElectronico);
                enviarError(respuesta, HttpServletResponse.SC_FORBIDDEN, "Acceso no permitido");
                return;
            }

            // Logging con ID de usuario
            GestorRegistros.info(usuario.getId(), "Usuario de Google autenticado correctamente");

            // Crear sesión
            HttpSession sesion = peticion.getSession();
            sesion.setAttribute("usuario", usuario);
            
            GestorRegistros.sistemaInfo("Acceso exitoso con Google para: " + correoElectronico);
            respuesta.getWriter().write("OK");

        } catch (Exception e) {
            GestorRegistros.sistemaError("Error en autenticación Google: " + e.getMessage());
            enviarError(respuesta, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error en autenticación");
        } finally {
            // Limpiar MDC
            MDC.remove("userId");
        }
    }
    
    /**
     * Envía una respuesta de error al cliente.
     * 
     * @param respuesta La respuesta HTTP
     * @param estado El código de estado HTTP
     * @param mensaje El mensaje de error
     * @throws IOException Si ocurre un error al escribir la respuesta
     */
    private void enviarError(HttpServletResponse respuesta, int estado, String mensaje) throws IOException {
        respuesta.setStatus(estado);
        respuesta.getWriter().write(mensaje);
    }
}
