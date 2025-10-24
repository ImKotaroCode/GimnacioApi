package alexander.gimnacio.service;

import alexander.gimnacio.dto.request.*;
import alexander.gimnacio.dto.response.*;
import alexander.gimnacio.entities.*;
import alexander.gimnacio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClaseService {
    private final ClaseRepository repositorioClase;
    private final UsuarioRepository repositorioUsuario;

    // ✅ Cambiar retorno a ClaseDTO
    public List<ClaseDTO> obtenerTodasLasClasesActivas() {
        return repositorioClase.findByEstaActivoTrue().stream()
                .map(this::convertirAClaseDTO) // ✅ Usar método helper
                .collect(Collectors.toList());
    }

    private ClaseDTO convertirAClaseDTO(Clase clase) {
        return ClaseDTO.builder()
                .id(clase.getId())
                .nombreClase(clase.getNombreClase())
                .descripcion(clase.getDescripcion())
                .horario(clase.getHorario())
                .nombreInstructor(clase.getNombreInstructor())
                .capacidadMaxima(clase.getCapacidadMaxima())
                .duracionMinutos(clase.getDuracionMinutos())
                .usuariosInscritos(clase.getUsuariosInscritos() != null ? clase.getUsuariosInscritos().size() : 0)
                .estaActivo(clase.isEstaActivo())
                .build();
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

        usuario.getClasesInscritas().add(clase);
        repositorioUsuario.save(usuario);

        return "Inscripción exitosa a " + clase.getNombreClase();
    }

    public List<Clase> obtenerClasesInscritasPorUsuario(Long usuarioId) {
        Usuario usuario = repositorioUsuario.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return new ArrayList<>(usuario.getClasesInscritas());
    }
}