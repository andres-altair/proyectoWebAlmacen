package com.andres.gestionalmacen.servlets.usuario;

import com.andres.gestionalmacen.dtos.UsuarioDto;
import com.andres.gestionalmacen.servicios.UsuarioServicio;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import com.andres.gestionalmacen.utilidades.GestorRegistros;
import com.andres.gestionalmacen.utilidades.ImagenUtil;


/**
 * Servlet que gestiona el perfil del usuario autenticado.
 * Permite visualizar y actualizar los datos personales y la foto de perfil.
 *
 * @author andres
 */
@WebServlet("/usuario/perfil")
@MultipartConfig // para soporte de subida de foto
public class PerfilUsuarioServlet extends HttpServlet {
    private UsuarioServicio usuarioServicio = new UsuarioServicio();

    /**
     * Maneja las peticiones GET para mostrar el perfil del usuario autenticado.
     * Valida la sesión y prepara los datos para la vista.
     *
     * @param peticion  Petición HTTP recibida
     * @param respuesta Respuesta HTTP a enviar
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
        // Obtener la sesión sin crear una nueva si no existe
        HttpSession sesion = peticion.getSession(false);
        UsuarioDto usuario = (UsuarioDto) (sesion != null ? sesion.getAttribute("usuario") : null);
        if (sesion == null || usuario == null) {
            GestorRegistros.warning(null, "Intento de acceso a perfil sin sesión o usuario válido.");
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }
        // Verificar el rol del usuario (4 = Usuario)
        if (usuario.getRolId() != 4) {
            GestorRegistros.warning(usuario.getId(), "Intento de acceso a perfil con rol incorrecto.");
            sesion.setAttribute("error", "No tienes permiso para acceder a esta página");
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }
        try {
            // Pasar datos del usuario a la vista
            peticion.setAttribute("usuario", usuario);
            GestorRegistros.info(usuario.getId(), "Acceso a la vista de perfil de usuario.");
            peticion.getRequestDispatcher("/usuario/perfilUsuario.jsp").forward(peticion, respuesta);
        } catch (Exception e) {
            GestorRegistros.error(usuario.getId(), "Error al cargar el perfil: " + e.getMessage());
            peticion.setAttribute("error", "No se pudo cargar el perfil: " + e.getMessage());
            peticion.getRequestDispatcher("/usuario/perfilUsuario.jsp").forward(peticion, respuesta);
        }
    }

    /**
     * Maneja las peticiones POST para actualizar los datos del perfil del usuario.
     * Permite modificar nombre, móvil y foto de perfil.
     *
     * @param peticion  Petición HTTP recibida
     * @param respuesta Respuesta HTTP a enviar
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de entrada/salida
     */
    @Override
    protected void doPost(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
        // Obtener la sesión sin crear una nueva si no existe
        HttpSession sesion = peticion.getSession(false);
        UsuarioDto usuario = (UsuarioDto) (sesion != null ? sesion.getAttribute("usuario") : null);
        if (sesion == null || usuario == null) {
            GestorRegistros.warning(null, "Intento de acceso a actualización de perfil sin sesión o usuario válido.");
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }
        Long usuarioId = usuario.getId();
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
                    respuesta.sendRedirect(peticion.getContextPath() + "/usuario/perfilUsuario.jsp");
                    return;
                }
            }

            // Actualizar usuario (requiere método de actualización en UsuarioServicio)
            usuarioServicio.actualizarPerfil(usuarioId, usuario);
            GestorRegistros.info(usuarioId, "Perfil de usuario actualizado correctamente.");
            peticion.setAttribute("usuario", usuario);
            peticion.setAttribute("mensaje", "Perfil actualizado correctamente");
            peticion.getRequestDispatcher("/usuario/perfilUsuario.jsp").forward(peticion, respuesta);
        } catch (Exception e) {
            GestorRegistros.error(usuarioId, "Error al actualizar el perfil: " + e.getMessage());
            peticion.setAttribute("error", "No se pudo actualizar el perfil: " + e.getMessage());
            peticion.getRequestDispatcher("/usuario/perfilUsuario.jsp").forward(peticion, respuesta);
        }
    }


}