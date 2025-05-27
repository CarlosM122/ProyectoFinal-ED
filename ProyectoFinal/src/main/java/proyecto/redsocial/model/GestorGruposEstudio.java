package proyecto.redsocial.model;

import proyecto.redsocial.factory.ModelFactory;
import proyecto.redsocial.model.EstructurasPropias.ListaEnlazada;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GestorGruposEstudio implements Serializable {
    private static final long serialVersionUID = 1L;
    private final ListaEnlazada<GrupoEstudio> gruposPorTema = new ListaEnlazada<>();
    private ModelFactory modelFactory;

    public GestorGruposEstudio() {
    }

    public void agregarGrupos(ListaEnlazada<GrupoEstudio> grupos) {
        gruposPorTema.addAll(grupos.aLista());
    }

    public void agregarEstudianteAGrupo(Estudiante estudiante, String tema, ModelFactory modelFactory) {
        this.modelFactory = modelFactory;
        GrupoEstudio grupoEstudio = buscarGrupoPorTema(tema);
        if (grupoEstudio.esMiembro(estudiante)) {
            return;
        }
        grupoEstudio.agregarMiembro(estudiante);
        estudiante.getGruposEstudio().agregar(grupoEstudio);
    }

    public void asignarEstudiantesAGrupos(List<Estudiante> estudiantes) {
        for (Estudiante estudiante : estudiantes) {
            for (String interes : estudiante.getIntereses()) {
                GrupoEstudio grupo = buscarGrupoPorTema(interes);
                grupo.agregarMiembro(estudiante);
            }
        }
    }

    private GrupoEstudio buscarGrupoPorTema(String tema) {
        for (GrupoEstudio grupo : gruposPorTema) {
            if (grupo.getTema().equalsIgnoreCase(tema)) {
                return grupo;
            }
        }
        GrupoEstudio grupoEstudio = new GrupoEstudio();
        grupoEstudio.setIdGrupoEstudio("grupo_" + tema.toLowerCase());
        grupoEstudio.setTema(tema);
        grupoEstudio.setMiembros(new ListaEnlazada<>());
        modelFactory.agregarGrupo(grupoEstudio);

        return grupoEstudio;
    }

    public List<GrupoEstudio> obtenerGruposDeEstudiante(Estudiante estudiante) {
        List<GrupoEstudio> grupos = new ArrayList<>();
        for (GrupoEstudio grupo : gruposPorTema) {
            if (grupo.esMiembro(estudiante)) {
                grupos.add(grupo);
            }
        }
        return grupos;
    }

    public List<GrupoEstudio> obtenerGruposFormados() {
        return gruposPorTema.aLista();
    }
}
