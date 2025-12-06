package alexander.gimnacio.controller;

import alexander.gimnacio.dto.request.CrearMaquinaRequest;
import alexander.gimnacio.dto.response.MaquinaAdminDTO;
import alexander.gimnacio.entities.Maquina;
import alexander.gimnacio.service.MaquinaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/maquinas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@PreAuthorize("hasAnyRole('ADMINISTRADOR')")

public class MaquinaController {

    private final MaquinaService maquinaService;


    @GetMapping
    public ResponseEntity<List<MaquinaAdminDTO>> listarTodas() {
        return ResponseEntity.ok(maquinaService.obtenerMaquinas());
    }

    @GetMapping("/ubicacion/{ubicacionId}")
    public ResponseEntity<List<MaquinaAdminDTO>> listarPorUbicacion(@PathVariable Long ubicacionId) {
        return ResponseEntity.ok(maquinaService.obtenerMaquinasPorUbicacion(ubicacionId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<MaquinaAdminDTO>> listarPorEstado(@PathVariable String estado) {
        Maquina.Estado est = Maquina.Estado.valueOf(estado.toUpperCase());
        return ResponseEntity.ok(maquinaService.obtenerMaquinasPorEstado(est));
    }


    @PostMapping
    public ResponseEntity<MaquinaAdminDTO> crear(@RequestBody CrearMaquinaRequest request) {
        MaquinaAdminDTO dto = maquinaService.crearMaquina(request);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaquinaAdminDTO> actualizar(
            @PathVariable Long id,
            @RequestBody CrearMaquinaRequest request
    ) {
        MaquinaAdminDTO dto = maquinaService.actualizarMaquina(id, request);
        return ResponseEntity.ok(dto);
    }


    @PatchMapping("/{id}/estado")
    public ResponseEntity<MaquinaAdminDTO> cambiarEstado(
            @PathVariable Long id,
            @RequestParam("nuevoEstado") String nuevoEstado
    ) {
        Maquina.Estado estado = Maquina.Estado.valueOf(nuevoEstado.toUpperCase());
        MaquinaAdminDTO dto = maquinaService.cambiarEstadoMaquina(id, estado);
        return ResponseEntity.ok(dto);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        maquinaService.eliminarMaquina(id);
        return ResponseEntity.noContent().build();
    }

}