package GestionAlojamiento.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

//Lombok
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelRegistroDTO {

    @NotNull(message = "Campo Obligatorio")
    @Min(value = 1, message = "Id invalido")
    private Long idAnfitrion;

    @Min(value = 0, message = "Valor minimo de 0 estrellas")
    @Max(value = 5, message = "Valor maximo de 5 estrellas")
    @NotNull(message = "Campo obligatorio")
    private Integer estrellas;

    @NotNull(message = "Campo obligatorio")
    private boolean incluyeLimpieza;

    @NotNull(message = "Campo obligatorio")
    private boolean incluyeDesayuno;

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
