package com.eloyroldan.tp_final_alojamiento.backend.Repository.Galeria;

import com.eloyroldan.tp_final_alojamiento.backend.Model.Galeria.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen, Long> {

    List<Imagen> findByGaleria_Id(Long idGaleria);

    /// Usado al borrar una galeria completa (ver GaleriaService.borrarPorAlojamientoId).
    void deleteByGaleria_Id(Long idGaleria);
}
