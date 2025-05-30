package com.andres.gestionalmacen.servicios;

import com.andres.gestionalmacen.dtos.IncidenciaDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.List;
/**
 * Clase que representa el servicio de incidencias.
 * 
 * @author andres
 */
public class IncidenciaServicio {
    private final String apiUrl;
    private final ObjectMapper mapeador;
    /**
     * Constructor de la clase IncidenciaServicio.
     * 
     * @author andres
     */
    public IncidenciaServicio() {
        this.apiUrl = com.andres.gestionalmacen.configuracion.Configuracion.obtenerPropiedad(
            "api.incidencia.base.url", "http://localhost:8081/incidencias"
        );
        mapeador = new ObjectMapper();
        mapeador.registerModule(new JavaTimeModule());
    }
    /**
     * Obtiene todas las incidencias.
     * @author andres
     * 
     * @return List<IncidenciaDto> lista de incidencias
     * @throws Exception si ocurre un error al obtener las incidencias
     */
    public List<IncidenciaDto> obtenerTodas() throws Exception {
        HttpClient cliente = HttpClient.newHttpClient();
        HttpRequest solicitud = HttpRequest.newBuilder().uri(URI.create(apiUrl)).GET().header("Accept", "application/json").build();
        HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
        int estatus = respuesta.statusCode();
        if (estatus == 200) {
            String responseBody = respuesta.body();
            return mapeador.readValue(responseBody, new TypeReference<List<IncidenciaDto>>(){});
        } else {
            String errorJson = respuesta.body();
        try {
            com.fasterxml.jackson.databind.JsonNode node = mapeador.readTree(errorJson);
            if (node.has("error")) {
                throw new Exception(node.get("error").asText());
            } else {
                throw new Exception(errorJson);
            }
        } catch (Exception ex) {
            throw new Exception("Error backend (no JSON): " + errorJson);
        }
        }
    }
    /**
     * Obtiene todas las incidencias por usuario.
     * @author andres
     * 
     * @param usuarioId ID del usuario
     * @return List<IncidenciaDto> lista de incidencias
     * @throws Exception si ocurre un error al obtener las incidencias
     */
    public List<IncidenciaDto> obtenerPorUsuario(Long usuarioId) throws Exception {
        String url = apiUrl + "/usuario/" + usuarioId;
        HttpClient cliente = HttpClient.newHttpClient();
        HttpRequest solicitud = HttpRequest.newBuilder().uri(URI.create(url)).GET().header("Accept", "application/json").build();
        HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
        int estatus = respuesta.statusCode();
        if (estatus == 200) {
            String responseBody = respuesta.body();
            return mapeador.readValue(responseBody, new TypeReference<List<IncidenciaDto>>(){});
        } else {
            lanzarErrorDesdeRespuesta(respuesta.body());
            return java.util.Collections.emptyList(); // nunca se alcanza
        }
    }
    /**
     * Obtiene todas las incidencias pendientes.
     * @author andres
     * 
     * @return List<IncidenciaDto> lista de incidencias
     * @throws Exception si ocurre un error al obtener las incidencias
     */
    public List<IncidenciaDto> obtenerPendientes() {
        try {
            HttpClient cliente = HttpClient.newHttpClient();
            HttpRequest solicitud = HttpRequest.newBuilder().uri(URI.create(apiUrl + "/pendientes")).GET().header("Accept", "application/json").build();
            HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
            int estatus = respuesta.statusCode();
            if (estatus == 200) {
                List<IncidenciaDto> pendientes = mapeador.readValue(respuesta.body(), new TypeReference<List<IncidenciaDto>>(){});
                return pendientes;
            } else {
                lanzarErrorDesdeRespuesta(respuesta.body());
                return java.util.Collections.emptyList(); // nunca se alcanza
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener incidencias pendientes: " + e.getMessage(), e);
        }
    }
    /**
     * Obtiene todas las incidencias en proceso.
     * @author andres
     * 
     * @return List<IncidenciaDto> lista de incidencias
     * @throws Exception si ocurre un error al obtener las incidencias
     */
    public List<IncidenciaDto> obtenerEnProceso() {
        try {
            HttpClient cliente = HttpClient.newHttpClient();
            HttpRequest solicitud = HttpRequest.newBuilder().uri(URI.create(apiUrl + "/en-proceso")).GET().header("Accept", "application/json").build();
            HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
            int estatus = respuesta.statusCode();
            if (estatus == 200) {
                List<IncidenciaDto> enProceso = mapeador.readValue(respuesta.body(), new TypeReference<List<IncidenciaDto>>(){});
                return enProceso;
            } else {
                lanzarErrorDesdeRespuesta(respuesta.body());
                return java.util.Collections.emptyList(); // nunca se alcanza
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener incidencias en proceso: " + e.getMessage(), e);
        }
    }
    /**
     * Obtiene todas las incidencias completadas.
     * @author andres
     * 
     * @return List<IncidenciaDto> lista de incidencias
     * @throws Exception si ocurre un error al obtener las incidencias
     */
    public List<IncidenciaDto> obtenerCompletadas() {
        try {
            HttpClient cliente = HttpClient.newHttpClient();
            HttpRequest solicitud = HttpRequest.newBuilder().uri(URI.create(apiUrl + "/completadas")).GET().header("Accept", "application/json").build();
            HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
            int estatus = respuesta.statusCode();
            if (estatus == 200) {
                List<IncidenciaDto> completadas = mapeador.readValue(respuesta.body(), new TypeReference<List<IncidenciaDto>>(){});
                return completadas;
            } else {
                lanzarErrorDesdeRespuesta(respuesta.body());
                return java.util.Collections.emptyList();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener incidencias completadas: " + e.getMessage(), e);
        }
    }
    /**
     * Crea una nueva incidencia.
     * @author andres
     * 
     * @param dto incidencia a crear
     * @throws Exception si ocurre un error al crear la incidencia
     */
    public void crear(IncidenciaDto dto) {
        try {
            HttpClient cliente = HttpClient.newHttpClient();
            String json = mapeador.writeValueAsString(dto);
            HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
            HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
            int estatus = respuesta.statusCode();
            if (estatus != 200 && estatus != 201) {
                lanzarErrorDesdeRespuesta(respuesta.body());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al crear incidencia: " + e.getMessage(), e);
        }
    }
    /**
     * Cambia el estado de una incidencia.
     * @author andres
     * 
     * @param id ID de la incidencia
     * @param nuevoEstado nuevo estado de la incidencia
     * @throws Exception si ocurre un error al cambiar el estado de la incidencia
     */
    public void cambiarEstado(Long id, IncidenciaDto.Estado nuevoEstado) {
        try {   
            HttpClient cliente = HttpClient.newHttpClient();
            String json = "{\"estado\":\"" + nuevoEstado.name() + "\"}";
            HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/" + id + "/estado"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
            HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
            int estatus = respuesta.statusCode();
            if (estatus != 200) {
                lanzarErrorDesdeRespuesta(respuesta.body());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al cambiar estado de incidencia: " + e.getMessage(), e);
        }
    }

    /**
     * Extrae el mensaje de error del backend y lanza una excepción con el mensaje adecuado.
     * @param responseBody respuesta JSON del backend
     * @throws Exception con el mensaje de error extraído
     */
    private void lanzarErrorDesdeRespuesta(String responseBody) throws Exception {
        try {
            com.fasterxml.jackson.databind.JsonNode node = mapeador.readTree(responseBody);
            if (node.has("error")) {
                throw new Exception(node.get("error").asText());
            } else {
                throw new Exception(responseBody);
            }
        } catch (Exception ex) {
            throw new Exception("Error backend (no JSON): " + responseBody);
        }
    }
}
