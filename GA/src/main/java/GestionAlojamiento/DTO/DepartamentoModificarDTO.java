package GestionAlojamiento.DTO;

import jakarta.validation.constraints.Min;
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
public class DepartamentoModificarDTO {

    @NotNull(message = "Campo Obligatorio")
    private Long id;


    @Min(value = 1, message = "Id invalido")
    private Long idAnfitrion;

    private Integer piso;

    private boolean tieneAscensor;

    private boolean expensasIncluidas;


    //[ALOJAMIENTO]
    @Min(value = 1, message = "Valor Invalido")
    private Integer cantAmbientes;

    @Min(value = 0, message = "Valor Invalido")
    private Integer cantBanios;

    @Min(value = 0, message = "Valor Invalido")
    private Integer cantCamas;

    @Min(value = 0, message = "Valor Invalido")
    private Integer cantHabitaciones;

    @Min(value = 1)
    private Integer capacidad;

    @Size(max = 3000)
    private String descripcion;

    @Min(value = 0, message = "Valor Invalido")
    private BigDecimal precioNoche;

    //[DIRECCION]

    private String pais;

    private String provincia;

    private String codigoPostal;

    private String ciudad;

    private String calle;

    @Min(value = 0, message = "Valor Invalido")
    private Integer altura;
}
