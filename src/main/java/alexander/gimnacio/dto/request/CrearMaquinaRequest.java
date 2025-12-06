package alexander.gimnacio.dto.request;

import alexander.gimnacio.entities.Maquina;
import lombok.Data;

@Data
public class CrearMaquinaRequest {

    private String nombre;
    private String descripcion;

    private Maquina.Categoria categoria;

    private Long ubicacionId;

    private Maquina.Estado estado;
}