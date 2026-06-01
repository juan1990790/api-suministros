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
package cl.duoc.api_suministros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Data
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
    @Min(value = 0, message = "El stock no puede ser menor a cero.")
    @Column(nullable = false)
    private Integer unidades;


    @Column(nullable = false)
    private Double valorComercial;


    @Column(nullable = false, name = "ESTADO")
    @NotBlank(message = "El estado es requerido.")
    @Pattern(regexp="EN_STOCK|ASIGNADO|AGOTADO", message = "El estado debe ser EN_STOCK, EN_USO o AGOTADO.")
    private String estado; // EN_STOCK, ASIGNADO, AGOTADO

    private String sku;
}
