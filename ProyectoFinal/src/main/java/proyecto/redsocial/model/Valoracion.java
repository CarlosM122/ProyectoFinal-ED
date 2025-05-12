package proyecto.redsocial.model;

import lombok.Data;

@Data
public class Valoracion {
    private Publicacion publicacion;
    private Estudiante estudiante;
    private int valoracion;
    private String comentario;
}
