package alexander.gimnacio.dto.request;
import lombok.*;
import jakarta.validation.constraints.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudPago {
    @NotNull(message = "El ID de membresía es requerido")
    private Long membresiaId;

    @NotNull(message = "El ID de usuario es requerido")
    private Long usuarioId;

    @NotBlank(message = "El nombre en la tarjeta es requerido")
    private String nombreTarjeta;

    @NotBlank(message = "El número de tarjeta es requerido")
    @Pattern(regexp = "^[0-9]{13,19}$", message = "Número de tarjeta inválido")
    private String numeroTarjeta;

    @NotBlank(message = "La fecha de expiración es requerida")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/([0-9]{2})$", message = "Formato de fecha inválido (MM/AA)")
    private String fechaExpiracion;

    @NotBlank(message = "El CVV es requerido")
    @Pattern(regexp = "^[0-9]{3,4}$", message = "CVV inválido")
    private String cvv;

    @NotNull(message = "El monto es requerido")
    @Positive(message = "El monto debe ser positivo")
    private Double monto;
}
