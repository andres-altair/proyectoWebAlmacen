<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Pago Cancelado</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <div class="alert alert-warning">
            <h3>Pago Cancelado</h3>
            <p>El proceso de pago ha sido cancelado.</p>
            <a href="${pageContext.request.contextPath}/usuario/panel" class="btn btn-primary">Volver al Panel de Control</a>
        </div>
    </div>
</body>
</html>