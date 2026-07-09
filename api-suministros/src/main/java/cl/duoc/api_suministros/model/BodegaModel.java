package cl.duoc.api_suministros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "BODEGAS")
public class BodegaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "La ciudad es obligatoria")
    @Column(name = "CIUDAD", nullable = false, length = 100)
    private String ciudad;

    @NotBlank(message = "El país es obligatorio")
    @Column(name = "PAIS", nullable = false, length = 50)
    private String pais;
}
