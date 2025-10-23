package alexander.gimnacio.dto.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReporteMembresias {
    private Long totalMembresias;
    private Long membresiaBasico;
    private Long membresiaPremium;
    private Long membresiaAnual;
    private Long membresiaActivas;
    private Long membresiasPausadas;
    private Long membresiasCanceladas;
    private Long membresiasExpiradas;
}
