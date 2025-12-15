package alexander.gimnacio.dto.request;

import jakarta.validation.constraints.Email;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarUsuarioRequest {
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String dni;
    private String direccion;

    @Email
    private String correoElectronico;

    private String numeroTelefono;
    private String rol;
    private Boolean estaActivo;
}
