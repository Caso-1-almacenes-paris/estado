package cl.paris.estado.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.paris.estado.client.VentaClient;
import cl.paris.estado.dto.ActualizarEstadoRequest;
import cl.paris.estado.dto.CrearSeguimientoRequest;
import cl.paris.estado.dto.VentaResponse;
import cl.paris.estado.exception.BusinessException;
import cl.paris.estado.exception.ResourceNotFoundException;
import cl.paris.estado.model.EstadoEnvio;
import cl.paris.estado.model.HistorialEstado;
import cl.paris.estado.model.Seguimiento;
import cl.paris.estado.repository.SeguimientoRepository;

@Service
public class SeguimientoService {

    private static final String ESTADO_VENTA_PAGADA = "PAGADA";

    private final SeguimientoRepository seguimientoRepository;
    private final VentaClient ventaClient;

    public SeguimientoService(SeguimientoRepository seguimientoRepository, VentaClient ventaClient) {
        this.seguimientoRepository = seguimientoRepository;
        this.ventaClient = ventaClient;
    }

    /**
     * Crea el seguimiento de una venta pagada. Toma clienteId y proveedor del
     * primer detalle desde el servicio de ventas e inicia el historial en PENDIENTE.
     */
    @Transactional
    public Seguimiento crear(CrearSeguimientoRequest request) {
        // Validacion cruzada: la venta debe existir
        VentaResponse venta = ventaClient.obtenerVenta(request.ventaId());

        // Regla de negocio: solo se despacha una venta pagada
        if (!ESTADO_VENTA_PAGADA.equalsIgnoreCase(venta.estado())) {
            throw new BusinessException(
                    "No se puede crear el seguimiento: la venta " + venta.id()
                            + " no esta PAGADA (estado: " + venta.estado() + ")");
        }

        // Regla de negocio: un seguimiento por venta
        if (seguimientoRepository.existsByVentaId(venta.id())) {
            throw new BusinessException(
                    "La venta " + venta.id() + " ya tiene un seguimiento de despacho");
        }

        Long proveedorId = (venta.detalles() != null && !venta.detalles().isEmpty())
                ? venta.detalles().get(0).proveedorId()
                : null;

        Seguimiento seguimiento = new Seguimiento();
        seguimiento.setVentaId(venta.id());
        seguimiento.setClienteId(venta.clienteId());
        seguimiento.setProveedorId(proveedorId);
        seguimiento.setEstadoActual(EstadoEnvio.PENDIENTE);
        seguimiento.setNumSeguimiento("ENV-" + venta.id());
        seguimiento.setFechaActualizacion(LocalDateTime.now());
        agregarEntradaHistorial(seguimiento, EstadoEnvio.PENDIENTE, "Seguimiento creado");

        return seguimientoRepository.save(seguimiento);
    }

    /** Actualiza el estado del envio y lo registra en el historial. */
    @Transactional
    public Seguimiento actualizarEstado(Long id, ActualizarEstadoRequest request) {
        Seguimiento seguimiento = obtener(id);

        EstadoEnvio actual = seguimiento.getEstadoActual();
        if (actual == EstadoEnvio.ENTREGADO || actual == EstadoEnvio.CANCELADO) {
            throw new BusinessException(
                    "El envio esta en un estado terminal (" + actual + ") y no se puede modificar");
        }

        seguimiento.setEstadoActual(request.estado());
        seguimiento.setFechaActualizacion(LocalDateTime.now());
        agregarEntradaHistorial(seguimiento, request.estado(), request.comentario());

        return seguimientoRepository.save(seguimiento);
    }

    @Transactional(readOnly = true)
    public Seguimiento obtener(Long id) {
        return seguimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seguimiento " + id + " no encontrado"));
    }

    @Transactional(readOnly = true)
    public Seguimiento obtenerPorVenta(Long ventaId) {
        return seguimientoRepository.findByVentaId(ventaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe seguimiento para la venta " + ventaId));
    }

    @Transactional(readOnly = true)
    public List<Seguimiento> obtenerPorCliente(UUID clienteId) {
        return seguimientoRepository.findByClienteId(clienteId);
    }

    private void agregarEntradaHistorial(Seguimiento seguimiento, EstadoEnvio estado, String comentario) {
        HistorialEstado entrada = new HistorialEstado();
        entrada.setEstado(estado);
        entrada.setComentario(comentario);
        entrada.setFechaCambio(LocalDateTime.now());
        seguimiento.agregarHistorial(entrada);
    }
}
