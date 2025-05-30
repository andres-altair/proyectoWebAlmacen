package com.andres.gestionalmacen.servicios;

import com.andres.gestionalmacen.configuracion.Configuracion;
import com.andres.gestionalmacen.dtos.InventarioDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
/**
 * Clase que representa el servicio de inventario.
 * 
 * @author andres
 */
public class InventarioServicio {
    private final String apiUrl;
    /** 
     * Constructor de la clase InventarioServicio.
     * 
     * @author andres
     */
    public InventarioServicio() {
        this.apiUrl = Configuracion.obtenerPropiedad(
            "api.inventario.base.url", "http://localhost:8081/api/inventario"
        );
    }
    /**
     * Guarda un recuento de inventario en la base de datos.
     * @param dto objeto que contiene los datos del recuento
     * @throws Exception si ocurre un error al guardar el recuento
     */
    public void guardarRecuento(InventarioDto dto) throws Exception {
        HttpClient cliente = HttpClient.newHttpClient();
        ObjectMapper mapeador = new ObjectMapper();
    
        String json = mapeador.writeValueAsString(dto);
    
        HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/recuento"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();
    
        HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
        int estatus = respuesta.statusCode();
        if (estatus == 200 || estatus == 201) {
            return;
        } else {
            String errorJson = respuesta.body();
            try {
                JsonNode jsonNode = mapeador.readTree(errorJson);
                if (jsonNode.has("error")) {
                    throw new Exception(jsonNode.get("error").asText());
                } else {
                    throw new Exception(errorJson);
                }
            } catch (Exception ex) {
                throw new Exception("Error backend (no JSON): " + errorJson);
            }
        }
    }

    /**
     * Obtiene todos los recuentos de inventario.
     * @return lista de recuentos de inventario
     * @throws Exception si ocurre un error al obtener los recuentos
     */
    public List<InventarioDto> obtenerInventarioCompleto() throws Exception {
        HttpClient cliente = HttpClient.newHttpClient();
        ObjectMapper mapeador = new ObjectMapper();
        mapeador.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        mapeador.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        HttpRequest solicitud = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl + "/recuento"))
            .header("Accept", "application/json")
            .GET()
            .build();

        HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
        int estatus = respuesta.statusCode();
        if (estatus == 200) {
            String respuestaCuerpo = respuesta.body();
            return mapeador.readValue(respuestaCuerpo, new TypeReference<List<InventarioDto>>(){});
        } else {
            String errorJson = respuesta.body();
            try {
                JsonNode jsonNode = mapeador.readTree(errorJson);
                if (jsonNode.has("error")) {
                    throw new Exception(jsonNode.get("error").asText());
                } else {
                    throw new Exception(errorJson);
                }
            } catch (Exception ex) {
                throw new Exception("Error backend (no JSON): " + errorJson);
            }
        }
    }
}
