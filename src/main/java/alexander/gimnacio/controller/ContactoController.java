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
@RequestMapping("/api/contacto")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ContactoController {
    private final ContactoService servicioContacto;

    @PostMapping
    public ResponseEntity<String> enviarMensajeContacto(@Valid @RequestBody SolicitudContacto solicitud) {
        return ResponseEntity.ok(servicioContacto.guardarMensajeContacto(solicitud));
    }

    @GetMapping
    public ResponseEntity<List<MensajeContacto>> obtenerTodosLosMensajes() {
        return ResponseEntity.ok(servicioContacto.obtenerTodosLosMensajes());
    }

    @GetMapping("/no-leidos")
    public ResponseEntity<List<MensajeContacto>> obtenerMensajesNoLeidos() {
        return ResponseEntity.ok(servicioContacto.obtenerMensajesNoLeidos());
    }
}
