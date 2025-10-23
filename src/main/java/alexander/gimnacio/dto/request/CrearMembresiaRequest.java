// Nuevo DTO MUY simple para la petición:
package alexander.gimnacio.dto.request;
import alexander.gimnacio.entities.Membresia;
import lombok.Data;

@Data
public class CrearMembresiaRequest {
    private Long usuarioId;
    private Membresia.TipoPlan tipoPlan;
    private Boolean renovacionAutomatica;
}
