package alexander.gimnacio.dto.response;

import lombok.Data;


@Data
public class MaquinaAdminDTO {

    private Long id;
    private String nombre;
    private String categoria;
    private String estado;
    private String descripcion;

    private String fechaUltimoMantenimiento;

    private Long ubicacionId;
    private String nombreUbicacion;
    private String distrito;
    private String ciudad;
}
