/// Espejo de TIPOUSUARIO (backend). El registro nunca permite ADMIN (lo valida el propio backend).
export type TipoUsuario = 'CLIENTE' | 'ANFITRION' | 'ADMIN';

/// Espejo de UsuarioResponseDTO.
export interface Usuario {
  id: number;
  nombre: string;
  apellido: string;
  usuarioLogin: string;
  email: string;
  telefono: string;
  activo: boolean;
  emailVerificado: boolean;
  aliasCobro: string | null;
  fechaRegistro: string;
  tipo: TipoUsuario;
}

/// Espejo de UsuarioRegistroDTO.
export interface UsuarioRegistro {
  nombre: string;
  apellido: string;
  usuarioLogin: string;
  email: string;
  password: string;
  telefono: string;
  tipo: TipoUsuario;
}
