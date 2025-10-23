package alexander.gimnacio.controller;



import alexander.gimnacio.dto.request.*;
import alexander.gimnacio.dto.response.*;
import alexander.gimnacio.entities.*;
import alexander.gimnacio.repository.*;
import alexander.gimnacio.service.AdministradorService;
import lombok.RequiredArgsConstructor;
import java.util.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/admin") @CrossOrigin(origins="*") @RequiredArgsConstructor
public class AdministradorController {
    private final AdministradorService s;

    @GetMapping("/dashboard") public ResponseEntity<EstadisticasDashboard> dashboard(){ return ResponseEntity.ok(s.obtenerEstadisticasDashboard()); }
    @GetMapping("/reportes/membresias") public ResponseEntity<ReporteMembresias> repM(){ return ResponseEntity.ok(s.obtenerReporteMembresias()); }
    @GetMapping("/reportes/clases") public ResponseEntity<List<ReporteClases>> repC(){ return ResponseEntity.ok(s.obtenerReporteClases()); }
    @GetMapping("/reportes/ingresos") public ResponseEntity<ReporteIngresos> repI(@RequestParam(defaultValue="mensual") String periodo){ return ResponseEntity.ok(s.obtenerReporteIngresos(periodo)); }

    @GetMapping("/usuarios") public ResponseEntity<List<UsuarioAdminDTO>> usuarios(){ return ResponseEntity.ok(s.obtenerUsuariosAdmin()); }
    @PutMapping("/usuarios/{id}") public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @Valid @RequestBody ActualizarUsuarioRequest r){ return ResponseEntity.ok(s.actualizarUsuario(id,r)); }
    @DeleteMapping("/usuarios/{id}") public ResponseEntity<String> eliminar(@PathVariable Long id){ s.eliminarUsuario(id); return ResponseEntity.ok("Usuario eliminado exitosamente"); }

    @PostMapping("/clases") public ResponseEntity<Clase> crearClase(@Valid @RequestBody CrearClaseRequest r){ return ResponseEntity.ok(s.crearClase(r)); }
    @PutMapping("/clases/{id}") public ResponseEntity<Clase> actClase(@PathVariable Long id, @Valid @RequestBody CrearClaseRequest r){ return ResponseEntity.ok(s.actualizarClase(id,r)); }
    @DeleteMapping("/clases/{id}") public ResponseEntity<String> delClase(@PathVariable Long id){ s.eliminarClase(id); return ResponseEntity.ok("Clase eliminada exitosamente"); }

    @PostMapping("/ubicaciones") public ResponseEntity<Ubicacion> crearUb(@Valid @RequestBody CrearUbicacionRequest r){ return ResponseEntity.ok(s.crearUbicacion(r)); }
    @PutMapping("/ubicaciones/{id}") public ResponseEntity<Ubicacion> actUb(@PathVariable Long id, @Valid @RequestBody CrearUbicacionRequest r){ return ResponseEntity.ok(s.actualizarUbicacion(id,r)); }
    @DeleteMapping("/ubicaciones/{id}") public ResponseEntity<String> delUb(@PathVariable Long id){ s.eliminarUbicacion(id); return ResponseEntity.ok("Ubicación eliminada exitosamente"); }

    @GetMapping("/membresias") public ResponseEntity<List<MembresiaAdminDTO>> membresias(){ return ResponseEntity.ok(s.obtenerMembresiasAdmin()); }
    @GetMapping("/pagos") public ResponseEntity<List<PagoAdminDTO>> pagos(){ return ResponseEntity.ok(s.obtenerPagosAdmin()); }
}

