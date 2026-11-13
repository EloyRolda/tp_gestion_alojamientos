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
    return traducir(error.error.trim());
  }

  if (error.error && typeof error.error === 'object') {
    const cuerpo = error.error as { error?: string; message?: string };
    if (typeof cuerpo.message === 'string' && cuerpo.message.trim().length > 0) {
      return traducir(cuerpo.message.trim());
    }
  }

  if (error.status === 0) {
    return 'No se pudo conectar con el servidor. Revisá tu conexión y probá de nuevo.';
  }
  if (error.status === 401) {
    return 'Tenés que iniciar sesión para continuar.';
  }
  if (error.status === 403) {
    return 'No tenés permiso para hacer esto.';
  }
  if (error.status === 404) {
    return 'No encontramos lo que buscabas.';
  }
  return 'Ocurrió un error inesperado. Probá de nuevo en unos minutos.';
}

/// Mensajes del backend (sin tildes y tratando de "usted") pasados al tono de
/// la app. Los que no estan aca se muestran tal cual llegan.
const TRADUCCIONES: [RegExp, string][] = [
  [/Usuario o contrasenia invalidos/i, 'El usuario o la contraseña no son correctos.'],
  [/^El usuario esta desactivado/i, 'Esta cuenta está desactivada.'],
  [/Debe verificar su correo electronico/i, 'Primero tenés que verificar tu email: revisá tu casilla o pedí un enlace nuevo.'],
  [/^Ya existe una reserva en esas fechas/i, 'Esas fechas ya están reservadas. Probá con otras.'],
  [/^El alojamiento esta inactivo/i, 'Este alojamiento no está recibiendo reservas por ahora.'],
  [/^El anfitrion esta inactivo/i, 'El anfitrión de este alojamiento no está disponible por ahora.'],
  [/^El cliente esta inactivo/i, 'La cuenta del cliente está desactivada.'],
  [/^No se puede borrar: hay alojamientos usando este tipo/i, 'Hay alojamientos con este tipo, así que no se puede eliminar. Fusionalo con otro tipo en lugar de eliminarlo.'],
  [/^No se puede borrar: hay alojamientos usando este servicio/i, 'Hay alojamientos con este servicio, así que no se puede eliminar. Fusionalo con otro servicio en lugar de eliminarlo.'],
  [/^El anfitrion no tiene cargado un alias/i, 'El anfitrión todavía no cargó su alias de cobro.'],
  [/^La reserva aun no ha finalizado/i, 'La estadía todavía no terminó: se puede finalizar desde el día siguiente al check-out.'],
  [/^La fecha de inicio no puede ser anterior al dia actual/i, 'La fecha de llegada no puede ser anterior a hoy.'],
  [/^La fecha de fin no puede ser anterior o igual/i, 'La salida tiene que ser posterior a la llegada.'],
  [/^Hubo un problema con un servicio externo/i, 'MercadoPago o el servicio de correo no respondieron. Probá de nuevo en unos minutos.'],
];

function traducir(mensaje: string): string {
  return TRADUCCIONES.find(([patron]) => patron.test(mensaje))?.[1] ?? mensaje;
}
