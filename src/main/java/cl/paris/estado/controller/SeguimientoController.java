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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@Tag(name = "Seguimiento", description = "Endpoints para la gestión de seguimientos de envíos")
@RestController
@RequestMapping("/api/envios")
public class SeguimientoController {

    private final SeguimientoService seguimientoService;

    public SeguimientoController(SeguimientoService seguimientoService) {
        this.seguimientoService = seguimientoService;
    }

    @Operation(summary = "Crear un nuevo seguimiento")
    @ApiResponse(
        responseCode = "201", 
        description = "Seguimiento creado exitosamente",
        content = @Content(examples = @ExampleObject(value = "{\n  \"id\": 1,\n  \"estado\": \"CREADO\"\n}"))
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SeguimientoResponse crear(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = @Content(examples = @ExampleObject(value = "{\n  \"ventaId\": 1001,\n  \"clienteId\": \"123e4567-e89b-12d3-a456-426614174000\"\n}"))
            )
            @Valid @RequestBody CrearSeguimientoRequest request) {
        return SeguimientoMapper.toResponse(seguimientoService.crear(request));
    }

    @Operation(summary = "Obtener seguimiento por ID")
    @ApiResponse(
        responseCode = "200", 
        description = "Seguimiento encontrado",
        content = @Content(examples = @ExampleObject(value = "{\n  \"id\": 1,\n  \"estado\": \"EN_PREPARACION\"\n}"))
    )
    @GetMapping("/{id}")
    public SeguimientoResponse obtener(@PathVariable Long id) {
        return SeguimientoMapper.toResponse(seguimientoService.obtener(id));
    }

    @Operation(summary = "Obtener seguimiento por ID de venta")
    @ApiResponse(
        responseCode = "200", 
        description = "Seguimiento asociado a la venta",
        content = @Content(examples = @ExampleObject(value = "{\n  \"id\": 1,\n  \"ventaId\": 1001,\n  \"estado\": \"EN_DESPACHO\"\n}"))
    )
    @GetMapping("/venta/{ventaId}")
    public SeguimientoResponse obtenerPorVenta(@PathVariable Long ventaId) {
        return SeguimientoMapper.toResponse(seguimientoService.obtenerPorVenta(ventaId));
    }

    @Operation(summary = "Obtener todos los seguimientos de un cliente")
    @ApiResponse(
        responseCode = "200", 
        description = "Lista de seguimientos del cliente",
        content = @Content(examples = @ExampleObject(value = "[\n  {\n    \"id\": 1,\n    \"estado\": \"ENTREGADO\"\n  }\n]"))
    )
    @GetMapping("/cliente/{clienteId}")
    public List<SeguimientoResponse> obtenerPorCliente(@PathVariable UUID clienteId) {
        return seguimientoService.obtenerPorCliente(clienteId).stream()
                .map(SeguimientoMapper::toResponse)
                .toList();
    }

    @Operation(summary = "Actualizar el estado de un seguimiento")
    @ApiResponse(
        responseCode = "200", 
        description = "Estado del seguimiento actualizado",
        content = @Content(examples = @ExampleObject(value = "{\n  \"id\": 1,\n  \"estado\": \"ENTREGADO\"\n}"))
    )
    @PatchMapping("/{id}/estado")
    public SeguimientoResponse actualizarEstado(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = @Content(examples = @ExampleObject(value = "{\n  \"estado\": \"ENTREGADO\"\n}"))
            )
            @Valid @RequestBody ActualizarEstadoRequest request) {
        return SeguimientoMapper.toResponse(seguimientoService.actualizarEstado(id, request));
    }

    @Operation(summary = "Obtener el historial de estados de un seguimiento")
    @ApiResponse(
        responseCode = "200", 
        description = "Historial completo de estados",
        content = @Content(examples = @ExampleObject(value = "[\n  {\n    \"estado\": \"CREADO\",\n    \"fecha\": \"2026-07-01T10:00:00\"\n  }\n]"))
    )
    @GetMapping("/{id}/historial")
    public List<HistorialEstadoResponse> historial(@PathVariable Long id) {
        return seguimientoService.obtener(id).getHistorial().stream()
                .map(SeguimientoMapper::toHistorialResponse)
                .toList();
    }
}