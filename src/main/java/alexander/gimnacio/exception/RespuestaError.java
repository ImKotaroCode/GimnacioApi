package alexander.gimnacio.exception;

import java.time.LocalDateTime;

@lombok.Data
@lombok.Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
class RespuestaError {
    private LocalDateTime marcaTiempo;
    private int estado;
    private String error;
    private String mensaje;

}
