package alexander.gimnacio.controller;
import alexander.gimnacio.dto.request.*;
import alexander.gimnacio.dto.response.*;
import alexander.gimnacio.entities.*;
import alexander.gimnacio.service.*;
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

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Clase>> obtenerClasesPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(servicioClase.obtenerClasesInscritasPorUsuario(usuarioId));
    }
}