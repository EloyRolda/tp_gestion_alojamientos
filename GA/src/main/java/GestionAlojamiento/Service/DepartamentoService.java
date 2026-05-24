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
    private final AnfitrionService anfitrionService;
    private final AlojamientoService alojamientoService;

    //---------------------------------------- LISTAR ----------------------------------------

    public List<Departamento> listarTodos() {
        return departamentoRepository.findAll();
    }

    public Departamento obtenerPorId(Long id) {
        return departamentoRepository.findById(id).orElseThrow(() -> new RuntimeException("Error, id no encontrado en la base de datos."));
    }

    //---------------------------------------- CREAR ----------------------------------------
    @Transactional
    public Departamento crear(DepartamentoRegistroDTO dto) {

        Departamento departamento = new Departamento();
        departamento.setTieneAscensor(dto.isTieneAscensor());
        departamento.setExpensasIncluidas(dto.isExpensasIncluidas());
        departamento.setPiso(dto.getPiso());

        departamento.setAlojamiento(mapearAlojamiento(dto));
        return departamentoRepository.save(departamento);
    }
    //---------------------------------------- BORRAR ----------------------------------------
    @Transactional
    public void borrarPorId(Long id_departamento) {
        if (!departamentoRepository.existsById(id_departamento)) {
            throw new IdNoEncontradoException("Error, el id de HOTEL no se encuentra en la base de datos:" + id_departamento);
        }
        departamentoRepository.deleteById(id_departamento);
    }

    //---------------------------------------- MODIFICAR ----------------------------------------
    @Transactional
    public Departamento actualizar(DepartamentoModificarDTO dto) {

        Departamento departamento = obtenerPorId(dto.getId());

        // [DEPARTAMENTO]

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

    /// Mapea los valores de MODIFICAR DTO a un HOTEL.
    private Alojamiento mapearAlojamiento(DepartamentoModificarDTO dto) {

        Direccion direccion = new Direccion(
                null,                   // id
                dto.getPais(),
                dto.getProvincia(),
                dto.getCodigoPostal(),
                dto.getCiudad(),
                dto.getCalle(),
                dto.getAltura()
        );

        Servicio servicio = new Servicio(
                null,                   // id
                dto.getTieneCocina(),
                dto.getTieneLavarropa(),
                dto.getTieneWifi(),
                dto.getTieneEstacionamiento()
        );

        Anfitrion anfitrion = null;
        if (dto.getAnfitrion_id() != null) {
            anfitrion = anfitrionService.obtenerPorId(dto.getAnfitrion_id());
        }
        Alojamiento alojamiento = new Alojamiento(
                null,                   // id
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
        return alojamiento;
    }

    /// Mapea los valores de REGISTRO DTO a un HOTEL.
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
                true,                   // activo por defecto al crear
                TipoInmueble.DEPARTAMENTO,
                anfitrionService.obtenerPorId(dto.getIdAnfitrion()),
                direccion,
                servicio
        );

    }


}
