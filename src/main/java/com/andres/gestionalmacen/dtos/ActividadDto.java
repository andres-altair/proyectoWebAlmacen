package com.andres.gestionalmacen.dtos;

import java.time.LocalDateTime;
/**
 * Clase que representa una actividad.
 * 
 * @author Andrés
 */
public class ActividadDto {
    private Long id;
    private String descripcion;
    private Estado estado;
    private LocalDateTime fechaCreacion;
    private String fechaCreacionStr; // Fecha formateada para la vista
    private Long operarioId;
    private Long gerenteId;
    private String operarioNombre;

    public enum Estado { pendiente, en_proceso, completado }
    public String getOperarioNombre() { return operarioNombre; }
    public void setOperarioNombre(String operarioNombre) { this.operarioNombre = operarioNombre; }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getFechaCreacionStr() { return fechaCreacionStr; }
    public void setFechaCreacionStr(String fechaCreacionStr) { this.fechaCreacionStr = fechaCreacionStr; }

    public Long getOperarioId() { return operarioId; }
    public void setOperarioId(Long operarioId) { this.operarioId = operarioId; }
    public Long getGerenteId() { return gerenteId; }
    public void setGerenteId(Long gerenteId) { this.gerenteId = gerenteId; }
}
