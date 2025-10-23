package alexander.gimnacio.service;

import alexander.gimnacio.dto.request.*;
import alexander.gimnacio.dto.response.*;
import alexander.gimnacio.entities.*;
import alexander.gimnacio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
@RequiredArgsConstructor
public class ClaseService {
    private final ClaseRepository repositorioClase;
    private final UsuarioRepository repositorioUsuario;

    public List<Clase> obtenerTodasLasClasesActivas() {
        return repositorioClase.findByEstaActivoTrue();
    }

    @Transactional
    public String inscribirUsuarioEnClase(SolicitudInscripciones solicitud) {
        Usuario usuario = repositorioUsuario.findById(solicitud.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Clase clase = repositorioClase.findById(solicitud.getClaseId())
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));

        if (clase.getUsuariosInscritos().contains(usuario)) {
            throw new RuntimeException("Ya estás inscrito en esta clase");
        }

        if (clase.obtenerCuposDisponibles() <= 0) {
            throw new RuntimeException("Clase llena");
        }

        clase.getUsuariosInscritos().add(usuario);
        usuario.getClasesInscritas().add(clase);

        repositorioClase.save(clase);
        repositorioUsuario.save(usuario);

        return "Inscripción exitosa a " + clase.getNombreClase();
    }

    public List<Clase> obtenerClasesInscritasPorUsuario(Long usuarioId) {
        Usuario usuario = repositorioUsuario.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return new ArrayList<>(usuario.getClasesInscritas());
    }
}

