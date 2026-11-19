package com.eloyroldan.tp_final_alojamiento.backend.Model.Servicio;

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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/// Catalogo compartido de servicios/comodidades (WiFi, Pileta, Lavarropas, etc).
/// Normalizado como: Primera letra mayuscula, resto minuscula. Si un anfitrion
/// pide un servicio que no existe, se crea; si ya existe (comparacion sin
/// distinguir mayusculas/minusculas), se reutiliza el existente.
/// Sin coleccion inversa de ServicioxAlojamiento a proposito: la tabla puente la
/// maneja solo ServicioxAlojamientoService. Antes habia una con cascade ALL +
/// orphanRemoval y, al fusionar dos servicios, borrar el duplicado arrastraba
/// tambien los vinculos recien movidos al correcto (el alojamiento se quedaba
/// sin el servicio).
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "idServicio")
@ToString
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
}
