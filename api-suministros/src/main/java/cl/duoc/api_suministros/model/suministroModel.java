package cl.duoc.api_suministros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "COMPONENTES")
public class suministroModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "ID Fabricante obligatorio")
    @Column(name = "ID_FABRICANTE", nullable = false, unique = true, length = 50) // SEGURIDAD: Unique y no nulo
    private String idFabricante;

    @NotBlank(message = "Nombre obligatorio")
    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    @Column(name = "SKU", length = 50)
    private String sku;

    @NotNull(message = "Unidades obligatorias")
    @Min(value = 0, message = "No pueden ser negativas")
    @Column(name = "UNIDADES", nullable = false) // SEGURIDAD: No nulo en BD
    private Integer unidades;

    @NotNull(message = "Valor obligatorio")
    @Min(value = 0, message = "El valor debe ser positivo")
    @Column(name = "VALOR_COMERCIAL", nullable = false, precision = 10, scale = 2)
    private Double valorComercial;

    @Pattern(regexp = "EN_STOCK|ASIGNADO|AGOTADO", message = "Estado no válido")
    @Column(name = "ESTADO", nullable = false, length = 20) // SEGURIDAD: Restricción de valores en BD
    private String estado;

    @NotNull(message = "Bodega ID obligatoria")
    @Column(name = "BODEGA_ID", nullable = false)
    private Long bodegaId;
}
