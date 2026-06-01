package cl.paris.estado.dto;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Espejo de la respuesta del servicio "ventas" (GET /api/v1/ventas/{id}).
 * estado se modela como String para no acoplar enums; estado solo necesita
 * el clienteId, el proveedor del primer detalle y comprobar que este PAGADA.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record VentaResponse(
        Long id,
        UUID clienteId,
        String estado,
        List<VentaDetalleResponse> detalles
) {
}
