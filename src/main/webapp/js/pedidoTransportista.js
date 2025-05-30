// Variables globales para el mapa, los servicios de direcciones y el control de la fila del mapa
let mapa;
let servicioDirecciones;
let renderizadorDirecciones;
let filaMapaActual = null;

/**
 * Muestra o cierra el mapa con la ruta entre origen y destino debajo de la fila seleccionada.
 * Si ya hay un mapa abierto en esa fila, lo cierra. Si hay uno en otra fila, lo reemplaza.
 * @param {HTMLElement} btn - Botón que dispara la acción
 * @param {string} origen - Dirección de origen
 * @param {string} destino - Dirección de destino
 */
function mostrarMapaEnFila(btn, origen, destino) {
    // Encuentra la fila de la tabla donde se hizo clic
    const tr = btn.closest('tr');
    // Si el mapa ya está abierto para esta fila, ciérralo
    if (filaMapaActual && filaMapaActual.previousSibling === tr) {
        filaMapaActual.remove();
        filaMapaActual = null;
        return;
    }
    // Si hay otro mapa abierto en otra fila, ciérralo
    if (filaMapaActual) {
        filaMapaActual.remove();
        filaMapaActual = null;
    }
    // Crea una nueva fila para mostrar el mapa
    const filaMapa = document.createElement('tr');
    filaMapa.className = 'fila-mapa';
    filaMapa.innerHTML = `<td colspan="3"><div id="map" style="width: 100%; height: 450px;"></div></td>`;
    // Inserta la fila del mapa justo debajo de la fila seleccionada
    tr.parentNode.insertBefore(filaMapa, tr.nextSibling);
    filaMapaActual = filaMapa;
    // Inicializa el mapa y calcula la ruta después de un pequeño retraso para asegurar que el div existe
    setTimeout(function() {
        if (!window.google || !window.google.maps) {
            alert('No se pudo cargar Google Maps.');
            return;
        }
        inicializarMapa();
        calcularRuta(origen, destino);
    }, 200);
}

/**
 * Inicializa el mapa de Google Maps en el div con id "map"
 * y prepara los servicios de direcciones.
 */
function inicializarMapa() {
    const mapaDiv = document.getElementById("map");
    mapa = new google.maps.Map(mapaDiv, {
        zoom: 7,
        center: { lat: 40, lng: -3 } // Centro aproximado de España
    });
    servicioDirecciones = new google.maps.DirectionsService();
    renderizadorDirecciones = new google.maps.DirectionsRenderer();
    renderizadorDirecciones.setMap(mapa);
}

/**
 * Calcula la ruta entre el origen y el destino usando el servicio de direcciones de Google Maps
 * y la muestra en el mapa.
 * @param {string} origen - Dirección de origen
 * @param {string} destino - Dirección de destino
 */
function calcularRuta(origen, destino) {
    // Objeto de solicitud para Google Maps Directions API
    const request = {
        origin: origen,
        destination: destino,
        travelMode: 'DRIVING' // Modo de viaje en coche
    };

    // Calcula la ruta y la muestra en el mapa
    servicioDirecciones.route(request, function(resultado, estado) {
        if (estado == 'OK') {
            renderizadorDirecciones.setDirections(resultado);
        } else {
            alert('No se pudo calcular la ruta: ' + estado);
        }
    });
}