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
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PagoController {
    private final PagoService servicioPago;

    @PostMapping
    public ResponseEntity<RespuestaPago> procesarPago(@Valid @RequestBody SolicitudPago solicitud) {
        return ResponseEntity.ok(servicioPago.procesarPago(solicitud));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pago>> obtenerPagosPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(servicioPago.obtenerPagosPorUsuario(usuarioId));
    }
}
