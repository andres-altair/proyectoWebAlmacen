package com.andres.gestionalmacen.servicios;

import com.andres.gestionalmacen.dtos.SectoresAlquilerDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.List;
import java.util.Properties;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
/** 
 * Clase que representa el servicio de alquileres.
 * 
 * @author andres
 */
public class AlquilerServicio {
    private final String apiUrlAlquileres;
    /** 
     * Constructor de la clase AlquilerServicio.
     * 
     * @author andres
     */
    public AlquilerServicio() {
        String entorno = System.getProperty("app.env", "local");
        Properties propiedades = new Properties();
        try {
            if ("prod".equals(entorno)) {
                propiedades.load(getClass().getClassLoader().getResourceAsStream("config-prod.properties"));
            } else {
                propiedades.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
            }
        } catch (Exception e) {
            throw new RuntimeException("No se pudo cargar el archivo de configuración", e);
        }
        this.apiUrlAlquileres = propiedades.getProperty("api.alquileres.base.url", "http://localhost:8081/api/sectores-alquiler");
    }
    /** 
     * Obtiene todos los alquileres de un usuario.
     * 
     * @author andres
     * 
     * @param usuarioId ID del usuario
     * @return Lista de alquileres
     * @throws Exception si ocurre un error al obtener los alquileres
     */
    public List<SectoresAlquilerDto> obtenerAlquileresUsuario(Long usuarioId) throws Exception {
        HttpClient cliente = HttpClient.newHttpClient();
        ObjectMapper mapeador = new ObjectMapper();
        mapeador.registerModule(new JavaTimeModule());
        mapeador.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    
        String url = apiUrlAlquileres + "/usuario/" + usuarioId;
        HttpRequest solicitud = HttpRequest.newBuilder().uri(URI.create(url)).GET().header("Accept", "application/json").build();
    
        HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
        int estatus = respuesta.statusCode();
        if (estatus == 200) {
            return mapeador.readValue(respuesta.body(), new TypeReference<List<SectoresAlquilerDto>>() {});
        } else {
            try {
                com.fasterxml.jackson.databind.JsonNode jsonNode = mapeador.readTree(respuesta.body());
                if (jsonNode.has("error")) {
                    throw new Exception(jsonNode.get("error").asText());
                } else {
                    throw new Exception(respuesta.body());
                }
            } catch (Exception ex) {
                throw new Exception("Error backend (no JSON): " + respuesta.body());
            }
        }
    }
    /** 
     * Registra un alquiler.
     * 
     * @author andres
     * 
     * @param usuarioId ID del usuario
     * @param sectorId ID del sector
     * @throws Exception si ocurre un error al registrar el alquiler
     */
    public void registrarAlquiler(Long usuarioId, Long sectorId) throws Exception {
        SectoresAlquilerDto dto = new SectoresAlquilerDto();
        dto.setUsuarioId(usuarioId);
        dto.setSectorId(sectorId);
        ObjectMapper mapeador = new ObjectMapper();
        mapeador.registerModule(new JavaTimeModule());
        mapeador.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String json = mapeador.writeValueAsString(dto);
    
        HttpClient cliente = HttpClient.newHttpClient();
        HttpRequest solicitud = HttpRequest.newBuilder().uri(URI.create(apiUrlAlquileres)).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();
    
        HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
        int estatus = respuesta.statusCode();
        String respuestaCuerpo = respuesta.body();
    
        if (estatus == 200 || estatus == 201) {
            return;
        } else {
            try {
                com.fasterxml.jackson.databind.JsonNode jsonNode = mapeador.readTree(respuestaCuerpo);
                if (jsonNode.has("error")) {
                    throw new Exception(jsonNode.get("error").asText());
                } else {
                    throw new Exception(respuestaCuerpo);
                }
            } catch (Exception ex) {
                throw new Exception("Error backend (no JSON): " + respuestaCuerpo);
            }
        }
    }
}

















































    
    

    

    
