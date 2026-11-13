/// URL base del backend real (Spring Boot): sin mocks, sin json-server.
/// Se toma de public/env.js (window.__ALOJAME_CONFIG__.apiUrl), que se edita en el
/// servidor sin recompilar; si no esta definido, cae en el backend local de desarrollo.
const config = (globalThis as { __ALOJAME_CONFIG__?: { apiUrl?: string; pagosSimulados?: boolean } })
  .__ALOJAME_CONFIG__;

export const API_BASE_URL: string = (config?.apiUrl ?? 'http://localhost:8081').replace(/\/+$/, '');

/// true cuando el backend corre con PAGOS_SIMULADOS=true (cuenta de MercadoPago de
/// desarrollo): los reembolsos y transferencias se registran pero no mueven plata.
/// El frontend lo usa solo para avisarlo en los carteles de resultado.
export const PAGOS_SIMULADOS: boolean = config?.pagosSimulados === true;
