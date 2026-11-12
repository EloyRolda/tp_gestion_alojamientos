package com.eloyroldan.tp_final_alojamiento.backend.DTO;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlojamientoModificarDTO {

    @NotNull(message = "Campo obligatorio")
    private Long idAlojamiento;

    @Size(max = 150)
    private String titulo;

    @Size(max = 3000)
    private String descripcion;

    @Min(value = 0, message = "Valor invalido")
    private BigDecimal precioNoche;

    @Min(value = 1)
    private Integer capacidad;

    @Min(value = 1, message = "Valor invalido")
    private Integer cantAmbientes;

    @Min(value = 1, message = "Valor invalido")
    private Integer cantHabitaciones;

    @Min(value = 1, message = "Valor invalido")
    private Integer cantBanios;

    private Boolean activo;

    /// Si viene cargado, se resuelve/crea en el catalogo y se reasigna.
    private String nombreTipoAlojamiento;

    /// Si viene cargado, reemplaza la direccion actual (por una existente).
    private Long idDireccion;

    /// Si vienen cargadas, actualizan el punto en el mapa de la direccion actual
    /// (calle y altura no se editan). Van siempre juntas.
    @DecimalMin(value = "-90.0", message = "Latitud fuera de rango")
    @DecimalMax(value = "90.0", message = "Latitud fuera de rango")
    private Double latitud;

    @DecimalMin(value = "-180.0", message = "Longitud fuera de rango")
    @DecimalMax(value = "180.0", message = "Longitud fuera de rango")
    private Double longitud;
}
