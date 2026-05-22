package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.CasaModificarDTO;
import GestionAlojamiento.DTO.CasaRegistroDTO;
import GestionAlojamiento.Model.*;
import GestionAlojamiento.Repository.AlojamientoRepository;
import GestionAlojamiento.Repository.CasaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CasaService {

    private final CasaRepository casaRepository;
    private final AnfitrionService anfitrionService;
    private  final DireccionService direccionService;

    //------------------------ LISTAR ------------------------
    public List<Casa> listar() {
        return casaRepository.findAll();
    }

    public Casa obtenerPorId(Long id) {
        return casaRepository.findById(id).orElseThrow(() -> new RuntimeException("Error, el id de la casa no se encuentra en la base de datos."));
    }

    public List<Casa> listarPorPatio() {
        return casaRepository.findByTienePatioTrue();
    }

    public List<Casa> listarPorParrilla() {
        return casaRepository.findByTieneParrillaTrue();
    }

    public List<Casa> listarPorPileta() {
        return casaRepository.findByTienePiletaTrue();
    }

    /*
    Deberian haber combos? ej parrilla y pileta? pileta y patio?
    */
    //------------------------ CREAR/BORRAR ------------------------
    @Transactional
    public Casa crear(CasaRegistroDTO dto) {

        Casa casa = new Casa();

        // [CASA]

        casa.setTienePatio(dto.isTienePatio());
        casa.setTienePileta(dto.isTienePileta());
        casa.setTieneParrilla(dto.isTieneParrilla());


        // [DIRECCION]

        Direccion direccion = new Direccion();

        direccion.setPais(dto.getPais());
        direccion.setProvincia(dto.getProvincia());
        direccion.setCodigoPostal(dto.getCodigoPostal());
        direccion.setCiudad(dto.getCiudad());
        direccion.setCalle(dto.getCalle());
        direccion.setAltura(dto.getAltura());


        // [ALOJAMIENTO]

        Alojamiento alojamiento = new Alojamiento();

        alojamiento.setAnfitrion(
                anfitrionService.obtenerPorId(dto.getIdAnfitrion())
        );

        alojamiento.setCantAmbientes(dto.getCantAmbientes());
        alojamiento.setCantBanios(dto.getCantBanios());
        alojamiento.setCantCamas(dto.getCantCamas());
        alojamiento.setCantHabitaciones(dto.getCantHabitaciones());
        alojamiento.setCapacidad(dto.getCapacidad());
        alojamiento.setDescripcion(dto.getDescripcion());
        alojamiento.setPrecioNoche(dto.getPrecioNoche());

        alojamiento.setDireccion(
                direccionService.crear(direccion)
        );


        // [SERVICIO]

        Servicio servicio = new Servicio();

        servicio.setTieneCocina(dto.isTieneCocina());
        servicio.setTieneLavarropa(dto.isTieneLavarropa());
        servicio.setTieneWifi(dto.isTieneWifi());
        servicio.setTieneEstacionamiento(dto.isTieneEstacionamiento());

        alojamiento.setServicio(servicio);


        // [DISPONIBILIDAD]

        Disponibilidad disponibilidad = new Disponibilidad();

        disponibilidad.setFecha(dto.getFecha());
        disponibilidad.setDisponible(dto.isDisponible());

        alojamiento.setDisponibilidad(disponibilidad);


        // [RELACION]

        casa.setAlojamiento(alojamiento);


        return casaRepository.save(casa);
    }
    @Transactional
    public void borrarPorId(Long id) {
        if (!casaRepository.existsById(id)) {
            throw new RuntimeException("Error, el id de la casa no se encuentra en la base de datos.");
        }
        casaRepository.deleteById(id);
    }

    //------------------------ MODIFICAR ------------------------
    @Transactional
    public Casa modificar(CasaModificarDTO dto) {

        Casa casa = obtenerPorId(dto.getIdCasa());

        // [CASA]

        if (dto.getTienePatio() != null) {
            casa.setTienePatio(dto.getTienePatio());
        }

        if (dto.getTienePileta() != null) {
            casa.setTienePileta(dto.getTienePileta());
        }

        if (dto.getTieneParrilla() != null) {
            casa.setTieneParrilla(dto.getTieneParrilla());
        }


        // [ALOJAMIENTO]

        if (dto.getCantAmbientes() != null) {
            casa.getAlojamiento().setCantAmbientes(dto.getCantAmbientes());
        }

        if (dto.getCantBanios() != null) {
            casa.getAlojamiento().setCantBanios(dto.getCantBanios());
        }

        if (dto.getCantCamas() != null) {
            casa.getAlojamiento().setCantCamas(dto.getCantCamas());
        }

        if (dto.getCantHabitaciones() != null) {
            casa.getAlojamiento().setCantHabitaciones(dto.getCantHabitaciones());
        }

        if (dto.getCapacidad() != null) {
            casa.getAlojamiento().setCapacidad(dto.getCapacidad());
        }

        if (dto.getDescripcion() != null && !dto.getDescripcion().isBlank()) {
            casa.getAlojamiento().setDescripcion(dto.getDescripcion());
        }

        if (dto.getPrecioNoche() != null) {
            casa.getAlojamiento().setPrecioNoche(dto.getPrecioNoche());
        }


        // [DIRECCION]

        if (dto.getPais() != null && !dto.getPais().isBlank()) {
            casa.getAlojamiento().getDireccion().setPais(dto.getPais());
        }

        if (dto.getProvincia() != null && !dto.getProvincia().isBlank()) {
            casa.getAlojamiento().getDireccion().setProvincia(dto.getProvincia());
        }

        if (dto.getCodigoPostal() != null && !dto.getCodigoPostal().isBlank()) {
            casa.getAlojamiento().getDireccion().setCodigoPostal(dto.getCodigoPostal());
        }

        if (dto.getCiudad() != null && !dto.getCiudad().isBlank()) {
            casa.getAlojamiento().getDireccion().setCiudad(dto.getCiudad());
        }

        if (dto.getCalle() != null && !dto.getCalle().isBlank()) {
            casa.getAlojamiento().getDireccion().setCalle(dto.getCalle());
        }

        if (dto.getAltura() != null) {
            casa.getAlojamiento().getDireccion().setAltura(dto.getAltura());
        }


        // [SERVICIO]

        if (casa.getAlojamiento().getServicio() == null) {

            Servicio servicio = new Servicio();

            servicio.setTieneCocina(dto.isTieneCocina());
            servicio.setTieneLavarropa(dto.isTieneLavarropa());
            servicio.setTieneWifi(dto.isTieneWifi());
            servicio.setTieneEstacionamiento(dto.isTieneEstacionamiento());

            casa.getAlojamiento().setServicio(servicio);

        } else {

            casa.getAlojamiento().getServicio()
                    .setTieneCocina(dto.isTieneCocina());

            casa.getAlojamiento().getServicio()
                    .setTieneLavarropa(dto.isTieneLavarropa());

            casa.getAlojamiento().getServicio()
                    .setTieneWifi(dto.isTieneWifi());

            casa.getAlojamiento().getServicio()
                    .setTieneEstacionamiento(dto.isTieneEstacionamiento());
        }


// [DISPONIBILIDAD]

        if (casa.getAlojamiento().getDisponibilidad() == null) {

            Disponibilidad disponibilidad = new Disponibilidad();

            disponibilidad.setFecha(dto.getFecha());
            disponibilidad.setDisponible(dto.isDisponible());

            casa.getAlojamiento().setDisponibilidad(disponibilidad);

        } else {

            if (dto.getFecha() != null) {

                casa.getAlojamiento().getDisponibilidad().setFecha(dto.getFecha());
            }

            casa.getAlojamiento().getDisponibilidad().setDisponible(dto.isDisponible());
        }


        return casaRepository.save(casa);
    }

}
