package GestionAlojamiento.Model;

import GestionAlojamiento.Model.Enums.CategoriaAmenity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// Comodidad/servicio modular que puede tener un alojamiento (wifi, cocina, pileta, cochera, etc).
/// Reemplaza al viejo "Servicio" de 4 booleanos fijos: ahora son filas de catalogo,
/// reutilizables entre alojamientos via la relacion muchos-a-muchos en Alojamiento.
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "amenity")
public class Amenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_amenity")
    private Long id;

    @Column(name = "nombre", length = 100, nullable = false, unique = true)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false)
    private CategoriaAmenity categoria;
}
