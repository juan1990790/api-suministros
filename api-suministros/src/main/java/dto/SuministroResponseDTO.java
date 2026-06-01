package cl.duoc.api_suministros.dto;

import lombok.Data;

@Data
public class SuministroResponseDTO {
    private Long id;
    private String idFabricante;
    private String nombre;
    private Integer unidades;
    private Double valorComercial;
    private String estado;
    private String nombreBodega;

    private Object informacionTransito;
}