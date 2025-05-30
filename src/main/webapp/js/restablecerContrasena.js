// Código para gestionar las contraseñas
document.addEventListener('DOMContentLoaded', function() {
    const nuevaContrasena = document.getElementById('nuevaContrasena');
    const confirmarContrasena = document.getElementById('confirmarContrasena');
    const btnMostrarContrasena = document.getElementById('mostrarContrasena');
    const btnMostrarConfirmarContrasena = document.getElementById('mostrarConfirmarContrasena');

    // Mostrar/ocultar contraseña nueva
    btnMostrarContrasena.addEventListener('click', function() {
        if (nuevaContrasena.type === "password") {
            nuevaContrasena.type = "text";
            this.querySelector('i').classList.replace('fa-eye', 'fa-eye-slash');
        } else {
            nuevaContrasena.type = "password";
            this.querySelector('i').classList.replace('fa-eye-slash', 'fa-eye');
        }
    });

    // Mostrar/ocultar confirmación de contraseña
    btnMostrarConfirmarContrasena.addEventListener('click', function() {
        if (confirmarContrasena.type === "password") {
            confirmarContrasena.type = "text";
            this.querySelector('i').classList.replace('fa-eye', 'fa-eye-slash');
        } else {
            confirmarContrasena.type = "password";
            this.querySelector('i').classList.replace('fa-eye-slash', 'fa-eye');
        }
    });

    // Validación de coincidencia de contraseñas al enviar el formulario
    const formulario = document.getElementById('formularioRestablecer');
    if(formulario) {
        formulario.addEventListener('submit', function(e) {
            if(nuevaContrasena && confirmarContrasena && nuevaContrasena.value !== confirmarContrasena.value) {
                confirmarContrasena.classList.add('is-invalid');
                // Buscar el div de feedback
                let feedback = confirmarContrasena.parentElement.querySelector('.invalid-feedback');
                if(feedback) feedback.textContent = 'Las contraseñas no coinciden.';
                e.preventDefault();
            } else {
                confirmarContrasena.classList.remove('is-invalid');
            }
        });
    }
});