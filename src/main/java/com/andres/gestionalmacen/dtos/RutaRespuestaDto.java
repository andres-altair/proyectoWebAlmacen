package com.andres.gestionalmacen.dtos;
/**
 * Clase que representa una ruta.
 * 
 * @author Andrés
 */
public class RutaRespuestaDto {
    private String origen;
    private String destino;
    private String urlGoogleMaps;

    
    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }
    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }
    public String getUrlGoogleMaps() { return urlGoogleMaps; }
    public void setUrlGoogleMaps(String urlGoogleMaps) { this.urlGoogleMaps = urlGoogleMaps; }
}