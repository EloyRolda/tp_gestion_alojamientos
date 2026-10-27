package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import com.eloyroldan.tp_final_alojamiento.backend.Model.Alojamiento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/// Debe armarse dentro de un metodo @Transactional del Service (no en el Controller):
/// anfitrion/direccion/tipoAlojamiento son relaciones LAZY y open-in-view esta en false.
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlojamientoResponseDTO {

    private Long idAlojamiento;
    private String titulo;
    private String descripcion;
    private BigDecimal precioNoche;
    private Integer capacidad;
    private Integer cantAmbientes;
    private Integer cantHabitaciones;
    private Integer cantBanios;
    private Boolean activo;
    private UsuarioResumenDTO anfitrion;
    private DireccionResponseDTO direccion;
    private TipoAlojamientoResponseDTO tipoAlojamiento;

    public static AlojamientoResponseDTO desde(Alojamiento a) {
        return new AlojamientoResponseDTO(
                a.getIdAlojamiento(), a.getTitulo(), a.getDescripcion(), a.getPrecioNoche(),
                a.getCapacidad(), a.getCantAmbientes(), a.getCantHabitaciones(), a.getCantBanios(),
                a.getActivo(),
                UsuarioResumenDTO.desde(a.getAnfitrion()),
                DireccionResponseDTO.desde(a.getDireccion()),
                TipoAlojamientoResponseDTO.desde(a.getTipoAlojamiento()));
    }
}
