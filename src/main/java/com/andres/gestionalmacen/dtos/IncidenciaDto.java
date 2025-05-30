package com.andres.gestionalmacen.dtos;

import java.time.LocalDateTime;
import java.util.Date;
/**
 * Clase que representa una incidencia.
 * 
 * @author Andrés
 */
public class IncidenciaDto {
    private Date fechaCreacionDate;//Mostar fecha en jsp en dd/MM/yyyy HH:mm
    private Long id;
    private Long usuarioId;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private Estado estado;
    private String nombreUsuario;

    public enum Estado { pendiente, en_proceso, completado }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public Date getFechaCreacionDate() { return fechaCreacionDate; }
    public void setFechaCreacionDate(Date fechaCreacionDate) { this.fechaCreacionDate = fechaCreacionDate; }
}
