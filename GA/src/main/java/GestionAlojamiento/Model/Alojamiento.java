package GestionAlojamiento.Model;

import GestionAlojamiento.Model.Enums.TipoAlojamiento;
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

/// Tabla UNICA para los 3 tipos de alojamiento (reemplaza a la vieja herencia
/// JOINED con Casa/Departamento/Hotel como tablas separadas). El motivo del
/// cambio: de los 9 campos que antes eran "especificos por subtipo", 7 eran
/// simples booleanos "tiene/no tiene" (patio, pileta, parrilla, ascensor,
/// desayuno, limpieza) que ya duplicaban lo que el catalogo de Amenity hace
/// mejor (modular, sin migraciones para agregar uno nuevo). Sacando esos
/// booleanos, lo unico que quedaba genuinamente distinto por tipo era "piso"
/// (Departamento) y "estrellas" (Hotel): dos columnas nullable no justifican
/// 3 tablas y un JOIN en cada lectura. "tipo" reemplaza al discriminador de
/// herencia; agregar un tipo de alojamiento nuevo a futuro es agregar un
/// valor al enum TipoAlojamiento, sin migraciones de esquema.
@Getter
@Setter
@ToString(exclude = {"amenities"})
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Entity
@Table(name = "alojamiento")
public class Alojamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alojamiento")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, updatable = false, length = 20)
    private TipoAlojamiento tipo;

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

    /// Solo aplica (y es obligatorio) cuando tipo = DEPARTAMENTO. Nullable para los demas tipos.
    @Column(name = "piso")
    private Integer piso;

    /// Solo aplica (y es obligatorio) cuando tipo = HOTEL. Nullable para los demas tipos.
    @Min(value = 0, message = "Valor minimo de 0 estrellas")
    @jakarta.validation.constraints.Max(value = 5, message = "Valor maximo de 5 estrellas")
    @Column(name = "estrellas")
    private Integer estrellas;

    /// Baja logica: la usa el dueno o el admin para "borrar" sin perder historial de reservas/reviews.
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
}
