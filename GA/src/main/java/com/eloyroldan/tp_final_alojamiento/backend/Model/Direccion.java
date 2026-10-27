package com.eloyroldan.tp_final_alojamiento.backend.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/// Direccion fisica de un alojamiento. Puede ser compartida por varios
/// alojamientos (ej: un camping con un unico ingreso para varias cabanias),
/// por lo que la relacion desde Alojamiento es de tipo ManyToOne.
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//JPA
@Entity
@Table(name = "direccion")
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion")
    private Long idDireccion;

    @NotNull
    @Column(name = "altura", nullable = false)
    private Integer altura;

    @NotBlank
    @Size(max = 30)
    @Column(name = "calle", length = 30, nullable = false)
    private String calle;

    @NotBlank
    @Size(max = 100)
    @Column(name = "ciudad", length = 100, nullable = false)
    private String ciudad;

    @NotBlank
    @Size(max = 10)
    @Column(name = "codigo_postal", length = 10, nullable = false)
    private String codigoPostal;

    @NotBlank
    @Size(max = 100)
    @Column(name = "pais", length = 100, nullable = false)
    private String pais;

    @NotBlank
    @Size(max = 100)
    @Column(name = "provincia", length = 100, nullable = false)
    private String provincia;
}
