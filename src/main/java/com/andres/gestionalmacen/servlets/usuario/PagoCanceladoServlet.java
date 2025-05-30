package com.andres.gestionalmacen.servlets.usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.andres.gestionalmacen.utilidades.GestorRegistros;

/**
 * Servlet que gestiona pago cancelado del usuario.
 *
 * @author andres
 */
@WebServlet("/usuario/pagoCancelado")
public class PagoCanceladoServlet extends HttpServlet {
    /**
     * Maneja las peticiones GET para mostrar gestiona pago cancelado del usuario.
     *
     * @param peticion  Petición HTTP recibida
     * @param respuesta Respuesta HTTP a enviar
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object usuario = req.getSession().getAttribute("usuario");
        Long usuarioId = (usuario instanceof com.andres.gestionalmacen.dtos.UsuarioDto)
            ? ((com.andres.gestionalmacen.dtos.UsuarioDto) usuario).getId()
            : null;
        GestorRegistros.info(usuarioId, "El usuario accedió a la pantalla de pago cancelado.");
        req.getRequestDispatcher("/usuario/pagoCancelado.jsp").forward(req, resp);
    }
    /**
     * Maneja las peticiones POST para mostrar gestiona pago cancelado del usuario.
     *
     * @param peticion  Petición HTTP recibida
     * @param respuesta Respuesta HTTP a enviar
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de entrada/salida
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}

