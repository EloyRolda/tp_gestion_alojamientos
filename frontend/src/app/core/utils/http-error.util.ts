import { HttpErrorResponse } from '@angular/common/http';

/// El backend devuelve el mensaje de error en dos formatos distintos segun donde
/// se origina (confirmado contra el backend real corriendo):
/// - Texto plano, cuando lo arma GlobalExceptionHandler (la gran mayoria de los
///   errores de negocio: 400/401/404/409).
/// - Un objeto {timestamp,status,error,path}, cuando Spring Security rechaza la
///   request ANTES de llegar a un Controller (401/403 por falta de autenticacion
///   o de rol, sin pasar por GlobalExceptionHandler).
/// Esta funcion normaliza ambos casos a un mensaje legible para mostrar en UI.
export function extraerMensajeError(error: HttpErrorResponse): string {
  if (typeof error.error === 'string' && error.error.trim().length > 0) {
    return error.error;
  }

  if (error.error && typeof error.error === 'object') {
    const cuerpo = error.error as { error?: string; message?: string };
    if (typeof cuerpo.message === 'string' && cuerpo.message.trim().length > 0) {
      return cuerpo.message;
    }
  }

  if (error.status === 0) {
    return 'No se pudo conectar con el servidor. Verifique su conexión e intente de nuevo.';
  }
  if (error.status === 401) {
    return 'Debe iniciar sesión para continuar.';
  }
  if (error.status === 403) {
    return 'No tiene permiso para realizar esta acción.';
  }
  if (error.status === 404) {
    return 'No se encontró lo que estaba buscando.';
  }
  return 'Ocurrió un error inesperado. Intente de nuevo en unos minutos.';
}
