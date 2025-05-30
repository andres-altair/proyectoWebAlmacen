// Función para mostrar vista previa de la imagen
function mostrarVistaPrevia(event) {
    const vistaPrevia = document.getElementById('vistaPrevia');
    const archivo = event.target.files[0];
    
    if (archivo) {
        const lector = new FileReader();
        lector.onload = function(e) {
            vistaPrevia.src = e.target.result;
            vistaPrevia.style.display = 'block';
        }
        reader.readAsDataURL(file);
    } else {
        vistaPrevia.src = '#';
        vistaPrevia.style.display = 'none';
    }
}

// Función para alternar la visibilidad de la contraseña
function verContrasena(inputId, button) {
    const contrasenaIntroducida = document.getElementById(inputId);
    const ojoIcono = button.querySelector('i');

    if (contrasenaIntroducida.type === "password") {
        contrasenaIntroducida.type = "text"; // Mostrar contraseña
        ojoIcono.classList.remove("fa-eye");
        ojoIcono.classList.add("fa-eye-slash"); // Cambiar ícono a "ojo tachado"
    } else {
        contrasenaIntroducida.type = "password"; // Ocultar contraseña
        ojoIcono.classList.remove("fa-eye-slash");
        ojoIcono.classList.add("fa-eye"); // Cambiar ícono a "ojo"
    }
}

document.addEventListener('DOMContentLoaded', function() {
    const formulario = document.getElementById('formularioRegistro');
    const contrasena = document.getElementById('contrasena');
    const confirmarContrasena = document.getElementById('confirmarContrasena');
    const foto = document.getElementById('foto');
    const fotoPrevia = document.getElementById('previewFoto');
    const btnMostrarContrasena = document.getElementById('mostrarContrasena');
    const btnMostrarConfirmarContrasena = document.getElementById('mostrarConfirmarContrasena');

    // Validación del formulario
    formulario.addEventListener('submit', function(evento) {
        if (!formulario.checkValidity()) {
            evento.preventDefault();
            evento.stopPropagation();
        } else if (contrasena.value !== confirmarContrasena.value) {
            evento.preventDefault();
            confirmarContrasena.setCustomValidity('Las contraseñas no coinciden');
        } else {
            confirmarContrasena.setCustomValidity('');
        }
        formulario.classList.add('was-validated');
    });

    // Validación en tiempo real de contraseñas
    confirmarContrasena.addEventListener('input', function() {
        if (contrasena.value === confirmarContrasena.value) {
            confirmarContrasena.setCustomValidity('');
        } else {
            confirmarContrasena.setCustomValidity('Las contraseñas no coinciden');
        }
    });

    // Mostrar/ocultar contraseña
    btnMostrarContrasena.addEventListener('click', function() {
        const tipo = contrasena.type === 'password' ? 'text' : 'password';
        contrasena.type = tipo;
        btnMostrarContrasena.querySelector('i').classList.toggle('fa-eye');
        btnMostrarContrasena.querySelector('i').classList.toggle('fa-eye-slash');
    });

    // Mostrar/ocultar confirmar contraseña
    btnMostrarConfirmarContrasena.addEventListener('click', function() {
        const tipo = confirmarContrasena.type === 'password' ? 'text' : 'password';
        confirmarContrasena.type = tipo;
        btnMostrarConfirmarContrasena.querySelector('i').classList.toggle('fa-eye');
        btnMostrarConfirmarContrasena.querySelector('i').classList.toggle('fa-eye-slash');
    });

    // Vista previa de la foto
    foto.addEventListener('change', function() {
        if (this.files && this.files[0]) {
            const archivo = this.files[0];
            
            // Validar tipo de archivo
            if (!archivo.type.startsWith('image/')) {
                this.value = '';
                fotoPrevia.classList.add('d-none');
                this.setCustomValidity('Por favor, selecciona una imagen válida');
                return;
            }
            
            // Validar tamaño (máximo 5MB)
            if (archivo.size > 5 * 1024 * 1024) {
                this.value = '';
                fotoPrevia.classList.add('d-none');
                this.setCustomValidity('La imagen no debe superar los 5MB');
                return;
            }

            this.setCustomValidity('');
            const lector = new FileReader();
            
            lector.onload = function(e) {
                fotoPrevia.querySelector('img').src = e.target.result;
                fotoPrevia.classList.remove('d-none');
            };
            
            lector.readAsDataURL(archivo);
        } else {
            fotoPrevia.classList.add('d-none');
        }
    });
});

// Función para manejar el registro con Google
function manejarRegistroGoogle(respuesta) {
    if (respuesta.credential) {
        // Obtener el contextPath desde el meta tag
        const contexto = document.querySelector('meta[name="context-path"]').content;
        
        // Enviar el token al servlet de registro
        const formulario = document.createElement('form');
        formulario.method = 'POST';
        formulario.action = contexto + '/GoogleRegistro';
        
        const entrada = document.createElement('input');
        entrada.type = 'hidden';
        entrada.name = 'credential';
        entrada.value = respuesta.credential;
        
        formulario.appendChild(entrada);
        document.body.appendChild(formulario);
        formulario.submit();
    }
}