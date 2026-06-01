package cl.duoc.api_suministros.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SuministroRequestDTO {

    @NotBlank(message = "El ID del fabricante es obligatorio.")
    private String idFabricante;

    @NotBlank(message = "El nombre no puede estar vacío.")
    private String nombre;

    // REGLA DE NEGOCIO: Flujo de Entrada (Jamás negativos o nulos)
    @NotNull(message = "Las unidades son obligatorias.")
    @Min(value = 0, message = "Las unidades no pueden ser valores negativos.")
    private Integer unidades;

    // REGLA DE NEGOCIO: Flujo de Entrada (Jamás negativos o nulos)
    @NotNull(message = "El valor comercial es obligatorio.")
    @Min(value = 0, message = "El valor comercial no puede ser negativo.")
    private Double valorComercial;

    @NotBlank(message = "El estado es requerido.")
    @Pattern(regexp = "^(EN_STOCK|ASIGNADO|AGOTADO)$", message = "El estado debe ser EN_STOCK, ASIGNADO o AGOTADO.")
    private String estado;

    @NotNull(message = "Debe asignar el suministro a una bodega válida.")
    private Long bodegaId;
}