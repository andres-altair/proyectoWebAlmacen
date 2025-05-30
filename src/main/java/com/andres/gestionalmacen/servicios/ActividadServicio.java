package com.andres.gestionalmacen.servicios;

import com.andres.gestionalmacen.dtos.ActividadDto;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
/** 
 * Clase que representa el servicio de actividades.
 * 
 * @author andres
 */
public class ActividadServicio {
    private final String baseUrl;
    private final ObjectMapper mapeador;
    /**
     * Constructor de la clase ActividadServicio.
     * 
     * @author andres
     */
    public ActividadServicio() {
        this.baseUrl = com.andres.gestionalmacen.configuracion.Configuracion.obtenerPropiedad(
            "api.actividad.base.url", "http://localhost:8081/api/actividades"
        );
        mapeador = new ObjectMapper();
        mapeador.registerModule(new JavaTimeModule());
        mapeador.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    /**
     * Obtiene todas las actividades creadas por un gerente, sin importar el estado.
     * @author andres
     * @param gerenteId ID del gerente
     * @return Lista de actividades
     * @throws Exception si ocurre un error al obtener las actividades
     */
    public List<ActividadDto> obtenerPorGerente(Long gerenteId) throws Exception {
        HttpClient cliente = HttpClient.newHttpClient();
        HttpRequest solicitud = HttpRequest.newBuilder().uri(URI.create(baseUrl + "/gerente/" + gerenteId)).GET().header("Accept", "application/json").build();
        HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
        int estatus = respuesta.statusCode();
        if (estatus == 200) {
            String respuestaCuerpo = respuesta.body();
            return mapeador.readValue(respuestaCuerpo, mapeador.getTypeFactory().constructCollectionType(List.class, ActividadDto.class));
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
    /**
     * Crea una nueva actividad.
     * @author andres
     * 
     * @param dto actividad a crear
     * @throws Exception si ocurre un error al crear la actividad
     */
    public void crear(ActividadDto dto) throws Exception {
        HttpClient cliente = HttpClient.newHttpClient();
        String json = mapeador.writeValueAsString(dto);
        HttpRequest solicitud = HttpRequest.newBuilder().uri(URI.create(baseUrl)).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
        int estatus = respuesta.statusCode();
        if (estatus == 200 || estatus == 201) {
            return;
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
    /**
     * Obtiene todas las actividades asignadas a un operario.
     * @author andres
     * 
     * @param operarioId ID del operario
     * @return Lista de actividades
     * @throws Exception si ocurre un error al obtener las actividades
     */
    public List<ActividadDto> obtenerParaOperario(Long operarioId) throws Exception {
        List<ActividadDto> resultado = new ArrayList<>();
        HttpClient cliente = HttpClient.newHttpClient();
        String url = baseUrl + "/operario/" + operarioId;
        HttpRequest solicitud = HttpRequest.newBuilder().uri(URI.create(url)).header("Accept", "application/json").GET().build();
        HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
        int estatus = respuesta.statusCode();
        if (estatus == 200) {
            List<ActividadDto> propias = mapeador.readValue(
                    respuesta.body(),
                    mapeador.getTypeFactory().constructCollectionType(List.class, ActividadDto.class)
            );
            // Aquí puedes evitar duplicados si lo necesitas
            resultado.addAll(propias);
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
        return resultado;
    }
    /**
     * Asigna un operario a una actividad y cambia su estado.
     * @author andres
     * 
     * @param actividadId ID de la actividad
     * @param operarioId ID del operario
     * @param nuevoEstado nuevo estado de la actividad
     * @throws Exception si ocurre un error al asignar el operario
     */
    public void asignarOperarioYCambiarEstado(Long actividadId, Long operarioId, String nuevoEstado) throws Exception {
        HttpClient cliente = HttpClient.newHttpClient();
        String url = baseUrl + "/" + actividadId;
        ActividadDto dto = new ActividadDto();
        dto.setId(actividadId);
        dto.setOperarioId(operarioId);
        dto.setEstado(ActividadDto.Estado.valueOf(nuevoEstado));
        String json = mapeador.writeValueAsString(dto);
    
        HttpRequest solicitud = HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/json").PUT(HttpRequest.BodyPublishers.ofString(json)).build();
    
        HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
        int estatus = respuesta.statusCode();
        if (estatus == 200 || estatus == 201) {
            return;
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






/** 

    // Obtener actividades por estado
    public List<ActividadDto> obtenerPorEstado(String estado) {
        try {
            URI uri = URI.create(baseUrl + "/estado/" + estado);
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                return mapeador.readValue(is, mapeador.getTypeFactory().constructCollectionType(List.class, ActividadDto.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    

    // Obtener todas las actividades de un gerente, independientemente del estado
    public List<ActividadDto> obtenerTodasPorGerente(Long gerenteId) {
        try {
            URI uri = URI.create(baseUrl + "/todas-gerente/" + gerenteId);
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                return mapeador.readValue(is, mapeador.getTypeFactory().constructCollectionType(List.class, ActividadDto.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
*/
    
}
