package GestionAlojamiento.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/// Lo que carga el CLIENTE al pedir una reserva. El id de cliente se toma
/// siempre de la sesion (JWT), nunca del body, para que nadie reserve en nombre de otro.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaSolicitudDTO {

    @NotNull(message = "Campo Obligatorio")
    private LocalDate fechaInicio;

    @NotNull(message = "Campo Obligatorio")
    private LocalDate fechaFin;

    @NotNull(message = "Campo Obligatorio")
    @Min(value = 1, message = "Debe haber al menos 1 persona")
    private Integer cantidadPersonas;

    @NotBlank(message = "Contale al anfitrion por que te gustaria hospedarte")
    @Size(max = 1000, message = "El mensaje es muy largo")
    private String mensajeSolicitud;

    @NotNull(message = "Campo Obligatorio")
    private Long idAlojamiento;
}
