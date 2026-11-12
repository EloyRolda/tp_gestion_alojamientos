package com.eloyroldan.tp_final_alojamiento.backend.Repository.Galeria;

import com.eloyroldan.tp_final_alojamiento.backend.Model.Galeria.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen, Long> {

    List<Imagen> findByGaleria_Id(Long idGaleria);

    /// Usado al borrar una galeria completa (ver GaleriaService.borrarPorAlojamientoId).
    void deleteByGaleria_Id(Long idGaleria);

    /// Primera imagen subida (id mas bajo) de la galeria de cada alojamiento pedido,
    /// en una sola consulta. Cada fila es [idAlojamiento, url].
    @Query("""
            SELECT g.alojamiento.idAlojamiento, i.url
            FROM Imagen i JOIN i.galeria g
            WHERE g.alojamiento.idAlojamiento IN :idsAlojamiento
              AND i.id = (SELECT MIN(i2.id) FROM Imagen i2 WHERE i2.galeria = g)
            """)
    List<Object[]> findPrimeraUrlPorAlojamiento(@Param("idsAlojamiento") Collection<Long> idsAlojamiento);
}
