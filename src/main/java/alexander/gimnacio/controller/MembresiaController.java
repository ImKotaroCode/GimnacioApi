package alexander.gimnacio.controller;
import alexander.gimnacio.dto.request.*;
import alexander.gimnacio.dto.response.*;
import alexander.gimnacio.entities.*;
import alexander.gimnacio.repository.MembresiaRepository;
import alexander.gimnacio.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/membresias")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MembresiaController {
    private final MembresiaService servicioMembresia;
    private final MembresiaRepository membresiaRepository;

    @PostMapping
    public ResponseEntity<RespuestaMembresia> crearMembresia(@Valid @RequestBody SolicitudMembresia solicitud) {
        return ResponseEntity.ok(servicioMembresia.crearMembresia(solicitud));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<RespuestaMembresia> obtenerMembresiaPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(servicioMembresia.obtenerMembresiaPorUsuario(usuarioId));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<String> cancelarMembresia(@PathVariable Long id) {
        try {
            Membresia membresia = membresiaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Membresía no encontrada"));

            membresia.setEstado(Membresia.Estado.CANCELADO);
            membresiaRepository.save(membresia);

            return ResponseEntity.ok("Membresía cancelada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al cancelar membresía: " + e.getMessage());
        }
    }
}
