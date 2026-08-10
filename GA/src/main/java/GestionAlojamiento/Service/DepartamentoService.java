package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.DepartamentoModificarDTO;
import GestionAlojamiento.DTO.DepartamentoRegistroDTO;
import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Model.Departamento;
import GestionAlojamiento.Model.Direccion;
import GestionAlojamiento.Model.Gallery;
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
    private final AmenityService amenityService;
    private final GalleryService galleryService;

    //---------------------------------------- LISTAR ----------------------------------------
    public List<Departamento> listarTodos() {
        return departamentoRepository.findAll();
    }

    public Departamento obtenerPorId(Long id) {
        return departamentoRepository.findById(id)
                .orElseThrow(() -> new IdNoEncontradoException("Error, id no encontrado en la base de datos."));
    }

    public List<Departamento> listarPorAnfitrion(String correoAnfitrion) {
        return departamentoRepository.findByAnfitrionEmail(correoAnfitrion);
    }

    //---------------------------------------- CREAR ----------------------------------------
    @Transactional
    public Departamento crear(DepartamentoRegistroDTO dto) {

        Departamento departamento = new Departamento();
        departamento.setTieneAscensor(dto.isTieneAscensor());
        departamento.setExpensasIncluidas(dto.isExpensasIncluidas());
        departamento.setPiso(dto.getPiso());

        mapearComun(departamento, dto);
        departamento.setAnfitrion(usuarioService.obtenerAnfitrionPorId(dto.getIdAnfitrion()));
        departamento.setActivo(true);

        Departamento guardado = departamentoRepository.save(departamento);

        Gallery gallery = new Gallery(null, dto.getTitulo(), guardado);
        galleryService.createGallery(gallery);

        return guardado;
    }

    //---------------------------------------- BORRAR (BAJA LOGICA) ----------------------------------------
    @Transactional
    public void borrarPorId(Long idDepartamento) {
        obtenerPorId(idDepartamento);
        alojamientoService.desactivar(idDepartamento);
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

        Departamento cambiosComunes = new Departamento();
        mapearComunModificar(cambiosComunes, dto);
        if (dto.getAnfitrion_id() != null) {
            cambiosComunes.setAnfitrion(usuarioService.obtenerAnfitrionPorId(dto.getAnfitrion_id()));
        }
        if (dto.getActivo() != null) {
            cambiosComunes.setActivo(dto.getActivo());
        }

        alojamientoService.modificarObjeto(departamento, cambiosComunes);

        return departamentoRepository.save(departamento);
    }

    //---------------------------------------- MAPEOS DTO [PRIVADOS] ----------------------------------------

    private void mapearComun(Departamento departamento, DepartamentoRegistroDTO dto) {
        departamento.setTitulo(dto.getTitulo());
        departamento.setDescripcion(dto.getDescripcion());
        departamento.setPrecioNoche(dto.getPrecioNoche());
        departamento.setCapacidad(dto.getCapacidad());
        departamento.setCantAmbientes(dto.getCantAmbientes());
        departamento.setCantHabitaciones(dto.getCantHabitaciones());
        departamento.setCantCamas(dto.getCantCamas());
        departamento.setCantBanios(dto.getCantBanios());
        departamento.setAmenities(amenityService.resolver(dto.getAmenityIds()));

        departamento.setDireccion(new Direccion(
                null,
                dto.getPais().toLowerCase(),
                dto.getProvincia().toLowerCase(),
                dto.getCodigoPostal(),
                dto.getCiudad(),
                dto.getCalle().toLowerCase(),
                dto.getAltura()
        ));
    }

    private void mapearComunModificar(Departamento cambios, DepartamentoModificarDTO dto) {
        cambios.setTitulo(dto.getTitulo());
        cambios.setDescripcion(dto.getDescripcion());
        cambios.setPrecioNoche(dto.getPrecioNoche());
        cambios.setCapacidad(dto.getCapacidad());
        cambios.setCantAmbientes(dto.getCantAmbientes());
        cambios.setCantHabitaciones(dto.getCantHabitaciones());
        cambios.setCantCamas(dto.getCantCamas());
        cambios.setCantBanios(dto.getCantBanios());
        if (dto.getAmenityIds() != null) {
            cambios.setAmenities(amenityService.resolver(dto.getAmenityIds()));
        }

        Direccion direccion = new Direccion();
        direccion.setPais(dto.getPais());
        direccion.setProvincia(dto.getProvincia());
        direccion.setCodigoPostal(dto.getCodigoPostal());
        direccion.setCiudad(dto.getCiudad());
        direccion.setCalle(dto.getCalle());
        direccion.setAltura(dto.getAltura());
        cambios.setDireccion(direccion);
    }
}
