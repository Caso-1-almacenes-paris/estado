package cl.paris.estado.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.paris.estado.model.Seguimiento;

@Repository
public interface SeguimientoRepository extends JpaRepository<Seguimiento, Long> {

    Optional<Seguimiento> findByVentaId(Long ventaId);

    List<Seguimiento> findByClienteId(UUID clienteId);

    boolean existsByVentaId(Long ventaId);
}
