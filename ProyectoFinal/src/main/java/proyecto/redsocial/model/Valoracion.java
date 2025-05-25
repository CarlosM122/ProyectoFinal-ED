package proyecto.redsocial.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Valoracion implements Serializable {
    private static final long serialVersionUID = 1L;
    private Publicacion publicacion;
    private Estudiante estudiante;
    private int valoracion;
    private String comentario;

    public Valoracion() {}
}
