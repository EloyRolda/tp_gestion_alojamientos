import { HttpErrorResponse } from '@angular/common/http';
import { extraerMensajeError } from './http-error.util';

/// Validacion de formularios con el error DEBAJO de cada campo.
///
/// Dos fuentes de errores, mismo formato (id del campo -> mensaje):
/// - validar(): reglas que se chequean en el navegador antes de enviar, asi el
///   usuario ve al instante que campo corregir sin esperar al servidor.
/// - erroresDelBackend(): el backend responde un 400 con el texto
///   "campo: mensaje, campo: mensaje" (GlobalExceptionHandler.manejarValidacion);
///   se separa por campo y lo que no corresponde a ningun campo del formulario
///   queda como error general.
///
/// La clave de cada error es el id del input en el HTML, que es lo que usan
/// app-error-campo (id "<campo>-error") y enfocarPrimerError().

export type ErroresCampo = Record<string, string>;
export type Regla = (valor: unknown) => string | null;

const RE_EMAIL = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/;
const RE_USUARIO = /^[A-Za-z0-9_.]{3,50}$/;
const RE_TELEFONO = /^\+?[0-9\s()-]{6,30}$/;

function vacio(valor: unknown): boolean {
  return valor === null || valor === undefined || String(valor).trim() === '';
}

export const reglas = {
  obligatorio: ((v) => (vacio(v) ? 'Completá este campo.' : null)) as Regla,
  email: ((v) => (!vacio(v) && !RE_EMAIL.test(String(v).trim()) ? 'Escribí un email válido, por ejemplo nombre@gmail.com.' : null)) as Regla,
  usuario: ((v) =>
    !vacio(v) && !RE_USUARIO.test(String(v).trim())
      ? 'Entre 3 y 50 caracteres: solo letras, números, punto o guion bajo (sin espacios).'
      : null) as Regla,
  telefono: ((v) =>
    !vacio(v) && !RE_TELEFONO.test(String(v).trim()) ? 'Escribí un teléfono válido: solo números, y si querés +, espacios o guiones.' : null) as Regla,
  minimoCaracteres:
    (n: number): Regla =>
    (v) =>
      !vacio(v) && String(v).length < n ? `Tiene que tener al menos ${n} caracteres.` : null,
  maximoCaracteres:
    (n: number): Regla =>
    (v) =>
      !vacio(v) && String(v).trim().length > n ? `Puede tener hasta ${n} caracteres.` : null,
  enteroPositivo: ((v) =>
    !vacio(v) && !(Number.isInteger(Number(v)) && Number(v) >= 1) ? 'Tiene que ser un número entero, de 1 en adelante.' : null) as Regla,
  enteroNoNegativo: ((v) =>
    !vacio(v) && !(Number.isInteger(Number(v)) && Number(v) >= 0) ? 'Tiene que ser un número entero, de 0 en adelante.' : null) as Regla,
  numeroPositivo: ((v) => (!vacio(v) && !(Number(v) > 0) ? 'Tiene que ser un número mayor a 0.' : null)) as Regla,
  numeroNoNegativo: ((v) => (!vacio(v) && !(Number(v) >= 0) ? 'Tiene que ser un número positivo (o 0).' : null)) as Regla,
  maximo:
    (max: number, mensaje: string): Regla =>
    (v) =>
      !vacio(v) && Number(v) > max ? mensaje : null,
};

/// Aplica las reglas de cada campo y devuelve el primer error de cada uno.
export function validar(valores: object, reglasPorCampo: Record<string, Regla[]>): ErroresCampo {
  const errores: ErroresCampo = {};
  for (const [campo, lista] of Object.entries(reglasPorCampo)) {
    for (const regla of lista) {
      const mensaje = regla((valores as Record<string, unknown>)[campo]);
      if (mensaje) {
        errores[campo] = mensaje;
        break;
      }
    }
  }
  return errores;
}

export function hayErrores(errores: ErroresCampo): boolean {
  return Object.keys(errores).length > 0;
}

/// Saca el error de un campo (cuando el usuario lo vuelve a editar).
export function sinError(errores: ErroresCampo, campo: string): ErroresCampo {
  if (!(campo in errores)) return errores;
  const copia = { ...errores };
  delete copia[campo];
  return copia;
}

/// Lleva el foco (y el scroll) al primer campo con error, en el orden en que
/// aparecen en la pantalla.
export function enfocarPrimerError(errores: ErroresCampo): void {
  setTimeout(() => {
    const campos = Object.keys(errores)
      .map((id) => document.getElementById(id))
      .filter((el): el is HTMLElement => !!el)
      .sort((a, b) => (a.compareDocumentPosition(b) & Node.DOCUMENT_POSITION_FOLLOWING ? -1 : 1));
    campos[0]?.focus();
  });
}

/// Mensajes de validacion del backend pasados al tono de la app.
const TRADUCCIONES: [RegExp, string | ((m: RegExpMatchArray) => string)][] = [
  [/^(Campo obligatorio|No puede dejar el campo vacio|must not be (blank|null|empty))$/i, 'Completá este campo.'],
  [/^El formato de correo es invalido$/i, 'Escribí un email válido, por ejemplo nombre@gmail.com.'],
  [/^El dominio de correo no esta permitido$/i, 'No encontramos ese dominio de email. Revisá que esté bien escrito.'],
  [/^La contrasenia debe tener al menos (\d+) caracteres$/i, (m) => `Tiene que tener al menos ${m[1]} caracteres.`],
  [/^El usuario solo puede contener/i, 'Entre 3 y 50 caracteres: solo letras, números, punto o guion bajo (sin espacios).'],
  [/^(size must be between 0 and|el tamaño debe estar entre 0 y) (\d+)$/i, (m) => `Puede tener hasta ${m[2]} caracteres.`],
  [/^(size must be between|el tamaño debe estar entre) (\d+) (and|y) (\d+)$/i, (m) => `Tiene que tener entre ${m[2]} y ${m[4]} caracteres.`],
  [/^Valor invalido$/i, 'Tiene que ser un número mayor a 0.'],
  [/^No puede ser negativo$/i, 'Tiene que ser un número positivo (o 0).'],
  [/^(must be greater than or equal to|debe ser mayor que o igual a) (\d+)$/i, (m) => `Tiene que ser ${m[2]} o más.`],
  [/^El puntaje minimo es 1$/i, 'Elegí un puntaje de 1 a 5.'],
  [/^El puntaje maximo es 5$/i, 'Elegí un puntaje de 1 a 5.'],
  [/^El comentario es muy largo$/i, 'El comentario es muy largo.'],
  [/^(Latitud|Longitud) fuera de rango$/i, 'La ubicación marcada en el mapa no es válida.'],
];

function traducirValidacion(mensaje: string): string {
  for (const [patron, reemplazo] of TRADUCCIONES) {
    const m = mensaje.match(patron);
    if (m) return typeof reemplazo === 'string' ? reemplazo : reemplazo(m);
  }
  return mensaje;
}

/// Separa un error del backend en errores por campo y un mensaje general.
/// `campos` mapea el nombre del campo del DTO (ej. "email" o "direccion.calle")
/// al id del input en el formulario; los campos que no estan en el mapa van al
/// mensaje general.
export function erroresDelBackend(
  err: HttpErrorResponse,
  campos: Record<string, string>,
): { porCampo: ErroresCampo; general: string | null } {
  const texto = err.status === 400 && typeof err.error === 'string' ? err.error.trim() : '';
  const partes = [...texto.matchAll(/([A-Za-z][\w.]*): (.+?)(?=, [A-Za-z][\w.]*: |$)/g)];
  if (partes.length === 0) {
    return { porCampo: {}, general: extraerMensajeError(err) };
  }
  const porCampo: ErroresCampo = {};
  const sueltos: string[] = [];
  for (const [, campo, mensaje] of partes) {
    const id = campos[campo];
    const legible = traducirValidacion(mensaje.trim());
    if (id) {
      porCampo[id] ??= legible;
    } else {
      sueltos.push(legible);
    }
  }
  return { porCampo, general: sueltos.length ? sueltos.join(' ') : null };
}

/// Datos de una cuenta: registro, alta de administrador, Mi perfil y la
/// edicion del admin. Mismas reglas que UsuarioRegistroDTO/UsuarioModificarDTO.
export function reglasCuenta(passwordObligatoria: boolean): Record<string, Regla[]> {
  return {
    nombre: [reglas.obligatorio, reglas.maximoCaracteres(50)],
    apellido: [reglas.obligatorio, reglas.maximoCaracteres(50)],
    usuarioLogin: [reglas.obligatorio, reglas.usuario],
    email: [reglas.obligatorio, reglas.email],
    telefono: [reglas.obligatorio, reglas.telefono, reglas.maximoCaracteres(30)],
    password: passwordObligatoria ? [reglas.obligatorio, reglas.minimoCaracteres(6)] : [reglas.minimoCaracteres(6)],
  };
}

/// Campos del DTO de usuario -> id del input (en estos formularios coinciden).
export const CAMPOS_CUENTA: Record<string, string> = {
  nombre: 'nombre',
  apellido: 'apellido',
  usuarioLogin: 'usuarioLogin',
  email: 'email',
  telefono: 'telefono',
  password: 'password',
};
