package com.andres.gestionalmacen.dtos;

import java.time.LocalDateTime;
/**
 * Clase que representa un sector alquilado.
 * 
 * @author Andrés
 */
public class SectoresAlquilerDto {
    private java.util.Date fechaInicioDate;// yyyy-MM-dd HH:mm en jsp
    private java.util.Date fechaFinDate;// yyyy-MM-dd HH:mm en jsp
    private Long id;
    private Long usuarioId;
    private Long sectorId; 
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Integer estado; 

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public Long getSectorId() { return sectorId; }
    public void setSectorId(Long sectorId) { this.sectorId = sectorId; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }

    public Integer getEstado() { return estado; }
    public void setEstado(Integer estado) { this.estado = estado; }

    public java.util.Date getFechaInicioDate() { return fechaInicioDate; }
    public void setFechaInicioDate(java.util.Date fechaInicioDate) { this.fechaInicioDate = fechaInicioDate; }
    public java.util.Date getFechaFinDate() { return fechaFinDate; }
    public void setFechaFinDate(java.util.Date fechaFinDate) { this.fechaFinDate = fechaFinDate; }
}