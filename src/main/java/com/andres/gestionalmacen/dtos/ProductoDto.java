package com.andres.gestionalmacen.dtos;

import java.time.LocalDateTime;
/**
 * Clase que representa un producto.
 * 
 * @author Andrés
 */
public class ProductoDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer cantidad;
    private LocalDateTime fechaCreacion;
    private String fechaCreacionStr; 

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getFechaCreacionStr() { return fechaCreacionStr; }
    public void setFechaCreacionStr(String fechaCreacionStr) { this.fechaCreacionStr = fechaCreacionStr; }
}