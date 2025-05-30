package com.andres.gestionalmacen.servlets.gerente;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import com.andres.gestionalmacen.dtos.UsuarioDto;
import com.andres.gestionalmacen.dtos.ProductoDto;
import com.andres.gestionalmacen.dtos.InventarioDto;
import com.andres.gestionalmacen.servicios.InventarioServicio;
import com.andres.gestionalmacen.servicios.ProductoServicio;
import com.andres.gestionalmacen.utilidades.GestorRegistros;

/**
 * Servlet que maneja las peticiones GET y POST para el inventario de gerente.
 * 
 * @author Andres
 */
@WebServlet("/gerente/inventario")
public class InventarioServlet extends HttpServlet {
    
    /**
     * Método que maneja la petición GET para el panel del gerente.
     * 
     * @param peticion objeto que contiene la petición HTTP
     * @param respuesta objeto que contiene la respuesta HTTP
     * @throws ServletException si ocurre un error en el servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta) 
            throws ServletException, IOException {
        HttpSession sesion = peticion.getSession(false);
        
        if (sesion == null || sesion.getAttribute("usuario") == null) {
            GestorRegistros.sistemaWarning("Intento de acceso al panel de gerente sin sesión válida desde IP: " 
                + peticion.getRemoteAddr());
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }

        UsuarioDto usuario = (UsuarioDto) sesion.getAttribute("usuario");
        
        if (usuario.getRolId() != 2) { // Verificar si es gerente
            GestorRegistros.warning(usuario.getId(), 
                "Intento no autorizado de acceso al panel de gerente. Rol actual: " + usuario.getRolId());
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }
        // Log de acceso exitoso al inventario del gerente
        GestorRegistros.info(usuario.getId(), "Acceso al inventario del gerente.");
        // Cargar lista de productos desde la API o servicio
        ProductoServicio productoServicio = new ProductoServicio();
        List<ProductoDto> productos;
        try {
            productos = productoServicio.obtenerTodos();
        } catch (Exception ex) {
            peticion.setAttribute("error", ex.getMessage());
            productos = java.util.Collections.emptyList();
        }
        // Cargar lista de recuentos de inventario
        InventarioServicio inventarioServicio = new InventarioServicio();
        List<InventarioDto> recuentos;
        try {
            recuentos = inventarioServicio.obtenerInventarioCompleto();
        } catch (Exception ex) {
            peticion.setAttribute("error", "Error al obtener recuentos: " + ex.getMessage());
            recuentos = java.util.Collections.emptyList();
        }

        // Formatear fechas a String para la vista
        java.time.format.DateTimeFormatter formatoFecha = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        for (ProductoDto prod : productos) {
            if (prod.getFechaCreacion() != null) {
                prod.setFechaCreacionStr(prod.getFechaCreacion().format(formatoFecha));
            }
        }
        for (InventarioDto rec : recuentos) {
            if (rec.getFechaRecuento() != null) {
                rec.setFechaRecuentoStr(rec.getFechaRecuento().format(formatoFecha));
            }
        }

        peticion.setAttribute("productos", productos);
        peticion.setAttribute("recuentos", recuentos);
        peticion.setAttribute("usuarioNombre", usuario.getNombreCompleto());
        peticion.getRequestDispatcher("/gerente/inventario.jsp").forward(peticion, respuesta);
    }
    /**
     * Maneja la petición POST para registrar un recuento de inventario.
     * 
     * @param peticion objeto que contiene la petición HTTP
     * @param respuesta objeto que contiene la respuesta HTTP
     * @throws ServletException si ocurre un error en el servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doPost(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
        HttpSession sesion = peticion.getSession(false);
        if (sesion == null || sesion.getAttribute("usuario") == null) {
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }
        UsuarioDto usuario = (UsuarioDto) sesion.getAttribute("usuario");
        if (usuario.getRolId() != 2) {
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }
        Long productoId = Long.valueOf(peticion.getParameter("producto_id"));
        Integer cantidadContada = Integer.valueOf(peticion.getParameter("cantidad_contada"));
        String observaciones = peticion.getParameter("observaciones");
        Long gerenteId = usuario.getId();
        // Construir el DTO y llamar al servicio
        InventarioDto dto = new InventarioDto();
        dto.setProductoId(productoId);
        dto.setGerenteId(gerenteId);
        dto.setCantidadContada(cantidadContada);
        dto.setObservaciones(observaciones);
        InventarioServicio inventarioServicio = new InventarioServicio();
        try {
            inventarioServicio.guardarRecuento(dto);
            GestorRegistros.info(gerenteId, "Recuento de inventario registrado. Producto ID: " + productoId + ", Cantidad: " + cantidadContada);
            respuesta.sendRedirect(peticion.getContextPath() + "/gerente/inventario?mensaje=Recuento+registrado+correctamente");
        } catch (Exception ex) {
            GestorRegistros.error(gerenteId, "Error al registrar recuento de inventario: " + ex.getMessage());
            respuesta.sendRedirect(peticion.getContextPath() + "/gerente/inventario?error=" + java.net.URLEncoder.encode("Error al registrar recuento", "UTF-8"));
        }
    }
}
