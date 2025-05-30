package com.andres.gestionalmacen.dtos;

import java.time.LocalDateTime;
import java.util.Date;
/**
 * Clase que representa un usuario.
 * 
 * @author Andrés
 */
public class UsuarioDto {
	    private Long id;
	    private String nombreCompleto;
	    private String movil;
	    private String correoElectronico;
	    private Long rolId; 
	    private byte[] foto;
		private String fotoBase64;
	    private LocalDateTime fechaCreacion;
	    private Date fechaCreacionDate; //Mostar fecha en jsp en dd/MM/yyyy HH:mm
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

	    public byte[] getFoto() {
	        return foto;
	    }

	    public void setFoto(byte[] foto) {
	        this.foto = foto;
	    }

	    public String getFotoBase64() {
	        return fotoBase64;
	    }

	    public void setFotoBase64(String fotoBase64) {
	        this.fotoBase64 = fotoBase64;
	    }
	    public LocalDateTime getFechaCreacion() {
	        return fechaCreacion;
	    }

	    public void setFechaCreacion(LocalDateTime fechaCreacion) {
	        this.fechaCreacion = fechaCreacion;
	    }

	    public Date getFechaCreacionDate() {
	        return fechaCreacionDate;
	    }

	    public void setFechaCreacionDate(Date fechaCreacionDate) {
	        this.fechaCreacionDate = fechaCreacionDate;
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
