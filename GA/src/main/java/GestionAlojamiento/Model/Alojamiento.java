package GestionAlojamiento.Model;

import GestionAlojamiento.Model.Enums.TipoInmueble;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
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
//JPA
@Entity
@Table(name = "alojamiento")
public class Alojamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alojamiento")
    private Long id;

    @Column(name = "titulo", length = 150, nullable = false)
    private String titulo;

    @Size(max = 3000)
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "precio_noche", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioNoche;

    @Min(value = 1)
    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @Column(name = "cant_ambientes", nullable = false)
    private Integer cantAmbientes;

    @Column(name = "cant_habitaciones", nullable = false)
    private Integer cantHabitaciones;

    @Column(name = "cant_camas", nullable = false)
    private Integer cantCamas;

    @Column(name = "cant_banios", nullable = false)
    private Integer cantBanios;

    @Column(name = "activo", nullable = false)
    private boolean activo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoInmueble tipoInmueble;

    @ManyToOne
    @JoinColumn(name = "id_anfitrion")
    private Anfitrion anfitrion;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_direccion")
    private Direccion direccion;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_servicio")
    private Servicio servicio;

}
