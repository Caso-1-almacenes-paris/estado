package cl.paris.estado.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cl.paris.estado.dto.ActualizarEstadoRequest;
import cl.paris.estado.dto.CrearSeguimientoRequest;
import cl.paris.estado.dto.HistorialEstadoResponse;
import cl.paris.estado.dto.SeguimientoResponse;
import cl.paris.estado.mapper.SeguimientoMapper;
import cl.paris.estado.service.SeguimientoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/envios")
public class SeguimientoController {

    private final SeguimientoService seguimientoService;

    public SeguimientoController(SeguimientoService seguimientoService) {
        this.seguimientoService = seguimientoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SeguimientoResponse crear(@Valid @RequestBody CrearSeguimientoRequest request) {
        return SeguimientoMapper.toResponse(seguimientoService.crear(request));
    }

    @GetMapping("/{id}")
    public SeguimientoResponse obtener(@PathVariable Long id) {
        return SeguimientoMapper.toResponse(seguimientoService.obtener(id));
    }

    @GetMapping("/venta/{ventaId}")
    public SeguimientoResponse obtenerPorVenta(@PathVariable Long ventaId) {
        return SeguimientoMapper.toResponse(seguimientoService.obtenerPorVenta(ventaId));
    }

    @GetMapping("/cliente/{clienteId}")
    public List<SeguimientoResponse> obtenerPorCliente(@PathVariable UUID clienteId) {
        return seguimientoService.obtenerPorCliente(clienteId).stream()
                .map(SeguimientoMapper::toResponse)
                .toList();
    }

    @PatchMapping("/{id}/estado")
    public SeguimientoResponse actualizarEstado(@PathVariable Long id,
                                                @Valid @RequestBody ActualizarEstadoRequest request) {
        return SeguimientoMapper.toResponse(seguimientoService.actualizarEstado(id, request));
    }

    @GetMapping("/{id}/historial")
    public List<HistorialEstadoResponse> historial(@PathVariable Long id) {
        return seguimientoService.obtener(id).getHistorial().stream()
                .map(SeguimientoMapper::toHistorialResponse)
                .toList();
    }
}
