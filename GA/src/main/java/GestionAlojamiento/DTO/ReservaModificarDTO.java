package GestionAlojamiento.DTO;

import GestionAlojamiento.Model.Enums.TipoEstado;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaModificarDTO {

    @NotNull(message = "Campo Obligatorio")
    private Long id;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    @Enumerated(EnumType.STRING)
    private TipoEstado tipoEstado;//POR DEFECTO PENDIENTE

    private Long idCliente;
    private Long idAlojamiento;

}
