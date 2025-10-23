package alexander.gimnacio.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioAdminDTO {
    private Long id;
    private String nombreCompleto;
    private String correoElectronico;
    private String numeroTelefono;
    private String rol;
    private Boolean estaActivo;
    private LocalDateTime fechaCreacion;
}
