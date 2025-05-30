// Función para editar usuario
function editarUsuario(id, nombre, correo, movil, rolId, fotoBase64) {
    console.log("ID recibido:", id);
    document.getElementById('usuarioId').value = id;
    document.getElementById('nombreCompleto').value = nombre;
    document.getElementById('correoElectronico').value = correo;
    
    // Validar longitud del móvil antes de asignarlo
    if (movil && movil.length > 15) {
        movil = movil.substring(0, 15);
        console.log("Móvil truncado a 15 caracteres:", movil);
    }
    document.getElementById('movil').value = movil;
    document.getElementById('rolId').value = rolId;

    // Manejo de la foto
    const imagenActual = document.getElementById('imagenActual');
    if (fotoBase64) {
        imagenActual.src = fotoBase64;
        imagenActual.style.display = 'block';
    } else {
        imagenActual.src = "";
        imagenActual.style.display = 'none';
    }
}

// Función para mostrar vista previa de imagen en el formulario de crear
function mostrarVistaPrevia(evento) {
    const entrada = evento.target;
    const vistaPrevia = document.getElementById('vistaPrevia');

    if (entrada.files && entrada.files[0]) {
        const lector = new FileReader();
        
        lector.onload = function(e) {
            vistaPrevia.src = e.target.result;
            vistaPrevia.style.display = 'block';
        };
        
        lector.readAsDataURL(entrada.files[0]);
    } else {
        vistaPrevia.src = '#';
        vistaPrevia.style.display = 'none';
    }
}

// Función para mostrar vista previa de imagen en el formulario de modificar
function mostrarVistaPreviaModificar(evento) {
    const entrada = evento.target;
    const vistaPrevia = document.getElementById('vistaPreviaModificar');
    const imagenActual = document.getElementById('imagenActual');

    if (entrada.files && entrada.files[0]) {
        const lector = new FileReader();
        
        lector.onload = function(e) {
            vistaPrevia.src = e.target.result;
            vistaPrevia.style.display = 'block';
            imagenActual.style.display = 'none';
        };
        
        lector.readAsDataURL(entrada.files[0]);
    } else {
        vistaPrevia.src = '#';
        vistaPrevia.style.display = 'none';
        imagenActual.style.display = 'block';
    }
}

// Función para alternar visibilidad de contraseña
function mostrarContrasena(idCampo, boton) {
    const campoContrasena = document.getElementById(idCampo);
    const iconoOjo = boton.querySelector('i');

    if (campoContrasena.type === "password") {
        campoContrasena.type = "text";
        iconoOjo.classList.remove("fa-eye");
        iconoOjo.classList.add("fa-eye-slash");
    } else {
        campoContrasena.type = "password";
        iconoOjo.classList.remove("fa-eye-slash");
        iconoOjo.classList.add("fa-eye");
    }
}

// Configuración del modal de eliminación
document.addEventListener('DOMContentLoaded', function() {
    const modalEliminar = document.getElementById('modalEliminarUsuario');
    if (modalEliminar) {
        modalEliminar.addEventListener('show.bs.modal', function(evento) {
            const boton = evento.relatedTarget;
            const usuarioId = boton.getAttribute('data-usuario-id');
            
            document.getElementById('usuarioIdEliminar').value = usuarioId;
            document.getElementById('idUsuarioAEliminar').textContent = usuarioId;
            
            // Resetear el campo de confirmación
            const campoConfirmacion = document.getElementById('confirmacionId');
            campoConfirmacion.value = '';
            document.getElementById('btnConfirmarEliminar').disabled = true;
        });
    }
});

// Validación de ID + ' eliminar' para eliminación
// Solo habilita el botón si el valor es exactamente '<ID> eliminar'
document.addEventListener('DOMContentLoaded', function() {
    const campoConfirmacion = document.getElementById('confirmacionId');
    const formEliminar = document.getElementById('formEliminarUsuario');
    if (campoConfirmacion) {
        campoConfirmacion.addEventListener('input', function() {
            const idEsperado = document.getElementById('idUsuarioAEliminar').textContent.trim();
            const botonConfirmar = document.getElementById('btnConfirmarEliminar');
            const valor = this.value.trim();
            const esValido = valor === (idEsperado + ' eliminar');
            this.classList.toggle('is-invalid', !esValido);
            botonConfirmar.disabled = !esValido;
            // Mensaje de error personalizado
            if (!esValido) {
                this.setCustomValidity('Debe escribir: ' + idEsperado + ' eliminar');
            } else {
                this.setCustomValidity('');
            }
        });
        // Al enviar el formulario, reemplaza el valor por solo el ID
        if (formEliminar) {
            formEliminar.addEventListener('submit', function(e) {
                const idEsperado = document.getElementById('idUsuarioAEliminar').textContent.trim();
                if (campoConfirmacion.value.trim() === idEsperado + ' eliminar') {
                    campoConfirmacion.value = idEsperado;
                }
            });
        }
    }
});

// Ocultar alertas automáticamente
document.addEventListener('DOMContentLoaded', function() {
    const alertas = document.querySelectorAll('.alert');
    alertas.forEach(function(alerta) {
        setTimeout(function() {
            const bsAlerta = new bootstrap.Alert(alerta);
            bsAlerta.close();
        }, 6000);
    });
});

