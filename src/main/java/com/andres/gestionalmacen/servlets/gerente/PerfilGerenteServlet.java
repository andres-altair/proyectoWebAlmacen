package com.andres.gestionalmacen.servlets.gerente;

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
 * Servlet que maneja el perfil del gerente.
 * @author Andrés
 */
@WebServlet("/gerente/perfil")
@MultipartConfig // para soporte de subida de foto
public class PerfilGerenteServlet extends HttpServlet {
    private UsuarioServicio usuarioServicio = new UsuarioServicio();
    /**
     * Método que maneja la petición GET para el perfil del gerente.
     * 
     * @param peticion objeto que contiene la petición HTTP
     * @param respuesta objeto que contiene la respuesta HTTP
     * @throws ServletException si ocurre un error en el servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
        HttpSession sesion = peticion.getSession(false);
        UsuarioDto usuario = (sesion != null) ? (UsuarioDto) sesion.getAttribute("usuario") : null;
        if (sesion == null || usuario == null) {
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }
        if (usuario.getRolId() != 2) { // Solo gerente
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }
        // Log de acceso exitoso al perfil de gerente
        GestorRegistros.info(usuario.getId(), "Acceso al perfil de gerente.");
        try {
            peticion.setAttribute("usuario", usuario);
            peticion.getRequestDispatcher("/gerente/perfilGerente.jsp").forward(peticion, respuesta);
        } catch (Exception e) {
            GestorRegistros.error(usuario.getId(), "Error al cargar el perfil de gerente: " + e.getMessage());
            peticion.setAttribute("error", "No se pudo cargar el perfil: " + e.getMessage());
            peticion.getRequestDispatcher("/gerente/perfilGerente.jsp").forward(peticion, respuesta);
        }
    }
    /**
     * Método que maneja la petición POST para el perfil del gerente.
     * 
     * @param peticion objeto que contiene la petición HTTP
     * @param respuesta objeto que contiene la respuesta HTTP
     * @throws ServletException si ocurre un error en el servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doPost(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
        HttpSession sesion = peticion.getSession(false);
        UsuarioDto usuario = (sesion != null) ? (UsuarioDto) sesion.getAttribute("usuario") : null;
        if (sesion == null || usuario == null) {
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }
        if (usuario.getRolId() != 2) { // Solo gerente
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }
        Long usuarioId = usuario.getId();
        try {
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
                    respuesta.sendRedirect(peticion.getContextPath() + "/gerente/perfilGerente.jsp");
                    return;
                }
            }

            usuarioServicio.actualizarPerfil(usuarioId, usuario);
            GestorRegistros.info(usuarioId, "Perfil de gerente actualizado correctamente.");
            peticion.setAttribute("usuario", usuario);
            peticion.setAttribute("mensaje", "Perfil actualizado correctamente");
            peticion.getRequestDispatcher("/gerente/perfilGerente.jsp").forward(peticion, respuesta);
        } catch (Exception e) {
            GestorRegistros.error(usuarioId, "Error al actualizar el perfil de gerente: " + e.getMessage());
            peticion.setAttribute("error", "No se pudo actualizar el perfil: " + e.getMessage());
            peticion.getRequestDispatcher("/gerente/perfilGerente.jsp").forward(peticion, respuesta);
        }
    }
}

