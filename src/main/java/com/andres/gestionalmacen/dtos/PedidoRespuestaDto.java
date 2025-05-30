package com.andres.gestionalmacen.dtos;

import java.time.LocalDateTime;
/**
 * Clase que representa un pedido.
 * 
 * @author Andrés
 */
public class PedidoRespuestaDto {
    private Long id;
    private Long usuarioId;
    private Long productoId;
    private Integer cantidad;
    private String estadoPedido;
    private Long operarioId;
    private Long transportistaId;
    private LocalDateTime fechaPedido;
    private String fechaPedidoStr;
    private String nombreProducto;
    

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public String getEstadoPedido() { return estadoPedido; }
    public void setEstadoPedido(String estadoPedido) { this.estadoPedido = estadoPedido; }

    public Long getOperarioId() { return operarioId; }
    public void setOperarioId(Long operarioId) { this.operarioId = operarioId; }

    public Long getTransportistaId() { return transportistaId; }
    public void setTransportistaId(Long transportistaId) { this.transportistaId = transportistaId; }

    public LocalDateTime getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDateTime fechaPedido) { this.fechaPedido = fechaPedido; }

    public String getFechaPedidoStr() { return fechaPedidoStr; }
    public void setFechaPedidoStr(String fechaPedidoStr) { this.fechaPedidoStr = fechaPedidoStr; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
}