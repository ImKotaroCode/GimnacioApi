package alexander.gimnacio.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CrearUsuarioAdminRequest {
    @NotBlank
    private String nombreCompleto;

    @Email @NotBlank
    private String correoElectronico;

    private String contrasena;

    private String numeroTelefono;

    @NotBlank
    private String rol;

    private Boolean estaActivo;
}
