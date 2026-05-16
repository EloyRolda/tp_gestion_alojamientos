package GestionAlojamiento.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

//Lombok
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartamentoRegistroDTO {
    @NotNull(message = "Campo Obligatorio")
    @Min(value = 1, message = "Id invalido")
    private Long idAnfitrion;

    @NotNull(message = "Campo Obligatorio")
    private Integer piso;

    private boolean tieneAscensor;

    private boolean expensasIncluidas;

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

    @NotNull(message = "Campo obligatorio")
    @Min(value = 0, message = "Valor Invalido")
    private BigDecimal precioNoche;

    //[DIRECCION]
    @NotBlank(message = "Campo obligatorio")
    private String pais;

    @NotBlank(message = "Campo obligatorio")
    private String provincia;

    @NotBlank(message = "Campo obligatorio")
    @Min(value = 0, message = "Valor Invalido")
    private String codigoPostal;

    @NotBlank(message = "Campo obligatorio")
    private String ciudad;

    @NotBlank(message = "Campo obligatorio")
    private String calle;

    @NotNull(message = "Campo obligatorio")
    @Min(value = 0, message = "Valor Invalido")
    private Integer altura;

}
