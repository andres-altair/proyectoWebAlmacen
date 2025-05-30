package com.andres.gestionalmacen.servlets.usuario;

import com.andres.gestionalmacen.dtos.SectorDto;
import com.andres.gestionalmacen.dtos.UsuarioDto;
import com.andres.gestionalmacen.servicios.SectorServicio;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import com.andres.gestionalmacen.utilidades.GestorRegistros;


/**
 * Servlet para manejar el pago de un sector seleccionado.
 * @author andres
 */
@WebServlet("/usuario/pagoSector")
public class PagoSectorServlet extends HttpServlet {
    private final SectorServicio sectorServicio = new SectorServicio();

    /**
     * Maneja las peticiones GET para mostrar la pantalla de pago de un sector seleccionado.
     * @param peticion  Petición HTTP recibida
     * @param respuesta Respuesta HTTP a enviar
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
        UsuarioDto usuario = (UsuarioDto) peticion.getSession().getAttribute("usuario");
        if (usuario == null || usuario.getRolId()!=4)  {
            GestorRegistros.warning(null, "Intento de acceso a pagoSector sin usuario válido.");
            respuesta.sendRedirect(peticion.getContextPath() + "/acceso");
            return;
        }
        // Obtener el parámetro sectorId de la petición
        String sectorIdStr = peticion.getParameter("sectorId");
        if (sectorIdStr == null) {
            GestorRegistros.warning(usuario.getId(), "Intento de pago de sector sin sectorId en la petición.");
            respuesta.sendRedirect(peticion.getContextPath() + "/usuario/alquiler");
            return;
        }
        Long sectorId = Long.valueOf(sectorIdStr);
        // Obtener la lista de sectores libres
        List<SectorDto> sectoresLibres;
        try {
            sectoresLibres = sectorServicio.obtenerSectoresLibres();
        } catch (Exception e) {
            GestorRegistros.error(usuario.getId(), "Error obteniendo sectores libres: " + e.getMessage());
            peticion.setAttribute("error", "No se pudieron obtener los sectores libres: " + e.getMessage());
            sectoresLibres = java.util.Collections.emptyList();
        }
        // Buscar el sector seleccionado en la lista de sectores libres
        SectorDto sectorSeleccionado = null;
        for (SectorDto s : sectoresLibres) {
            if (s.getId().equals(sectorId)) {
                sectorSeleccionado = s;
                break;
            }
        }
        // Si el sector no está disponible, redirigir
        if (sectorSeleccionado == null) {
            GestorRegistros.warning(usuario.getId(), "Intento de pago para sector no disponible. SectorId: " + sectorId);
            respuesta.sendRedirect(peticion.getContextPath() + "/usuario/alquiler");
            return;
        }
        // Guardar el sectorId en la sesión para el proceso de pago
        HttpSession sesion = peticion.getSession();
        sesion.setAttribute("sectorId", sectorIdStr);
        GestorRegistros.info(usuario.getId(), "Acceso a pago de sector. SectorId: " + sectorId);
        // Pasar el sector seleccionado a la vista JSP
        peticion.setAttribute("sectorSeleccionado", sectorSeleccionado);
        peticion.getRequestDispatcher("/usuario/pagoSector.jsp").forward(peticion, respuesta);
    }
}