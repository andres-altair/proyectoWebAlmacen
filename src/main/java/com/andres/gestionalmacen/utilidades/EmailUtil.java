package com.andres.gestionalmacen.utilidades;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.*;


import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Clase utilitaria para el manejo de correos electrónicos.
 * Esta clase proporciona métodos para generar tokens de confirmación,
 * validar tokens, y enviar correos de confirmación y recuperación.
 * 
 * <p>Funcionalidades principales:</p>
 * <ul>
 *   <li>Generación de tokens de confirmación</li>
 *   <li>Validación de tokens</li>
 *   <li>Envío de correos de confirmación</li>
 *   <li>Envío de correos de recuperación de contraseña</li>
 *   <li>Registro detallado de actividades</li>
 * </ul>
 * 
 * 
 * @author Andrés
 */
public class EmailUtil {
    private static final String DESDE_EMAIL = com.andres.gestionalmacen.configuracion.Configuracion.obtenerPropiedad("mail.from", "");
    private static final String CONTRASENA = com.andres.gestionalmacen.configuracion.Configuracion.obtenerPropiedad("mail.password", "");
    private static final long TOKEN_EXPIRACION_HORA = 1; // Token expira en 1 hora
    private static final Map<String, TokenInfo> tokenMapa = new HashMap<>();

    private static class TokenInfo {
        String email;
        Instant expiracion;
        
        TokenInfo(String email, Instant expirationTime) {
            this.email = email;
            this.expiracion = expirationTime;
        }
    }

    /**
     * Genera un token de confirmación para el correo proporcionado.
     * 
     * @param email El correo electrónico del usuario
     * @return El token generado
     */
    public static String generarToken(String email) {
        String token = Base64.getEncoder().encodeToString((email + ":" + System.currentTimeMillis()).getBytes());
        Instant expiracion = Instant.now().plus(TOKEN_EXPIRACION_HORA, ChronoUnit.HOURS);
        tokenMapa.put(token, new TokenInfo(email, expiracion));
        GestorRegistros.sistemaInfo("Token de confirmación generado para: " + email);
        return token;
    }

    /**
     * Valida un token de confirmación.
     * 
     * @param token El token a validar
     * @return true si el token es válido, false en caso contrario
     */
    public static boolean validarToken(String token) {
        GestorRegistros.sistemaInfo("[INFO] Tokens actualmente en memoria: " + tokenMapa.keySet());
        TokenInfo info = tokenMapa.get(token);
        if (info == null) {
            GestorRegistros.sistemaWarning("[WARNING] Token de confirmación inválido: " + token);
            return false;
        }
        if (Instant.now().isAfter(info.expiracion)) {
            tokenMapa.remove(token);
            GestorRegistros.sistemaWarning("[WARNING] Token de confirmación expirado: " + token);
            return false;
        }
        GestorRegistros.sistemaInfo("[INFO] Token de confirmación válido: " + token);
        return true;
    }

    /**
     * Obtiene el correo electrónico asociado a un token.
     * 
     * @param token El token del cual se desea obtener el correo
     * @return El correo electrónico asociado al token, o null si no existe
     */
    public static String obtenerCorreoDeToken(String token) {
        TokenInfo info = tokenMapa.get(token);
        return info != null ? info.email : null;
    }

    /**
     * Envía un correo de confirmación al usuario.
     * 
     * @param aEmail El correo electrónico del destinatario
     * @param token El token de confirmación
     */
    public static void enviarCorreoConfirmacion(String aEmail, String token) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session sesion = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(DESDE_EMAIL, CONTRASENA);
            }
        });

        try {
            Message mensaje = new MimeMessage(sesion);
            mensaje.setFrom(new InternetAddress(DESDE_EMAIL));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(aEmail));
            mensaje.setSubject("Confirma tu correo electrónico");

            String baseUrl = com.andres.gestionalmacen.configuracion.Configuracion.obtenerPropiedad("app.base.url", "http://localhost:8080");
            String enlace;
            if (baseUrl.contains("proyecto.andresxmd.eu")) {
                // Producción: sin /getion-almacen/
                enlace = baseUrl + "/confirmarCorreoNuevo?token=" + token;
            } else {
                // Local/desarrollo: con /getion-almacen/
                enlace = baseUrl + "/getion-almacen/confirmarCorreoNuevo?token=" + token;
            }
            String contenido = String.format(
                "Hola,<br/><br/>" +
                "Gracias por registrarte. Por favor, confirma tu correo electrónico haciendo clic en el siguiente enlace:<br/><br/>" +
                "<a href='%s'>Confirmar correo electrónico</a><br/><br/>" +
                "Este enlace expirará en %d horas.<br/><br/>" +
                "Si no has creado una cuenta, puedes ignorar este mensaje.<br/><br/>" +
                "Saludos,<br/>El equipo de Gestión de Almacén", 
                enlace, TOKEN_EXPIRACION_HORA);

            mensaje.setContent(contenido, "text/html; charset=utf-8");
            Transport.send(mensaje);
            
            GestorRegistros.sistemaInfo("Correo de confirmación enviado a: " + aEmail);
        } catch (MessagingException e) {
            GestorRegistros.sistemaError("Error al enviar correo de confirmación: " + e.getMessage());
            throw new RuntimeException("Error al enviar el correo de confirmación", e);
        }
    }

    /**
     * Reenvía el correo de confirmación al usuario.
     * 
     * @param email El correo electrónico del destinatario
     */
    public static void reenviarCorreoConfirmacion(String email) {
        // Invalidar token anterior si existe
        tokenMapa.entrySet().removeIf(entrada -> entrada.getValue().email.equals(email));
        
        // Generar nuevo token y enviar correo
        String nuevoToken = generarToken(email);
        enviarCorreoConfirmacion(email, nuevoToken);
    }

    /**
     * Envía un correo de recuperación de contraseña al usuario.
     * 
     * @param email El correo electrónico del destinatario
     * @param token El token de recuperación
     */
    public static void enviarCorreoRecuperacionContasena(String email, String token) {
        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.starttls.enable", "true");
        propiedades.put("mail.smtp.host", "smtp.gmail.com");
        propiedades.put("mail.smtp.port", "587");

        Session sesion = Session.getInstance(propiedades, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(DESDE_EMAIL, CONTRASENA);
            }
        });

        String baseUrl = com.andres.gestionalmacen.configuracion.Configuracion.obtenerPropiedad("app.base.url", "http://localhost:8080");
        String enlace;
        if (baseUrl.contains("proyecto.andresxmd.eu")) {
            // Producción: sin /getion-almacen/
            enlace = baseUrl + "/restablecerContrasena?token=" + token;
        } else {
            // Local/desarrollo: con /getion-almacen/
            enlace = baseUrl + "/getion-almacen/restablecerContrasena?token=" + token;
        }
        String contenido = String.format(
            "Hola,<br/><br/>" +
            "Has solicitado restablecer tu contraseña. Haz clic en el siguiente enlace:<br/><br/>" +
            "<a href='%s'>Restablecer contraseña</a><br/><br/>" +
            "Este enlace expirará en %d horas.<br/><br/>" +
            "Si no solicitaste esto, ignora este mensaje.<br/><br/>" +
            "Saludos,<br/>El equipo de Gestión de Almacén",
            enlace, TOKEN_EXPIRACION_HORA);

        try {
            Message mensage = new MimeMessage(sesion);
            mensage.setFrom(new InternetAddress(DESDE_EMAIL));
            mensage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            mensage.setSubject("Restablecer contraseña - Gestión Almacén");

            mensage.setContent(contenido, "text/html; charset=utf-8");

            Transport.send(mensage);
            GestorRegistros.sistemaInfo("Correo de recuperación de contraseña enviado a: " + email);
        } catch (MessagingException e) {
            GestorRegistros.sistemaError("Error al enviar correo de recuperación: " + e.getMessage());
            throw new RuntimeException("Error al enviar el correo de recuperación", e);
        }
    }
}