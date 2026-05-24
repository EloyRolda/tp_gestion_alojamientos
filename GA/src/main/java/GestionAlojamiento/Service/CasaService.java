package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.CasaModificarDTO;
import GestionAlojamiento.DTO.CasaRegistroDTO;
import GestionAlojamiento.Model.*;
import GestionAlojamiento.Model.Enums.TipoInmueble;
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
    private final DireccionService direccionService;
    private final ServicioService servicioService;

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

        alojamiento.setTitulo(dto.getTitulo());
        alojamiento.setCantAmbientes(dto.getCantAmbientes());
        alojamiento.setCantBanios(dto.getCantBanios());
        alojamiento.setCantCamas(dto.getCantCamas());
        alojamiento.setCantHabitaciones(dto.getCantHabitaciones());
        alojamiento.setCapacidad(dto.getCapacidad());
        alojamiento.setDescripcion(dto.getDescripcion());
        alojamiento.setPrecioNoche(dto.getPrecioNoche());
        alojamiento.setTipoInmueble(TipoInmueble.CASA);
        alojamiento.setDireccion(direccionService.crear(direccion));


        // [SERVICIO]

        Servicio servicio = new Servicio();

        servicio.setTieneCocina(dto.isTieneCocina());
        servicio.setTieneLavarropa(dto.isTieneLavarropa());
        servicio.setTieneWifi(dto.isTieneWifi());
        servicio.setTieneEstacionamiento(dto.isTieneEstacionamiento());
        servicioService.crear(servicio);
        alojamiento.setServicio(servicio);




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
        Alojamiento alojamiento = casa.getAlojamiento();

        if (dto.getCantAmbientes() != null) {
            alojamiento.setCantAmbientes(dto.getCantAmbientes());
        }

        if (dto.getCantBanios() != null) {
            alojamiento.setCantBanios(dto.getCantBanios());
        }

        if (dto.getCantCamas() != null) {
            alojamiento.setCantCamas(dto.getCantCamas());
        }

        if (dto.getCantHabitaciones() != null) {
            alojamiento.setCantHabitaciones(dto.getCantHabitaciones());
        }

        if (dto.getCapacidad() != null) {
            alojamiento.setCapacidad(dto.getCapacidad());
        }

        if (dto.getDescripcion() != null && !dto.getDescripcion().isBlank()) {
            alojamiento.setDescripcion(dto.getDescripcion());
        }

        if (dto.getPrecioNoche() != null) {
            alojamiento.setPrecioNoche(dto.getPrecioNoche());
        }

        // [DIRECCION]
        if (dto.getPais() != null && !dto.getPais().isBlank()) {
            alojamiento.getDireccion().setPais(dto.getPais());
        }

        if (dto.getProvincia() != null && !dto.getProvincia().isBlank()) {
            alojamiento.getDireccion().setProvincia(dto.getProvincia());
        }

        if (dto.getCodigoPostal() != null && !dto.getCodigoPostal().isBlank()) {
            alojamiento.getDireccion().setCodigoPostal(dto.getCodigoPostal());
        }

        if (dto.getCiudad() != null && !dto.getCiudad().isBlank()) {
            alojamiento.getDireccion().setCiudad(dto.getCiudad());
        }

        if (dto.getCalle() != null && !dto.getCalle().isBlank()) {
            alojamiento.getDireccion().setCalle(dto.getCalle());
        }

        if (dto.getAltura() != null) {
            alojamiento.getDireccion().setAltura(dto.getAltura());
        }

        // [SERVICIO]
        Servicio servicio = alojamiento.getServicio();

        if (servicio == null) {
            servicio = new Servicio();
            alojamiento.setServicio(servicio);
        }

        servicio.setTieneCocina(dto.isTieneCocina());
        servicio.setTieneLavarropa(dto.isTieneLavarropa());
        servicio.setTieneWifi(dto.isTieneWifi());
        servicio.setTieneEstacionamiento(dto.isTieneEstacionamiento());


        return casaRepository.save(casa);
    }

}
