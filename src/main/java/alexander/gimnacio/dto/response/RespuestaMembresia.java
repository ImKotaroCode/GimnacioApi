package alexander.gimnacio.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RespuestaMembresia {
    private Long id;
    private String tipoPlan;
    private Double precio;
    private String fechaInicio;
    private String fechaFin;
    private String estado;
    private boolean renovacionAutomatica;
}
