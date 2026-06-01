package cl.paris.estado.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import cl.paris.estado.model.EstadoEnvio;

public record SeguimientoResponse(
        Long id,
        Long ventaId,
        UUID clienteId,
        Long proveedorId,
        EstadoEnvio estadoActual,
        String numSeguimiento,
        LocalDateTime fechaActualizacion,
        List<HistorialEstadoResponse> historial
) {
}
