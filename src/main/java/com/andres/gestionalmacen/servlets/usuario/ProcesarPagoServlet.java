package com.andres.gestionalmacen.servlets.usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.andres.gestionalmacen.utilidades.GestorRegistros;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Servlet que maneja el procesamiento de pagos.
 * Este servlet verifica la sesión del usuario y procesa el pago a través de PayPal.
 * @author Andrés
 */
@WebServlet("/usuario/procesarPago")
public class ProcesarPagoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcesarPagoServlet.class);
    private APIContext contextoApi; // APIContext para PayPal

    /**
     * Inicializa el servlet y configura la conexión con PayPal.
     * 
     * @throws ServletException si ocurre un error durante la inicialización
     */
    @Override
    public void init() throws ServletException {
        try {
            String clienteId = com.andres.gestionalmacen.configuracion.Configuracion.obtenerPropiedad("paypal.client.id", "");
            String clienteSecreto = com.andres.gestionalmacen.configuracion.Configuracion.obtenerPropiedad("paypal.client.secret", "");
            String modo = com.andres.gestionalmacen.configuracion.Configuracion.obtenerPropiedad("paypal.mode", "sandbox");
            
            LOGGER.info("Inicializando PayPal - Mode: {}", modo);
            
            if (clienteId == null || clienteId.isEmpty()) {
                throw new ServletException("PayPal Client ID no configurado");
            }
            if (clienteSecreto == null || clienteSecreto.isEmpty()) {
                throw new ServletException("PayPal Client Secret no configurado");
            }
            
            // Configuración del SDK de PayPal
            Map<String, String> sdkConf = new HashMap<>();
            sdkConf.put("mode", modo);
            sdkConf.put("http.ConnectionTimeOut", "30000");
            sdkConf.put("http.RetryCount", "1");
            sdkConf.put("http.ReadTimeOut", "30000");
            sdkConf.put("http.MaxConnection", "100");
            
            if ("sandbox".equals(modo)) {
            sdkConf.put("service.EndPoint", "https://api.sandbox.paypal.com");
            sdkConf.put("oauth.EndPoint", "https://api.sandbox.paypal.com");
            } else {
                sdkConf.put("service.EndPoint", "https://api.paypal.com");
                sdkConf.put("oauth.EndPoint", "https://api.paypal.com");
            }
            
            contextoApi = new APIContext(clienteId, clienteSecreto, modo);
            contextoApi.setConfigurationMap(sdkConf);
            
            LOGGER.info("PayPal inicializado correctamente con modo: {} y endpoint: {}", 
                       modo, sdkConf.get("service.EndPoint"));
        } catch (Exception e) {
            LOGGER.error("Error al inicializar PayPal", e);
            throw new ServletException("Error al inicializar PayPal", e);
        }
    }

    /**
     * Maneja las solicitudes GET y redirige al panel de usuario.
     * 
     * @param solicitud la solicitud HTTP
     * @param respuesta la respuesta HTTP
     * @throws ServletException si ocurre un error durante el procesamiento
     * @throws IOException si ocurre un error de E/S
     */
    @Override
    protected void doGet(HttpServletRequest solicitud, HttpServletResponse respuesta) 
            throws ServletException, IOException {
        LOGGER.info("Intento de acceso GET a ProcesarPagoServlet");
        // Si intentan acceder directamente al servlet por GET, redirigir al panel
        respuesta.sendRedirect(solicitud.getContextPath() + "/usuario/panel");
    }

    /**
     * Maneja las solicitudes POST y procesa el pago a través de PayPal.
     * 
     * @param solicitud la solicitud HTTP
     * @param respuesta la respuesta HTTP
     * @throws ServletException si ocurre un error durante el procesamiento
     * @throws IOException si ocurre un error de E/S
     */
    @Override
    protected void doPost(HttpServletRequest solicitud, HttpServletResponse respuesta) 
            throws ServletException, IOException {
        
        LOGGER.info("Iniciando procesamiento de pago");
        
        try {
            HttpSession sesion = solicitud.getSession(false);
            Object usuarioObj = (sesion != null) ? sesion.getAttribute("usuario") : null;
            Long usuarioId = (usuarioObj instanceof com.andres.gestionalmacen.dtos.UsuarioDto)
                ? ((com.andres.gestionalmacen.dtos.UsuarioDto) usuarioObj).getId() : null;
            GestorRegistros.info(usuarioId, "Inicio de procesamiento de pago.");
            if (sesion == null || sesion.getAttribute("usuario") == null) {
                GestorRegistros.warning(null, "Intento de procesamiento de pago sin sesión válida.");
                LOGGER.error("Sesión inválida");
                respuesta.sendRedirect(solicitud.getContextPath() + "/acceso");
                return;
            }

            String montoTotalStr = solicitud.getParameter("total");
            String idSector = solicitud.getParameter("sectorId");
            String nombreSector = solicitud.getParameter("sectorName");
            
            LOGGER.info("Parámetros recibidos - montoTotal: {}, idSector: {}, nombreSector: {}", 
                       montoTotalStr, idSector, nombreSector);
            
            if (montoTotalStr == null || idSector == null) {
                GestorRegistros.warning(usuarioId, "Parámetros de pago incompletos o inválidos.");
                LOGGER.error("Parámetros faltantes");
                solicitud.setAttribute("error", "Parámetros de pago incompletos");
                solicitud.getRequestDispatcher("/usuario/pago-cancelado.jsp").forward(solicitud, respuesta);
                return;
            }

            try {
                double montoTotal = Double.parseDouble(montoTotalStr);
                if (montoTotal <= 0) {
                    GestorRegistros.warning(usuarioId, "Monto de pago inválido: " + montoTotal);
                    LOGGER.error("Monto inválido: {}", montoTotal);
                    solicitud.setAttribute("error", "Monto de pago inválido");
                    solicitud.getRequestDispatcher("/usuario/pago-cancelado.jsp").forward(solicitud, respuesta);
                    return;
                }

                String urlBase = solicitud.getScheme() + "://" + solicitud.getServerName() + ":" + 
                               solicitud.getServerPort() + solicitud.getContextPath();
                
                LOGGER.info("Creando pago en PayPal - Monto: {}, BaseURL: {}", montoTotal, urlBase);
                
                Payment pago = crearPago(
                    montoTotal,
                    "EUR",
                    urlBase + "/usuario/pagoCancelado",
                    urlBase + "/usuario/completarPago"
                );

                if (pago == null) {
                    GestorRegistros.error(usuarioId, "Error al crear el pago en PayPal.");
                    LOGGER.error("Error al crear el pago en PayPal - pago es null");
                    solicitud.setAttribute("error", "Error al crear el pago");
                    solicitud.getRequestDispatcher("/usuario/pago-cancelado.jsp").forward(solicitud, respuesta);
                    return;
                }

                GestorRegistros.info(usuarioId, "Pago creado en PayPal. paymentId: " + pago.getId());
                LOGGER.info("Pago creado en PayPal - ID: {}", pago.getId());

                String urlRedireccion = null;
                for(Links enlace : pago.getLinks()) {
                    LOGGER.info("Link encontrado - Rel: {}, Href: {}", enlace.getRel(), enlace.getHref());
                    if("approval_url".equals(enlace.getRel())) {
                        urlRedireccion = enlace.getHref();
                    }
                }

                if (urlRedireccion != null) {
                    sesion.setAttribute("paymentId", pago.getId());
                    sesion.setAttribute("sectorId", idSector);
                    sesion.setAttribute("sectorName", nombreSector);
                    GestorRegistros.info(usuarioId, "Redirigiendo a PayPal para aprobación del pago.");
                    LOGGER.info("Redirigiendo a PayPal: {}", urlRedireccion);
                    respuesta.sendRedirect(urlRedireccion);
                } else {
                    GestorRegistros.error(usuarioId, "No se encontró URL de aprobación en la respuesta de PayPal");
                    LOGGER.error("No se encontró URL de aprobación en la respuesta de PayPal");
                    solicitud.setAttribute("error", "Error en la configuración de PayPal");
                    solicitud.getRequestDispatcher("/usuario/pagoCancelado").forward(solicitud, respuesta);
                }
                
            } catch (NumberFormatException e) {
                GestorRegistros.error(usuarioId, "Error al convertir el monto: " + montoTotalStr);
                LOGGER.error("Error al convertir el monto: {}", montoTotalStr, e);
                solicitud.setAttribute("error", "Monto de pago inválido");
                solicitud.getRequestDispatcher("/usuario/pagoCancelado").forward(solicitud, respuesta);
            }
            
        } catch (PayPalRESTException e) {
            GestorRegistros.error(null, "Error de PayPal: " + e.getMessage());
            LOGGER.error("Error de PayPal: {}", e.getMessage(), e);
            solicitud.setAttribute("error", "Error en el servicio de PayPal: " + e.getMessage());
            solicitud.getRequestDispatcher("/usuario/pagoCancelado").forward(solicitud, respuesta);
        } catch (Exception e) {
            GestorRegistros.error(null, "Error inesperado al procesar el pago: " + e.getMessage());
            LOGGER.error("Error general: {}", e.getMessage(), e);
            solicitud.setAttribute("error", "Error inesperado al procesar el pago");
            solicitud.getRequestDispatcher("/usuario/pagoCancelado").forward(solicitud, respuesta);
        }
    }

    /**
     * Crea un pago en PayPal con los parámetros especificados.
     * 
     * @param monto el monto del pago
     * @param moneda la moneda del pago
     * @param urlCancelar la URL de cancelación del pago
     * @param urlOk la URL de éxito del pago
     * @return el pago creado en PayPal
     * @throws PayPalRESTException si ocurre un error durante la creación del pago
     */
    private Payment crearPago(double monto, String moneda, 
            String urlCancelar, String urlOk) throws PayPalRESTException {
                
            LOGGER.info("Creando pago - Monto: {}, Moneda: {}", monto, moneda);
            monto = Double.parseDouble(String.valueOf(monto).replace(",", ".")); // Reemplazar la coma por un punto
            
            // Formatear el monto correctamente para PayPal usando Locale.US para asegurar el punto decimal
            String montoFormateado = String.format(Locale.US, "%.2f", monto);
            LOGGER.info("Monto formateado: {}", montoFormateado);
            
            Amount cantidad = new Amount();
            cantidad.setCurrency(moneda);
            cantidad.setTotal(montoFormateado);

            Transaction transaccion = new Transaction();
            transaccion.setAmount(cantidad);
            transaccion.setDescription("Alquiler de Sector en Gestión Almacén");

            List<Transaction> transacciones = new ArrayList<>();
            transacciones.add(transaccion);

            Payer pagador = new Payer();
            pagador.setPaymentMethod("paypal");

            Payment pago = new Payment();
            pago.setIntent("sale");
            pago.setPayer(pagador);
            pago.setTransactions(transacciones);

            RedirectUrls urlsRedireccion = new RedirectUrls();
            urlsRedireccion.setCancelUrl(urlCancelar);
            urlsRedireccion.setReturnUrl(urlOk);
            
            pago.setRedirectUrls(urlsRedireccion);
            
            return pago.create(contextoApi);
    }
}