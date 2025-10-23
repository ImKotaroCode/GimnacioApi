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
@RequestMapping("/api/autenticacion")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AutenticacionService servicioAutenticacion;

    @PostMapping("/registrar")
    public ResponseEntity<RespuestaAutenticacion> registrar(@Valid @RequestBody SolicitudRegistro solicitud) {
        return ResponseEntity.ok(servicioAutenticacion.registrar(solicitud));
    }

    @PostMapping("/iniciar-sesion")
    public ResponseEntity<RespuestaAutenticacion> iniciarSesion(@Valid @RequestBody SolicitudInicioSesion solicitud) {
        return ResponseEntity.ok(servicioAutenticacion.iniciarSesion(solicitud));
    }
}
