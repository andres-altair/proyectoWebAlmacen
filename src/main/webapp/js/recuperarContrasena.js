/**
 * Validación del formulario de recuperación de contraseña
 */
document.addEventListener('DOMContentLoaded', function() {
    const formularios = document.querySelectorAll('.needs-validation');
    
    Array.from(formularios).forEach(formulario => {
        formulario.addEventListener('submit', evento => {
            if (!formulario.checkValidity()) {
                evento.preventDefault();
                evento.stopPropagation();
            }
            formulario.classList.add('was-validated');
        }, false);
    });
});

