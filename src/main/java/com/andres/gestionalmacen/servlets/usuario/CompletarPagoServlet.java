package com.andres.gestionalmacen.servlets.usuario;



/*
@WebServlet("/usuario/confirmarPago")
public class CompletarPagoServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompletarPagoServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/acceso");
            return;
        }
        String paymentId = (String) session.getAttribute("paymentId");
        String payerId = request.getParameter("PayerID");
        String sectorId = (String) session.getAttribute("sectorId");
        if (paymentId == null || payerId == null || sectorId == null) {
            request.setAttribute("error", "Parámetros de pago incompletos");
            request.getRequestDispatcher("/usuario/pago-cancelado.jsp").forward(request, response);
            return;
        }
        try {
            // Recupera el contexto PayPal desde el servlet de pago
            APIContext apiContext = (APIContext) getServletContext().getAttribute("apiContext");
            if (apiContext == null) {
                // Si no está en el contexto global, inicializa aquí (mejor compartirlo)
                String clientId = getServletContext().getInitParameter("paypal.client.id");
                String clientSecret = getServletContext().getInitParameter("paypal.client.secret");
                String mode = getServletContext().getInitParameter("paypal.mode");
                apiContext = new APIContext(clientId, clientSecret, mode);
            }
            Payment payment = new Payment();
            payment.setId(paymentId);
            PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(payerId);
            Payment executedPayment = payment.execute(apiContext, paymentExecution);
            if ("approved".equalsIgnoreCase(executedPayment.getState())) {
                // Registrar alquiler
                Object usuarioObj = session.getAttribute("usuario");
                Long usuarioId = null;
                try {
                    usuarioId = (Long) usuarioObj.getClass().getMethod("getId").invoke(usuarioObj);
                } catch (Exception e) {
                    LOGGER.error("No se pudo obtener el usuarioId de la sesión", e);
                }
                Long sectorIdLong = Long.valueOf(sectorId);
                AlquilerServicio alquilerServicio = new AlquilerServicio();
                boolean alquilerRegistrado = false;
                if (usuarioId != null) {
                    try {
                        alquilerServicio.registrarAlquiler(usuarioId, sectorIdLong);
                        alquilerRegistrado = true;
                    } catch (Exception e) {
                        LOGGER.error("No se pudo registrar el alquiler tras el pago", e);
                        request.setAttribute("error", e.getMessage());
                    }
                }
                if (alquilerRegistrado) {
                    // --- Limpieza de sesión tras el pago ---
                    session.removeAttribute("paymentId");
                    session.removeAttribute("sectorId");
                    session.removeAttribute("sectorName");
                    // --- Fin limpieza de sesión ---
                    request.getRequestDispatcher("/usuario/pago-completado.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "No se pudo registrar el alquiler tras el pago.");
                    request.getRequestDispatcher("/usuario/pago-cancelado.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("error", "El pago no fue aprobado por PayPal");
                request.getRequestDispatcher("/usuario/pago-cancelado.jsp").forward(request, response);
            }
        } catch (PayPalRESTException e) {
            LOGGER.error("Error al ejecutar el pago en PayPal", e);
            request.setAttribute("error", "Error al ejecutar el pago en PayPal: " + e.getMessage());
            request.getRequestDispatcher("/usuario/pago-cancelado.jsp").forward(request, response);
        }
    }
}
*/
