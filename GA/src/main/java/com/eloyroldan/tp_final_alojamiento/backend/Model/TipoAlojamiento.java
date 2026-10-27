package com.eloyroldan.tp_final_alojamiento.backend.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/// Catalogo dinamico de tipos de alojamiento (Casa, Departamento, Hotel, etc).
/// Si un anfitrion no encuentra el tipo que necesita, puede darlo de alta
/// (misma logica de normalizacion y de "usar si existe, crear si no" que Servicio).
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//JPA
@Entity
@Table(name = "tipo_alojamiento")
public class TipoAlojamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_alojamiento")
    private Long idTipoAlojamiento;

    /// Normalizado como: Primera letra mayuscula, resto minuscula. Ej: "Casa de campo".
    @NotBlank
    @Size(max = 100)
    @Column(name = "nombre_tipo", length = 100, nullable = false, unique = true)
    private String nombreTipo;
}
