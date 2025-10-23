package alexander.gimnacio.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearClaseRequest {
    @NotBlank
    private String nombreClase;
    private String descripcion;
    @NotBlank
    private String horario;
    private String nombreInstructor;
    private Integer capacidadMaxima;
    private Integer duracionMinutos;
}
