<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Pago de Sector</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <c:if test="${not empty error}">
            <div id="error-alert" class="alert alert-danger alert-dismissible fade show" role="alert">
              <i class="fas fa-exclamation-circle"></i> ${error}
              <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
            </div>
            <c:remove var="error" scope="session"/>
          </c:if>
          <a href="${pageContext.request.contextPath}/usuario/alquiler" class="btn btn-outline-primary mb-3 px-2 py-1" style="font-size: 0.85rem; border-color: #003366; color: #003366; background: #fff;" onmouseover="this.style.backgroundColor='#003366';this.style.color='#fff';" onmouseout="this.style.backgroundColor='#fff';this.style.color='#003366';">
              <i class="fas fa-arrow-left me-1"></i> Volver a mis alquileres
          </a>
          <div class="card">
            <div class="card-header text-white" style="background-color: #003366;">
                <h3>Pago de Sector: Sector #${sectorSeleccionado.id}</h3>
            </div>
            <div class="card-body">
                <h5>Precio mensual: €${sectorSeleccionado.precio}</h5>
                <form action="${pageContext.request.contextPath}/usuario/procesarPago" method="post">
                    <input type="hidden" name="sectorId" value="${sectorSeleccionado.id}">
                    <input type="hidden" name="total" value="${sectorSeleccionado.precio}">
                    <button type="submit" class="btn btn-lg mt-3" style="background-color: #003366; color: #fff;">
                        <i class="fab fa-paypal me-2"></i> Pagar con PayPal
                    </button>
                </form>
            </div>
        </div>
    </div>
</body>
</html>