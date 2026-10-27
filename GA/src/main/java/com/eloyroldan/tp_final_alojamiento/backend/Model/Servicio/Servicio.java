package com.eloyroldan.tp_final_alojamiento.backend.Model.Servicio;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/// Catalogo compartido de servicios/comodidades (WiFi, Pileta, Lavarropas, etc).
/// Normalizado como: Primera letra mayuscula, resto minuscula. Si un anfitrion
/// pide un servicio que no existe, se crea; si ya existe (comparacion sin
/// distinguir mayusculas/minusculas), se reutiliza el existente.
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "idServicio")
@ToString(exclude = "alojamientos")
//JPA
@Entity
@Table(name = "servicio")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio")
    private Long idServicio;

    @NotBlank
    @Size(max = 255)
    @Column(name = "nombre_servicio", length = 255, nullable = false, unique = true)
    private String nombreServicio;

    @Builder.Default
    @OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServicioxAlojamiento> alojamientos = new ArrayList<>();
}
