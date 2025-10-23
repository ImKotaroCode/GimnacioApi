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
@RequestMapping("/api/ubicaciones")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UbicacionController {
    private final UbicacionService servicioUbicacion;

    @GetMapping
    public ResponseEntity<List<Ubicacion>> obtenerTodasLasUbicaciones() {
        return ResponseEntity.ok(servicioUbicacion.obtenerTodasLasUbicacionesActivas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ubicacion> obtenerUbicacionPorId(@PathVariable Long id) {
        return ResponseEntity.ok(servicioUbicacion.obtenerUbicacionPorId(id));
    }

    @GetMapping("/distrito/{distrito}")
    public ResponseEntity<List<Ubicacion>> obtenerUbicacionesPorDistrito(@PathVariable String distrito) {
        return ResponseEntity.ok(servicioUbicacion.obtenerUbicacionesPorDistrito(distrito));
    }
}
