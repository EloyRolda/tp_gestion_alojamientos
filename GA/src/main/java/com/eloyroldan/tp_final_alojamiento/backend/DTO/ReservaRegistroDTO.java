package com.eloyroldan.tp_final_alojamiento.backend.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/// idCliente se ignora si lo envia un CLIENTE (se usa su propio id); el ADMIN
/// si puede reservar en nombre de otro cliente.
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaRegistroDTO {

    @NotNull(message = "Campo obligatorio")
    private LocalDate fechaInicio;

    @NotNull(message = "Campo obligatorio")
    private LocalDate fechaFin;

    @NotNull(message = "Campo obligatorio")
    private Long idAlojamiento;

    private Long idCliente;
}
