package GestionAlojamiento.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticasClienteDTO {
    private long cantidadReservasTotales;
    private long cantidadEstadiasFinalizadas;
    private long cantidadCanceladasOVencidas;
    private BigDecimal gastoTotal;
}
