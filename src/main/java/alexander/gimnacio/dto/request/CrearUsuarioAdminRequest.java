package alexander.gimnacio.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CrearUsuarioAdminRequest {

    @NotBlank private String nombres;
    @NotBlank private String apellidoPaterno;
    @NotBlank private String apellidoMaterno;

    @NotBlank
    @Pattern(regexp = "^\\d{8}$")
    private String dni;

    @NotBlank private String direccion;

    @Email @NotBlank
    private String correoElectronico;

    private String contrasena;
    private String numeroTelefono;

    @NotBlank
    private String rol;

    private Boolean estaActivo;
}
