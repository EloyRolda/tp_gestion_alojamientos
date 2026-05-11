package GestionAlojamiento.Model;

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
    private Long id;

    @Column(name = "titulo", length = 150, nullable = false)
    private String titulo;

    @Size(max= 3000, message = "El comentario es muy largo")//Indicamos el size maximo que deseamos guardar
    @Column(name = "descripcion", columnDefinition = "TEXT") // ColumnDefinition indica que es un campo tipo Text
    private String descripcion;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    //Indicamos el VALOR MINIMO ES 0 Y NO SE ADMITE NEGATIVOS
    @Column(name = "precio_noche", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio_noche;

    @Min(value = 1, message = "las noches deben ser 1 o mas de 1") //Indicamos que el valor minimo int es 1.
    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @Column(name = "cant_ambientes", nullable = false)
    private Integer cant_ambientes;
    @Column(name = "cant_habitaciones", nullable = false)
    private Integer cant_habitaciones;
    @Column(name = "cant_camas", nullable = false)
    private Integer cant_camas;
    @Column(name = "cant_banios", nullable = false)
    private Integer cant_banios;
    @Column(name = "activo", nullable = false)
    private boolean activo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoInmueble tipoInmueble;

    @ManyToOne
    @MapsId
    @JoinColumn(name = "id_anfitrion")
    private Anfitrion anfitrion;

    @OneToOne
    @JoinColumn(name = "id_direccion")
    private Direccion direccion;

    @OneToOne
    @JoinColumn(name = "id_servicio")
    private Servicio servicio;
}

enum TipoInmueble {CASA, DEPARTAMENTO, HOTEL}