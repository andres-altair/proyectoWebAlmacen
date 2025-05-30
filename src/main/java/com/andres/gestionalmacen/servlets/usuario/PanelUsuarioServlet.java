package com.andres.gestionalmacen.servlets.usuario;

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
 * Servlet que maneja el panel del usuario.
 * <p>
 * Este servlet verifica la sesión del usuario, comprueba el rol y carga la información
 * personal y la foto del usuario para mostrar en el panel principal.
 * </p>
 * @author Andrés
 */
@WebServlet("/usuario/panel")
public class PanelUsuarioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    /**
     * Maneja las peticiones GET para mostrar el panel principal del usuario.
     * <p>
     * Verifica la sesión y el rol, prepara los datos personales y la foto para la vista.
     * </p>
     *
     * @param peticion  Petición HTTP recibida
     * @param respuesta Respuesta HTTP a enviar
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta)
            throws ServletException, IOException {
        
        GestorRegistros.sistemaInfo("PanelUsuarioServlet: Iniciando procesamiento de petición GET");
        GestorRegistros.sistemaInfo("PanelUsuarioServlet: URI solicitada: " + peticion.getRequestURI());
        
        // Obtener la sesión sin crear una nueva si no existe
        HttpSession sesion = peticion.getSession(false);
        
        // Verificar si hay una sesión activa
        if (sesion == null) {
            GestorRegistros.sistemaWarning("PanelUsuarioServlet: No hay sesión activa");
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }

        // Obtener el usuario de la sesión
        UsuarioDto usuario = (UsuarioDto) sesion.getAttribute("usuario");
        if (usuario == null) {
            GestorRegistros.sistemaWarning("PanelUsuarioServlet: No hay usuario en la sesión");
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }

        // Guardar el usuarioId en la sesión para otros servlets
        sesion.setAttribute("usuarioId", usuario.getId());

        // Log de información del usuario
        GestorRegistros.sistemaInfo("PanelUsuarioServlet: Usuario encontrado - ID: " + usuario.getId() + 
            ", Rol: " + usuario.getRolId() + ", Nombre: " + usuario.getNombreCompleto());

        // Verificar el rol del usuario (4 = Usuario)
        if (usuario.getRolId() != 4) {
            GestorRegistros.sistemaWarning("PanelUsuarioServlet: Usuario con rol incorrecto: " + usuario.getRolId());
            sesion.setAttribute("error", "No tienes permiso para acceder a esta página");
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }

        try {
            // Preparar los datos personales y la foto para la vista
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
            
            // Establecer atributos con valores por defecto si son null
            peticion.setAttribute("usuarioNombre", nombreCompleto != null ? nombreCompleto : "Usuario");
            String fotoSrc = fotoBase64 != null ? fotoBase64 : "https://via.placeholder.com/100";
            peticion.setAttribute("usuarioFoto", fotoSrc);

            GestorRegistros.sistemaInfo("PanelUsuarioServlet: Preparando forward al JSP");
            peticion.getRequestDispatcher("/usuario/panelUsuario.jsp").forward(peticion, respuesta);
            
        } catch (Exception e) {
            GestorRegistros.sistemaError("Error al procesar la foto del usuario: " + e.getMessage());
            respuesta.sendRedirect(peticion.getContextPath() + "/error");
        }
    }
}
