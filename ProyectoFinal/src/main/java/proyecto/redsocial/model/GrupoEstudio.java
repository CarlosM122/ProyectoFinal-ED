package proyecto.redsocial.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import proyecto.redsocial.model.EstructurasPropias.ListaEnlazada;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GrupoEstudio implements Serializable {
    private static final long serialVersionUID = 1L;
    private String idGrupoEstudio;
    private String tema;
    private ListaEnlazada<Estudiante> miembros;
    private ListaEnlazada<Publicacion> publicaciones = new ListaEnlazada<>();

    public GrupoEstudio() {
    }

    public void agregarMiembro(Estudiante estudiante) {
        miembros.agregar(estudiante);
    }

    public boolean esMiembro(Estudiante estudiante) {
        return miembros.contiene(estudiante);
    }
}
