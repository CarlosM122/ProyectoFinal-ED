package proyecto.redsocial.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString(exclude = "estudiante")

public class Mensaje implements Serializable {
    private static final long serialVersionUID = 1L;
    private Estudiante estudiante;
    private String mensaje;
    private GrupoEstudio grupoEstudio;

    public Mensaje() {
    }
}
