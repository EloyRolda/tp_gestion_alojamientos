package GestionAlojamiento.Model.Enums;

/// Reemplaza a la vieja jerarquia de herencia (Casa/Departamento/Hotel como
/// entidades separadas). Alojamiento ahora es una unica tabla; este enum
/// distingue el tipo, y los pocos campos realmente exclusivos de cada tipo
/// (piso, estrellas) quedan como columnas nullable en esa misma tabla.
/// Agregar un tipo nuevo el dia de manana (cabania, habitacion privada, etc.)
/// es agregar un valor aca, sin migraciones de tablas.
public enum TipoAlojamiento {
    CASA,
    DEPARTAMENTO,
    HOTEL
}
