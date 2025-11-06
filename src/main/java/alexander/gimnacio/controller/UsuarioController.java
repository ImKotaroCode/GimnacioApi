package alexander.gimnacio.controller;

import alexander.gimnacio.dto.response.UsuarioAdminDTO;
import alexander.gimnacio.repository.UsuarioRepository;
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
        // auth.getName() = correo
        var u = usuarioRepo.findByCorreoElectronico(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UsuarioAdminDTO dto = new UsuarioAdminDTO();
        dto.setId(u.getId());
        dto.setNombreCompleto(u.getNombreCompleto());
        dto.setCorreoElectronico(u.getCorreoElectronico());
        dto.setNumeroTelefono(u.getNumeroTelefono());
        dto.setRol(u.getRol() != null ? u.getRol().name() : "USUARIO");
        dto.setEstaActivo(u.isEstaActivo());
        dto.setFechaCreacion(u.getFechaCreacion());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/contrasena")
    public ResponseEntity<Map<String,String>> cambiarPwd(@PathVariable Long id,
                                                         @RequestBody Map<String,String> body,
                                                         Authentication auth){
        var u = usuarioRepo.findById(id).orElseThrow(() -> new RuntimeException("No existe"));
        if(!u.getCorreoElectronico().equalsIgnoreCase(auth.getName()))
            return ResponseEntity.status(403).body(Map.of("mensaje","No autorizado"));

        // Espera { "actual": "...", "nueva": "..." }
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

        // TODO: sustituir por lectura real si tienes entidad/JSON en BD
        return ResponseEntity.ok(Map.of("emailNotificaciones", true, "tema", "oscuro"));
    }

    @PutMapping("/{id}/preferencias")
    public ResponseEntity<Map<String,String>> putPrefs(@PathVariable Long id,
                                                       @RequestBody Map<String,Object> prefs,
                                                       Authentication auth){
        var u = usuarioRepo.findById(id).orElseThrow();
        if(!u.getCorreoElectronico().equalsIgnoreCase(auth.getName()))
            return ResponseEntity.status(403).build();

        // TODO: persistir en BD si corresponde (entidad Preferencias o columna JSON)
        return ResponseEntity.ok(Map.of("mensaje","Preferencias guardadas"));
    }
}
