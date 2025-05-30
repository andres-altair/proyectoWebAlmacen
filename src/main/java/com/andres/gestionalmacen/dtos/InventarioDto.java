package com.andres.gestionalmacen.dtos;

import java.time.LocalDateTime;
/**
 * Clase que representa un inventario.
 * 
 * @author Andrés
 */
public class InventarioDto {
    private Long id;
    private Long productoId;
    private Long gerenteId;
    private Integer cantidadContada;
    private LocalDateTime fechaRecuento;
    private String fechaRecuentoStr; // Fecha formateada para jsp en dd/MM/yyyy HH:mm
    private String observaciones;
    private String nombreProducto;
    private String nombreGerente;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public Long getGerenteId() { return gerenteId; }
    public void setGerenteId(Long gerenteId) { this.gerenteId = gerenteId; }

    public Integer getCantidadContada() { return cantidadContada; }
    public void setCantidadContada(Integer cantidadContada) { this.cantidadContada = cantidadContada; }

    public LocalDateTime getFechaRecuento() { return fechaRecuento; }
    public void setFechaRecuento(LocalDateTime fechaRecuento) { this.fechaRecuento = fechaRecuento; }

    public String getFechaRecuentoStr() { return fechaRecuentoStr; }
    public void setFechaRecuentoStr(String fechaRecuentoStr) { this.fechaRecuentoStr = fechaRecuentoStr; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public String getNombreGerente() { return nombreGerente; }
    public void setNombreGerente(String nombreGerente) { this.nombreGerente = nombreGerente; }
}
