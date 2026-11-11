/// Configuracion de ejecucion del frontend: se lee al cargar la pagina, ANTES de
/// Angular, asi que se puede cambiar en el servidor sin recompilar.
/// - Desarrollo (ng serve): el backend corre aparte en http://localhost:8081.
/// - Produccion detras de Nginx: usar "/api" (Nginx reenvia /api/ al backend).
window.__ALOJAME_CONFIG__ = {
  apiUrl: 'http://localhost:8081'
};
