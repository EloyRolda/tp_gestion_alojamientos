package com.eloyroldan.tp_final_alojamiento.backend.DTO;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADOPAGO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaPagoDTO {

    @NotNull(message = "Campo obligatorio")
    private Long idReserva;

    @NotNull(message = "Campo obligatorio")
    private ESTADOPAGO estadoPago;
}
