package com.andres.gestionalmacen.servlets.administrador;

import com.andres.gestionalmacen.dtos.UsuarioDto;
import com.andres.gestionalmacen.servicios.UsuarioServicio;
import com.andres.gestionalmacen.utilidades.GestorRegistros;
import com.andres.gestionalmacen.utilidades.ImagenUtil;
import com.google.api.client.http.MultipartContent.Part;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
/**
 * Servlet que maneja el perfil de usuarios.
 * @author Andrés
 */
@WebServlet("/admin/perfil")
@MultipartConfig // para soporte de subida de foto
public class PerfilAdminServlet extends HttpServlet {
    private UsuarioServicio servicioUsuario = new UsuarioServicio();
     /**
     * Método que maneja la petición GET para el perfil de administración.
     * 
     * @param peticion La petición HTTP
     * @param respuesta La respuesta HTTP
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
        HttpSession sesion = peticion.getSession(false);
        UsuarioDto usuario = (sesion != null) ? (UsuarioDto) sesion.getAttribute("usuario") : null;
        if (sesion == null || usuario == null) {
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }
        UsuarioDto adminActual = (UsuarioDto) sesion.getAttribute("usuario");
        // Verificar si el usuario tiene permisos de administrador
        if (adminActual.getRolId() != 1) {
            GestorRegistros.warning(((com.andres.gestionalmacen.dtos.UsuarioDto) usuario).getId(), 
                "Intento no autorizado de acceso al panel de incidencias. Rol actual: " + ((com.andres.gestionalmacen.dtos.UsuarioDto) usuario).getRolId());
            respuesta.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
            return;
        }
        // Log de acceso exitoso al perfil admin
        GestorRegistros.info(adminActual.getId(), "Acceso al perfil de administrador.");
        try {
            peticion.setAttribute("usuario", usuario);
            peticion.getRequestDispatcher("/admin/perfilAdmin.jsp").forward(peticion, respuesta);
        } catch (Exception e) {
            GestorRegistros.error(adminActual.getId(), "Error al cargar el perfil de administrador: " + e.getMessage());
            peticion.setAttribute("error", "No se pudo cargar el perfil: " + e.getMessage());
            peticion.getRequestDispatcher("/admin/perfilAdmin.jsp").forward(peticion, respuesta);
        }
    }
     /**
     * Método que maneja la petición POST para perfil .
     * 
     * @param peticion  objeto que contiene la petición HTTP
     * @param respuesta objeto que contiene la respuesta HTTP
     * @throws ServletException si ocurre un error en la ejecución del servlet
     * @throws IOException      si ocurre un error en la lectura o escritura de datos
     */
    @Override
    protected void doPost(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
        HttpSession sesion = peticion.getSession(false);
        UsuarioDto usuario = (sesion != null) ? (UsuarioDto) sesion.getAttribute("usuario") : null;
        if (sesion == null || usuario == null) {
            respuesta.sendRedirect(peticion.getContextPath() + "/login.jsp");
            return;
        }
        
        Long usuarioId = usuario.getId();
        if (usuarioId == 4L) {
            GestorRegistros.warning(usuarioId, "Intento de actualizar perfil de usuario especial. ID: " + usuarioId);
            peticion.setAttribute("error", "No está permitido actualizar el perfil de este usuario especial.");
            try {
                peticion.setAttribute("usuario", usuario);
            } catch (Exception ignored) {}
            peticion.getRequestDispatcher("/admin/perfilAdmin.jsp").forward(peticion, respuesta);
            return;
        }
        try {
            // Solo se pueden modificar estos campos
            String nombreCompleto = peticion.getParameter("nombreCompleto");
            String movil = peticion.getParameter("movil");

            if (nombreCompleto != null) usuario.setNombreCompleto(nombreCompleto);
            if (movil != null) usuario.setMovil(movil);

            // Procesar foto (opcional)
            jakarta.servlet.http.Part parteFoto = peticion.getPart("foto");
            if (parteFoto != null && parteFoto.getSize() > 0) {
                byte[] bytesFoto = parteFoto.getInputStream().readAllBytes();
                try {
                    String nombreArchivo = parteFoto.getSubmittedFileName();
                    ImagenUtil.verificarImagen(bytesFoto, nombreArchivo);
                    usuario.setFoto(bytesFoto);
                } catch (IllegalArgumentException e) {
                    GestorRegistros.warning(usuarioId, 
                        "Error al procesar foto para usuario " + usuarioId + ": " + e.getMessage());
                    peticion.getSession().setAttribute("error", 
                        "La imagen debe tener un formato válido (JPEG, PNG, GIF, BMP, WEBP) y la extensión debe coincidir con el tipo de archivo.");
                    respuesta.sendRedirect(peticion.getContextPath() + "/admin/perfilAdmin.jsp");
                    return;
                }
            }

            // Actualizar usuario (requiere método de actualización en UsuarioServicio)
            servicioUsuario.actualizarPerfil(usuarioId, usuario);
            GestorRegistros.info(usuarioId, "Perfil de administrador actualizado correctamente.");
            peticion.setAttribute("usuario", usuario);
            peticion.setAttribute("mensaje", "Perfil actualizado correctamente");
            peticion.getRequestDispatcher("/admin/perfilAdmin.jsp").forward(peticion, respuesta);
        } catch (Exception e) {
            if (usuarioId != null) {
                GestorRegistros.error(usuarioId, "Error al actualizar el perfil: " + e.getMessage());
            } else {
                GestorRegistros.sistemaError("Error al actualizar perfil - IP: " + peticion.getRemoteAddr() + " - Error: " + e.getMessage());
            }
            String mensajeError = e.getMessage();
            if (mensajeError != null && !mensajeError.isBlank() && !mensajeError.toLowerCase().contains("error al actualizar perfil")) {
                peticion.setAttribute("error", mensajeError);
            } else {
                peticion.setAttribute("error", "Ha ocurrido un error al actualizar el perfil. Por favor, inténtalo de nuevo.");
            }
            peticion.getRequestDispatcher("/admin/perfilAdmin.jsp").forward(peticion, respuesta);
        }   
    }
}