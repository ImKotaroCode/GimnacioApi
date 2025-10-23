package alexander.gimnacio.dto.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespuestaAutenticacion {
    private String token;
    private String tipo = "Bearer";
    private Long usuarioId;
    private String correoElectronico;
    private String nombreCompleto;
    private String rol;
}