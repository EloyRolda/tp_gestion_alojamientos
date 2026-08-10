package GestionAlojamiento.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CasaRegistroDTO {
    @NotNull(message = "Campo Obligatorio")
    @Min(value = 1, message = "Id invalido")
    private Long idAnfitrion;

    private boolean tienePatio;
    private boolean tienePileta;
    private boolean tieneParrilla;

    //[ALOJAMIENTO]
    @NotNull(message = "Campo obligatorio")
    @Min(value = 1, message = "Valor Invalido")
    private Integer cantAmbientes;

    @NotNull(message = "Campo obligatorio")
    @Min(value = 0, message = "Valor Invalido")
    private Integer cantBanios;

    @NotNull(message = "Campo obligatorio")
    @Min(value = 0, message = "Valor Invalido")
    private Integer cantCamas;

    @NotNull(message = "Campo obligatorio")
    @Min(value = 0, message = "Valor Invalido")
    private Integer cantHabitaciones;

    @NotNull(message = "Campo obligatorio")
    @Min(value = 1)
    private Integer capacidad;

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 3000)
    private String descripcion;

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 150)
    private String titulo;

    @NotNull(message = "Campo obligatorio")
    @Min(value = 0, message = "Valor Invalido")
    private BigDecimal precioNoche;

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
