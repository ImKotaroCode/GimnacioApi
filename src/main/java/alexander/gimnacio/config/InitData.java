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
                            .nombreUbicacion("SmartFit La Molina")
                            .direccion("Av. Javier Prado Este 6210, La Molina - La Molina")
                            .distrito("La Molina")
                            .ciudad("Lima")
                            .numeroTelefono("+51 999 999 999")
                            .horario("24/7")
                            .latitud(-12.07052)
                            .longitud(-76.95275)
                            .estaActivo(true)
                            .build(),

                    Ubicacion.builder()
                            .nombreUbicacion("SmartFit Puruchuco")
                            .direccion("Av. Nicolás Ayllón 4770, Ate - Lima, Lima - 15494")
                            .distrito("Ate")
                            .ciudad("Lima")
                            .numeroTelefono("+51 999 333 444")
                            .horario("24/7")
                            .latitud(-12.04139)
                            .longitud(-76.93185)
                            .estaActivo(true)
                            .build(),

                    Ubicacion.builder()
                            .nombreUbicacion("SmartFit Santa Anita")
                            .direccion("Av. Carretera Central 111 Int. GM01 (Mall Aventura Santa Anita) Lima, Santa Anita - Lima, Lima - 15008")
                            .distrito("Santa Anita")
                            .ciudad("Lima")
                            .numeroTelefono("+51 999 555 666")
                            .horario("24/7")
                            .latitud(-12.05706)
                            .longitud(-76.97129)
                            .estaActivo(true)
                            .build(),

                    Ubicacion.builder()
                            .nombreUbicacion("FitZone Surco")
                            .direccion("Av. Javier Prado Este 4200, Santiago de Surco - Lima, Lima - 15023")
                            .distrito("Surco")
                            .ciudad("Lima")
                            .numeroTelefono("+51 999 777 888")
                            .horario("24/7")
                            .latitud(-12.088887)
                            .longitud(-77.00583)
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
                    .nombreCompleto("Alexander Ferrua")
                    .correoElectronico("alexferruarua564@gmail.com")
                    .contrasena(codificadorContrasena.encode("123456"))
                    .numeroTelefono("+51902487635")
                    .rol(Usuario.Rol.ADMINISTRADOR)
                    .estaActivo(true)
                    .build();

            repositorioUsuario.save(usuarioPrueba);
        }
    }
}

