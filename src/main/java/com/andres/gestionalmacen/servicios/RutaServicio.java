package com.andres.gestionalmacen.servicios;

import com.andres.gestionalmacen.dtos.RutaRespuestaDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.net.URI;
import java.io.IOException;
/**
 * Clase que representa el servicio de rutas.
 *
 * @author Andres
 */
public class RutaServicio {

    private final String apiUrl;
    /**
     * Constructor de la clase RutaServicio.
     *
     * @author Andres
     */
    public RutaServicio() {
        // Carga centralizada de configuración usando AppConfig
        this.apiUrl = com.andres.gestionalmacen.configuracion.Configuracion.obtenerPropiedad(
            "api.ruta.base.url", "http://localhost:8081/api/rutas/activas?transportistaId="
        );
    }
    /**
     * Obtiene las rutas activas por transportista.
     *
     * @author Andres
     *
     * @param transportistaId ID del transportista
     * @return List<RutaRespuestaDto> lista de rutas
     * @throws IOException si ocurre un error al obtener las rutas
     */
    public List<RutaRespuestaDto> obtenerRutasActivasPorTransportista(Long transportistaId) throws IOException {
        HttpClient cliente = HttpClient.newHttpClient();
        String uri = apiUrl + transportistaId;
        HttpRequest solicitud = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .GET()
            .header("Accept", "application/json")
            .build();
        try {
            HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
            int estatus = respuesta.statusCode();
            if (estatus == 200) {
                ObjectMapper mapeador = new ObjectMapper();
                return mapeador.readValue(respuesta.body(),
                    mapeador.getTypeFactory().constructCollectionType(List.class, RutaRespuestaDto.class));
            } else {
                String errorJson = respuesta.body();
                try {
                    ObjectMapper mapeador = new ObjectMapper();
                    JsonNode jsonNode = mapeador.readTree(errorJson);
                    if (jsonNode.has("error")) {
                    throw new IOException(jsonNode.get("error").asText());
                } else {
                    throw new IOException(errorJson);
                }
            } catch (Exception ex) {
                throw new IOException("Error backend (no JSON): " + errorJson);
            }
        }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
        }
    }
}