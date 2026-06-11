package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.DepartamentoModificarDTO;
import GestionAlojamiento.DTO.DepartamentoRegistroDTO;
import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Model.*;
import GestionAlojamiento.Model.Enums.TipoInmueble;
import GestionAlojamiento.Repository.DepartamentoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;
    private final UsuarioService usuarioService;
    private final AlojamientoService alojamientoService;
    private final GalleryService galleryService;
    private final ReservaService reservaService;
    private final ReviewService reviewService;

    //---------------------------------------- LISTAR ----------------------------------------
    public List<Departamento> listarTodos() {
        return departamentoRepository.findAll();
    }

    public Departamento obtenerPorId(Long id) {
        return departamentoRepository.findById(id).orElseThrow(() -> new IdNoEncontradoException("Error, id no encontrado en la base de datos."));
    }

    /// Recibe un id de un anfitrion y devuelve una lista de los hoteles que posee.
    public List<Departamento> listarPorAnfitrion(String correoAnfitrion) {
        return departamentoRepository.findByAlojamientoAnfitrionEmail(correoAnfitrion);
    }

    //---------------------------------------- CREAR ----------------------------------------
    @Transactional
    public Departamento crear(DepartamentoRegistroDTO dto) {

        Departamento departamento = new Departamento();
        departamento.setTieneAscensor(dto.isTieneAscensor());
        departamento.setExpensasIncluidas(dto.isExpensasIncluidas());
        departamento.setPiso(dto.getPiso());

        departamento.setAlojamiento(mapearAlojamiento(dto));

        Gallery gallery = new Gallery(null, dto.getTitulo(), departamento.getAlojamiento());
        galleryService.createGallery(gallery);

        return departamentoRepository.save(departamento);
    }

    //---------------------------------------- BORRAR ----------------------------------------
    @Transactional
    public void borrarPorId(Long id_departamento) {
        Departamento departamento = departamentoRepository.findById(id_departamento)
                .orElseThrow(() -> new IdNoEncontradoException("Error, el id de DEPARTAMENTO no se encuentra en la base de datos:" + id_departamento));
        Alojamiento alojamiento = departamento.getAlojamiento();
        reservaService.borrarPorAlojamientoId(alojamiento.getId());
        reviewService.borrarPorAlojamiento(alojamiento);
        galleryService.borrarPorAlojamientoId(alojamiento.getId());
        departamentoRepository.deleteById(id_departamento);
    }

    //---------------------------------------- MODIFICAR ----------------------------------------
    @Transactional
    public Departamento actualizar(DepartamentoModificarDTO dto) {

        Departamento departamento = obtenerPorId(dto.getId());

        if (dto.getPiso() != null) {
            departamento.setPiso(dto.getPiso());
        }
        if (dto.getTieneAscensor() != null) {
            departamento.setTieneAscensor(dto.getTieneAscensor());
        }
        if (dto.getExpensasIncluidas() != null) {
            departamento.setExpensasIncluidas(dto.getExpensasIncluidas());
        }

        departamento.setAlojamiento(alojamientoService.modificarObjeto(departamento.getAlojamiento(), mapearAlojamiento(dto)));

        return departamentoRepository.save(departamento);
    }

    //---------------------------------------- MAPEOS DTO [PRIVADOS] ----------------------------------------

    private Alojamiento mapearAlojamiento(DepartamentoModificarDTO dto) {

        Direccion direccion = new Direccion(
                null,
                dto.getPais(),
                dto.getProvincia(),
                dto.getCodigoPostal(),
                dto.getCiudad(),
                dto.getCalle(),
                dto.getAltura()
        );

        Servicio servicio = new Servicio(
                null,
                dto.getTieneCocina(),
                dto.getTieneLavarropa(),
                dto.getTieneWifi(),
                dto.getTieneEstacionamiento()
        );

        Usuario anfitrion = null;
        if (dto.getAnfitrion_id() != null) {
            anfitrion = usuarioService.obtenerAnfitrionPorId(dto.getAnfitrion_id());
        }

        return new Alojamiento(
                null,
                dto.getTitulo(),
                dto.getDescripcion(),
                dto.getPrecioNoche(),
                dto.getCapacidad(),
                dto.getCantAmbientes(),
                dto.getCantHabitaciones(),
                dto.getCantCamas(),
                dto.getCantBanios(),
                dto.getActivo(),
                TipoInmueble.DEPARTAMENTO,
                anfitrion,
                direccion,
                servicio
        );
    }

    private Alojamiento mapearAlojamiento(DepartamentoRegistroDTO dto) {

        Direccion direccion = new Direccion(
                null,
                dto.getPais(),
                dto.getProvincia(),
                dto.getCodigoPostal(),
                dto.getCiudad(),
                dto.getCalle(),
                dto.getAltura()
        );

        Servicio servicio = new Servicio(
                null,
                dto.isTieneCocina(),
                dto.isTieneLavarropa(),
                dto.isTieneWifi(),
                dto.isTieneEstacionamiento()
        );

        return new Alojamiento(
                null,
                dto.getTitulo(),
                dto.getDescripcion(),
                dto.getPrecioNoche(),
                dto.getCapacidad(),
                dto.getCantAmbientes(),
                dto.getCantHabitaciones(),
                dto.getCantCamas(),
                dto.getCantBanios(),
                true,
                TipoInmueble.DEPARTAMENTO,
                usuarioService.obtenerAnfitrionPorId(dto.getIdAnfitrion()),
                direccion,
                servicio
        );
    }
}