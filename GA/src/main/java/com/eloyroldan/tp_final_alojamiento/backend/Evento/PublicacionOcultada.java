package com.eloyroldan.tp_final_alojamiento.backend.Evento;

/// Evento publicado cuando una publicación es ocultada por moderación.
public record PublicacionOcultada(Long idAnfitrion, Long idAlojamiento, String tituloAlojamiento, String etiquetaMotivo) {}
