package cl.duoc.api_suministros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "componentes")
public class suministroModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El tiene que llevar el id del fabricante.")
    @Column(nullable = false, unique = true)
    private String idFabricante;

    @NotBlank(message = "El nombre no puede estar vacio.")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El stock no puede ser cero.")
    @Column(nullable = false)
    private int unidades;


    @Column(nullable = false)
    private Double valorComercial;


    @Column(nullable = false, name = "ESTADO")
    @NotBlank(message = "El estado es requerido.")
    @Pattern(regexp="EN_STOCK|ASIGNADO|AGOTADO", message = "El estado debe ser EN_STOCK, EN_USO o AGOTADO.")
    private String estado; // EN_STOCK, ASIGNADO, AGOTADO

    private String sku;
}
