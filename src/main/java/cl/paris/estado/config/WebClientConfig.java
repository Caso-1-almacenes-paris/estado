package cl.paris.estado.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    @LoadBalanced 
    public WebClient.Builder ventasWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient ventasWebClient(@Value("${services.ventas.url}") String url) {
        return WebClient.builder().baseUrl(url).build();
    }
}
