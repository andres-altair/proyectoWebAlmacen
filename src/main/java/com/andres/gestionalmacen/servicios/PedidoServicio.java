package com.andres.gestionalmacen.servicios;

import java.io.*;
import java.net.URI;
import java.util.List;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.andres.gestionalmacen.dtos.PedidoRespuestaDto;
import com.fasterxml.jackson.databind.JsonNode;
/**
 * Clase que representa el servicio de pedidos.
 * 
 * @author andres
 */
public class PedidoServicio {
    private final String apiBaseUrl;
    private final ObjectMapper mapeador = new ObjectMapper();
    /**
     * Constructor de la clase PedidoServicio.
     * 
     * @author andres
     */
    public PedidoServicio() {
        this.apiBaseUrl = com.andres.gestionalmacen.configuracion.Configuracion.obtenerPropiedad(
            "api.pedido.base.url", "http://localhost:8081/api/pedidos"
        );
    }
    /**
     * Obtiene todos los pedidos por estado.
     * 
     * @author andres
     * 
     * @param estado estado del pedido
     * @return String lista de pedidos
     * @throws IOException si ocurre un error al obtener los pedidos
     */
    public String listarPedidosPorEstado(String estado) throws IOException {
        HttpClient cliente = HttpClient.newHttpClient();
        String uri = apiBaseUrl + "?estado=" + estado;
        HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();
        try {
            HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
            int estatus = respuesta.statusCode();
            if (estatus == 200) {
                return respuesta.body();
            } else {
                String errorJson = respuesta.body();
                try {
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
    
    /**
     * Obtiene todos los pedidos por estado y tipo.
     * 
     * @author andres
     * 
     * @param estado estado del pedido
     * @param tipoIdParam tipo del pedido
     * @param id ID del pedido
     * @return String lista de pedidos
     * @throws IOException si ocurre un error al obtener los pedidos
     */
    public String listarPedidosPorEstadoYTipo(String estado, String tipoIdParam, Long id) throws IOException {
    HttpClient cliente = HttpClient.newHttpClient();
    String uri = apiBaseUrl + "?estado=" + estado + "&" + tipoIdParam + "=" + id;
    HttpRequest solicitud = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .GET()
            .header("Accept", "application/json")
            .build();
    try {
        HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
        int estatus = respuesta.statusCode();
        if (estatus == 200) {
            return respuesta.body();
        } else {
            String errorJson = respuesta.body();
            try {
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
   
    /**
     * Asigna un operario a un pedido.
     * 
     * @author andres
     * 
     * @param pedidoId ID del pedido
     * @param usuarioId ID del operario
     * @return String
     * @throws Exception si ocurre un error al asignar el operario
     */
    public String asignarOperario(Long pedidoId, Long usuarioId) throws Exception {
        HttpClient cliente = HttpClient.newHttpClient();
        // Construir el JSON del body
        String json = "{\"usuarioId\":" + usuarioId + "}";
        HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl + "/" + pedidoId + "/asignar-operario"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
            int estatus = respuesta.statusCode();
            if (estatus == 200 || estatus == 204) {
                return respuesta.body();
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
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
        }
    }
   
    /**
     * Marca un pedido como procesado.
     * 
     * @author andres
     * 
     * @param pedidoId ID del pedido
     * @param usuarioId ID del operario
     * @return String
     * @throws Exception si ocurre un error al marcar el pedido como procesado
     */
    public String marcarProcesado(Long pedidoId, Long usuarioId) throws Exception {
        HttpClient cliente = HttpClient.newHttpClient();
        String json = "{\"usuarioId\":" + usuarioId + "}";
        HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl + "/" + pedidoId + "/procesar"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
            int estatus = respuesta.statusCode();
            if (estatus == 200 || estatus == 204) {
                return respuesta.body();
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
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
        }
    }
    /**
     * Asigna un transportista a un pedido.
     * 
     * @author andres
     * 
     * @param pedidoId ID del pedido
     * @param usuarioId ID del transportista
     * @return String
     * @throws Exception si ocurre un error al asignar el transportista
     */
    public String asignarTransportista(Long pedidoId, Long usuarioId) throws Exception {
        HttpClient cliente = HttpClient.newHttpClient();
        String json = "{\"usuarioId\":" + usuarioId + "}";
        HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl + "/" + pedidoId + "/asignar-transportista"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
            int estatus = respuesta.statusCode();
            if (estatus == 200 || estatus == 204) {
                return respuesta.body();
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
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
        }
    }
    /**
     * Marca un pedido como entregado.
     * 
     * @author andres
     * 
     * @param pedidoId ID del pedido
     * @param usuarioId ID del operario
     * @return String
     * @throws Exception si ocurre un error al marcar el pedido como entregado
     */
    public String marcarEntregado(Long pedidoId, Long usuarioId) throws Exception {
        HttpClient cliente = HttpClient.newHttpClient();
        String json = "{\"usuarioId\":" + usuarioId + "}";
        HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl + "/" + pedidoId + "/entregar"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
            int estatus = respuesta.statusCode();
            if (estatus == 200 || estatus == 204) {
                return respuesta.body();
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
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
        }
    }
    /**
     * Obtiene todos los pedidos por usuario.
     * 
     * @author andres
     * 
     * @param usuarioId ID del usuario
     * @return List<PedidoRespuestaDto> lista de pedidos
     * @throws Exception si ocurre un error al obtener los pedidos
     */
    public List<PedidoRespuestaDto> listarPedidosPorUsuario(Long usuarioId) throws Exception {
        HttpClient cliente = HttpClient.newHttpClient();
        ObjectMapper mapeador = new ObjectMapper();
        mapeador.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        mapeador.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        HttpRequest solicitud = HttpRequest.newBuilder()
            .uri(URI.create(apiBaseUrl + "?usuarioId=" + usuarioId))
            .GET()
            .header("Accept", "application/json")
            .build();

        HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
        int estatus = respuesta.statusCode();
        if (estatus == 200) {
            return mapeador.readValue(respuesta.body(), new com.fasterxml.jackson.core.type.TypeReference<List<PedidoRespuestaDto>>() {});
        } else {
            // Manejo robusto de error backend
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

    public String crearPedidoFicticio() throws IOException {
        URL url = URI.create(apiBaseUrl + "/ficticio").toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        return leerRespuesta(conn);
    }

    

    private String enviarPutConUsuarioId(URL url, Long usuarioId) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        String json = "{\"usuarioId\":" + usuarioId + "}";
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }
        return leerRespuesta(conn);
    }

    private String leerRespuesta(HttpURLConnection conn) throws IOException {
        int status = conn.getResponseCode();
        try (InputStream entrada = (status < 400) ? conn.getInputStream() : conn.getErrorStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(entrada, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) sb.append(linea);
            return sb.toString();
        }
    }
    */
}