package alexander.gimnacio.controller;

import alexander.gimnacio.dto.request.ActualizarUsuarioRequest;
import alexander.gimnacio.dto.response.UsuarioAdminDTO;
import alexander.gimnacio.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/me")
    public ResponseEntity<UsuarioAdminDTO> me(Authentication auth) {
        var u = usuarioRepo.findByCorreoElectronico(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UsuarioAdminDTO dto = new UsuarioAdminDTO();
        dto.setId(u.getId());

        dto.setNombres(u.getNombres());
        dto.setApellidoPaterno(u.getApellidoPaterno());
        dto.setApellidoMaterno(u.getApellidoMaterno());
        dto.setDni(u.getDni());
        dto.setDireccion(u.getDireccion());

        dto.setNombreCompleto(u.getNombreCompleto());

        dto.setCorreoElectronico(u.getCorreoElectronico());
        dto.setNumeroTelefono(u.getNumeroTelefono());
        dto.setRol(u.getRol() != null ? u.getRol().name() : "USUARIO");
        dto.setEstaActivo(u.isEstaActivo());
        dto.setFechaCreacion(u.getFechaCreacion());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPerfil(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarUsuarioRequest body,
            Authentication auth
    ) {
        var u = usuarioRepo.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!u.getCorreoElectronico().equalsIgnoreCase(auth.getName())) {
            return ResponseEntity.status(403).body(Map.of("mensaje", "No autorizado"));
        }


        if (body.getNombres() != null) u.setNombres(body.getNombres().trim());
        if (body.getApellidoPaterno() != null) u.setApellidoPaterno(body.getApellidoPaterno().trim());
        if (body.getApellidoMaterno() != null) u.setApellidoMaterno(body.getApellidoMaterno().trim());

        if (body.getNumeroTelefono() != null) u.setNumeroTelefono(body.getNumeroTelefono().trim());

        if (body.getDireccion() != null) u.setDireccion(body.getDireccion().trim());

        if (body.getDni() != null) u.setDni(body.getDni().trim());



        usuarioRepo.save(u);

        return ResponseEntity.ok(Map.of("mensaje", "Perfil actualizado"));
    }

    @PutMapping("/{id}/contrasena")
    public ResponseEntity<Map<String,String>> cambiarPwd(
            @PathVariable Long id,
            @RequestBody Map<String,String> body,
            Authentication auth
    ){
        var u = usuarioRepo.findById(id).orElseThrow(() -> new RuntimeException("No existe"));
        if(!u.getCorreoElectronico().equalsIgnoreCase(auth.getName()))
            return ResponseEntity.status(403).body(Map.of("mensaje","No autorizado"));

        String actual = body.getOrDefault("actual","");
        String nueva  = body.getOrDefault("nueva","");

        if(actual.isBlank() || nueva.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("mensaje","Campos incompletos"));
        }
        if(!passwordEncoder.matches(actual, u.getContrasena()))
            return ResponseEntity.badRequest().body(Map.of("mensaje","Contraseña actual incorrecta"));

        u.setContrasena(passwordEncoder.encode(nueva));
        usuarioRepo.save(u);
        return ResponseEntity.ok(Map.of("mensaje","Actualizada"));
    }

    @GetMapping("/{id}/preferencias")
    public ResponseEntity<Map<String,Object>> getPrefs(@PathVariable Long id, Authentication auth){
        var u = usuarioRepo.findById(id).orElseThrow();
        if(!u.getCorreoElectronico().equalsIgnoreCase(auth.getName()))
            return ResponseEntity.status(403).build();

        return ResponseEntity.ok(Map.of("emailNotificaciones", true, "tema", "oscuro", "recordatorios", "none"));
    }

    @PutMapping("/{id}/preferencias")
    public ResponseEntity<Map<String,String>> putPrefs(
            @PathVariable Long id,
            @RequestBody Map<String,Object> prefs,
            Authentication auth
    ){
        var u = usuarioRepo.findById(id).orElseThrow();
        if(!u.getCorreoElectronico().equalsIgnoreCase(auth.getName()))
            return ResponseEntity.status(403).build();

        return ResponseEntity.ok(Map.of("mensaje","Preferencias guardadas"));
    }
}
