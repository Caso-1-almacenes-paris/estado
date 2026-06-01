package cl.paris.estado.dto;

import jakarta.validation.constraints.NotNull;

/** Crea el seguimiento de despacho para una venta. */
public record CrearSeguimientoRequest(

        @NotNull(message = "El ventaId es obligatorio")
        Long ventaId
) {
}
