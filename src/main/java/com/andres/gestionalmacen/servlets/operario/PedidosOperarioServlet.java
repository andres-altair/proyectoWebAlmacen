package com.andres.gestionalmacen.servlets.operario;

import com.andres.gestionalmacen.dtos.UsuarioDto;
import com.andres.gestionalmacen.dtos.PedidoDto;
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
 * Servlet que gestiona el pedido  del operario.
 * @author Andrés
 */
@WebServlet("/operario/pedidos")
public class PedidosOperarioServlet extends HttpServlet {
    private PedidoServicio pedidoServicio = new PedidoServicio();
    
    /**
     * Maneja las peticiones GET el pedido  del operario.
     *
     * @param peticion La petición HTTP del cliente
     * @param respuesta La respuesta HTTP al cliente
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de E/S
     */
    @Override
    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
        UsuarioDto usuario = (UsuarioDto) peticion.getSession().getAttribute("usuario");
        if (usuario == null || usuario.getRolId() != 3) { 
            GestorRegistros.warning(
                usuario != null ? usuario.getId() : null, 
                "Intento no autorizado de acceso al panel de pedidos de operario. Rol actual: " + (usuario != null ? usuario.getRolId() : "null")
            );
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

            // Obtener pedidos pendientes y en_proceso del operario
            String pendientesJson = pedidoServicio.listarPedidosPorEstado("pendiente");
            String enProcesoJson = pedidoServicio.listarPedidosPorEstadoYTipo("en_proceso", "operarioId", usuario.getId());
            String procesadosJson = pedidoServicio.listarPedidosPorEstadoYTipo("procesado", "operarioId", usuario.getId());

            ObjectMapper mapeador = new ObjectMapper();
            List<PedidoDto> pedidosPendientes = mapeador.readValue(
                pendientesJson, new TypeReference<List<PedidoDto>>() {}
            );
            List<PedidoDto> pedidosEnProceso = mapeador.readValue(
                enProcesoJson, new TypeReference<List<PedidoDto>>() {}
            );
            List<PedidoDto> pedidosProcesados = mapeador.readValue(
                procesadosJson, new TypeReference<List<PedidoDto>>() {}
            );

            peticion.setAttribute("pedidosPendientes", pedidosPendientes);
            peticion.setAttribute("pedidosEnProceso", pedidosEnProceso);
            peticion.setAttribute("pedidosProcesados", pedidosProcesados);

            // Mostrar panel
            GestorRegistros.info(usuario.getId(), "Panel de operario cargado exitosamente");
            peticion.getRequestDispatcher("/operario/pedidos.jsp").forward(peticion, respuesta);
            return; // <-- IMPORTANTE: Termina el método aquí

        } catch (Exception e) {
            GestorRegistros.error(usuario.getId(), 
                "Error al cargar el panel de operario: " + e.getMessage());
            peticion.setAttribute("error", "No se pudieron cargar los pedidos: " + e.getMessage());
            peticion.getRequestDispatcher("/operario/pedidos.jsp").forward(peticion, respuesta);
        }
    }
    /**
     * Maneja las peticiones POST el pedido  del operario.
     * @param peticion objeto que contiene la petición HTTP
     * @param respuesta objeto que contiene la respuesta HTTP
     * @throws ServletException si ocurre un error en el servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doPost(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
        String accion = peticion.getParameter("accion");
        Long pedidoId = Long.parseLong(peticion.getParameter("pedidoId"));
        Long usuarioId = Long.parseLong(peticion.getParameter("usuarioId"));

        try {
            String resultado = "";
            if ("asignarOperario".equals(accion)) {
                resultado = pedidoServicio.asignarOperario(pedidoId, usuarioId);
                GestorRegistros.info(usuarioId, "Operario asignado al pedido. Pedido ID: " + pedidoId);
                peticion.setAttribute("resultado", resultado);
            } else if ("marcarProcesado".equals(accion)) {
                resultado = pedidoServicio.marcarProcesado(pedidoId, usuarioId);
                GestorRegistros.info(usuarioId, "Pedido marcado como procesado. Pedido ID: " + pedidoId);
                peticion.setAttribute("resultado", resultado);
            }
            // Redirige tras éxito
            respuesta.sendRedirect(peticion.getContextPath() + "/operario/pedidos");
        } catch (Exception e) {
            GestorRegistros.error(usuarioId, "Error en acción POST de pedidos de operario: " + e.getMessage());
            // Captura y muestra el error del backend o de negocio
            peticion.setAttribute("error", e.getMessage());
            peticion.getRequestDispatcher("/operario/pedidos.jsp").forward(peticion, respuesta);
        }
    }
}