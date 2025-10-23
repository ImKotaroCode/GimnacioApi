package alexander.gimnacio.dto.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReporteClases {
    private String nombreClase;
    private Integer capacidadMaxima;
    private Integer usuariosInscritos;
    private Double tasaOcupacion;
    private String nombreInstructor;
}
