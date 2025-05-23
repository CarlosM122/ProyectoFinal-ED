package proyecto.redsocial.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class GrupoEstudio    {
    private String idGrupoEstudio;
    private String tema;
    private List<Estudiante> miembros;
    private List<Publicacion> publicaciones = new ArrayList<>();

    public GrupoEstudio() {
    }
    public void agregarMiembro(Estudiante estudiante){
        miembros.add(estudiante);
    }
    public boolean esMiembro(Estudiante estudiante){
        return miembros.contains(estudiante);
    }
}
