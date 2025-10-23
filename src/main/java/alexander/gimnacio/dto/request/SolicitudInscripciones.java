package alexander.gimnacio.dto.request;
import lombok.*;
import jakarta.validation.constraints.*;
@Data
@Builder
public class SolicitudInscripciones {
    @NotNull(message = "El ID de usuario es requerido")
    private Long usuarioId;

    @NotNull(message = "El ID de clase es requerido")
    private Long claseId;
}
