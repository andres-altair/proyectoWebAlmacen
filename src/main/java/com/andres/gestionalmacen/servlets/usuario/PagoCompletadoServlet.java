package com.andres.gestionalmacen.servlets.usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.andres.gestionalmacen.servicios.SectorServicio;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.andres.gestionalmacen.utilidades.GestorRegistros;
import com.andres.gestionalmacen.servicios.AlquilerServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
/**
 * Servlet que gestiona completar pago del usuario.
 *
 * @author andres
 */
@WebServlet("/usuario/completarPago")
public class PagoCompletadoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(PagoCompletadoServlet.class);
    private APIContext contextoApi;
    /**
     * Inicializa el contexto de la API de PayPal con las credenciales y modo configurados.
     *
     * @throws ServletException si ocurre un error durante la inicialización
     */
    @Override
    public void init() throws ServletException {
        String clienteId = com.andres.gestionalmacen.configuracion.Configuracion.obtenerPropiedad("paypal.client.id", "");
        String clienteSecreto = com.andres.gestionalmacen.configuracion.Configuracion.obtenerPropiedad("paypal.client.secret", "");
        String modo = com.andres.gestionalmacen.configuracion.Configuracion.obtenerPropiedad("paypal.mode", "sandbox");
        contextoApi = new APIContext(clienteId, clienteSecreto, modo);
        GestorRegistros.sistemaInfo("Inicializado PagoCompletadoServlet con modo PayPal: " + modo);
    }
    /**
     * Maneja las peticiones GET para completar el pago del usuario.
     * Ejecuta el pago con PayPal, registra el alquiler y actualiza el estado del sector.
     * Redirige o muestra la vista correspondiente según el resultado.
     *
     * @param solicitud  Petición HTTP recibida
     * @param respuesta  Respuesta HTTP a enviar
     * @throws ServletException si ocurre un error en el servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest solicitud, HttpServletResponse respuesta) 
            throws ServletException, IOException {
        try {
            HttpSession sesion = solicitud.getSession(false);
            if (sesion == null || sesion.getAttribute("usuario") == null) {
                GestorRegistros.sistemaWarning("Intento de completar pago sin sesión válida desde IP: " + solicitud.getRemoteAddr());
                LOGGER.error("Intento de completar pago sin sesión válida");
                respuesta.sendRedirect(solicitud.getContextPath() + "/acceso");
                return;
            }
            String idPago = solicitud.getParameter("paymentId");
            String idPagador = solicitud.getParameter("PayerID");
            String idPagoGuardado = (String) sesion.getAttribute("paymentId");
            String idSector = (String) sesion.getAttribute("sectorId");
            Long idUsuario = (Long) sesion.getAttribute("usuarioId");

            if (idPago == null || idPagador == null || idPagoGuardado == null || 
                !idPago.equals(idPagoGuardado) || idSector == null) {
                GestorRegistros.sistemaWarning("Parámetros de pago inválidos - PaymentId: " + idPago + ", SectorId: " + idSector);
                LOGGER.error("Parámetros de pago inválidos o faltantes");
                respuesta.sendRedirect(solicitud.getContextPath() + "/usuario/pagoCancelado");
                return;
            }

            Payment pago = new Payment();
            pago.setId(idPago);
            PaymentExecution ejecucionPago = new PaymentExecution();
            ejecucionPago.setPayerId(idPagador);

            Payment pagoEjecutado = pago.execute(contextoApi, ejecucionPago);

            if ("approved".equals(pagoEjecutado.getState().toLowerCase())) {
                GestorRegistros.sistemaInfo("Pago completado exitosamente - PaymentId: " + idPago + 
                    ", SectorId: " + idSector + ", UsuarioId: " + idUsuario);
                LOGGER.info("Pago completado exitosamente. PaymentId: {}, SectorId: {}, UsuarioId: {}", idPago, idSector, idUsuario);
                AlquilerServicio alquilerServicio = new AlquilerServicio();
                // 1. Registrar alquiler en sectores_alquiler
                if (idUsuario != null) {
                    alquilerServicio.registrarAlquiler(idUsuario, Long.parseLong(idSector));
                }
                SectorServicio sectorServicio = new SectorServicio();
                // 2. Cambiar estado del sector a ocupado
                sectorServicio.ocuparSector(Long.parseLong(idSector));

                // Limpiar sesión
                sesion.removeAttribute("paymentId");
                sesion.removeAttribute("sectorId");
                solicitud.getRequestDispatcher("/usuario/pagoCompletado.jsp").forward(solicitud, respuesta);
            } else {
                GestorRegistros.sistemaWarning("Estado de pago no aprobado - Estado: " + pagoEjecutado.getState() + 
                    ", PaymentId: " + idPago);
                LOGGER.error("Estado de pago no aprobado: {}", pagoEjecutado.getState());
                respuesta.sendRedirect(solicitud.getContextPath() + "/usuario/pagoCancelado");
            }
        } catch (PayPalRESTException e) {
            GestorRegistros.sistemaError("Error procesando pago con PayPal: " + e.getMessage());
            LOGGER.error("Error procesando pago con PayPal", e);
            respuesta.sendRedirect(solicitud.getContextPath() + "/usuario/pagoCancelado");
        } catch (Exception e) {
            GestorRegistros.sistemaError("Error general completando pago: " + e.getMessage());
            LOGGER.error("Error general completando pago", e);
            respuesta.sendRedirect(solicitud.getContextPath() + "/usuario/pagoCancelado");
        }
    }
    /**
     * Maneja las peticiones POST para completar el pago del usuario.
     * Simplemente delega en el método doGet para reutilizar la lógica.
     *
     * @param solicitud  Petición HTTP recibida
     * @param respuesta  Respuesta HTTP a enviar
     * @throws ServletException si ocurre un error en el servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doPost(HttpServletRequest solicitud, HttpServletResponse respuesta) 
            throws ServletException, IOException {
        doGet(solicitud, respuesta);
    }
}