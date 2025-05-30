package com.andres.gestionalmacen.servlets.transpotista;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.andres.gestionalmacen.dtos.UsuarioDto;
import com.andres.gestionalmacen.utilidades.GestorRegistros;
import com.andres.gestionalmacen.utilidades.ImagenUtil;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Servlet que maneja el panel del transportista.
 * @author Andrés
 */
@WebServlet("/transportista/panel")
public class PanelTransportistaServlet extends HttpServlet {
    /**
     * Maneja las peticiones GET para mostrar el panel del transportista.
     * @param peticion  Petición HTTP recibida
     * @param respuesta Respuesta HTTP a enviar
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta) 
            throws ServletException, IOException {
        
        // Log de inicio de la petición GET
        GestorRegistros.sistemaInfo("PanelTransportistaServlet: Iniciando procesamiento de petición GET");
        GestorRegistros.sistemaInfo("PanelTransportistaServlet: URI solicitada: " + peticion.getRequestURI());
        
        // Obtener la sesión sin crear una nueva si no existe
        HttpSession sesion = peticion.getSession(false);
        
        // Verificar si hay una sesión activa
        if (sesion == null) {
            GestorRegistros.sistemaWarning("PanelTransportistaServlet: No hay sesión activa");
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }

        // Obtener el usuario de la sesión
        UsuarioDto usuario = (UsuarioDto) sesion.getAttribute("usuario");
        if (usuario == null) {
            GestorRegistros.sistemaWarning("PanelTransportistaServlet: No hay usuario en la sesión");
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }

        // Log de información del usuario autenticado
        GestorRegistros.sistemaInfo("PanelTransportistaServlet: Usuario encontrado - ID: " + usuario.getId() + 
            ", Rol: " + usuario.getRolId() + ", Nombre: " + usuario.getNombreCompleto());

        // Verificar el rol del usuario (5 = Transportista)
        if (usuario.getRolId() != 5) {
            GestorRegistros.sistemaWarning("PanelTransportistaServlet: Usuario con rol incorrecto: " + usuario.getRolId());
            sesion.setAttribute("error", "No tienes permiso para acceder a esta página");
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }

        try {
            // Preparar los datos del usuario para la vista
            String nombreCompleto = usuario.getNombreCompleto();
            String fotoBase64 = null;
            
            // Procesar la foto si existe
            if (usuario.getFoto() != null) {
                byte[] fotoConMime = ImagenUtil.asegurarMimeTypeImagen(usuario.getFoto());
                if (fotoConMime != null) {
                    String fotoStr = new String(fotoConMime, StandardCharsets.UTF_8);
                    fotoBase64 = fotoStr;
                } else {
                    GestorRegistros.warning(usuario.getId(), "No se pudo procesar la foto de perfil");
                }
            }
            
            // Establecer atributos para el JSP, usando valores por defecto si es necesario
            peticion.setAttribute("transportistaNombre", nombreCompleto != null ? nombreCompleto : "Transportista");
            String fotoSrc = fotoBase64 != null ? fotoBase64 : "https://via.placeholder.com/100";
            peticion.setAttribute("usuarioFoto", fotoSrc);

            // Redirigir a la vista del panel del transportista
            GestorRegistros.sistemaInfo("PanelTransportistaServlet: Preparando forward al JSP");
            peticion.getRequestDispatcher("/transportista/panelTransportista.jsp").forward(peticion, respuesta);
            
        } catch (Exception e) {
            GestorRegistros.sistemaError("Error al procesar la foto del usuario: " + e.getMessage());
            respuesta.sendRedirect(peticion.getContextPath() + "/error");
        }
    }
}
