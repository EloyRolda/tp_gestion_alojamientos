package GestionAlojamiento.DTO;

import GestionAlojamiento.Model.Enums.TipoEstado;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReservaModificarDTO {

    @NotNull(message = "Campo Obligatorio")
    private Long id;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    @DecimalMin(value = "0.0", inclusive = true, message = "Valor invalido, el precio no puede ser menor a cero")
    private BigDecimal precioTotal;

    @Enumerated(EnumType.STRING)
    private TipoEstado tipoEstado;//POR DEFECTO PENDIENTE

    private Long idCliente;
    private Long idAlojamiento;

}
