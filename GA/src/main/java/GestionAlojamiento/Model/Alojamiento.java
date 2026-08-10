package GestionAlojamiento.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/// Clase base de todo alojamiento publicable (Casa, Departamento u Hotel).
/// Usa herencia JPA real (JOINED): cada subtipo tiene su propia tabla con SOLO
/// sus columnas particulares, y comparte esta tabla "alojamiento" para los datos
/// comunes. Esto permite:
///   - Listar/filtrar TODOS los alojamientos mezclados (estilo Airbnb) desde
///     AlojamientoRepository sin tocar las tablas de cada subtipo.
///   - Evitar la logica de mapeo triplicada que existia antes en cada Service.
@Getter
@Setter
@ToString(exclude = {"amenities"})
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_alojamiento", discriminatorType = DiscriminatorType.STRING, length = 20)
@Table(name = "alojamiento")
public abstract class Alojamiento {

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

    /// Baja logica: la usa el dueño o el admin para "borrar" sin perder historial de reservas/reviews.
    @Column(name = "activo")
    private Boolean activo = true;

    /// Un alojamiento recien creado no aparece en el listado publico hasta que
    /// cumple los requisitos minimos para publicarse (precio ya es obligatorio
    /// a nivel de columna, y como minimo 3 fotos en la galeria). Ver AlojamientoService.publicar().
    @Column(name = "publicado", nullable = false)
    private Boolean publicado = false;

    @ManyToOne
    @JoinColumn(name = "id_anfitrion")
    private Usuario anfitrion;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_direccion")
    private Direccion direccion;

    @ManyToMany
    @JoinTable(
            name = "alojamiento_amenity",
            joinColumns = @JoinColumn(name = "id_alojamiento"),
            inverseJoinColumns = @JoinColumn(name = "id_amenity")
    )
    private Set<Amenity> amenities = new HashSet<>();

    /// Nombre de subtipo en base a la clase real (CASA, DEPARTAMENTO, HOTEL), util para el frontend.
    @Transient
    public String getTipo() {
        return this.getClass().getSimpleName().toUpperCase();
    }
}
