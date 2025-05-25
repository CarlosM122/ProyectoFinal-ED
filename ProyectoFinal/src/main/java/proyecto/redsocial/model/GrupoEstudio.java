package proyecto.redsocial.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GrupoEstudio implements Serializable {
    private static final long serialVersionUID = 1L;
    private String idGrupoEstudio;
    private String tema;
    private List<Estudiante> miembros;
    private List<Publicacion> publicaciones = new ArrayList<>();

    public GrupoEstudio() {
    }

    public void agregarMiembro(Estudiante estudiante) {
        miembros.add(estudiante);
    }

    public boolean esMiembro(Estudiante estudiante) {
        return miembros.contains(estudiante);
    }
}
