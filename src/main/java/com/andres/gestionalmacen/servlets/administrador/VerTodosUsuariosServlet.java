package com.andres.gestionalmacen.servlets.administrador;

import com.andres.gestionalmacen.dtos.UsuarioDto;
import com.andres.gestionalmacen.servicios.UsuarioServicio;
import com.andres.gestionalmacen.utilidades.GestorRegistros;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.andres.gestionalmacen.dtos.PaginacionDto;
import java.io.IOException;
import java.util.List;

/**
 * Servlet que maneja la visualización de todos los usuarios.
 * 
 * @author Andrés
 */
@WebServlet("/admin/usuarios")
public class VerTodosUsuariosServlet extends HttpServlet {

    private final UsuarioServicio usuarioServicio;

    /**
     * Constructor que inicializa el servicio de usuarios.
     */
    public VerTodosUsuariosServlet() {
        this.usuarioServicio = new UsuarioServicio();
    }

    /**
     * Método que maneja la solicitud GET para visualizar la lista de usuarios.
     * 
     * @param peticion  La solicitud HTTP.
     * @param respuesta La respuesta HTTP.
     * @throws ServletException Si ocurre un error en la ejecución del servlet.
     * @throws IOException      Si ocurre un error en la lectura o escritura de la solicitud o respuesta.
     */
    @Override
    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
        try {
            // Verificar sesión y rol (código existente)
            HttpSession sesion = peticion.getSession(false);
            
            if (sesion == null || sesion.getAttribute("usuario") == null) {
                GestorRegistros.sistemaWarning("Intento de acceso a gestión de usuarios sin sesión válida desde IP: " 
                    + peticion.getRemoteAddr());
                respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
                return;
            }
            
            UsuarioDto usuarioActual = (UsuarioDto) sesion.getAttribute("usuario");
            
            if (usuarioActual.getRolId() != 1) {
                GestorRegistros.warning(usuarioActual.getId(), 
                    "Intento de acceso no autorizado a gestión de usuarios. Rol actual: " + usuarioActual.getRolId());
                respuesta.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
                return;
            }
            
            // Log de acceso exitoso
            GestorRegistros.info(usuarioActual.getId(), "Acceso a la gestión de usuarios");

            // Recoger mensaje de error de la URL (si existe)
            String error = peticion.getParameter("error");
            if (error != null) {
                peticion.setAttribute("error", error);
            }

            // Parámetros de paginación
            int pagina = 0;
            String paginaParam = peticion.getParameter("page");
            System.out.println("[DEBUG] Valor recibido de page: " + paginaParam);
            if (paginaParam != null) {
                try { pagina = Integer.parseInt(paginaParam); } catch (NumberFormatException ignored) {}
            }
            System.out.println("[DEBUG] Página solicitada: " + pagina);
            int tamanio = 10;
            PaginacionDto<UsuarioDto> paginacion = usuarioServicio.obtenerUsuariosPaginados(pagina, tamanio);
            if (paginacion != null) {
                System.out.println("[DEBUG] Página actual en DTO: " + paginacion.getNumero());
                System.out.println("[DEBUG] Total de páginas: " + paginacion.getTotalPaginas());
                System.out.println("[DEBUG] Usuarios en la página: " + (paginacion.getContenido() != null ? paginacion.getContenido().size() : 0));
            }
            List<UsuarioDto> usuarios = paginacion != null ? paginacion.getContenido() : null;
            // Convertir LocalDateTime a java.util.Date para cada usuario y mostrar fecha en jsp en dd/MM/yyyy HH:mm
            if (usuarios != null) {
                for (UsuarioDto usuario : usuarios) {
                    if (usuario.getFechaCreacion() != null) {
                        usuario.setFechaCreacionDate(java.util.Date.from(usuario.getFechaCreacion().atZone(java.time.ZoneId.systemDefault()).toInstant()));
                    }
                    // No asignar imagen por defecto aquí; la JSP lo gestiona
                }
            }

            peticion.setAttribute("paginacion", paginacion);
            if (usuarios == null) {
                GestorRegistros.warning(usuarioActual.getId(), "No se encontraron usuarios en el sistema");
            }

            peticion.getRequestDispatcher("/admin/gestionUsuarios.jsp").forward(peticion, respuesta);

        } catch (Exception e) {
            // Obtener el ID del usuario si está disponible
            Long usuId = null;
            try {
                HttpSession sesion = peticion.getSession(false);
                if (sesion != null && sesion.getAttribute("usuario") != null) {
                    usuId = ((UsuarioDto) sesion.getAttribute("usuario")).getId();
                }
            } catch (Exception ex) {
                // Si hay error al obtener el usuario, se registrará como error del sistema
            }

            // Registrar el error
            if (usuId != null) {
                GestorRegistros.error(usuId, "Error al cargar la gestión de usuarios: " + e.getMessage());
            } else {
                GestorRegistros.sistemaError("Error al cargar la gestión de usuarios: " + e.getMessage() 
                    + " - IP: " + peticion.getRemoteAddr());
            }

            // Mostrar el mensaje de error en la vista de gestión de usuarios
            peticion.setAttribute("error", e.getMessage());
            peticion.getRequestDispatcher("/admin/gestionUsuarios.jsp").forward(peticion, respuesta);
        }
    }
}