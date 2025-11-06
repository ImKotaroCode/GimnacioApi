package alexander.gimnacio.controller;

import alexander.gimnacio.dto.request.SolicitudInscripciones;
import alexander.gimnacio.dto.response.ClaseDTO;
import alexander.gimnacio.service.ClaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clases")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ClaseController {
    private final ClaseService servicioClase;

    @GetMapping
    public ResponseEntity<List<ClaseDTO>> obtenerTodasLasClases() {
        return ResponseEntity.ok(servicioClase.obtenerTodasLasClasesActivas());
    }

    @PostMapping("/inscribir")
    public ResponseEntity<Map<String, String>> inscribirEnClase(@Valid @RequestBody SolicitudInscripciones solicitud) {
        String mensaje = servicioClase.inscribirUsuarioEnClase(solicitud);
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", mensaje);
        response.put("success", "true");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{claseId}/cancelar-inscripcion")
    public ResponseEntity<Map<String, Object>> cancelarInscripcion(
            @PathVariable Long claseId,
            @RequestBody(required = false) Map<String, Object> body
    ) {
        System.out.println("🟠 Body recibido: " + body);

        if (body == null || !body.containsKey("usuarioId")) {
            return ResponseEntity.badRequest().body(Map.of(
                    "mensaje", "Body vacío o usuarioId no enviado",
                    "body", body
            ));
        }

        Long usuarioId = Long.valueOf(body.get("usuarioId").toString());
        String mensaje = servicioClase.cancelarInscripcionDeUsuario(claseId, usuarioId).toString();
        return ResponseEntity.ok(Map.of("mensaje", mensaje));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ClaseDTO>> obtenerClasesPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(servicioClase.obtenerClasesDTOInscritasPorUsuario(usuarioId));
    }
}
