package com.eloyroldan.tp_final_alojamiento.backend.Service;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.AlojamientoResponseDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.ParametroInvalidoException;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Alojamiento;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Favorito;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Usuario;
import com.eloyroldan.tp_final_alojamiento.backend.Repository.FavoritoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/// Guarda y quita alojamientos favoritos de un cliente, y arma el listado de esos
/// alojamientos como DTO. Usa solo su propio repositorio: los datos de Alojamiento
/// los pide al AlojamientoService (nunca a su repositorio).
/// Como AlojamientoService depende de este Service para limpiar los favoritos al
/// borrar un alojamiento, esa referencia de vuelta se inyecta con @Lazy alla.
@Service
@RequiredArgsConstructor
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final AlojamientoService alojamientoService;

    //---------------------------------------- LISTAR ----------------------------------------

    /// Alojamientos favoritos del usuario, del mas reciente al mas viejo y solo
    /// activos. Los ids salen de una consulta propia y los DTOs se arman en una
    /// sola consulta mas (sin N+1).
    @Transactional
    public List<AlojamientoResponseDTO> listarMiosDTO(Long idUsuario) {
        List<Long> ids = favoritoRepository.idsAlojamientosFavoritos(idUsuario);
        if (ids.isEmpty()) {
            return List.of();
        }
        return alojamientoService.listarPorIdsDTO(ids);
    }

    /// Solo los ids, para pintar los corazones en el frontend sin traer todo.
    public List<Long> idsDeMisFavoritos(Long idUsuario) {
        return favoritoRepository.idsAlojamientosFavoritos(idUsuario);
    }

    //---------------------------------------- GUARDAR / QUITAR ----------------------------------------

    /// Idempotente: si el alojamiento ya estaba guardado no hace nada (no duplica
    /// ni da error). Un alojamiento inexistente lo rechaza AlojamientoService con
    /// IdNoEncontradoException; uno inactivo, con ParametroInvalidoException.
    @Transactional
    public void guardar(Usuario usuario, Long idAlojamiento) {
        Alojamiento alojamiento = alojamientoService.obtenerPorId(idAlojamiento);
        if (!Boolean.TRUE.equals(alojamiento.getActivo())) {
            throw new ParametroInvalidoException(
                    "No se puede guardar como favorito un alojamiento que no esta activo.");
        }
        if (favoritoRepository.existsByUsuario_IdAndAlojamiento_IdAlojamiento(usuario.getId(), idAlojamiento)) {
            return;
        }
        favoritoRepository.save(Favorito.builder()
                .usuario(usuario)
                .alojamiento(alojamiento)
                .build());
    }

    /// Idempotente: si no estaba guardado no hace nada y responde igual.
    @Transactional
    public void quitar(Long idUsuario, Long idAlojamiento) {
        favoritoRepository.deleteByUsuario_IdAndAlojamiento_IdAlojamiento(idUsuario, idAlojamiento);
    }

    //---------------------------------------- USO INTERNO (AlojamientoService) ----------------------------------------

    /// Usado por AlojamientoService.borrarPorId antes de borrar el alojamiento:
    /// la FK de favorito no tiene cascada, asi que los vinculos se limpian aca.
    @Transactional
    public void borrarPorAlojamientoId(Long idAlojamiento) {
        favoritoRepository.deleteByAlojamiento_IdAlojamiento(idAlojamiento);
    }
}
