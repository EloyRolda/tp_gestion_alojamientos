/// Fechas "de calendario" (sin hora) en formato ISO yyyy-MM-dd, que es como las
/// manda y recibe el backend (LocalDate). Se arman siempre con la fecha LOCAL:
/// new Date().toISOString() usa la hora de Greenwich y en Argentina, despues de
/// las 21 h, ya devuelve el dia siguiente.

export function hoyIso(): string {
  return aIso(new Date());
}

export function aIso(d: Date): string {
  const mes = String(d.getMonth() + 1).padStart(2, '0');
  const dia = String(d.getDate()).padStart(2, '0');
  return `${d.getFullYear()}-${mes}-${dia}`;
}

/// "2026-12-14" -> Date local a las 00:00 (new Date("2026-12-14") la tomaria en UTC).
export function desdeIso(iso: string): Date {
  const [a, m, d] = iso.slice(0, 10).split('-').map(Number);
  return new Date(a, m - 1, d);
}

export function sumarDias(iso: string, dias: number): string {
  const d = desdeIso(iso);
  d.setDate(d.getDate() + dias);
  return aIso(d);
}
