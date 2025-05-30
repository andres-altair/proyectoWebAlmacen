package com.andres.gestionalmacen.servicios;

import java.io.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import jakarta.servlet.http.HttpServletResponse;
/**
 * Clase que representa el servicio de respaldo de bases de datos.
 *
 * @author Andres
 */
public class RespaldoBBDDServicio {
    private final String apiBaseUrl;
    /**
     * Constructor de la clase RespaldoBBDDServicio.
     *
     * @author Andres
     */
    public RespaldoBBDDServicio() {
        this.apiBaseUrl = com.andres.gestionalmacen.configuracion.Configuracion.obtenerPropiedad(
            "api.respaldo.base.url", "http://localhost:8081"
        );
    }

    /**
     * Lanza el respaldo completo de ambas bases de datos a través de la API.
     *
     * @author Andres
     * @return Mensaje de éxito o el mensaje de error devuelto por el backend.
     * @throws IOException Si ocurre un error en la petición HTTP o si la petición es interrumpida.
     */
    public String respaldoCompletoAmbas() throws IOException {
        HttpClient cliente = HttpClient.newHttpClient();
        HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl + "/respaldo/completo/ambas"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
    
        try {
            HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
            int estatus = respuesta.statusCode();
            String cuerpo = respuesta.body();
            if (estatus == 200) {
                return "Respaldo de ambas bases generado correctamente";
            } else {
                // Si el backend devuelve un error detallado, lo mostramos
                if (cuerpo != null && !cuerpo.isBlank()) {
                    return cuerpo;
                } else {
                    return "Error al generar el respaldo de ambas bases";
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("La petición fue interrumpida", e);
        }
    }

    /**
     * Descarga el respaldo de una base de datos específica a través de la API.
     *
     * @author Andres
     * @param respuesta El objeto HttpServletResponse para enviar la respuesta al cliente.
     * @param nombreBBDD El nombre de la base de datos a respaldar.
     * @throws IOException Si ocurre un error en la petición HTTP o si la petición es interrumpida.
     */
    public void descargarExportacion(HttpServletResponse respuesta, String nombreBBDD) throws IOException {
        HttpClient cliente = HttpClient.newHttpClient();
        HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl + "/exportar/estructura?nombreBBDD=" + nombreBBDD))
                .GET()
                .build();
        try {
            HttpResponse<byte[]> respuestaHttp = cliente.send(solicitud, HttpResponse.BodyHandlers.ofByteArray());
            int estatus = respuestaHttp.statusCode();
            if (estatus == 200) {
                respuesta.setContentType("application/sql");
                respuesta.setHeader("Content-Disposition", "attachment; filename=estructura_" + nombreBBDD + ".sql");
                    try (OutputStream salida     = respuesta.getOutputStream()) {
                        salida.write(respuestaHttp.body());
                        salida.flush();
                    }
                } else {
                    String errorMsg = "Error al exportar estructura";
                    String cuerpo = new String(respuestaHttp.body(), java.nio.charset.StandardCharsets.UTF_8);
                    if (cuerpo != null && !cuerpo.isBlank()) {
                        // Extraer mensaje de error si viene en JSON
                        int idx = cuerpo.indexOf(":");
                        if (cuerpo.contains("\"error\"") && idx > 0) {
                            errorMsg = cuerpo.substring(idx + 2, cuerpo.length() - 2); // Simple extracción
                        } else {
                            errorMsg = cuerpo;
                        }
                    }
                    throw new IOException(errorMsg);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("La petición fue interrumpida", e);
            }
    }
    


    /**
     * Descarga el respaldo de una base de datos específica a través de la API.
     *
     * @author Andres
     * @param respuesta El objeto HttpServletResponse para enviar la respuesta al cliente.
     * @param nombreBBDD El nombre de la base de datos a respaldar.
     * @throws IOException Si ocurre un error en la petición HTTP o si la petición es interrumpida.
     */
    public void descargarExportacionDatos(HttpServletResponse respuesta, String nombreBBDD) throws IOException {
        HttpClient cliente = HttpClient.newHttpClient();
        HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl + "/exportar/datos?nombreBBDD=" + nombreBBDD))
                .GET()
                .build();
        try {
        HttpResponse<byte[]> respuestaHttp = cliente.send(solicitud, HttpResponse.BodyHandlers.ofByteArray());
        int estatus = respuestaHttp.statusCode();
        if (estatus == 200) {
            respuesta.setContentType("application/sql");
            respuesta.setHeader("Content-Disposition", "attachment; filename=datos_" + nombreBBDD + ".sql");
            try (OutputStream salida = respuesta.getOutputStream()) {
                salida.write(respuestaHttp.body());
                salida.flush();
            }
        } else {
            String errorMsg = "Error al exportar datos";
            String cuerpo = new String(respuestaHttp.body(), java.nio.charset.StandardCharsets.UTF_8);
            if (cuerpo != null && !cuerpo.isBlank()) {
                int idx = cuerpo.indexOf(":");
                if (cuerpo.contains("\"error\"") && idx > 0) {
                    errorMsg = cuerpo.substring(idx + 2, cuerpo.length() - 2); // Simple extracción
                } else {
                    errorMsg = cuerpo;
                }
            }
            throw new IOException(errorMsg);
        }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("La petición fue interrumpida", e);
        }
    }

}