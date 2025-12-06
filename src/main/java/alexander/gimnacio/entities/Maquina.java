package alexander.gimnacio.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "maquina")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Maquina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    private String descripcion;

    private LocalDate fechaUltimoMantenimiento;

    @ManyToOne
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;

    public enum Categoria { CARDIO, FUERZA, FUNCIONAL }
    public enum Estado { OPERATIVA, MANTENIMIENTO, FUERA_SERVICIO }
}
