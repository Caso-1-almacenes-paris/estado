package cl.paris.estado.dto;

import java.time.LocalDateTime;

import cl.paris.estado.model.EstadoEnvio;

public record HistorialEstadoResponse(
        EstadoEnvio estado,
        String comentario,
        LocalDateTime fechaCambio
) {
}
