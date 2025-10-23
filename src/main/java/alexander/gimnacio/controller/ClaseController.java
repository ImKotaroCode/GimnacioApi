package alexander.gimnacio.controller;
import alexander.gimnacio.dto.request.*;
import alexander.gimnacio.dto.response.*;
import alexander.gimnacio.entities.*;
import alexander.gimnacio.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/clases")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ClaseController {
    private final ClaseService servicioClase;

    @GetMapping
    public ResponseEntity<List<Clase>> obtenerTodasLasClases() {
        return ResponseEntity.ok(servicioClase.obtenerTodasLasClasesActivas());
    }

    @PostMapping("/inscribir")
    public ResponseEntity<String> inscribirEnClase(@Valid @RequestBody SolicitudInscripciones solicitud) {
        return ResponseEntity.ok(servicioClase.inscribirUsuarioEnClase(solicitud));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Clase>> obtenerClasesPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(servicioClase.obtenerClasesInscritasPorUsuario(usuarioId));
    }
}