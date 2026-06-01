package cl.paris.estado.mapper;

import java.util.List;

import cl.paris.estado.dto.HistorialEstadoResponse;
import cl.paris.estado.dto.SeguimientoResponse;
import cl.paris.estado.model.HistorialEstado;
import cl.paris.estado.model.Seguimiento;

public final class SeguimientoMapper {

    private SeguimientoMapper() {
    }

    public static SeguimientoResponse toResponse(Seguimiento seguimiento) {
        List<HistorialEstadoResponse> historial = seguimiento.getHistorial().stream()
                .map(SeguimientoMapper::toHistorialResponse)
                .toList();

        return new SeguimientoResponse(
                seguimiento.getId(),
                seguimiento.getVentaId(),
                seguimiento.getClienteId(),
                seguimiento.getProveedorId(),
                seguimiento.getEstadoActual(),
                seguimiento.getNumSeguimiento(),
                seguimiento.getFechaActualizacion(),
                historial
        );
    }

    public static HistorialEstadoResponse toHistorialResponse(HistorialEstado entrada) {
        return new HistorialEstadoResponse(
                entrada.getEstado(),
                entrada.getComentario(),
                entrada.getFechaCambio()
        );
    }
}
