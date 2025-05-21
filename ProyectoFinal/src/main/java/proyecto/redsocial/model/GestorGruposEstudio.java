package proyecto.redsocial.model;

import proyecto.redsocial.factory.ModelFactory;

import java.util.ArrayList;
import java.util.List;

public class GestorGruposEstudio {
    private final List<GrupoEstudio> gruposPorTema = new ArrayList<>();
    private ModelFactory modelFactory;

    public GestorGruposEstudio() {
    }

    public void agregarGrupos(List<GrupoEstudio> grupos){
        gruposPorTema.addAll(grupos);
    }

    public void agregarEstudianteAGrupo(Estudiante estudiante, String tema, ModelFactory modelFactory){
        this.modelFactory = modelFactory;
        GrupoEstudio grupoEstudio = buscarGrupoPorTema(tema);
        grupoEstudio.agregarMiembro(estudiante);
        estudiante.getGruposEstudio().add(grupoEstudio);
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
        grupoEstudio.setMiembros(new ArrayList<>());
        modelFactory.agregarGrupo(grupoEstudio);

        return grupoEstudio;
    }

    public List<GrupoEstudio> obtenerGruposDeEstudiante(Estudiante estudiante){
        List<GrupoEstudio> grupos = new ArrayList<>();
        for(GrupoEstudio grupo : gruposPorTema){
            if(grupo.esMiembro(estudiante)){
                grupos.add(grupo);
            }
        }
        return grupos;
    }

    public List<GrupoEstudio> obtenerGruposFormados() {
        return gruposPorTema;
    }
}
