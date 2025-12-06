package alexander.gimnacio.controller;

import alexander.gimnacio.dto.request.*;
import alexander.gimnacio.dto.response.*;
import alexander.gimnacio.entities.*;
import alexander.gimnacio.service.AdministradorService;
import alexander.gimnacio.service.ClaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins="*")
@RequiredArgsConstructor
public class AdministradorController {

    private final AdministradorService s;
    private final ClaseService claseService;

    /* ========= DASHBOARD & REPORTES GENERALES (solo ADMINISTRADOR) ========= */

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<EstadisticasDashboard> dashboard(){
        return ResponseEntity.ok(s.obtenerEstadisticasDashboard());
    }

    @GetMapping("/reportes/membresias")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ReporteMembresias> repM(){
        return ResponseEntity.ok(s.obtenerReporteMembresias());
    }

    @GetMapping("/reportes/ingresos")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ReporteIngresos> repI(@RequestParam(defaultValue="mensual") String periodo){
        return ResponseEntity.ok(s.obtenerReporteIngresos(periodo));
    }

    @GetMapping("/reportes/suscripciones-mensuales")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Map<String, Long>> suscripcionesMensuales() {
        return ResponseEntity.ok(s.obtenerSuscripcionesMensuales());
    }

    /* ========= REPORTES DE CLASES (ADMIN + ENTRENADOR) ========= */

    @GetMapping("/reportes/clases")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ENTRENADOR')")
    public ResponseEntity<List<ReporteClases>> repC(){
        return ResponseEntity.ok(s.obtenerReporteClases());
    }

    /* ========= USUARIOS (solo ADMINISTRADOR) ========= */

    @GetMapping("/usuarios")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<UsuarioAdminDTO>> usuarios(){
        return ResponseEntity.ok(s.obtenerUsuariosAdmin());
    }

    @PostMapping("/usuarios")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioAdminDTO> crearUsuario(@Valid @RequestBody CrearUsuarioAdminRequest r){
        var dto = s.crearUsuarioAdmin(r);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/usuarios/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id,
                                              @Valid @RequestBody ActualizarUsuarioRequest r){
        return ResponseEntity.ok(s.actualizarUsuario(id,r));
    }

    @DeleteMapping("/usuarios/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<String> eliminar(@PathVariable Long id){
        s.eliminarUsuario(id);
        return ResponseEntity.ok("Usuario eliminado exitosamente");
    }

    /* ========= CLASES (ADMIN + ENTRENADOR) ========= */

    @GetMapping("/clases")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ENTRENADOR')")
    public ResponseEntity<List<ClaseDTO>> listarClasesAdmin() {
        return ResponseEntity.ok(claseService.obtenerClasesParaAdmin());
    }

    @PostMapping("/clases")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ENTRENADOR')")
    public ResponseEntity<Clase> crearClase(@Valid @RequestBody CrearClaseRequest r){
        return ResponseEntity.ok(s.crearClase(r));
    }

    @PutMapping("/clases/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ENTRENADOR')")
    public ResponseEntity<Clase> actClase(@PathVariable Long id,
                                          @Valid @RequestBody CrearClaseRequest r){
        return ResponseEntity.ok(s.actualizarClase(id,r));
    }

    @DeleteMapping("/clases/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ENTRENADOR')")
    public ResponseEntity<String> delClase(@PathVariable Long id){
        s.eliminarClase(id);
        return ResponseEntity.ok("Clase eliminada exitosamente");
    }

    /* ========= UBICACIONES (solo ADMINISTRADOR) ========= */

    @PostMapping("/ubicaciones")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Ubicacion> crearUb(@Valid @RequestBody CrearUbicacionRequest r){
        return ResponseEntity.ok(s.crearUbicacion(r));
    }

    @PutMapping("/ubicaciones/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Ubicacion> actUb(@PathVariable Long id,
                                           @Valid @RequestBody CrearUbicacionRequest r){
        return ResponseEntity.ok(s.actualizarUbicacion(id,r));
    }

    @DeleteMapping("/ubicaciones/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<String> delUb(@PathVariable Long id){
        s.eliminarUbicacion(id);
        return ResponseEntity.ok("Ubicación eliminada exitosamente");
    }

    /* ========= MEMBRESÍAS / PAGOS (solo ADMINISTRADOR) ========= */

    @GetMapping("/membresias")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<MembresiaAdminDTO>> membresias(){
        return ResponseEntity.ok(s.obtenerMembresiasAdmin());
    }

    @GetMapping("/pagos")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<PagoAdminDTO>> pagos(){
        return ResponseEntity.ok(s.obtenerPagosAdmin());
    }
}
