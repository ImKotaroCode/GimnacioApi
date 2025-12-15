package alexander.gimnacio.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudRegistro {

    @NotBlank(message = "Los nombres son requeridos")
    private String nombres;

    @NotBlank(message = "Apellido paterno requerido")
    private String apellidoPaterno;

    @NotBlank(message = "Apellido materno requerido")
    private String apellidoMaterno;

    @NotBlank(message = "DNI requerido")
    @Pattern(regexp = "^\\d{8}$", message = "DNI inválido (8 dígitos)")
    private String dni;

    @NotBlank(message = "Dirección requerida")
    private String direccion;

    @Email(message = "Formato de correo inválido")
    @NotBlank(message = "El correo es requerido")
    private String correoElectronico;

    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String contrasena;

    @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "Número de teléfono inválido")
    private String numeroTelefono;
}
