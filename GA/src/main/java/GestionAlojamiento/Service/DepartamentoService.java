package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.DepartamentoModificarDTO;
import GestionAlojamiento.DTO.DepartamentoRegistroDTO;
import GestionAlojamiento.Model.*;
import GestionAlojamiento.Repository.AlojamientoRepository;
import GestionAlojamiento.Repository.DepartamentoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;
    private final AnfitrionService anfitrionService;
    private final DireccionService direccionService;

    //------------------------ LISTAR ------------------------
    public List<Departamento> listarTodos() {
        return departamentoRepository.findAll();
    }

    public Departamento obtenerPorId(Long id) {
        return departamentoRepository.findById(id).orElseThrow(() -> new RuntimeException("Error, id no encontrado en la base de datos."));
    }

    public List<Departamento> listarPorTiene_Ascensor() {
        return departamentoRepository.findByTieneAscensorTrue();
    }

    public List<Departamento> listarPorExpensas_Incluidas() {
        return departamentoRepository.findByExpensasIncluidasTrue();
    }

    //------------------------ CREAR/BORRAR ------------------------
    @Transactional
    public Departamento crear(DepartamentoRegistroDTO departamentoRegistroDTO) {

        Alojamiento alojamiento = new Alojamiento();

        alojamiento.setCantAmbientes(departamentoRegistroDTO.getCantAmbientes());
        alojamiento.setCantBanios(departamentoRegistroDTO.getCantBanios());
        alojamiento.setCantCamas(departamentoRegistroDTO.getCantCamas());
        alojamiento.setCantHabitaciones(departamentoRegistroDTO.getCantHabitaciones());
        alojamiento.setCapacidad(departamentoRegistroDTO.getCapacidad());
        alojamiento.setDescripcion(departamentoRegistroDTO.getDescripcion());
        alojamiento.setPrecioNoche(departamentoRegistroDTO.getPrecioNoche());

        alojamiento.setAnfitrion(
                anfitrionService.obtenerPorId(
                        departamentoRegistroDTO.getIdAnfitrion()
                )
        );


        // [DIRECCION]

        Direccion direccion = new Direccion();

        direccion.setPais(departamentoRegistroDTO.getPais());
        direccion.setProvincia(departamentoRegistroDTO.getProvincia());
        direccion.setCodigoPostal(departamentoRegistroDTO.getCodigoPostal());
        direccion.setCiudad(departamentoRegistroDTO.getCiudad());
        direccion.setCalle(departamentoRegistroDTO.getCalle());
        direccion.setAltura(departamentoRegistroDTO.getAltura());

        alojamiento.setDireccion(
                direccionService.crear(direccion)
        );


        // [SERVICIO]

        Servicio servicio = new Servicio();

        servicio.setTieneCocina(departamentoRegistroDTO.isTieneCocina());
        servicio.setTieneLavarropa(departamentoRegistroDTO.isTieneLavarropa());
        servicio.setTieneWifi(departamentoRegistroDTO.isTieneWifi());
        servicio.setTieneEstacionamiento(departamentoRegistroDTO.isTieneEstacionamiento());

        alojamiento.setServicio(servicio);


        // [DISPONIBILIDAD]

        Disponibilidad disponibilidad = new Disponibilidad();

        disponibilidad.setFecha(departamentoRegistroDTO.getFecha());
        disponibilidad.setDisponible(departamentoRegistroDTO.isDisponible());

        alojamiento.setDisponibilidad(disponibilidad);


        // [DEPARTAMENTO]

        Departamento departamento = new Departamento();

        departamento.setTieneAscensor(
                departamentoRegistroDTO.isTieneAscensor()
        );

        departamento.setExpensasIncluidas(
                departamentoRegistroDTO.isExpensasIncluidas()
        );

        departamento.setPiso(
                departamentoRegistroDTO.getPiso()
        );

        departamento.setAlojamiento(alojamiento);


        return departamentoRepository.save(departamento);
    }

    @Transactional
    public void borrarPorId(Long id_departamento) {
        if (!departamentoRepository.existsById(id_departamento)) {
            throw new RuntimeException("Error, el id alojamiento no se encuentra en la base de datos.");
        }
        departamentoRepository.deleteById(id_departamento);
    }

    //------------------------ MODIFICAR ------------------------

    @Transactional
    public Departamento actualizar(DepartamentoModificarDTO dto) {

        Departamento departamento = obtenerPorId(dto.getId());

        // [DEPARTAMENTO]

        if (dto.getPiso() != null) {
            departamento.setPiso(dto.getPiso());
        }

        departamento.setTieneAscensor(dto.isTieneAscensor());

        departamento.setExpensasIncluidas(dto.isExpensasIncluidas());


        // [ANFITRION]

        if (dto.getIdAnfitrion() != null) {

            departamento.getAlojamiento().setAnfitrion(
                    anfitrionService.obtenerPorId(dto.getIdAnfitrion())
            );
        }


        // [ALOJAMIENTO]

        if (dto.getCantAmbientes() != null) {
            departamento.getAlojamiento().setCantAmbientes(dto.getCantAmbientes());
        }

        if (dto.getCantBanios() != null) {
            departamento.getAlojamiento().setCantBanios(dto.getCantBanios());
        }

        if (dto.getCantCamas() != null) {
            departamento.getAlojamiento().setCantCamas(dto.getCantCamas());
        }

        if (dto.getCantHabitaciones() != null) {
            departamento.getAlojamiento().setCantHabitaciones(dto.getCantHabitaciones());
        }

        if (dto.getCapacidad() != null) {
            departamento.getAlojamiento().setCapacidad(dto.getCapacidad());
        }

        if (dto.getDescripcion() != null &&
                !dto.getDescripcion().isBlank()) {

            departamento.getAlojamiento()
                    .setDescripcion(dto.getDescripcion());
        }

        if (dto.getPrecioNoche() != null) {
            departamento.getAlojamiento()
                    .setPrecioNoche(dto.getPrecioNoche());
        }


        // [DIRECCION]

        if (dto.getPais() != null &&
                !dto.getPais().isBlank()) {

            departamento.getAlojamiento()
                    .getDireccion()
                    .setPais(dto.getPais());
        }

        if (dto.getProvincia() != null &&
                !dto.getProvincia().isBlank()) {

            departamento.getAlojamiento()
                    .getDireccion()
                    .setProvincia(dto.getProvincia());
        }

        if (dto.getCodigoPostal() != null &&
                !dto.getCodigoPostal().isBlank()) {

            departamento.getAlojamiento()
                    .getDireccion()
                    .setCodigoPostal(dto.getCodigoPostal());
        }

        if (dto.getCiudad() != null &&
                !dto.getCiudad().isBlank()) {

            departamento.getAlojamiento()
                    .getDireccion()
                    .setCiudad(dto.getCiudad());
        }

        if (dto.getCalle() != null &&
                !dto.getCalle().isBlank()) {

            departamento.getAlojamiento()
                    .getDireccion()
                    .setCalle(dto.getCalle());
        }

        if (dto.getAltura() != null) {

            departamento.getAlojamiento()
                    .getDireccion()
                    .setAltura(dto.getAltura());
        }


        // [SERVICIO]

        if (departamento.getAlojamiento().getServicio() == null) {

            Servicio servicio = new Servicio();

            servicio.setTieneCocina(dto.isTieneCocina());
            servicio.setTieneLavarropa(dto.isTieneLavarropa());
            servicio.setTieneWifi(dto.isTieneWifi());
            servicio.setTieneEstacionamiento(dto.isTieneEstacionamiento());

            departamento.getAlojamiento().setServicio(servicio);

        } else {

            departamento.getAlojamiento().getServicio()
                    .setTieneCocina(dto.isTieneCocina());

            departamento.getAlojamiento().getServicio()
                    .setTieneLavarropa(dto.isTieneLavarropa());

            departamento.getAlojamiento().getServicio()
                    .setTieneWifi(dto.isTieneWifi());

            departamento.getAlojamiento().getServicio()
                    .setTieneEstacionamiento(dto.isTieneEstacionamiento());
        }


        // [DISPONIBILIDAD]

        if (departamento.getAlojamiento().getDisponibilidad() == null) {

            Disponibilidad disponibilidad = new Disponibilidad();

            disponibilidad.setFecha(dto.getFecha());
            disponibilidad.setDisponible(dto.isDisponible());

            departamento.getAlojamiento()
                    .setDisponibilidad(disponibilidad);

        } else {

            if (dto.getFecha() != null) {

                departamento.getAlojamiento()
                        .getDisponibilidad()
                        .setFecha(dto.getFecha());
            }

            departamento.getAlojamiento()
                    .getDisponibilidad()
                    .setDisponible(dto.isDisponible());
        }


        return departamentoRepository.save(departamento);
    }
}
