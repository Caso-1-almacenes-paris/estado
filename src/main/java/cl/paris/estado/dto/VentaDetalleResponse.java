package cl.paris.estado.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** Espejo de un detalle de la respuesta del servicio "ventas" (solo lo que usa estado). */
@JsonIgnoreProperties(ignoreUnknown = true)
public record VentaDetalleResponse(
        Long productoId,
        Long proveedorId
) {
}
