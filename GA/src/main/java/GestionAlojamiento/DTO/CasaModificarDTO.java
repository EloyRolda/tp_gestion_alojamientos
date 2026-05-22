package GestionAlojamiento.DTO;

import GestionAlojamiento.Model.Alojamiento;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

//Lombok
@Data
@AllArgsConstructor
@NoArgsConstructor

public class CasaModificarDTO {

    @NotNull(message = "Id obligatorio")
    @Min(value = 1, message = "Id invalido")
    private Long idCasa;

    private Boolean tienePatio;

    private Boolean tienePileta;

    private Boolean tieneParrilla;

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

    //[DISPONIBILIDAD]
    private LocalDate fecha;

    private boolean disponible;

    private Alojamiento alojamiento;

    //[SERVICIO]

    private boolean tieneCocina;

    private boolean tieneLavarropa;

    private boolean tieneWifi;

    private boolean tieneEstacionamiento;


}