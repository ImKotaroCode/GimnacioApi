package alexander.gimnacio.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PagoAdminDTO{
        private Long id;
        private Long usuarioId;
        private String usuarioNombre;
        private Long membresiaId;
        private Double monto;
        private  LocalDateTime fechaPago;
        private String estadoPago;
        private String metodoPago;
        private String idTransaccion;
}
