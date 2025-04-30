package proyecto.redsocial.model;

import lombok.Data;

@Data
public class Valoracion {
    private Contenido contenido;
    private Estudiante estudiante;
    private int valoracion;
    private String comentario;
}
