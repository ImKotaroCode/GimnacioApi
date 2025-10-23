package alexander.gimnacio.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearUbicacionRequest {
    @NotBlank
    private String nombreUbicacion;
    @NotBlank
    private String direccion;
    private String distrito;
    private String ciudad;
    private String numeroTelefono;
    private String horario;
    private Double latitud;
    private Double longitud;
}
