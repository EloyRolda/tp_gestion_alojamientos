package GestionAlojamiento.Config;

import GestionAlojamiento.DTO.AmenityRegistroDTO;
import GestionAlojamiento.Model.Enums.CategoriaAmenity;
import GestionAlojamiento.Service.AmenityService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/// Carga el catalogo base de amenities la primera vez que arranca la app
/// (si ya existen, no hace nada). Reemplaza a un data.sql para no depender
/// de que ddl-auto termine de crear las tablas antes de insertar filas.
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final AmenityService amenityService;

    @Override
    public void run(String... args) {
        if (!amenityService.listarTodos().isEmpty()) {
            return;
        }

        crearSiNoExiste("Wifi", CategoriaAmenity.CONECTIVIDAD);
        crearSiNoExiste("Cocina equipada", CategoriaAmenity.COCINA);
        crearSiNoExiste("Microondas", CategoriaAmenity.COCINA);
        crearSiNoExiste("Heladera", CategoriaAmenity.COCINA);
        crearSiNoExiste("Lavarropas", CategoriaAmenity.LAVADERO);
        crearSiNoExiste("Secarropas", CategoriaAmenity.LAVADERO);
        crearSiNoExiste("Aire acondicionado", CategoriaAmenity.CLIMATIZACION);
        crearSiNoExiste("Calefaccion", CategoriaAmenity.CLIMATIZACION);
        crearSiNoExiste("TV con streaming", CategoriaAmenity.ENTRETENIMIENTO);
        crearSiNoExiste("Ropa de cama", CategoriaAmenity.HABITACION);
        crearSiNoExiste("Toallas", CategoriaAmenity.BANIO);
        crearSiNoExiste("Secador de pelo", CategoriaAmenity.BANIO);
        crearSiNoExiste("Cochera", CategoriaAmenity.TRANSPORTE);
        crearSiNoExiste("Ascensor", CategoriaAmenity.ACCESIBILIDAD);
        crearSiNoExiste("Patio", CategoriaAmenity.EXTERIOR);
        crearSiNoExiste("Parrilla", CategoriaAmenity.EXTERIOR);
        crearSiNoExiste("Balcon", CategoriaAmenity.EXTERIOR);
        crearSiNoExiste("Pileta", CategoriaAmenity.PILETA_SPA);
        crearSiNoExiste("Jacuzzi", CategoriaAmenity.PILETA_SPA);
        crearSiNoExiste("Camaras de seguridad", CategoriaAmenity.SEGURIDAD);
        crearSiNoExiste("Alarma", CategoriaAmenity.SEGURIDAD);
        crearSiNoExiste("Detector de humo", CategoriaAmenity.SEGURIDAD);
        crearSiNoExiste("Apto para ninos", CategoriaAmenity.FAMILIA);
        crearSiNoExiste("Cuna disponible", CategoriaAmenity.FAMILIA);
        crearSiNoExiste("Desayuno incluido", CategoriaAmenity.SERVICIOS);
        crearSiNoExiste("Limpieza incluida", CategoriaAmenity.SERVICIOS);
        crearSiNoExiste("Expensas incluidas", CategoriaAmenity.SERVICIOS);
        crearSiNoExiste("Se admiten mascotas", CategoriaAmenity.MASCOTAS);
    }

    private void crearSiNoExiste(String nombre, CategoriaAmenity categoria) {
        amenityService.crear(new AmenityRegistroDTO(nombre, categoria));
    }
}
