package alexander.gimnacio.dto.request;
import lombok.*;
import jakarta.validation.constraints.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudContacto {
    @NotBlank(message = "El nombre es requerido")
    private String nombreRemitente;

    @Email(message = "Formato de correo inválido")
    @NotBlank(message = "El correo es requerido")
    private String correoRemitente;

    @NotBlank(message = "El asunto es requerido")
    private String asunto;

    @NotBlank(message = "El mensaje es requerido")
    @Size(max = 2000, message = "El mensaje no puede exceder 2000 caracteres")
    private String mensaje;
}

