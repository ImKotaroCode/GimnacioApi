package alexander.gimnacio.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "clases")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nombre_clase", nullable = false)
    private String nombreClase;

    @Column(length = 500)
    private String descripcion;

    @NotBlank
    @Column(nullable = false)
    private String horario;

    @Column(name = "nombre_instructor")
    private String nombreInstructor;

    @Column(name = "capacidad_maxima", nullable = false)
    private Integer capacidadMaxima = 20;

    @Column(name = "duracion_minutos")
    private Integer duracionMinutos = 60;

    @JsonIgnore
    @ManyToMany(mappedBy = "clasesInscritas")
    private Set<Usuario> usuariosInscritos = new HashSet<>();

    @Column(name = "esta_activo")
    private boolean estaActivo = true;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    public int obtenerCuposDisponibles() {
        return capacidadMaxima - usuariosInscritos.size();
    }
}
