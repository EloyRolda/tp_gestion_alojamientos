package com.eloyroldan.tp_final_alojamiento.backend.Model.Galeria;

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

/// Imagen individual dentro de una galeria, alojada en Cloudinary.
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "galeria")
//JPA
@Entity
@Table(name = "imagen")
public class Imagen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imagen")
    private Long id;

    @Size(max = 255)
    @Column(name = "alt", length = 255)
    private String alt;

    @NotBlank
    @Size(max = 255)
    @Column(name = "public_id", length = 255, nullable = false)
    private String publicId;

    @NotBlank
    @Size(max = 255)
    @Column(name = "url", length = 255, nullable = false)
    private String url;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_galeria", nullable = false,
            foreignKey = @ForeignKey(name = "fk_imagen_galeria"))
    private Galeria galeria;
}
