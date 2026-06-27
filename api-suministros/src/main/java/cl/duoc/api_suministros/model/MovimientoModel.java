package cl.duoc.api_suministros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "MOVIMIENTOS_INVENTARIO")
public class MovimientoModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "SKU_COMPONENTE", nullable = false, length = 50)
    private String skuComponente;

    @NotBlank
    @Column(name = "ACCION", nullable = false, length = 50)
    private String accion;

    @NotNull
    @Column(name = "FECHA_MOVIMIENTO", nullable = false)
    private LocalDateTime fechaMovimiento;

    @NotNull
    @Column(name = "CANTIDAD", nullable = false)
    private Integer cantidad;
}