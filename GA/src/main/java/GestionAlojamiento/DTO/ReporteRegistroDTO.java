package GestionAlojamiento.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// Debe venir cargado exactamente UNO de los dos ids (se valida en ReporteService).
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReporteRegistroDTO {

    private Long idUsuarioReportado;

    private Long idAlojamientoReportado;

    @NotBlank(message = "Campo Obligatorio")
    @Size(max = 2000)
    private String descripcion;
}
