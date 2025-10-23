package alexander.gimnacio.config;

import alexander.gimnacio.entities.*;
import alexander.gimnacio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class InitData implements CommandLineRunner {

    private final UbicacionRepository repositorioUbicacion;
    private final ClaseRepository repositorioClase;
    private final UsuarioRepository repositorioUsuario;
    private final PasswordEncoder codificadorContrasena;

    @Override
    public void run(String... args) {
        inicializarUbicaciones();
        inicializarClases();
        inicializarUsuarioPrueba();
    }

    private void inicializarUbicaciones() {
        if (repositorioUbicacion.count() == 0) {
            repositorioUbicacion.saveAll(Arrays.asList(
                    Ubicacion.builder()
                            .nombreUbicacion("FitZone Miraflores")
                            .direccion("Av. Larco 1234, Miraflores")
                            .distrito("Miraflores")
                            .ciudad("Lima")
                            .numeroTelefono("+51 999 111 222")
                            .horario("24/7")
                            .latitud(-12.1196)
                            .longitud(-77.0365)
                            .estaActivo(true)
                            .build(),

                    Ubicacion.builder()
                            .nombreUbicacion("FitZone San Isidro")
                            .direccion("Av. Conquistadores 567, San Isidro")
                            .distrito("San Isidro")
                            .ciudad("Lima")
                            .numeroTelefono("+51 999 333 444")
                            .horario("24/7")
                            .latitud(-12.0931)
                            .longitud(-77.0465)
                            .estaActivo(true)
                            .build(),

                    Ubicacion.builder()
                            .nombreUbicacion("FitZone Surco")
                            .direccion("Av. Primavera 890, Surco")
                            .distrito("Surco")
                            .ciudad("Lima")
                            .numeroTelefono("+51 999 555 666")
                            .horario("24/7")
                            .latitud(-12.1431)
                            .longitud(-76.9965)
                            .estaActivo(true)
                            .build(),

                    Ubicacion.builder()
                            .nombreUbicacion("FitZone La Molina")
                            .direccion("Av. Javier Prado 2345, La Molina")
                            .distrito("La Molina")
                            .ciudad("Lima")
                            .numeroTelefono("+51 999 777 888")
                            .horario("24/7")
                            .latitud(-12.0731)
                            .longitud(-76.9565)
                            .estaActivo(true)
                            .build()
            ));
        }
    }

    private void inicializarClases() {
        if (repositorioClase.count() == 0) {
            repositorioClase.saveAll(Arrays.asList(
                    Clase.builder()
                            .nombreClase("Yoga Flow")
                            .descripcion("Mejora tu flexibilidad y encuentra paz interior")
                            .horario("Lunes, Miércoles, Viernes - 7:00 AM")
                            .nombreInstructor("María González")
                            .capacidadMaxima(20)
                            .duracionMinutos(60)
                            .estaActivo(true)
                            .build(),

                    Clase.builder()
                            .nombreClase("Spinning")
                            .descripcion("Cardio intenso con música energizante")
                            .horario("Martes, Jueves - 6:30 PM")
                            .nombreInstructor("Carlos Ramírez")
                            .capacidadMaxima(25)
                            .duracionMinutos(45)
                            .estaActivo(true)
                            .build(),

                    Clase.builder()
                            .nombreClase("CrossFit")
                            .descripcion("Entrenamiento funcional de alta intensidad")
                            .horario("Lunes a Viernes - 8:00 PM")
                            .nombreInstructor("Luis Torres")
                            .capacidadMaxima(15)
                            .duracionMinutos(60)
                            .estaActivo(true)
                            .build(),

                    Clase.builder()
                            .nombreClase("Zumba")
                            .descripcion("Baila mientras te pones en forma")
                            .horario("Sábados - 10:00 AM")
                            .nombreInstructor("Ana Martínez")
                            .capacidadMaxima(30)
                            .duracionMinutos(60)
                            .estaActivo(true)
                            .build(),

                    Clase.builder()
                            .nombreClase("Pilates")
                            .descripcion("Fortalece tu core y mejora postura")
                            .horario("Miércoles, Viernes - 9:00 AM")
                            .nombreInstructor("Sofia López")
                            .capacidadMaxima(18)
                            .duracionMinutos(50)
                            .estaActivo(true)
                            .build(),

                    Clase.builder()
                            .nombreClase("Boxing")
                            .descripcion("Libera estrés mientras desarrollas fuerza")
                            .horario("Martes, Jueves - 7:00 PM")
                            .nombreInstructor("Pedro Sánchez")
                            .capacidadMaxima(20)
                            .duracionMinutos(60)
                            .estaActivo(true)
                            .build()
            ));
        }
    }

    private void inicializarUsuarioPrueba() {
        if (repositorioUsuario.count() == 0) {
            Usuario usuarioPrueba = Usuario.builder()
                    .nombreCompleto("Usuario Prueba")
                    .correoElectronico("prueba@fitzone.com")
                    .contrasena(codificadorContrasena.encode("password123"))
                    .numeroTelefono("+51999123456")
                    .rol(Usuario.Rol.USUARIO)
                    .estaActivo(true)
                    .build();

            repositorioUsuario.save(usuarioPrueba);
        }
    }
}

