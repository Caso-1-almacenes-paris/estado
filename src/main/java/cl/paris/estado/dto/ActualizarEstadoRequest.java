package cl.paris.estado.dto;

import cl.paris.estado.model.EstadoEnvio;
import jakarta.validation.constraints.NotNull;

/** Actualiza el estado del envio (lo usa el vendedor al despachar). */
public record ActualizarEstadoRequest(

        @NotNull(message = "El estado es obligatorio")
        EstadoEnvio estado,

        String comentario
) {
}
