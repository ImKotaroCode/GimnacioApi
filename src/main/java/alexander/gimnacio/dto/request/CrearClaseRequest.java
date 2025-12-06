package alexander.gimnacio.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    private String nombreInstructor;

    @NotNull
    @Min(1)
    private Integer capacidadMaxima;

    @NotNull
    @Min(1)
    private Integer duracionMinutos;

    private Boolean estaActivo;
}