/// Un reporte que hace un usuario sobre una publicacion o sobre otra persona
/// (RF44). El objetivo no tiene FK a proposito: id_objetivo apunta a un
/// alojamiento o a un usuario segun `tipo`, y quien lo validó es ReporteService.
/// Sin colecciones inversas en Usuario: los reportes se consultan desde
/// ReporteRepository.
package com.eloyroldan.tp_final_alojamiento.backend.Model;

import com.eloyroldan.tp_final_alojamiento.backend.Converter.AccionModeracionConverter;
import com.eloyroldan.tp_final_alojamiento.backend.Converter.EstadoReporteConverter;
import com.eloyroldan.tp_final_alojamiento.backend.Converter.MotivoReporteConverter;
import com.eloyroldan.tp_final_alojamiento.backend.Converter.TipoReporteConverter;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.ACCIONMODERACION;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADOREPORTE;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.MOTIVOREPORTE;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPOREPORTE;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "idReporte")
@ToString(exclude = {"autor"})
//JPA
@Entity
@Table(name = "reporte", indexes = {
        @Index(name = "idx_reporte_objetivo", columnList = "tipo, id_objetivo"),
        @Index(name = "idx_reporte_estado", columnList = "estado")
})
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private Long idReporte;

    /// Quien hace el reporte. Necesario tambien para la regla de "un reporte
    /// pendiente por objetivo y por autor".
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_autor", nullable = false)
    private Usuario autor;

    @Convert(converter = TipoReporteConverter.class)
    @Column(name = "tipo", nullable = false, length = 20)
    private TIPOREPORTE tipo;

    /// Puede ser un alojamiento o un usuario segun `tipo`: por eso no lleva FK.
    @Column(name = "id_objetivo", nullable = false)
    private Long idObjetivo;

    @Convert(converter = MotivoReporteConverter.class)
    @Column(name = "motivo", nullable = false, length = 40)
    private MOTIVOREPORTE motivo;

    /// Obligatorio solo cuando el motivo es OTRO (lo valida ReporteService).
    @Column(name = "detalle", length = 500)
    private String detalle;

    @Convert(converter = EstadoReporteConverter.class)
    @Column(name = "estado", nullable = false, length = 20)
    private ESTADOREPORTE estado;

    /// La completa la moderacion (SCRUM-79); null mientras el reporte esta PENDIENTE.
    @Convert(converter = AccionModeracionConverter.class)
    @Column(name = "accion_tomada", length = 30)
    private ACCIONMODERACION accionTomada;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;
}
