/// Mismo calculo que PagoService.calcularMontoBase() en el backend (noches *
/// precio_noche_pactado, nunca Reserva.getPrecioTotal() -- ver gotcha #1 de
/// CLAUDE.md). Se centraliza aca para no repetirlo en cada componente que
/// necesita mostrar un total: formulario de reserva, listado, detalle, resumen.
export function calcularNoches(fechaInicio: string, fechaFin: string): number {
  const inicio = new Date(fechaInicio);
  const fin = new Date(fechaFin);
  const msPorDia = 1000 * 60 * 60 * 24;
  return Math.round((fin.getTime() - inicio.getTime()) / msPorDia);
}

export function calcularTotal(fechaInicio: string, fechaFin: string, precioNoche: number): number {
  return calcularNoches(fechaInicio, fechaFin) * precioNoche;
}
