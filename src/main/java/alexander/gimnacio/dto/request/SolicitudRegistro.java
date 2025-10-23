package alexander.gimnacio.dto.request;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudRegistro {
    @NotBlank(message = "El nombre completo es requerido")
    private String nombreCompleto;

    @Email(message = "Formato de correo inválido")
    @NotBlank(message = "El correo es requerido")
    private String correoElectronico;

    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String contrasena;

    @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "Número de teléfono inválido")
    private String numeroTelefono;
}
