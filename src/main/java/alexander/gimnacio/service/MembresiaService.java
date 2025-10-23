package alexander.gimnacio.service;

import alexander.gimnacio.dto.request.SolicitudMembresia;
import alexander.gimnacio.dto.response.RespuestaMembresia;
import alexander.gimnacio.entities.Membresia;
import alexander.gimnacio.entities.Usuario;
import alexander.gimnacio.repository.MembresiaRepository;
import alexander.gimnacio.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class MembresiaService {

    private final MembresiaRepository repositorioMembresia;
    private final UsuarioRepository repositorioUsuario;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    public RespuestaMembresia crearMembresia(SolicitudMembresia req) {
        Usuario usuario = repositorioUsuario.findById(req.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        var existente = repositorioMembresia.findByUsuarioId(usuario.getId());
        if (existente.isPresent() && existente.get().getEstado() == Membresia.Estado.ACTIVO) {
            throw new IllegalStateException("Ya tienes una membresía activa");
        }

        Membresia m = existente.orElseGet(Membresia::new);
        m.setUsuario(usuario);

        Membresia.TipoPlan plan = req.getTipoPlan();
        boolean renov = req.getRenovacionAutomatica() == null ? true : req.getRenovacionAutomatica();
        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fin = (plan == Membresia.TipoPlan.ANUAL) ? inicio.plusYears(1) : inicio.plusMonths(1);

        m.setTipoPlan(plan);
        m.setPrecio(precioPorPlan(plan));
        m.setFechaInicio(inicio);
        m.setFechaFin(fin);
        m.setEstado(Membresia.Estado.ACTIVO);
        m.setRenovacionAutomatica(renov);

        usuario.setMembresia(m);

        Membresia guardada = repositorioMembresia.save(m);
        repositorioUsuario.save(usuario);

        return mapearRespuesta(guardada);
    }

    @Transactional(readOnly = true)
    public RespuestaMembresia obtenerMembresiaPorUsuario(Long usuarioId) {
        Membresia m = repositorioMembresia.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró membresía"));
        return mapearRespuesta(m);
    }

    // --- helpers ---

    private double precioPorPlan(Membresia.TipoPlan plan) {
        return switch (plan) {
            case BASICO -> 29.0;
            case PREMIUM -> 49.0;
            case ANUAL -> 399.0;
        };
    }

    private RespuestaMembresia mapearRespuesta(Membresia m) {
        return RespuestaMembresia.builder()
                .id(m.getId())
                .tipoPlan(m.getTipoPlan().name())
                .precio(m.getPrecio())
                .fechaInicio(m.getFechaInicio().format(FMT))
                .fechaFin(m.getFechaFin().format(FMT))
                .estado(m.getEstado().name())
                .renovacionAutomatica(m.isRenovacionAutomatica())
                .build();
    }
}
