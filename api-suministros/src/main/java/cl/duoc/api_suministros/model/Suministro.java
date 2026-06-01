package cl.duoc.api_suministros.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "COMPONENTES")
public class Suministro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ID_FABRICANTE", nullable = false, unique = true, length = 50)
    private String idFabricante;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private Integer unidades;

    @Column(name = "VALOR_COMERCIAL", nullable = false)
    private Double valorComercial;

    @Column(nullable = false, length = 20)
    private String estado; // EN_STOCK, ASIGNADO, AGOTADO

    // Relación con Bodega (Estricta coherencia relacional)
    @ManyToOne(optional = false)
    @JoinColumn(name = "BODEGA_ID", nullable = false)
    private Bodega bodega;

    // Registros de auditoría
    @Column(name = "USUARIO_AUDITORIA", nullable = false, length = 50)
    private String usuarioAuditoria = "SISTEMA_AUTO"; // Valor por defecto para el ejemplo

    @CreationTimestamp
    @Column(name = "FECHA_MODIFICACION", updatable = false)
    private LocalDateTime fechaModificacion;
}