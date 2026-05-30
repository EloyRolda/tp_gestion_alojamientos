package GestionAlojamiento.DTO;

import GestionAlojamiento.Model.Enums.TipoEstado;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaRegistroDTO {
    @NotNull(message = "Campo Obligatorio")
    private LocalDate fechaInicio;

    @NotNull(message = "Campo Obligatorio")
    private LocalDate fechaFin;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Campo Obligatorio")
    private TipoEstado tipoEstado = TipoEstado.PENDIENTE; //POR DEFECTO PENDIENTE

    @NotNull(message = "Campo Obligatorio")
    private Long idCliente; //Se obtiene automaticamente desde la sesion

    @NotNull(message = "Campo Obligatorio")
    private Long idAlojamiento; //Se obtiene automaticamente


}
