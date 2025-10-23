package alexander.gimnacio.entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;


@Entity
@Table(name = "mensajes_contacto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MensajeContacto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nombre_remitente", nullable = false)
    private String nombreRemitente;

    @Email
    @NotBlank
    @Column(name = "correo_remitente", nullable = false)
    private String correoRemitente;

    @NotBlank
    @Column(nullable = false)
    private String asunto;

    @NotBlank
    @Column(length = 2000, nullable = false)
    private String mensaje;

    @Column(name = "esta_leido")
    private boolean estaLeido = false;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;
}
