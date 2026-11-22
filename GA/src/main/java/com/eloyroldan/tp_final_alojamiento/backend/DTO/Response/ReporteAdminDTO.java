/// Fila del listado de moderacion (RF44 / SCRUM-79): el reporte con los nombres ya
/// resueltos del autor y del objetivo, para que la pantalla del admin no tenga
/// que pedir nada mas. Se arma dentro de un metodo @Transactional.
package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.ACCIONMODERACION;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADOREPORTE;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPOREPORTE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteAdminDTO {

    private Long id;
    private TIPOREPORTE tipo;
    private Long idObjetivo;
    /// Titulo del alojamiento o "Nombre Apellido" de la persona reportada.
    private String nombreObjetivo;

    private Long idAutor;
    private String nombreAutor;

    private String motivo;
    /// Etiqueta en español del motivo (la que ve el usuario).
    private String etiquetaMotivo;
    private String detalle;

    private ESTADOREPORTE estado;
    /// Null mientras el reporte esta pendiente.
    private ACCIONMODERACION accionTomada;
    private LocalDateTime fechaCreacion;
    /// Null mientras el reporte esta pendiente.
    private LocalDateTime fechaResolucion;
}
