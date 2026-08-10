package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.ReporteRegistroDTO;
import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Exception.ParametroInvalidoException;
import GestionAlojamiento.Model.Alojamiento;
import GestionAlojamiento.Model.Enums.EstadoReporte;
import GestionAlojamiento.Model.Reporte;
import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Repository.ReporteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final ReporteRepository reporteRepository;
    private final UsuarioService usuarioService;
    private final AlojamientoService alojamientoService;

    public List<Reporte> listarTodos() {
        return reporteRepository.findAllByOrderByFechaDesc();
    }

    public List<Reporte> listarPorEstado(EstadoReporte estado) {
        return reporteRepository.findByEstado(estado);
    }

    public Reporte obtenerPorId(Long id) {
        return reporteRepository.findById(id)
                .orElseThrow(() -> new IdNoEncontradoException("Reporte no encontrado: " + id));
    }

    @Transactional
    public Reporte crear(ReporteRegistroDTO dto, String emailReportante) {

        boolean tieneUsuario = dto.getIdUsuarioReportado() != null;
        boolean tieneAlojamiento = dto.getIdAlojamientoReportado() != null;

        if (tieneUsuario == tieneAlojamiento) { // los dos true o los dos false
            throw new ParametroInvalidoException("Tenes que reportar exactamente un usuario O un alojamiento, no ambos ni ninguno.");
        }

        Usuario reportante = usuarioService.obtenerPorEmail(emailReportante);

        Reporte reporte = new Reporte();
        reporte.setReportante(reportante);
        reporte.setDescripcion(dto.getDescripcion());
        reporte.setEstado(EstadoReporte.PENDIENTE);
        reporte.setFecha(LocalDateTime.now());

        if (tieneUsuario) {
            Usuario reportado = usuarioService.obtenerPorId(dto.getIdUsuarioReportado());
            if (reportado.getId().equals(reportante.getId())) {
                throw new ParametroInvalidoException("No podes reportarte a vos mismo.");
            }
            reporte.setUsuarioReportado(reportado);
        } else {
            Alojamiento alojamiento = alojamientoService.obtenerPorId(dto.getIdAlojamientoReportado());
            reporte.setAlojamientoReportado(alojamiento);
        }

        return reporteRepository.save(reporte);
    }

    @Transactional
    public Reporte cambiarEstado(Long idReporte, EstadoReporte nuevoEstado) {
        Reporte reporte = obtenerPorId(idReporte);
        reporte.setEstado(nuevoEstado);
        return reporteRepository.save(reporte);
    }
}
