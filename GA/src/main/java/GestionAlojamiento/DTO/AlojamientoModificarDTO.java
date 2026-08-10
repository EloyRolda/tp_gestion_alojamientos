package GestionAlojamiento.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/// DTO unico de edicion. No incluye "tipo": una vez creado, el tipo de
/// alojamiento no se puede cambiar (cambiaria el significado de piso/estrellas
/// y de las reservas/reviews ya asociadas). Todos los campos son opcionales
/// salvo el id: el frontend precarga el formulario con los valores actuales
/// y reenvia todo (no hay ambiguedad de "en blanco = no cambiar").
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlojamientoModificarDTO {

    @NotNull(message = "Campo Obligatorio")
    @Min(value = 1, message = "Id invalido")
    private Long id;

    @Size(max = 150)
    private String titulo;

    @Size(max = 3000)
    private String descripcion;

    @Min(value = 0, message = "Valor Invalido")
    private BigDecimal precioNoche;

    @Min(value = 1)
    private Integer capacidad;

    @Min(value = 1, message = "Valor Invalido")
    private Integer cantAmbientes;

    @Min(value = 0, message = "Valor Invalido")
    private Integer cantHabitaciones;

    @Min(value = 0, message = "Valor Invalido")
    private Integer cantCamas;

    @Min(value = 0, message = "Valor Invalido")
    private Integer cantBanios;

    /// Solo tiene efecto si el alojamiento es un DEPARTAMENTO.
    private Integer piso;

    /// Solo tiene efecto si el alojamiento es un HOTEL.
    @Min(value = 0, message = "Valor minimo de 0 estrellas")
    @Max(value = 5, message = "Valor maximo de 5 estrellas")
    private Integer estrellas;

    private Boolean activo;

    //[DIRECCION]
    private String pais;
    private String provincia;
    private String codigoPostal;
    private String ciudad;
    private String calle;

    @Min(value = 0, message = "Valor Invalido")
    private Integer altura;

    //[AMENITIES] reemplaza la lista completa (puede venir vacia para sacar todos)
    private List<Long> amenityIds;

    private Long anfitrionId;
}
