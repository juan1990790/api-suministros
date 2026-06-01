package cl.duoc.api_suministros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "componentes")
public class suministroModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String idFabricante;

    @Column(nullable = false)
    private String nombre;

    // Validación del Caso 2: Jamás negativos
    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer unidades;

    // Validación del Caso 2: Jamás negativos
    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Double valorComercial;

    @Column(nullable = false)
    private String estado;

    private String sku;

    // Campo dinámico: Se muestra en Postman, pero no se guarda en la BD
    @Transient
    private String infoProveedorRemoto;
}