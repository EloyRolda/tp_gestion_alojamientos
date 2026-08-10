package GestionAlojamiento.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticasAnfitrionDTO {
    private int cantidadAlojamientos;
    private long cantidadReservasTotales;
    private long cantidadReservasFinalizadas;
    private BigDecimal ingresosTotales;
    private double promedioNochesPorEstadia;
    private double tasaDeAceptacionPorcentaje;
    private double calificacionPromedio;
    private List<IngresoMensualDTO> ingresosPorMes;
}
