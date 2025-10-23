package alexander.gimnacio.dto.request;

import alexander.gimnacio.entities.Membresia;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SolicitudMembresia {
    @NotNull
    private Long usuarioId;

    @NotNull
    private Membresia.TipoPlan tipoPlan;
    private Boolean renovacionAutomatica;
}
