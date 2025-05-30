package com.andres.gestionalmacen.servlets.usuario;

import com.andres.gestionalmacen.servicios.PedidoServicio;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.andres.gestionalmacen.dtos.PedidoRespuestaDto;
import com.andres.gestionalmacen.utilidades.GestorRegistros;


/**
 * Servlet que maneja la visualización de los pedidos del usuario autenticado.
 * Obtiene la lista de pedidos del usuario, formatea las fechas y prepara los datos para la vista.
 *
 * @author andres
 */
@WebServlet("/usuario/pedido")
public class PedidoUsuarioServlet extends HttpServlet {
    private PedidoServicio pedidoServicio = new PedidoServicio();

    /**
     * Maneja las peticiones GET para mostrar la lista de pedidos del usuario autenticado.
     * Valida la sesión, obtiene los pedidos, formatea las fechas y prepara los datos para la vista.
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
        if (sesion == null || sesion.getAttribute("usuarioId") == null) {
            GestorRegistros.warning(null, "Intento de acceso a pedidos sin sesión activa.");
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }
        // Obtener el usuario autenticado de la sesión
        com.andres.gestionalmacen.dtos.UsuarioDto usuario = (com.andres.gestionalmacen.dtos.UsuarioDto) sesion.getAttribute("usuario");
        // Verificar el rol del usuario (4 = Usuario)
        if (usuario == null || usuario.getRolId() != 4) {
            GestorRegistros.warning(null, "Intento de acceso a pedidos con usuario inválido o sin permisos.");
            sesion.setAttribute("error", "No tienes permiso para acceder a esta página");
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }
        // Obtener el ID del usuario autenticado
        Long usuarioId = usuario.getId();
        List<PedidoRespuestaDto> pedidosList = new ArrayList<>();
        String error = null;
        try {
            // Obtener la lista de pedidos del usuario
            pedidosList = pedidoServicio.listarPedidosPorUsuario(usuarioId);
        } catch (Exception e) {
            GestorRegistros.error(usuarioId, "Error al obtener la lista de pedidos: " + e.getMessage());
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("no tiene pedidos")) {
                // No mostrar error si simplemente no hay pedidos
                pedidosList = new ArrayList<>();
            } else {
                error = e.getMessage();
            }
        }
        // Formatear fechaPedido a fechaPedidoStr para mostrar en la vista
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        for (PedidoRespuestaDto pedido : pedidosList) {
            if (pedido.getFechaPedido() != null) {
                try {
                    String fechaStr = pedido.getFechaPedido().format(formatter);
                    pedido.setFechaPedidoStr(fechaStr);
                } catch (Exception e) {
                    pedido.setFechaPedidoStr(pedido.getFechaPedido().toString());
                }
            } else {
                pedido.setFechaPedidoStr("");
            }
        }
        // Determinar si hay algún pedido en estado de envío o procesamiento
        boolean hayEnviando = false;
        for (PedidoRespuestaDto pedido : pedidosList) {
            String estado = pedido.getEstadoPedido();
            if ("pendiente".equalsIgnoreCase(estado) ||
                "en_proceso".equalsIgnoreCase(estado) ||
                "procesado".equalsIgnoreCase(estado) ||
                "enviando".equalsIgnoreCase(estado) ||
                "entregado".equalsIgnoreCase(estado)) {
                hayEnviando = true;
                break;
            }
        }
        // Pasar los datos a la vista JSP
        peticion.setAttribute("pedidosList", pedidosList);
        peticion.setAttribute("hayEnviando", hayEnviando);
        if (error == null || error.trim().isEmpty()) {
            error = null;
        }
        peticion.setAttribute("error", error);
        GestorRegistros.info(usuarioId, "Acceso a la vista de pedidos del usuario.");
        peticion.getRequestDispatcher("/usuario/pedidoUsuario.jsp").forward(peticion, respuesta);
    }
}
