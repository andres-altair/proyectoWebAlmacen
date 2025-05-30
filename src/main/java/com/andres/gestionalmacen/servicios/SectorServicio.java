package com.andres.gestionalmacen.servicios;

import java.util.List;
import java.util.Properties;

import com.andres.gestionalmacen.dtos.SectorDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
/**
 * Clase que representa el servicio de sectores.
 *
 * @author Andres
 */
public class SectorServicio {
    private final String apiUrlSectoresLibres;
    private final String apiUrlSectores;
    /**
     * Constructor de la clase SectorServicio.
     *
     * @author Andres
     */
    public SectorServicio() {
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
        this.apiUrlSectoresLibres = propiedades.getProperty("api.sectores.libres.url", "http://localhost:8081/api/sectores/libre");
        this.apiUrlSectores = propiedades.getProperty("api.sectores.base.url", "http://localhost:8081/api/sectores");
    }
    /**
     * Obtiene los sectores libres.
     *
     * @author Andres
     *
     * @return List<SectorDto> lista de sectores libres
     * @throws Exception si ocurre un error al obtener los sectores libres
     */
    public List<SectorDto> obtenerSectoresLibres() throws Exception {
        HttpClient cliente = HttpClient.newHttpClient();
        ObjectMapper mapeador = new ObjectMapper();
        mapeador.registerModule(new JavaTimeModule());
        mapeador.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(apiUrlSectoresLibres))
                .GET()
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
        int estatus = respuesta.statusCode();
        if (estatus == 200) {
            return mapeador.readValue(respuesta.body(), new TypeReference<List<SectorDto>>() {});
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
     * Ocupa un sector.
     *
     * @author Andres
     *
     * @param sectorId ID del sector
     * @return boolean true si el sector se ocupó correctamente
     * @throws Exception si ocurre un error al ocupar el sector
     */
    public boolean ocuparSector(Long sectorId) throws Exception {
        HttpClient cliente = HttpClient.newHttpClient();
        ObjectMapper mapeador = new ObjectMapper();
        mapeador.registerModule(new JavaTimeModule());
        mapeador.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String json = "{\"id\": " + sectorId + ", \"estado\": \"ocupado\"}";
        HttpRequest solicitud = HttpRequest.newBuilder()
            .uri(URI.create(apiUrlSectores + "/" + sectorId))
            .PUT(HttpRequest.BodyPublishers.ofString(json))
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .build();

        HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
        int estatus = respuesta.statusCode();
        if (estatus >= 200 && estatus < 300) {
            return true;
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
}
