package alexander.gimnacio.dto.request;
import lombok.*;
import jakarta.validation.constraints.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudInicioSesion {
    @Email(message = "Formato de correo inválido")
    @NotBlank(message = "El correo es requerido")
    private String correoElectronico;

    @NotBlank(message = "La contraseña es requerida")
    private String contrasena;
}

