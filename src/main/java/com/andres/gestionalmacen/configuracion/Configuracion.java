package com.andres.gestionalmacen.configuracion;

import java.util.Properties;
/**
 * Clase que representa la configuración de la aplicación.
 * 
 * @author Andres
 */
public class Configuracion {
    private static final Properties propiedades = new Properties();
    private static final String entorno;
    static {
        entorno = System.getProperty("app.env", "local");
        try {
            if ("prod".equals(entorno)) {
                propiedades.load(Configuracion.class.getClassLoader().getResourceAsStream("config-prod.properties"));
            } else {
                propiedades.load(Configuracion.class.getClassLoader().getResourceAsStream("config.properties"));
            }
        } catch (Exception e) {
            throw new RuntimeException("No se pudo cargar el archivo de configuración de la aplicación", e);
        }
    }

    /**
     * Obtiene el valor de una propiedad.
     * @param llave la clave de la propiedad
     * @return el valor de la propiedad
     * 
     * @author Andrés
     
    public static String obtenerPropiedad(String llave) {
        return propiedades.getProperty(llave);
    }*/

    /**
     * Obtiene el valor de una propiedad.
     * @param llave la clave de la propiedad
     * @param valorPorDefecto el valor por defecto de la propiedad
     * @return el valor de la propiedad
     * 
     * @author Andrés
     */
    public static String obtenerPropiedad(String llave, String valorPorDefecto) {
        return propiedades.getProperty(llave, valorPorDefecto);
    }

    /**
     * Obtiene el entorno de la aplicación.
     * @return el entorno de la aplicación
     * 
     * @author Andrés
     */
    public static String obtenerEntorno() {
        return entorno;
    }
}
