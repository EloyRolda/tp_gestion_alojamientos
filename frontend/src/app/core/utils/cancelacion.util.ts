import { Reserva } from '../models/reserva.model';
import { hoyIso, sumarDias } from './fecha.util';

/// Mismo valor por defecto que pago.porcentaje-retencion-tardia del backend
/// (RetencionTardiaPolicy): si el cliente cancela con menos de 48 h, el
/// anfitrion se queda con este porcentaje del monto base.
const RETENCION_TARDIA = 0.35;
/// Mismo corte que ReservaService.DIAS_CORTE_CANCELACION del backend.
const DIAS_CORTE = 2;

const pesos = new Intl.NumberFormat('es-AR', { style: 'currency', currency: 'ARS', maximumFractionDigits: 2 });

export function formatearPesos(monto: number): string {
  return pesos.format(monto);
}

function pagada(r: Reserva): boolean {
  return r.pago?.estado === 'PAGADO';
}

/// Cancelacion tardia: hoy ya es el dia de corte (2 dias antes de la llegada) o despues.
export function esCancelacionTardia(r: Reserva): boolean {
  return hoyIso() >= sumarDias(r.fechaInicio, -DIAS_CORTE);
}

/// Que pasa con la plata si se cancela (las mismas reglas que aplica el
/// backend). "quien" cambia la redaccion: "te devolvemos" / "el cliente recibe".
export function explicarCancelacion(r: Reserva, quien: 'cliente' | 'anfitrion' | 'admin'): string[] {
  if (!pagada(r)) {
    return [
      quien === 'cliente'
        ? 'Todavía no pagaste nada, así que no hay dinero que devolver.'
        : 'El cliente todavía no pagó, así que no hay dinero que devolver.',
      'La reserva se cancela y esas fechas quedan libres.',
    ];
  }
  const p = r.pago!;
  const base = Number(p.montoBase);
  const cargos = Number(p.comisionPlataforma) + Number(p.comisionMercadopago);
  const avisoCargos = `Los cargos de servicio (${formatearPesos(cargos)}) no se devuelven.`;

  if (quien === 'anfitrion') {
    return [
      `El cliente recibe el 100% del alojamiento (${formatearPesos(base)}) y vos no cobrás esta reserva.`,
      'La cancelación no se puede deshacer.',
    ];
  }
  if (!esCancelacionTardia(r)) {
    return [
      quien === 'cliente'
        ? `Te devolvemos ${formatearPesos(base)}, el 100% del alojamiento.`
        : `Faltan más de 48 horas para la llegada: el cliente recibe ${formatearPesos(base)}, el 100% del alojamiento.`,
      avisoCargos,
    ];
  }
  const retenido = Math.round(base * RETENCION_TARDIA * 100) / 100;
  const devuelto = Math.round((base - retenido) * 100) / 100;
  return [
    quien === 'cliente'
      ? `Faltan menos de 48 horas para la llegada: te devolvemos ${formatearPesos(devuelto)} (el 65% del alojamiento) y el anfitrión se queda con ${formatearPesos(retenido)}.`
      : `Faltan menos de 48 horas para la llegada: el cliente recibe ${formatearPesos(devuelto)} (65%) y el anfitrión ${formatearPesos(retenido)} (35%).`,
    avisoCargos,
  ];
}

/// Aviso que se agrega cuando la plata se movio en modo simulado.
export const NOTA_PAGOS_SIMULADOS =
  '[Transacción simulada: MercadoPago está en modo desarrollo, así que no se movió dinero real.]';

/// Cartel con el resultado de cancelar o finalizar, armado con lo que el
/// backend guardo en el Pago (montos reales de la operacion, no estimados).
export function explicarResultado(
  r: Reserva,
  quien: 'cliente' | 'anfitrion' | 'admin',
  simulado: boolean,
): { titulo: string; mensaje: string[]; nota?: string } {
  const p = r.pago;
  const devuelto = Number(p?.montoReembolsadoCliente ?? 0);
  const retenido = Number(p?.montoRetenidoAnfitrion ?? 0);
  const transferido = !!p?.fechaTransferencia;
  let titulo: string;
  let mensaje: string[];

  if (r.estadoReserva === 'FINALIZADA') {
    titulo = 'Estadía finalizada';
    mensaje = [
      quien === 'anfitrion'
        ? `Te transferimos ${formatearPesos(retenido)} a tu alias de cobro.`
        : `Se le transfirieron ${formatearPesos(retenido)} al anfitrión.`,
      quien === 'anfitrion' ? 'Ahora el cliente puede dejar su reseña.' : 'El cliente ya puede dejar su reseña.',
    ];
  } else {
    titulo = 'Reserva cancelada';
    if (devuelto <= 0 && retenido <= 0) {
      mensaje = ['No había ningún pago hecho, así que no se movió dinero.', 'Las fechas quedaron libres.'];
    } else if (retenido <= 0) {
      mensaje = [
        quien === 'cliente'
          ? `Te devolvimos ${formatearPesos(devuelto)}, el 100% del alojamiento.`
          : `Se le devolvieron ${formatearPesos(devuelto)} al cliente, el 100% del alojamiento.`,
        'Los cargos de servicio no se devuelven.',
      ];
    } else {
      const paraAnfitrion = transferido
        ? `se le transfirieron ${formatearPesos(retenido)} al anfitrión`
        : `${formatearPesos(retenido)} quedan para el anfitrión`;
      mensaje = [
        quien === 'cliente'
          ? `Te devolvimos ${formatearPesos(devuelto)} y ${paraAnfitrion}.`
          : `Se le devolvieron ${formatearPesos(devuelto)} al cliente y ${paraAnfitrion}.`,
        'Los cargos de servicio no se devuelven.',
      ];
    }
  }
  const huboMovimiento = devuelto > 0 || transferido;
  return { titulo, mensaje, nota: simulado && huboMovimiento ? NOTA_PAGOS_SIMULADOS : undefined };
}

/// Nota para la confirmacion previa: avisa que la plata se va a simular.
export function notaPrevia(r: Reserva, simulado: boolean): string | undefined {
  return simulado && r.pago?.estado === 'PAGADO'
    ? '[Modo desarrollo: el movimiento de dinero con MercadoPago se simula.]'
    : undefined;
}
