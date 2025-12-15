package alexander.gimnacio.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nombres", nullable = false)
    private String nombres;

    @NotBlank
    @Column(name = "apellido_paterno", nullable = false)
    private String apellidoPaterno;

    @NotBlank
    @Column(name = "apellido_materno", nullable = false)
    private String apellidoMaterno;

    @NotBlank
    @Column(name = "dni", nullable = false, unique = true, length = 8)
    private String dni;

    @NotBlank
    @Column(name = "direccion", nullable = false)
    private String direccion;

    @Transient
    public String getNombreCompleto() {
        String n = (nombres == null) ? "" : nombres.trim();
        String ap = (apellidoPaterno == null) ? "" : apellidoPaterno.trim();
        String am = (apellidoMaterno == null) ? "" : apellidoMaterno.trim();
        return (n + " " + ap + " " + am).trim();
    }

    @Email
    @NotBlank
    @Column(name = "correo_electronico", unique = true, nullable = false)
    private String correoElectronico;

    @NotBlank
    @Column(name = "contrasena", nullable = false)
    @JsonIgnore
    private String contrasena;

    @Column(name = "numero_telefono")
    private String numeroTelefono;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol = Rol.USUARIO;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Membresia membresia;

    @ManyToMany
    @JoinTable(
            name = "usuarios_clases",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "clase_id")
    )
    @JsonIgnore
    private Set<Clase> clasesInscritas = new HashSet<>();

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "esta_activo")
    private boolean estaActivo = true;

    public enum Rol { USUARIO, ADMINISTRADOR, ENTRENADOR }
}
