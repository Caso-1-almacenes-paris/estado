package cl.paris.estado.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import cl.paris.estado.dto.VentaResponse;
import cl.paris.estado.exception.ResourceNotFoundException;
import reactor.core.publisher.Mono;

/** Consume el microservicio "ventas" para validar la venta del seguimiento. */
@Component
public class VentaClient {

    private final WebClient webClient;

    public VentaClient(@Qualifier("ventasWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public VentaResponse obtenerVenta(Long ventaId) {
        return webClient.get()
                .uri("/api/v1/ventas/{id}", ventaId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, resp ->
                        Mono.error(new ResourceNotFoundException(
                                "Venta " + ventaId + " no encontrada en el servicio de ventas")))
                .bodyToMono(VentaResponse.class)
                .block();
    }
}
