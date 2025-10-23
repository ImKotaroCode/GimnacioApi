package alexander.gimnacio.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarUsuarioRequest {
    private String nombreCompleto;
    private String numeroTelefono;
    private String rol;
    private Boolean estaActivo;
}
