package com.andres.gestionalmacen;

import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.ServletContext;
import com.andres.gestionalmacen.utilidades.GestorRegistros;
/**
 * Clase que inicializa la aplicación.
 * 
 * @author Andrés
 */
@WebListener
public class InicializadorApp implements ServletContextListener {
    /**
     * Método que se ejecuta cuando se inicia el contexto de la aplicación.
     * @param sce el evento del contexto de la aplicación
     * 
     * @author Andrés
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();

        // Detecta el entorno y ajusta allowed.origins automáticamente
        String entorno = System.getenv("APP_ENV"); // Ejemplo: "dev" o "prod"
        String origenPermitido = System.getenv("ALLOWED_ORIGINS"); // Opcional: valor explícito

        if ("prod".equalsIgnoreCase(entorno)) {
            // Si es producción, usa variable de entorno o dominio fijo
            if (origenPermitido != null && !origenPermitido.isEmpty()) {
                ctx.setInitParameter("allowed.origins", origenPermitido);
            } else {
                ctx.setInitParameter("allowed.origins", "https://proyecto.andresxmd.ue");
            }
        } else {
            // Si es desarrollo, deja el valor por defecto (localhost)
            ctx.setInitParameter("allowed.origins",
                ctx.getInitParameter("allowed.origins"));
        }

        try {
            // Inicializar el sistema de logs
            GestorRegistros.inicializar(ctx);
        } catch (Exception e) {
            GestorRegistros.sistemaError("Error al inicializar logs: " + e.getMessage());
        }
    }
    
    /**
     * Método que se ejecuta cuando se destruye el contexto de la aplicación.
     * @param sce el evento del contexto de la aplicación
     * 
     * @author Andrés
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        GestorRegistros.sistemaInfo("Deteniendo AppInitializer");
    }
}