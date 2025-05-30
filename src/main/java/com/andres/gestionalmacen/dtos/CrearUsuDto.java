package com.andres.gestionalmacen.dtos;
/**
 * Clase que representa un creacion de usuario.
 * 
 * @author Andrés
 */
public class CrearUsuDto {
	private Long id;
	private String nombreCompleto;
    private String movil;
    private String correoElectronico;
    private Long rolId;
    private String contrasena;
    private byte[] foto;
	private boolean correoConfirmado;
    private boolean google; 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

    public String getNombreCompleto() {
		return nombreCompleto;
	}
	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}
	public String getMovil() {
		return movil;
	}
	public void setMovil(String movil) {
		this.movil = movil;
	}
	public String getCorreoElectronico() {
		return correoElectronico;
	}
	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}
	public Long getRolId() {
		return rolId;
	}
	public void setRolId(Long rolId) {
		this.rolId = rolId;
	}
	public String getContrasena() {
		return contrasena;
	}
	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
	public byte[] getFoto() {
		return foto;
	}
	public void setFoto(byte[] foto) {
		this.foto = foto;
	}
	public boolean isCorreoConfirmado() {
		return correoConfirmado;
	}
	public void setCorreoConfirmado(boolean correoConfirmado) {
		this.correoConfirmado = correoConfirmado;
	}
	public boolean isGoogle() {
		return google;
	}
	public void setGoogle(boolean google) {
		this.google = google;
	}
	
}
