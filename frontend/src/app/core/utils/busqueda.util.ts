/// Busqueda de texto en los listados (sin pegarle al backend: los listados ya
/// vienen completos en una sola llamada). Ignora mayusculas y tildes, y cada
/// palabra escrita tiene que aparecer en alguno de los campos, en cualquier
/// orden: "perez juan" encuentra a "Juan Pérez".
export function normalizar(texto: string | number | null | undefined): string {
  return String(texto ?? '')
    .normalize('NFD')
    .replace(/[̀-ͯ]/g, '')
    .toLowerCase()
    .trim();
}

export function coincide(busqueda: string, ...campos: (string | number | null | undefined)[]): boolean {
  const palabras = normalizar(busqueda).split(/\s+/).filter(Boolean);
  if (palabras.length === 0) {
    return true;
  }
  const texto = campos.map(normalizar).join(' ');
  return palabras.every((p) => texto.includes(p));
}
