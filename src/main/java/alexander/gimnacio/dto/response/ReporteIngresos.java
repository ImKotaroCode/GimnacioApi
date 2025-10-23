package alexander.gimnacio.dto.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReporteIngresos {
    private String periodo;
    private Double totalIngresos;
    private Long cantidadPagos;
    private Double promedioIngreso;
    private String metodoPagoMasUsado;
}

