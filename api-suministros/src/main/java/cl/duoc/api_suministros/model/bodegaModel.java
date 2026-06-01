package cl.duoc.api_suministros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="BODEGAS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class bodegaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombreBodega;

    @NotBlank
    private String ubicacion;
}