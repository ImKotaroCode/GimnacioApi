package alexander.gimnacio.dto.response;

import alexander.gimnacio.entities.Membresia;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MembresiaResponse {
    private Long id;
    private Long usuarioId;
    private Membresia.TipoPlan tipoPlan;
    private Double precio;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Membresia.Estado estado;
    private boolean renovacionAutomatica;
}
