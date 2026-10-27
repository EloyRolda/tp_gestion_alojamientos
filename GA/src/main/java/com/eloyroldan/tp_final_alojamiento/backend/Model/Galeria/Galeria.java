package com.eloyroldan.tp_final_alojamiento.backend.Model.Galeria;

import com.eloyroldan.tp_final_alojamiento.backend.Model.Alojamiento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/// Galeria de fotos de un alojamiento. La base de datos no impone unicidad
/// sobre id_alojamiento, pero a nivel aplicacion se mantiene la regla de
/// "una galeria por alojamiento" (se valida en el Service).
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "alojamiento")
//JPA
@Entity
@Table(name = "galeria")
public class Galeria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_galeria")
    private Long id;

    @NotBlank
    @Size(max = 256)
    @Column(name = "nombre_galeria", length = 256, nullable = false)
    private String nombreGaleria;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_alojamiento", nullable = false,
            foreignKey = @ForeignKey(name = "fk_galeria_alojamiento"))
    private Alojamiento alojamiento;
}
