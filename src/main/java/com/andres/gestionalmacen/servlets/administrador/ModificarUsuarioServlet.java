package com.andres.gestionalmacen.servlets.administrador;

import com.andres.gestionalmacen.dtos.CrearUsuDto;
import com.andres.gestionalmacen.dtos.UsuarioDto;
import com.andres.gestionalmacen.servicios.UsuarioServicio;
import com.andres.gestionalmacen.utilidades.GestorRegistros;
import com.andres.gestionalmacen.utilidades.ImagenUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Servlet que maneja la modificación de usuarios.
 * @author Andrés
 */
@WebServlet("/admin/usuarios/modificar")
@MultipartConfig
public class ModificarUsuarioServlet extends HttpServlet {

    private final UsuarioServicio servicioUsuario;

    /**
     * Constructor que inicializa el servicio de usuarios.
     */
    public ModificarUsuarioServlet() {
        this.servicioUsuario = new UsuarioServicio();
    }

    /**
     * Método que maneja la petición POST para modificar un usuario.
     * 
     * @param peticion  objeto que contiene la petición HTTP
     * @param respuesta objeto que contiene la respuesta HTTP
     * @throws ServletException si ocurre un error en la ejecución del servlet
     * @throws IOException      si ocurre un error en la lectura o escritura de datos
     */
    @Override
    protected void doPost(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
        try {
            // Verificar sesión y rol
            HttpSession sesion = peticion.getSession(false);
            if (sesion == null) {
                GestorRegistros.sistemaWarning("Intento de modificar usuario sin sesión válida desde IP: " 
                    + peticion.getRemoteAddr());
                respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
                return;
            }

            UsuarioDto adminActual = (UsuarioDto) sesion.getAttribute("usuario");
            if (adminActual == null) {
                GestorRegistros.sistemaWarning("Intento de modificar usuario sin usuario en sesión desde IP: " 
                    + peticion.getRemoteAddr());
                respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
                return;
            }
            
            if (adminActual.getRolId() != 1) {
                GestorRegistros.warning(adminActual.getId(), 
                    "Intento no autorizado de modificar usuario. Rol actual: " + adminActual.getRolId());
                respuesta.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
                return;
            }

            // Obtener datos del formulario
            String idCadena = peticion.getParameter("id");
            if (idCadena == null || idCadena.trim().isEmpty()) {
                GestorRegistros.warning(adminActual.getId(), "Intento de modificar usuario sin proporcionar ID");
                throw new ServletException("ID de usuario no proporcionado");
            }
            
            long id = Long.parseLong(idCadena);
            if (id == 4L) {
                String errorMsg = "No está permitido modificar este usuario.";
                respuesta.sendRedirect(peticion.getContextPath() + "/admin/usuarios?error=" + URLEncoder.encode(errorMsg, "UTF-8"));
                return;
            }
            String nombreCompleto = peticion.getParameter("nombreCompleto");
            String correoElectronico = peticion.getParameter("correoElectronico");
            String movil = peticion.getParameter("movil");
            Long rolId = Long.parseLong(peticion.getParameter("rolId"));
            
            // Crear DTO para actualización
            CrearUsuDto usuarioActualizar = new CrearUsuDto();
            usuarioActualizar.setNombreCompleto(nombreCompleto);
            usuarioActualizar.setCorreoElectronico(correoElectronico);
            usuarioActualizar.setRolId(rolId);
            usuarioActualizar.setMovil(movil);

            // Procesar la foto si existe
            Part parteFoto = peticion.getPart("foto");
            if (parteFoto != null && parteFoto.getSize() > 0) {
                byte[] bytesFoto = parteFoto.getInputStream().readAllBytes();
                try {
                    String nombreArchivo = parteFoto.getSubmittedFileName();
                    ImagenUtil.verificarImagen(bytesFoto, nombreArchivo);
                    usuarioActualizar.setFoto(bytesFoto);
                } catch (IllegalArgumentException e) {
                    GestorRegistros.warning(adminActual.getId(), 
                        "Error al procesar foto para usuario " + id + ": " + e.getMessage());
                    String errorMsg = "La imagen debe tener un formato válido (JPEG, PNG, GIF, BMP, WEBP) y la extensión debe coincidir con el tipo de archivo.";
                    respuesta.sendRedirect(peticion.getContextPath() + "/admin/usuarios?error=" + URLEncoder.encode(errorMsg, "UTF-8"));
                    return;
                }
            }

            // Actualizar el usuario
            servicioUsuario.actualizarUsuario(id, usuarioActualizar);
            GestorRegistros.info(adminActual.getId(), "Usuario modificado exitosamente. ID: " + id );

            // Redirigir de vuelta a la lista con mensaje de éxito
            peticion.getSession().setAttribute("mensaje", "Usuario actualizado con éxito");
            respuesta.sendRedirect(peticion.getContextPath() + "/admin/usuarios");

        } catch (IllegalArgumentException e) {
            try {
                UsuarioDto admin = (UsuarioDto) peticion.getSession().getAttribute("usuario");
                GestorRegistros.error(admin.getId(), 
                    "Error de validación al modificar usuario: " + e.getMessage());
            } catch (Exception ex) {
                GestorRegistros.sistemaError("Error de validación al modificar usuario - IP: " 
                    + peticion.getRemoteAddr());
            }
            peticion.getSession().setAttribute("error", "Error al validar la imagen: " + e.getMessage());
            respuesta.sendRedirect(peticion.getContextPath() + "/admin/usuarios");
        } catch (Exception e) {
            try {
                UsuarioDto admin = (UsuarioDto) peticion.getSession().getAttribute("usuario");
                GestorRegistros.error(admin.getId(), 
                    "Error al modificar usuario: " + e.getMessage());
            } catch (Exception ex) {
                GestorRegistros.sistemaError("Error al modificar usuario - IP: " + peticion.getRemoteAddr() 
                    + " - Error: " + e.getMessage());
            }
            String mensajeError = e.getMessage();
            // Si el mensaje es útil y no es genérico, mostrarlo tal cual
            if (mensajeError != null && !mensajeError.isBlank() && !mensajeError.toLowerCase().contains("error al actualizar usuario")) {
                peticion.getSession().setAttribute("error", mensajeError);
            } else {
                peticion.getSession().setAttribute("error", "Ha ocurrido un error al actualizar el usuario. Por favor, inténtalo de nuevo.");
            }
            respuesta.sendRedirect(peticion.getContextPath() + "/admin/usuarios");
        }
    }
}