package com.andres.gestionalmacen.servlets.transpotista;

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
import java.io.IOException;
import java.io.InputStream;
/**
 * Servlet que gestiona el perfil del transportista.
 * @author andres
 */
@WebServlet("/transportista/perfil")
@MultipartConfig // para soporte de subida de foto
public class PerfilTransportistaServlet extends HttpServlet {
    private UsuarioServicio usuarioServicio = new UsuarioServicio();
    /**
     * Maneja las peticiones GET para mostrar el perfil del transportista autenticado.
     * 
     * @param peticion objeto que contiene la petición HTTP
     * @param respuesta objeto que contiene la respuesta HTTP
     * @throws ServletException si ocurre un error en el servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
        HttpSession sesion = peticion.getSession(false);
        UsuarioDto usuario = (UsuarioDto) (sesion != null ? sesion.getAttribute("usuario") : null);
        if (sesion == null || usuario == null) {
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }
        // Solo transportistas (rolId=5)
        if (usuario.getRolId() != 5) {
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }
        GestorRegistros.info(usuario.getId(), "Acceso al perfil de transportista.");
        try {
            peticion.setAttribute("usuario", usuario);
            peticion.getRequestDispatcher("/transportista/perfilTransportista.jsp").forward(peticion, respuesta);
        } catch (Exception e) {
            GestorRegistros.error(usuario.getId(), "Error al cargar el perfil de transportista: " + e.getMessage());
            peticion.setAttribute("error", "No se pudo cargar el perfil: " + e.getMessage());
            peticion.getRequestDispatcher("/transportista/perfilTransportista.jsp").forward(peticion, respuesta);
        }
    }
    /**
     * Maneja las peticiones POST para actualizar el perfil del transportista.
     * 
     * @param peticion objeto que contiene la petición HTTP
     * @param respuesta objeto que contiene la respuesta HTTP
     * @throws ServletException si ocurre un error en el servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doPost(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
        HttpSession sesion = peticion.getSession(false);
        UsuarioDto usuario = (UsuarioDto) (sesion != null ? sesion.getAttribute("usuario") : null);
        if (sesion == null || usuario == null) {
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }
        // Solo transportistas (rolId=5)
        if (usuario.getRolId() != 5) {
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
                    respuesta.sendRedirect(peticion.getContextPath() + "/transportista/perfilTransportista.jsp");
                    return;
                }
            }

            // Actualizar usuario (requiere método de actualización en UsuarioServicio)
            usuarioServicio.actualizarPerfil(usuarioId, usuario);
            GestorRegistros.info(usuarioId, "Perfil de transportista actualizado correctamente.");
            peticion.setAttribute("usuario", usuario);
            peticion.setAttribute("mensaje", "Perfil actualizado correctamente");
            peticion.getRequestDispatcher("/transportista/perfilTransportista.jsp").forward(peticion, respuesta);
        } catch (Exception e) {
            GestorRegistros.error(usuarioId, "Error al actualizar el perfil de transportista: " + e.getMessage());
            peticion.setAttribute("error", "No se pudo actualizar el perfil: " + e.getMessage());
            peticion.getRequestDispatcher("/transportista/perfilTransportista.jsp").forward(peticion, respuesta);
        }
    }
}