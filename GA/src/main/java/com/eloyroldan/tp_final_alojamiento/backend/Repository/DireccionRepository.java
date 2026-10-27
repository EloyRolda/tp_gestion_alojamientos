package com.eloyroldan.tp_final_alojamiento.backend.Repository;

import com.eloyroldan.tp_final_alojamiento.backend.Model.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Long> {
    /// Permite a un anfitrion encontrar una direccion ya cargada (ej: un predio con
    /// varias cabanias) para reutilizarla en lugar de crear una nueva.
    List<Direccion> findByCalleIgnoreCaseAndAlturaAndCiudadIgnoreCase(String calle, Integer altura, String ciudad);
}
