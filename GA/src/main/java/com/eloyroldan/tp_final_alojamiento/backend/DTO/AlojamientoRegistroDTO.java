package com.eloyroldan.tp_final_alojamiento.backend.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/// Si idDireccion viene cargado, se reutiliza esa direccion existente
/// (ignorando "direccion"). Si no, "direccion" es obligatorio y se crea una nueva.
/// idAnfitrion solo lo puede usar el ADMIN para publicar a nombre de otro anfitrion;
/// si lo publica un ANFITRION, el servicio ignora este campo y usa su propio id.
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlojamientoRegistroDTO {

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 150)
    private String titulo;

    @Size(max = 3000)
    private String descripcion;

    @NotNull(message = "Campo obligatorio")
    @Positive(message = "Valor invalido")
    private BigDecimal precioNoche;

    @NotNull(message = "Campo obligatorio")
    @Min(value = 1)
    private Integer capacidad;

    @NotNull(message = "Campo obligatorio")
    @Min(value = 1, message = "Valor invalido")
    private Integer cantAmbientes;

    @NotNull(message = "Campo obligatorio")
    @Min(value = 1, message = "Valor invalido")
    private Integer cantHabitaciones;

    @NotNull(message = "Campo obligatorio")
    @Min(value = 1, message = "Valor invalido")
    private Integer cantBanios;

    /// Nombre del tipo de alojamiento (catalogo dinamico). Si no existe, se crea.
    @NotBlank(message = "Campo obligatorio")
    @Size(max = 100)
    private String nombreTipoAlojamiento;

    /// Nombres de servicios/comodidades (catalogo dinamico). Opcional.
    private List<String> nombresServicios = new ArrayList<>();

    private Long idDireccion;

    @Valid
    private DireccionDTO direccion;

    private Long idAnfitrion;
}
