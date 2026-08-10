package GestionAlojamiento.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/// Fila del reporte de ingresos por mes de un anfitrion (para la seccion de estadisticas).
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngresoMensualDTO {
    private String mes; // formato "YYYY-MM"
    private BigDecimal ingresos;
    private long cantidadReservas;
}
