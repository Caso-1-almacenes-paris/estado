package cl.paris.estado.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import cl.paris.estado.dto.*;
import cl.paris.estado.mapper.SeguimientoMapper;
import cl.paris.estado.service.SeguimientoService;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody; // Importación directa

@Tag(name = "Seguimiento", description = "Endpoints para la gestión de seguimientos de envíos")
@RestController
@RequestMapping("/api/envios")
public class SeguimientoController {

    private final SeguimientoService seguimientoService;

    public SeguimientoController(SeguimientoService seguimientoService) {
        this.seguimientoService = seguimientoService;
    }

    @Operation(summary = "Crear un nuevo seguimiento", 
        requestBody = @RequestBody(content = @Content(examples = @ExampleObject(value = "{\"ventaId\": 1001, \"clienteId\": \"123e4567-e89b-12d3-a456-426614174000\"}")))
    )
    @ApiResponse(
        responseCode = "201", 
        description = "Seguimiento creado exitosamente",
        content = @Content(examples = @ExampleObject(value = "{\"id\": 1, \"estado\": \"CREADO\"}"))
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SeguimientoResponse crear(@Valid @org.springframework.web.bind.annotation.RequestBody CrearSeguimientoRequest request) {
        return SeguimientoMapper.toResponse(seguimientoService.crear(request));
    }

    @Operation(summary = "Obtener seguimiento por ID")
    @ApiResponse(
        responseCode = "200", 
        description = "Seguimiento encontrado",
        content = @Content(examples = @ExampleObject(value = "{\"id\": 1, \"estado\": \"EN_PREPARACION\"}"))
    )
    @GetMapping("/{id}")
    public SeguimientoResponse obtener(@PathVariable Long id) {
        return SeguimientoMapper.toResponse(seguimientoService.obtener(id));
    }

    @Operation(summary = "Actualizar el estado de un seguimiento",
        requestBody = @RequestBody(content = @Content(examples = @ExampleObject(value = "{\"estado\": \"ENTREGADO\"}")))
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Estado del seguimiento actualizado",
        content = @Content(examples = @ExampleObject(value = "{\"id\": 1, \"estado\": \"ENTREGADO\"}"))
    )
    @PatchMapping("/{id}/estado")
    public SeguimientoResponse actualizarEstado(
            @PathVariable Long id,
            @Valid @org.springframework.web.bind.annotation.RequestBody ActualizarEstadoRequest request) {
        return SeguimientoMapper.toResponse(seguimientoService.actualizarEstado(id, request));
    }

    
}