package com.eloyroldan.tp_final_alojamiento.backend.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// La review se crea sobre una reserva puntual y ya FINALIZADA. No existe
/// DTO de modificacion: una vez creada, solo se puede eliminar.
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRegistroDTO {

    @NotNull(message = "Campo obligatorio")
    private Long idReserva;

    @NotNull(message = "Campo obligatorio")
    @Min(value = 1, message = "El puntaje minimo es 1")
    @Max(value = 5, message = "El puntaje maximo es 5")
    private Integer puntuacion;

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 2000, message = "El comentario es muy largo")
    private String comentario;
}
