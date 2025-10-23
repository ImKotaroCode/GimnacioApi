package alexander.gimnacio.entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ubicaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ubicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nombre_ubicacion", nullable = false)
    private String nombreUbicacion;

    @NotBlank
    @Column(nullable = false)
    private String direccion;

    private String distrito;

    private String ciudad;

    @Column(name = "numero_telefono")
    private String numeroTelefono;

    @Column(nullable = false)
    private String horario = "24/7";

    private Double latitud;

    private Double longitud;

    @Column(name = "esta_activo")
    private boolean estaActivo = true;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
}
