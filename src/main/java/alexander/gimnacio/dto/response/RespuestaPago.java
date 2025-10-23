package alexander.gimnacio.dto.response;

import lombok.*;

@Data
@Builder
public class RespuestaPago {
    private Long pagoId;
    private String estado;
    private String idTransaccion;
    private String fechaPago;
    private String mensaje;
}