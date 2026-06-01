package cl.paris.estado;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EstadoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EstadoApplication.class, args);
    }

}
