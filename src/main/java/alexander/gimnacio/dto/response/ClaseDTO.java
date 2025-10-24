package alexander.gimnacio.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaseDTO {
    private Long id;
    private String nombreClase;
    private String descripcion;
    private String horario;
    private String nombreInstructor;
    private Integer capacidadMaxima;
    private Integer duracionMinutos;
    private Integer usuariosInscritos;
    private boolean estaActivo;

    public Integer obtenerCuposDisponibles() {
        return capacidadMaxima - usuariosInscritos;
    }
}