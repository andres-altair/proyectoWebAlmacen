package com.andres.gestionalmacen.dtos;
/**
 * Clase que representa un pedido.
 * 
 * @author Andrés
 */
public class PedidoDto {
    private Long id;
    private Long usuarioId;
    private Long productoId;
    private Integer cantidad;
    private String estadoPedido;
    private Long operarioId;
    private Long transportistaId;
    private String fechaPedido;
    private String nombreProducto;
    private String operarioNombre;
    private String transportistaNombre;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public Long getProductoId() {
        return productoId;
    }
    
    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }
    
    public Integer getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
    
    public String getEstadoPedido() {
        return estadoPedido;
    }
    
    public void setEstadoPedido(String estadoPedido) {
        this.estadoPedido = estadoPedido;
    }
    
    public Long getOperarioId() {
        return operarioId;
    }
    
    public void setOperarioId(Long operarioId) {
        this.operarioId = operarioId;
    }
    
    public Long getTransportistaId() {
        return transportistaId;
    }
    
    public void setTransportistaId(Long transportistaId) {
        this.transportistaId = transportistaId;
    }
    
    public String getFechaPedido() {
        return fechaPedido;
    }
    
    public void setFechaPedido(String fechaPedido) {
        this.fechaPedido = fechaPedido;
    }
    
    public String getNombreProducto() {
        return nombreProducto;
    }
    
    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }
    
    public String getOperarioNombre() {
        return operarioNombre;
    }
    
    public void setOperarioNombre(String operarioNombre) {
        this.operarioNombre = operarioNombre;
    }
    
    public String getTransportistaNombre() {
        return transportistaNombre;
    }
    
    public void setTransportistaNombre(String transportistaNombre) {
        this.transportistaNombre = transportistaNombre;
    }   
}