package com.eloyroldan.tp_final_alojamiento.backend.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// DTO generico para que el admin cree o renombre una entrada de un catalogo
/// (Servicio o TipoAlojamiento) a mano. Mismo criterio de normalizacion que
/// el alta implicita desde AlojamientoRegistroDTO (ver TipoAlojamientoService/
/// ServicioService.normalizar): se aplica en el Service, no aca.
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NombreCatalogoDTO {

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 255)
    private String nombre;
}
