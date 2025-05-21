package proyecto.redsocial.model;

import lombok.Data;
import proyecto.redsocial.model.EstructurasPropias.ArbolABB;
import proyecto.redsocial.model.EstructurasPropias.GrafoAfinidad;
import proyecto.redsocial.utils.RedSocialUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Sistema implements Serializable {
    private List<Estudiante> estudiantes;
    private List<Moderador> moderadores;
    private List<Publicacion> publicacions;
    private GrafoAfinidad grafoAfinidad;
    private ColaPrioridadAyuda colaPrioridadAyuda;
    private List<GrupoEstudio> gruposEstudio;
    private transient ArbolABB arbolPublicaciones;
    private GestorGruposEstudio gestorGruposEstudio;

    public Sistema() {
        this.estudiantes = new ArrayList<>();
        this.moderadores = new ArrayList<>();
        this.gruposEstudio = new ArrayList<>();
        this.publicacions = new ArrayList<>();
        this.grafoAfinidad = new GrafoAfinidad();
        this.colaPrioridadAyuda = new ColaPrioridadAyuda();
        this.gestorGruposEstudio = new GestorGruposEstudio();
    }

    public void inicializarSistema() {
        this.cargarArbol();
        this.gestorGruposEstudio.agregarGrupos(gruposEstudio);
    }

    public void cargarArbol() {
        arbolPublicaciones = new ArbolABB();
        for (Publicacion publicacion : publicacions) {
            arbolPublicaciones.insertar(publicacion);
        }
    }

    public Estudiante buscarEstudiante(String correo) {
        Estudiante estudiante = null;
        for (Estudiante est : estudiantes) {
            if (est.getCorreo().equals(correo)) {
                estudiante = est;
                break;
            }
        }
        return estudiante;
    }

    public void guardarEstudiante(Estudiante estudiante) {
        estudiante.setId(estudiantes.size()+1);
        estudiantes.add(estudiante);
    }

    public Moderador buscarModerador(String correo) {
        for (Moderador mod : moderadores) {
            if (mod.getCorreo().equals(correo)) {
                return mod;
            }
        }
        return null;
    }

    public List<Publicacion> cargarPublicaciones() {
        return publicacions;
    }
}
