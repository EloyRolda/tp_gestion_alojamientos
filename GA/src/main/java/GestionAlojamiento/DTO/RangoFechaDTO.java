package GestionAlojamiento.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/// Representa un rango de fechas ocupado, para pintar el calendario de
/// disponibilidad publico sin exponer datos del cliente ni de la reserva.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RangoFechaDTO {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
