package com.andres.gestionalmacen.servlets.administrador;

import com.andres.gestionalmacen.dtos.CrearUsuDto;
import com.andres.gestionalmacen.dtos.UsuarioDto;
import com.andres.gestionalmacen.servicios.UsuarioServicio;
import com.andres.gestionalmacen.utilidades.EncriptarUtil;
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


@WebServlet("/admin/usuarios/crear")
@MultipartConfig
/**
 * Servlet que maneja la creacion  de usuarios
 * 
 * @author Andrés
 */
public class CrearUsuarioServlet extends HttpServlet {

    private final UsuarioServicio usuarioServicio;

    /**
     * Constructor que inicializa el servicio de usuarios.
     */
    public CrearUsuarioServlet() {
        this.usuarioServicio = new UsuarioServicio();
    }

    /**
     * Método que maneja la petición POST para crear un nuevo usuario.
     * 
     * @param peticion  La petición HTTP.
     * @param respusta La respuesta HTTP.
     * @throws ServletException Si ocurre un error en la ejecución del servlet.
     * @throws IOException      Si ocurre un error en la lectura o escritura de datos.
     */
    @Override
    protected void doPost(HttpServletRequest peticion, HttpServletResponse respusta) throws ServletException, IOException {
        try {
            // Verificar sesión y permisos
            HttpSession sesion = peticion.getSession(false);
            if (sesion == null || sesion.getAttribute("usuario") == null) {
                GestorRegistros.sistemaWarning("Intento de crear usuario sin sesión válida desde IP: " 
                    + peticion.getRemoteAddr());
                    respusta.sendRedirect(peticion.getContextPath() + "/acceso");
                return;
            }
            
            UsuarioDto adminActual = (UsuarioDto) sesion.getAttribute("usuario");
            if (adminActual.getRolId() != 1) {
                GestorRegistros.warning(adminActual.getId(), 
                    "Intento no autorizado de crear usuario. Rol actual: " + adminActual.getRolId());
                    respusta.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
                return;
            }

            // Obtener datos del formulario
            String nombreCompleto = peticion.getParameter("nombreCompleto");
            String correoElectronico = peticion.getParameter("correoElectronico");
            String movil = peticion.getParameter("movil");
            String contrasena = peticion.getParameter("contrasena");
            Long rolId = Long.valueOf(peticion.getParameter("rolId"));
            
            GestorRegistros.info(adminActual.getId(), 
                "Iniciando creación de usuario: " + correoElectronico + " con rol: " + rolId);

            String contrasenaEncriptada = EncriptarUtil.contraseñaHash(contrasena);

            // Procesar la foto si existe
            Part fotoPart = peticion.getPart("foto");
            byte[] fotoBytes = null;
            if (fotoPart != null && fotoPart.getSize() > 0) {
                fotoBytes = fotoPart.getInputStream().readAllBytes();
                try {
                    String nombreArchivo = fotoPart.getSubmittedFileName();
                    ImagenUtil.verificarImagen(fotoBytes, nombreArchivo);
                } catch (IllegalArgumentException e) {
                    GestorRegistros.warning(adminActual.getId(), 
                        "Error al procesar foto para usuario " + correoElectronico + ": " + e.getMessage());
                        String errorMsg = "La imagen debe tener un formato válido (JPEG, PNG, GIF, BMP, WEBP) y la extensión debe coincidir con el tipo de archivo.";
                        respusta.sendRedirect(peticion.getContextPath() + "/admin/usuarios?error=" + java.net.URLEncoder.encode(errorMsg, "UTF-8"));
                        return;
                }
            }

            // Crear el DTO
            CrearUsuDto nuevoUsuario = new CrearUsuDto();
            nuevoUsuario.setNombreCompleto(nombreCompleto);
            nuevoUsuario.setMovil(movil);
            nuevoUsuario.setCorreoElectronico(correoElectronico);
            nuevoUsuario.setContrasena(contrasenaEncriptada);
            nuevoUsuario.setRolId(rolId);
            nuevoUsuario.setFoto(fotoBytes);
            nuevoUsuario.setCorreoConfirmado(true); // Por defecto, está confirmado
            nuevoUsuario.setGoogle(false); // Los usuarios creados por el admin no son de Google

            // Limpiar errores previos
            peticion.getSession().removeAttribute("error");

            // Guardar el usuario
            usuarioServicio.crearUsuario(nuevoUsuario);
            GestorRegistros.info(adminActual.getId(), 
                "Usuario creado exitosamente: " + correoElectronico);

            // Redirigir de vuelta a la lista con mensaje de éxito
            peticion.getSession().setAttribute("mensaje", "Usuario creado con éxito");
            respusta.sendRedirect(peticion.getContextPath() + "/admin/usuarios");

        }catch (Exception e) {
                try {
                    UsuarioDto admin = (UsuarioDto) peticion.getSession().getAttribute("usuario");
                    GestorRegistros.error(admin.getId(), "Error al crear usuario: " + e.getMessage());
                } catch (Exception ex) {
                    GestorRegistros.sistemaError("Error al crear usuario - IP: " + peticion.getRemoteAddr() 
                        + " - Error: " + e.getMessage());
                }
                String mensajeError = e.getMessage();
                // Si el mensaje es conocido y útil, mostrarlo al usuario; si no, uno genérico
                if (mensajeError != null && !mensajeError.isBlank() && !mensajeError.toLowerCase().contains("error al crear usuario")) {
                    peticion.getSession().setAttribute("error", mensajeError);
                } else {
                    peticion.getSession().setAttribute("error", "Ha ocurrido un error al crear el usuario. Por favor, inténtalo de nuevo.");
                }
                respusta.sendRedirect(peticion.getContextPath() + "/admin/usuarios");
            }
        }
    
}