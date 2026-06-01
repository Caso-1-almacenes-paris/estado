package cl.paris.estado.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "seguimientos")
@Getter
@Setter
public class Seguimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Una venta tiene un unico seguimiento
    @Column(name = "venta_id", nullable = false, unique = true)
    private Long ventaId;

    @Column(name = "cliente_id", nullable = false)
    private UUID clienteId;

    @Column(name = "proveedor_id")
    private Long proveedorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_actual", nullable = false, length = 20)
    private EstadoEnvio estadoActual;

    @Column(name = "num_seguimiento", length = 30)
    private String numSeguimiento;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "seguimiento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialEstado> historial = new ArrayList<>();

    public void agregarHistorial(HistorialEstado entrada) {
        entrada.setSeguimiento(this);
        this.historial.add(entrada);
    }
}
