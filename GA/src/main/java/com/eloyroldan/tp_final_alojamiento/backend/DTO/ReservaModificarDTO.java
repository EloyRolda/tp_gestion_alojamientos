package com.eloyroldan.tp_final_alojamiento.backend.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/// Solo permite reprogramar fechas, y solo mientras la reserva este SOLICITADA.
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaModificarDTO {

    @NotNull(message = "Campo obligatorio")
    private Long idReserva;

    @NotNull(message = "Campo obligatorio")
    private LocalDate fechaInicio;

    @NotNull(message = "Campo obligatorio")
    private LocalDate fechaFin;
}
