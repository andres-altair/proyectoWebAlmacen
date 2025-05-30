package com.andres.gestionalmacen.servicios;

import com.andres.gestionalmacen.dtos.ProductoDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.List;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ProductoServicio {
    private final String apiUrl;
    /** 
     * Constructor de la clase ProductoServicio.
     * 
     * @author andres
     */
    public ProductoServicio() {
        this.apiUrl = com.andres.gestionalmacen.configuracion.Configuracion.obtenerPropiedad(
            "api.producto.base.url", "http://localhost:8081/productos/todos"
        );
    }
    /**
     * Obtiene todos los productos.
     * @return Lista de productos
     * @throws Exception si ocurre un error al obtener los productos
     */
    public List<ProductoDto> obtenerTodos() throws Exception {
        HttpClient cliente = HttpClient.newHttpClient();
        HttpRequest solicitud = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl + "/todos"))
            .GET()
            .header("Accept", "application/json")
            .build();
    
        HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
        int estatus = respuesta.statusCode();
    
        ObjectMapper mapeador = new ObjectMapper();
        mapeador.registerModule(new JavaTimeModule());
        mapeador.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    
        if (estatus == 200) {
            return mapeador.readValue(respuesta.body(), new TypeReference<List<ProductoDto>>() {});
        } else {
            String errorJson = respuesta.body();
            try {
                com.fasterxml.jackson.databind.JsonNode jsonNode = mapeador.readTree(errorJson);
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