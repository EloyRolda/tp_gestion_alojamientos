package GestionAlojamiento.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("HOTEL")
@Table(name = "hotel")
public class Hotel extends Alojamiento {

    @Min(value = 0, message = "Valor minimo de 0 estrellas")
    @Max(value = 5, message = "Valor maximo de 5 estrellas")
    @Column(name = "estrellas", nullable = false)
    private Integer estrellas;

    @Column(name = "incluye_desayuno", nullable = false)
    private Boolean incluyeDesayuno;

    @Column(name = "servicio_limpieza", nullable = false)
    private Boolean incluyeLimpieza;
}
