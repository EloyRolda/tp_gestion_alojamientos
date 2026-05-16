package GestionAlojamiento.DTO;

import GestionAlojamiento.Model.Alojamiento;
import GestionAlojamiento.Model.Cliente;
import GestionAlojamiento.Model.Enums.TipoEstado;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReservaRegistroDTO {
    @NotBlank(message = "Campo Obligatorio")
    private LocalDate fechaInicio;

    @NotBlank(message = "Campo Obligatorio")
    private LocalDate fechaFin;

    @DecimalMin(value = "0.0", inclusive = true, message = "Valor invalido, el precio no puede ser menor a cero")
    @NotNull(message = "Campo Obligatorio")
    private BigDecimal precioTotal;

    @Enumerated(EnumType.STRING)
    @NotBlank(message = "Campo Obligatorio")
    private TipoEstado tipoEstado = TipoEstado.PENDIENTE; //POR DEFECTO PENDIENTE

    @NotBlank(message = "Campo Obligatorio")
    private Long idCliente; //Se obtiene automaticamente desde la sesion

    @NotBlank(message = "Campo Obligatorio")
    private Long idAlojamiento; //Se obtiene automaticamente


}
