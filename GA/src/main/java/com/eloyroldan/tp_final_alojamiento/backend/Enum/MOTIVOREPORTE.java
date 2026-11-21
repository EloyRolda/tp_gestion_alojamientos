/// Motivos fijos de un reporte (RF44). Cada uno tiene su etiqueta en espanol
/// (la que ve el usuario en el select) y el conjunto de categorias a las que
/// aplica, para poder rechazar un motivo que no corresponde al objetivo real.
/// En la base va en minuscula, via MotivoReporteConverter.
package com.eloyroldan.tp_final_alojamiento.backend.Enum;

import java.util.List;
import java.util.Set;

public enum MOTIVOREPORTE {

    // ── Cliente ──
    MAL_COMPORTAMIENTO("Mal comportamiento", Set.of("CLIENTE")),
    DANO_PROPIEDAD("Daño a la propiedad", Set.of("CLIENTE")),
    RESERVAS_SIN_PAGO("Varias reservas sin concretar el pago", Set.of("CLIENTE")),
    MAS_HUESPEDES("Más huéspedes de los acordados", Set.of("CLIENTE")),

    // ── Anfitrion ──
    SIN_RESPUESTA_O_CANCELACION("No respondió o canceló sin motivo", Set.of("ANFITRION")),
    COBRO_POR_FUERA("Cobro por fuera de la plataforma", Set.of("ANFITRION")),
    INFORMACION_FALSA("Información falsa", Set.of("ANFITRION")),

    // ── Cliente y anfitrion ──
    TRATO_OFENSIVO("Trato ofensivo", Set.of("CLIENTE", "ANFITRION")),

    // ── Alojamiento ──
    ALOJAMIENTO_FANTASMA("Alojamiento fantasma (no existe o no está disponible)", Set.of("ALOJAMIENTO")),
    FOTOS_ENGANOSAS("Fotos o descripción engañosas", Set.of("ALOJAMIENTO")),
    UBICACION_INCORRECTA("Ubicación incorrecta", Set.of("ALOJAMIENTO")),
    HIGIENE_SEGURIDAD("Higiene o seguridad", Set.of("ALOJAMIENTO")),

    // ── Cualquier objetivo ──
    OTRO("Otro", Set.of("CLIENTE", "ANFITRION", "ALOJAMIENTO"));

    private final String etiqueta;
    private final Set<String> categorias;

    MOTIVOREPORTE(String etiqueta, Set<String> categorias) {
        this.etiqueta = etiqueta;
        this.categorias = categorias;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    /// Categorias a las que aplica, para el catalogo que arma el formulario.
    public List<String> getAplicaA() {
        return List.copyOf(categorias);
    }

    /// True si este motivo corresponde a la categoria indicada
    /// (CLIENTE, ANFITRION o ALOJAMIENTO).
    public boolean aplicaA(String categoria) {
        return categoria != null && categorias.contains(categoria.toUpperCase());
    }
}
