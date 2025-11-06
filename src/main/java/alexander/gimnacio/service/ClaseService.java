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

    public List<ClaseDTO> obtenerTodasLasClasesActivas() {
        return repositorioClase.findByEstaActivoTrue().stream()
                .map(this::convertirAClaseDTO)
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

        if (usuario.getRol() == Usuario.Rol.ADMINISTRADOR) {
            throw new RuntimeException("Los administradores no pueden inscribirse en clases.");
        }

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



    public List<ClaseDTO> obtenerClasesDTOInscritasPorUsuario(Long usuarioId) {
        var usuario = repositorioUsuario.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        var clases = repositorioClase.findAll().stream()
                .filter(c -> c.getUsuariosInscritos() != null && c.getUsuariosInscritos().contains(usuario))
                .toList();

        return clases.stream().map(c -> {
            ClaseDTO dto = new ClaseDTO();
            dto.setId(c.getId());
            dto.setNombreClase(c.getNombreClase());
            dto.setDescripcion(c.getDescripcion());
            dto.setNombreInstructor(c.getNombreInstructor());
            dto.setHorario(c.getHorario());
            dto.setCapacidadMaxima(c.getCapacidadMaxima());
            dto.setDuracionMinutos(c.getDuracionMinutos());
            dto.setEstaActivo(c.isEstaActivo());
            return dto;
        }).toList();
    }
    @Transactional
    public Map<String, Object> cancelarInscripcionDeUsuario(Long usuarioId, Long claseId) {
        var usuario = repositorioUsuario.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        var clase = repositorioClase.findById(claseId)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));

        if (!clase.getUsuariosInscritos().contains(usuario)) {
            return Map.of("success", false, "mensaje", "El usuario no estaba inscrito en esta clase");
        }

        // Quitar relación en ambos lados
        usuario.getClasesInscritas().remove(clase);
        clase.getUsuariosInscritos().remove(usuario);

        repositorioUsuario.save(usuario);
        repositorioClase.save(clase);

        return Map.of("success", true, "mensaje", "Inscripción cancelada exitosamente");
    }



}