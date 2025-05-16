package proyecto.redsocial.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "estudiante")

public class Mensaje {
    private Estudiante estudiante;
    private String mensaje;
    private GrupoEstudio grupoEstudio;
}
