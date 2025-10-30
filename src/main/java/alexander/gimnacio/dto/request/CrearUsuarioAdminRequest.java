package alexander.gimnacio.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CrearUsuarioAdminRequest {
    @NotBlank
    private String nombreCompleto;

    @Email @NotBlank
    private String correoElectronico;

    // opcional: si viene vacío, pones una por defecto
    private String contrasena;

    private String numeroTelefono; // opcional

    @NotBlank
    private String rol; // "USUARIO" | "ADMINISTRADOR" | "ENTRENADOR"

    private Boolean estaActivo; // opcional (default true)
}
