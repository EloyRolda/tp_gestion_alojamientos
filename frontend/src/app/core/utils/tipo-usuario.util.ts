/// Nombre para mostrar de cada tipo de usuario: en la base y en el JWT van en
/// mayusculas y sin tilde (ANFITRION), en pantalla como "Anfitrión".
export const NOMBRE_TIPO_USUARIO: Record<string, string> = {
  TODOS: 'Todos',
  CLIENTE: 'Cliente',
  ANFITRION: 'Anfitrión',
  ADMIN: 'Administrador',
};

export function nombreTipoUsuario(tipo: string | null | undefined): string {
  return tipo ? (NOMBRE_TIPO_USUARIO[tipo] ?? tipo) : '';
}
