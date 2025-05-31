package com.andres.gestionalmacen.servlets.transpotista;

import com.andres.gestionalmacen.dtos.PedidoDto;
import com.andres.gestionalmacen.dtos.UsuarioDto;
import com.andres.gestionalmacen.servicios.PedidoServicio;
import com.andres.gestionalmacen.utilidades.GestorRegistros;
import com.andres.gestionalmacen.utilidades.ImagenUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
/**
 * Servlet que maneja el pedidos del transportista.
 * @author Andrés
 */
@WebServlet("/transportista/pedidos")
public class PedidosTransportistaServlet extends HttpServlet {
    private PedidoServicio pedidoServicio = new PedidoServicio();
    /**
     * Maneja las peticiones GET para mostrar pedidos del transportista.
     * @param peticion  Petición HTTP recibida
     * @param respuesta Respuesta HTTP a enviar
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
        // Obtener la sesión actual
        HttpSession sesion = peticion.getSession(false);
        
        // Verificar si la sesión es válida
        if (sesion == null || sesion.getAttribute("usuario") == null) {
            GestorRegistros.sistemaWarning("Intento de acceso al panel de transportista sin sesión válida desde IP: " 
                + peticion.getRemoteAddr());
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }

        // Obtener el usuario logueado
        UsuarioDto usuario = (UsuarioDto) sesion.getAttribute("usuario");
        
        // Verificar si el usuario es operario
        if (usuario.getRolId() != 5) { 
            GestorRegistros.warning(usuario.getId(), 
                "Intento no autorizado de acceso al panel de transportista. Rol actual: " + usuario.getRolId());
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }

        try {
        // Preparar los datos de manera segura
        String nombreCompleto = usuario.getNombreCompleto();
        String fotoBase64 = null;
    
        // Procesar la foto con MIME type
        if (usuario.getFoto() != null) {
        byte[] fotoConMime = ImagenUtil.asegurarMimeTypeImagen(usuario.getFoto());
        if (fotoConMime != null) {
            fotoBase64 = new String(fotoConMime, StandardCharsets.UTF_8);
        } else {
            GestorRegistros.warning(usuario.getId(), "No se pudo procesar la foto de perfil");
        }
    }
    
    // Establecer atributos con valores por defecto si son null
    peticion.setAttribute("usuarioNombre", nombreCompleto != null ? nombreCompleto : "Usuario");
    peticion.setAttribute("usuarioFoto", fotoBase64 != null ? fotoBase64 : "/img/sinUsu.png");

    // Obtener pedidos procesados y enviando del transportista
    String procesadosJson = pedidoServicio.listarPedidosPorEstado("procesado");
    String enviandoJson = pedidoServicio.listarPedidosPorEstadoYTipo("enviando", "transportistaId", usuario.getId());
    String entregadosJson = pedidoServicio.listarPedidosPorEstadoYTipo("entregado", "transportistaId", usuario.getId());


    // CORRECCIÓN: Parsear JSON a lista de objetos
    ObjectMapper mapeador = new ObjectMapper();
    List<PedidoDto> pedidosProcesados = mapeador.readValue(procesadosJson, new TypeReference<List<PedidoDto>>() {});
    List<PedidoDto> pedidosEnviando = mapeador.readValue(enviandoJson, new TypeReference<List<PedidoDto>>() {});
    List<PedidoDto> pedidosEntregados = mapeador.readValue(entregadosJson, new TypeReference<List<PedidoDto>>() {});

    peticion.setAttribute("pedidosProcesados", pedidosProcesados);
    peticion.setAttribute("pedidosEnviando", pedidosEnviando);
    peticion.setAttribute("pedidosEntregados", pedidosEntregados);

    // Mostrar panel
    GestorRegistros.info(usuario.getId(), "Panel de transportista cargado exitosamente");
    peticion.getRequestDispatcher("/transportista/pedidos.jsp").forward(peticion, respuesta);
    return; 
    } catch (Exception e) {
    GestorRegistros.error(usuario.getId(), 
        "Error al cargar el panel de transportista: " + e.getMessage());
    respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
    return;
    }
    }
     /**
     * Maneja las peticiones POST para mostrar pedidos del transportista.
     * @param peticion  Petición HTTP recibida
     * @param respuesta Respuesta HTTP a enviar
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de entrada/salida
     */
    @Override
    protected void doPost(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
    String accion = peticion.getParameter("accion");
    Long pedidoId = Long.parseLong(peticion.getParameter("pedidoId"));
    UsuarioDto usuario = (UsuarioDto) peticion.getSession().getAttribute("usuario");
    Long usuarioId = usuario.getId();

    String resultado = "";
    try {
        if ("asignarTransportista".equals(accion)) {
            resultado = pedidoServicio.asignarTransportista(pedidoId, usuarioId);
            GestorRegistros.info(usuarioId, "Transportista asignado al pedido. Pedido ID: " + pedidoId);
        } else if ("marcarEntregado".equals(accion)) {
            resultado = pedidoServicio.marcarEntregado(pedidoId, usuarioId);
            GestorRegistros.info(usuarioId, "Pedido marcado como entregado. Pedido ID: " + pedidoId);
        }
        peticion.setAttribute("resultado", resultado);
        respuesta.sendRedirect(peticion.getContextPath() + "/transportista/pedidos");
    } catch (Exception e) {
        GestorRegistros.error(usuarioId, "Error en acción POST de pedidos de transportista: " + e.getMessage());
        peticion.setAttribute("error", e.getMessage());
        try {
            peticion.getRequestDispatcher("/transportista/pedidos.jsp").forward(peticion, respuesta);
        } catch (Exception ex) {
            throw new ServletException("Error al mostrar el mensaje de error en pedidos.jsp", ex);
        }
    }
    }
}
