package GestionAlojamiento.DTO;

import GestionAlojamiento.Model.Enums.TipoAlojamiento;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/// DTO unico de alta para los 3 tipos de alojamiento. "piso" es obligatorio
/// (se valida en el Service, no aca, porque depende del valor de "tipo")
/// solo si tipo=DEPARTAMENTO, "estrellas" solo si tipo=HOTEL.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlojamientoRegistroDTO {

    @NotNull(message = "Campo Obligatorio")
    @Min(value = 1, message = "Id invalido")
    private Long idAnfitrion;

    @NotNull(message = "Campo Obligatorio")
    private TipoAlojamiento tipo;

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 150)
    private String titulo;

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 3000)
    private String descripcion;

    @NotNull(message = "Campo obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "Valor Invalido")
    private BigDecimal precioNoche;

    @NotNull(message = "Campo obligatorio")
    @Min(value = 1)
    private Integer capacidad;

    @NotNull(message = "Campo obligatorio")
    @Min(value = 1, message = "Valor Invalido")
    private Integer cantAmbientes;

    @NotNull(message = "Campo obligatorio")
    @Min(value = 0, message = "Valor Invalido")
    private Integer cantHabitaciones;

    @NotNull(message = "Campo obligatorio")
    @Min(value = 0, message = "Valor Invalido")
    private Integer cantCamas;

    @NotNull(message = "Campo obligatorio")
    @Min(value = 0, message = "Valor Invalido")
    private Integer cantBanios;

    /// Obligatorio solo si tipo=DEPARTAMENTO (se valida en AlojamientoService).
    private Integer piso;

    /// Obligatorio solo si tipo=HOTEL (se valida en AlojamientoService).
    @Min(value = 0, message = "Valor minimo de 0 estrellas")
    @Max(value = 5, message = "Valor maximo de 5 estrellas")
    private Integer estrellas;

    //[DIRECCION]
    @NotBlank(message = "Campo obligatorio")
    private String pais;

    @NotBlank(message = "Campo obligatorio")
    private String provincia;

    @NotBlank(message = "Campo obligatorio")
    private String codigoPostal;

    @NotBlank(message = "Campo obligatorio")
    private String ciudad;

    @NotBlank(message = "Campo obligatorio")
    private String calle;

    @NotNull(message = "Campo obligatorio")
    @Min(value = 0, message = "Valor Invalido")
    private Integer altura;

    //[AMENITIES] ids del catalogo (GET /Amenity/listar)
    private List<Long> amenityIds;
}
