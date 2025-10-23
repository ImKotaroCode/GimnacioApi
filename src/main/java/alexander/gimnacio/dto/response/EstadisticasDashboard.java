package alexander.gimnacio.dto.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticasDashboard {
    private Long totalUsuarios;
    private Long usuariosActivos;
    private Long totalMembresias;
    private Long membresiasActivas;
    private Long totalClases;
    private Long totalUbicaciones;
    private Double ingresosMensuales;
    private Double ingresosAnuales;
    private Long inscripcionesClasesMes;
    private Long nuevosMiembrosMes;
}
