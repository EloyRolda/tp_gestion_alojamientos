# Ideas de requisitos funcionales extra

Candidatos para sumar como requisitos adicionales (TFI o lotes futuros). No forman parte del alcance actual.

## Favoritos

**Origen:** recomendación del análisis de diseño del Lote 5. Airbnb y Booking lo ofrecen como "Guardar" o "Lista de deseos".

**Qué es:** el cliente marca alojamientos con un corazón en la tarjeta y después los ve todos juntos en "Mis favoritos".

**Criterio de aceptación propuesto:**
- Un cliente logueado agrega o quita un favorito desde la tarjeta o desde el detalle del alojamiento.
- La lista de favoritos muestra solo alojamientos activos.
- Un mismo alojamiento no se puede guardar dos veces.
- Un visitante sin sesión que toca el corazón va a "Iniciar sesión".

**Cambios necesarios:**
- **Backend:**
  - Entidad `Favorito` (`id_usuario`, `id_alojamiento`, `fecha`), con restricción única sobre el par usuario y alojamiento.
  - `FavoritoService` y su repositorio.
  - Endpoints `GET /Favorito/mios`, `POST /Favorito/{idAlojamiento}` y `DELETE /Favorito/{idAlojamiento}`, solo para el rol cliente.
- **Frontend:**
  - Corazón en la tarjeta de alojamiento.
  - Pantalla "Mis favoritos" en el menú del perfil.
