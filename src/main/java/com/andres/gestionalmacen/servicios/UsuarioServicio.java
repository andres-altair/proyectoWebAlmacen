package com.andres.gestionalmacen.servicios;

import com.andres.gestionalmacen.dtos.CrearUsuDto;
import com.andres.gestionalmacen.dtos.UsuarioDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.andres.gestionalmacen.dtos.PaginacionDto;
import com.fasterxml.jackson.databind.JavaType;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Clase que representa el servicio de usuarios.
 * 
 * @author andres
 */
public class UsuarioServicio {
    private final String apiBaseUrl;
    private final ObjectMapper objetoMapeador;

    /**
     * Constructor de la clase UsuarioServicio.
     * Este constructor inicializa el objeto ObjectMapper con configuraciones específicas
     * para manejar la serialización y deserialización de objetos JSON.
     */
    public UsuarioServicio() {
        // Carga centralizada de configuración usando AppConfig
        this.apiBaseUrl = com.andres.gestionalmacen.configuracion.Configuracion.obtenerPropiedad(
            "api.base.url", "http://localhost:8081/api/usuarios"
        );

        this.objetoMapeador = new ObjectMapper()
            .registerModule(new JavaTimeModule())//manejar fechas:localdatetime,date...
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)//fecha legible no en ms
            .configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)//Permite nombres de campo no entrecomillados en el JSON
            .configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)//Permite el uso de comillas simples:campo y valores
            .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//para que no falle si contiene campos que no están presentes en la clase Java
    }

    /**
     * Busca un usuario por su correo electrónico enviando una petición a la API.
     * 
     * @param correoElectronico El correo electrónico del usuario a buscar
     * @return El objeto UsuarioDto correspondiente al correo electrónico
     * @throws Exception Si ocurre un error durante la búsqueda del usuario
     */
    public UsuarioDto buscarPorCorreo(String correoElectronico) throws Exception {
        HttpClient cliente = HttpClient.newHttpClient();
        HttpRequest solicitud = HttpRequest.newBuilder()
            .uri(URI.create(apiBaseUrl + "/correo/" + correoElectronico))
            .header("Accept", "application/json")
            .GET()
            .build();
    
        HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
    
        if (respuesta.statusCode() == 200) {
            // Usuario encontrado
            return objetoMapeador.readValue(respuesta.body(), UsuarioDto.class);
        } else {
            // Error: extraer mensaje del campo "error"
            String mensaje = respuesta.body();
            String errorMsg = mensaje;
            try {
                JsonNode jsonNode = objetoMapeador.readTree(mensaje);
                if (jsonNode.has("error")) errorMsg = jsonNode.get("error").asText();
            } catch (Exception ignore) {}
            throw new Exception(errorMsg);
        }
    }   
     /**
     * Busca un usuario por su correo electrónico enviando una petición a la API.
     * 
     * @param correoElectronico El correo electrónico del usuario a buscar
     * @return El objeto UsuarioDto correspondiente al correo electrónico
     * @throws Exception Si ocurre un error durante la búsqueda del usuario
     */
    public UsuarioDto buscarPorCorreoRegistro(String correoElectronico) {
        try {
            HttpClient cliente = HttpClient.newHttpClient();
            HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl + "/correo/" + correoElectronico))
                .header("Accept", "application/json")
                .GET()
                .build();

            HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());

            if (respuesta.statusCode() == 200) {
                // Usuario encontrado
                return objetoMapeador.readValue(respuesta.body(), UsuarioDto.class);
            } else if (respuesta.statusCode() == 404) {
                // Usuario no encontrado, retorna null
                return null;
            } else {
                // Otro error, loguea y retorna null o lanza excepción
                String mensaje = respuesta.body();
                String errorMsg = mensaje;
                try {
                    JsonNode jsonNode = objetoMapeador.readTree(mensaje);
                    if (jsonNode.has("error")) errorMsg = jsonNode.get("error").asText();
                } catch (Exception ignore) {}
                return null;
            }
        } catch (Exception e) { 
            return null;
        }
    }
  
    
    /**
     * Valida las credenciales del usuario enviando una petición a la API.
     * 
     * @param correoElectronico El correo electrónico del usuario
     * @param contrasenaEncriptada La contraseña encriptada del usuario
     * @return Un objeto UsuarioDto con la información del usuario autenticado
     * @throws Exception Si ocurre un error durante la autenticación
     */
    public UsuarioDto validarCredenciales(String correoElectronico, String contrasenaEncriptada) throws Exception {
    HttpClient cliente = HttpClient.newHttpClient();
    Map<String, String> credencialesMap = new HashMap<>();
    credencialesMap.put("correoElectronico", correoElectronico);
    credencialesMap.put("contrasena", contrasenaEncriptada);

    String cuerpoJson = objetoMapeador.writeValueAsString(credencialesMap);

    HttpRequest solicitud = HttpRequest.newBuilder()
        .uri(URI.create(apiBaseUrl + "/autenticar"))
        .header("Content-Type", "application/json")
        .POST(BodyPublishers.ofString(cuerpoJson))
        .build();

    HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());

    if (respuesta.statusCode() == 200) {
        // Autenticación exitosa
        return objetoMapeador.readValue(respuesta.body(), UsuarioDto.class);
    } else {
        // Error: extraer mensaje del campo "error"
        String mensaje = respuesta.body();
        String errorMsg = mensaje;
        try {
            JsonNode jsonNode = objetoMapeador.readTree(mensaje);
            if (jsonNode.has("error")) errorMsg = jsonNode.get("error").asText();
        } catch (Exception ignore) {}
        throw new Exception(errorMsg);
    }
}

   /**
     * Crea un nuevo usuario enviando una petición a la API.
     * 
     * @param usuarioDTO El objeto CrearUsuDto con la información del nuevo usuario
     * @return El objeto UsuarioDto creado
     * @throws Exception Si ocurre un error durante la creación del usuario
     */
    public CrearUsuDto crearUsuario(CrearUsuDto usuarioDTO) throws Exception {
        try {
            String usuarioJson = objetoMapeador.writeValueAsString(usuarioDTO);
    
            HttpClient cliente = HttpClient.newHttpClient();
            HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(usuarioJson))
                .build();
    
            HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
            int estatus = respuesta.statusCode();
            String respuestaStr = respuesta.body();
    
            if (estatus == 201) {
                return objetoMapeador.readValue(respuestaStr, CrearUsuDto.class);
            } else {
                // Extraer mensaje de error del JSON
                String errorMsg = respuestaStr;
                try {
                    JsonNode jsonNode = objetoMapeador.readTree(respuestaStr);
                    if (jsonNode.has("error")) errorMsg = jsonNode.get("error").asText();
                } catch (Exception ignore) {}
                throw new RuntimeException(errorMsg);
            }
        } catch (Exception e) {
            throw new Exception("Error al crear usuario: ");
        }
    }

    /**
     * Confirma el correo electrónico del usuario enviando una petición a la API.
     * 
     * @param email El correo electrónico del usuario a confirmar
     */
    public void confirmarCorreo(String email) {
        try {
            HttpClient cliente = HttpClient.newHttpClient();
            HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl + "/confirmarCorreo/" + email))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
    
            HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
            int estatus = respuesta.statusCode();
            String respuestaStr = respuesta.body();
    
            if (estatus != 200) {
                String errorMsg = "Error al confirmar correo: " + estatus;
                try {
                    ObjectMapper mapeador = new ObjectMapper();
                    JsonNode jsonNode = mapeador.readTree(respuestaStr);
                    if (jsonNode.has("error")) errorMsg = jsonNode.get("error").asText();
                } catch (Exception ignore) {}
                throw new RuntimeException(errorMsg);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al confirmar correo");
        }
    }

        /**
     * Elimina un usuario existente enviando una petición a la API.
     * 
     * @param id El ID del usuario a eliminar
     * @throws Exception Si ocurre un error durante la eliminación del usuario
     */
    public void eliminarUsuario(Long id) throws Exception {
        try {
            HttpClient cliente = HttpClient.newHttpClient();
            HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl + "/" + id))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

            HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
            int estatus = respuesta.statusCode();
            String respuestaStr = respuesta.body();

            if (estatus != 200 && estatus != 204) {
                // Intenta extraer el mensaje de error del JSON
                String errorMsg = respuestaStr;
                try {
                    ObjectMapper mapeador = new ObjectMapper();
                    JsonNode jsonNode = mapeador.readTree(respuestaStr);
                    if (jsonNode.has("error")) errorMsg = jsonNode.get("error").asText();
                } catch (Exception ignore) {}
                throw new RuntimeException(errorMsg);
            }
        } catch (Exception e) {
            throw new Exception("Error al eliminar usuario: ");
        }
    }
    /**
     * Obtiene la lista de usuarios desde la API.
     * 
     * @param pagina El número de página
     * @param tamanio El tamaño de la página
     * @return Una lista de objetos UsuarioDto
     * @throws Exception Si ocurre un error al obtener los usuarios
     */
    public PaginacionDto<UsuarioDto> obtenerUsuariosPaginados(int pagina, int tamanio) throws Exception {
        try {
            String url = apiBaseUrl + "?pagina=" + pagina + "&tamanio=" + tamanio;
            HttpClient cliente = HttpClient.newHttpClient();
            HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json;charset=UTF-8")
                .GET()
                .build();

            HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
            int estatus = respuesta.statusCode();
            String respuestaStr = respuesta.body();
            if (estatus != 200) {
                String errorMsg = "Error HTTP: " + estatus;
                // Intentar extraer mensaje de error del JSON
            try {
                ObjectMapper mapeador = new ObjectMapper();
                JsonNode jsonNode = mapeador.readTree(respuestaStr);
                if (jsonNode.has("error")) errorMsg = jsonNode.get("error").asText();
            } catch (Exception ignore) {}
            throw new RuntimeException(errorMsg);
            }
            ObjectMapper mapeador = new ObjectMapper();
            mapeador.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
            JavaType tipo = mapeador.getTypeFactory().constructParametricType(PaginacionDto.class, UsuarioDto.class);
            PaginacionDto<UsuarioDto> paginacion = mapeador.readValue(respuestaStr, tipo);
            System.out.println("[DEBUG] DTO deserializado, numero: " + paginacion.getNumero());
            return paginacion;
        } catch (Exception e) {
            throw new Exception("Error al obtener usuarios: ");
        }
    }
    

    /**
     * Actualiza un usuario existente enviando una petición a la API.
     * 
     * @param id El ID del usuario a actualizar
     * @param usuario El objeto CrearUsuDto con la información actualizada del usuario
     * @return El objeto CrearUsuDto actualizado
     * @throws Exception Si ocurre un error durante la actualización del usuario
     */
    public CrearUsuDto actualizarUsuario(Long id, CrearUsuDto usuario) throws Exception {
        try {
            String jsonCuerpo = objetoMapeador.writeValueAsString(usuario);
    
            HttpClient cliente = HttpClient.newHttpClient();
            HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonCuerpo))
                .build();
    
            HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
            int estatus = respuesta.statusCode();
            String respuestaStr = respuesta.body();
    
            if (estatus == 200) {
                return objetoMapeador.readValue(respuestaStr, CrearUsuDto.class);
            } else {
                String errorMsg = "Error al actualizar usuario: " + estatus;
                // Intentar extraer mensaje de error del JSON
                try {
                    ObjectMapper mapeador = new ObjectMapper();
                    JsonNode jsonNode = mapeador.readTree(respuestaStr);
                    if (jsonNode.has("error")) errorMsg = jsonNode.get("error").asText();
                } catch (Exception ignore) {}
                throw new RuntimeException(errorMsg);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }
    /**
     * Actualiza la contraseña de un usuario existente enviando una petición a la API.
     * 
     * @param datos El mapa con la información de la contraseña a actualizar
     * @throws Exception Si ocurre un error durante la actualización de la contraseña
     */
    public void actualizarContrasena(Map<String, String> datos) throws Exception {
        try {
            String jsonDatos = objetoMapeador.writeValueAsString(datos);

            HttpClient cliente = HttpClient.newHttpClient();
            HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl + "/actualizarContrasena"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonDatos))
                .build();

            HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
            int estatus = respuesta.statusCode();
            String respuestaStr = respuesta.body();

            if (estatus != 200) {
                // Intenta extraer el mensaje de error del JSON
                String errorMsg = respuestaStr;
                try {
                    ObjectMapper mapeador = new ObjectMapper();
                    JsonNode jsonNode = mapeador.readTree(respuestaStr);
                    if (jsonNode.has("error")) errorMsg = jsonNode.get("error").asText();
                } catch (Exception ignore) {}
                throw new Exception(errorMsg);
            }

        } catch (Exception e) {
            throw new Exception("Error al actualizar contraseña: " + e.getMessage());
        }
    }
    /**
     * Actualiza el perfil del usuario (campos editables: nombreCompleto, movil, foto) enviando una petición a la API.
     * @param id El ID del usuario a actualizar
     * @param usuario El objeto UsuarioDto con los datos editados
     * @throws Exception Si ocurre un error durante la actualización
     */
    public void actualizarPerfil(Long id, UsuarioDto usuario) throws Exception {
    try {
        // Construir un mapa solo con los campos editados (no nulos)
        Map<String, Object> datosActualizados = new HashMap<>();
        if (usuario.getNombreCompleto() != null && !usuario.getNombreCompleto().isBlank()) {
            datosActualizados.put("nombreCompleto", usuario.getNombreCompleto());
        }
        if (usuario.getMovil() != null && !usuario.getMovil().isBlank()) {
            datosActualizados.put("movil", usuario.getMovil());
        }
        if (usuario.getFoto() != null && usuario.getFoto().length > 0) {
            datosActualizados.put("foto", usuario.getFoto());
        }
        if (datosActualizados.isEmpty()) {
            return;
        }
        String jsonCuerpo = objetoMapeador.writeValueAsString(datosActualizados);

        HttpClient cliente = HttpClient.newHttpClient();
        HttpRequest solicitud = HttpRequest.newBuilder()
            .uri(URI.create(apiBaseUrl + "/" + id + "/perfil"))
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(jsonCuerpo))
            .build();

        HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
        int estatus = respuesta.statusCode();
        String respuestaStr = respuesta.body();

        if (estatus != 200) {
            String errorMsg = "Error al actualizar perfil: " + estatus;
            // Intentar extraer mensaje de error del JSON
            try {
                ObjectMapper mapeador = new ObjectMapper();
                JsonNode jsonNode = mapeador.readTree(respuestaStr);
                if (jsonNode.has("error")) errorMsg = jsonNode.get("error").asText();
            } catch (Exception ignore) {}
            throw new RuntimeException(errorMsg);
        }
    } catch (Exception e) {
        throw new Exception(e.getMessage(), e);
    }
}
    /**
     * Obtiene un usuario por su ID enviando una petición a la API.
     * 
     * @param id El ID del usuario a obtener
     * @return El objeto UsuarioDto correspondiente al ID
     * @throws Exception Si ocurre un error durante la obtención del usuario
     
    public UsuarioDto obtenerUsuarioPorId(Long id) throws Exception {
        try {
            URL url = URI.create(apiBaseUrl + "/" + id).toURL();
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");
            conexion.setRequestProperty("Accept", "application/json");
            
            int responseCode = conexion.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("Error al obtener usuario: " + responseCode);
            }
            
            BufferedReader br = new BufferedReader(new InputStreamReader(conexion.getInputStream(), "UTF-8"));
            StringBuilder respuesta = new StringBuilder();
            String salida;
            while ((salida = br.readLine()) != null) {
                respuesta.append(salida);
            }
            
            return objetoMapeador.readValue(respuesta.toString(), UsuarioDto.class);
        } catch (Exception e) {
            throw new Exception("Error al obtener usuario: " + e.getMessage(), e);
        }
    }*/

}
