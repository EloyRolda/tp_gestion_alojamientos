package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/// "valor" cambia de significado segun el criterio del ranking (ingresos,
/// cantidad de reservas o rating promedio) — se aclara en el campo "criterio".
/// Tiene 3 constructores en vez de @AllArgsConstructor porque cada consulta JPQL
/// que arma esta proyeccion devuelve un tipo numerico distinto (BigDecimal, Long,
/// Double) segun la funcion de agregacion usada (sum, count, avg).
//Lombok
@Data
@NoArgsConstructor
public class AlojamientoRankingDTO {
    private Long idAlojamiento;
    private String titulo;
    private BigDecimal valor;
    private String criterio;

    public AlojamientoRankingDTO(Long idAlojamiento, String titulo, BigDecimal valor, String criterio) {
        this.idAlojamiento = idAlojamiento;
        this.titulo = titulo;
        this.valor = valor;
        this.criterio = criterio;
    }

    public AlojamientoRankingDTO(Long idAlojamiento, String titulo, Long valor, String criterio) {
        this.idAlojamiento = idAlojamiento;
        this.titulo = titulo;
        this.valor = valor == null ? null : BigDecimal.valueOf(valor);
        this.criterio = criterio;
    }

    public AlojamientoRankingDTO(Long idAlojamiento, String titulo, Double valor, String criterio) {
        this.idAlojamiento = idAlojamiento;
        this.titulo = titulo;
        this.valor = valor == null ? null : BigDecimal.valueOf(valor).setScale(2, java.math.RoundingMode.HALF_UP);
        this.criterio = criterio;
    }
}
