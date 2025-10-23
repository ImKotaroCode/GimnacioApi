package alexander.gimnacio.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MembresiaAdminDTO {
    private Long id;
    private Long usuarioId;
    private String usuarioNombre;
    private String tipoPlan;
    private Double precio;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String estado;
    private Boolean renovacionAutomatica;
}
