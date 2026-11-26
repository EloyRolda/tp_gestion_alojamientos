package com.eloyroldan.tp_final_alojamiento.backend.Evento;

/// Evento publicado cuando una publicación vuelve a estar visible.
public record PublicacionRepublicada(Long idAnfitrion, Long idAlojamiento, String tituloAlojamiento) {}
