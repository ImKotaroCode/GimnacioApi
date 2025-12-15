package alexander.gimnacio.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioAdminDTO {
    private Long id;

    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String dni;
    private String direccion;

    private String nombreCompleto;

    private String correoElectronico;
    private String numeroTelefono;
    private String rol;
    private Boolean estaActivo;
    private LocalDateTime fechaCreacion;
}
