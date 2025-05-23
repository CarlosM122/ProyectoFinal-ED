package proyecto.redsocial.model;

import lombok.Data;
import proyecto.redsocial.model.EstructurasPropias.ArbolABB;
import proyecto.redsocial.model.EstructurasPropias.ColaPrioridadSolicitudes;
import proyecto.redsocial.model.EstructurasPropias.GrafoAfinidad;
import proyecto.redsocial.model.EstructurasPropias.ListaEnlazada;
import proyecto.redsocial.utils.RedSocialUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Sistema implements Serializable {
    private List<Estudiante> estudiantes;
    private ListaEnlazada<Estudiante> listaEstudiantes;
    private List<Moderador> moderadores;
    private ListaEnlazada<Moderador> listaModeradores;
    private List<Publicacion> publicacions;
    private ListaEnlazada<Publicacion> listaPublicaciones;
    private List<SolicitudAyuda> listaSolicitudesAyuda;
    private GrafoAfinidad grafoAfinidad;
    private ColaPrioridadSolicitudes colaPrioridadSolicitudes;
    private List<GrupoEstudio> gruposEstudio;
    private transient ArbolABB arbolPublicaciones;
    private GestorGruposEstudio gestorGruposEstudio;

    public Sistema() {
        this.estudiantes = new ArrayList<>();
        this.moderadores = new ArrayList<>();
        this.gruposEstudio = new ArrayList<>();
        this.publicacions = new ArrayList<>();
        this.grafoAfinidad = new GrafoAfinidad();
        this.gestorGruposEstudio = new GestorGruposEstudio();
        this.listaEstudiantes = new ListaEnlazada<>();
        this.listaModeradores = new ListaEnlazada<>();
        this.listaPublicaciones = new ListaEnlazada<>();
        this.colaPrioridadSolicitudes = new ColaPrioridadSolicitudes();
        this.listaSolicitudesAyuda = new ArrayList<>();
    }

    public void inicializarSistema() {
        this.cargarArbol();
        this.gestorGruposEstudio.agregarGrupos(gruposEstudio);
        for (SolicitudAyuda solicitudAyuda : listaSolicitudesAyuda) {
            colaPrioridadSolicitudes.insertar(solicitudAyuda);
        }
        for (Estudiante estudiante : estudiantes) {
            listaEstudiantes.agregar(estudiante);
        }
        for (Moderador moderador : moderadores) {
            listaModeradores.agregar(moderador);
        }
        for (Publicacion publicacion : publicacions) {
            listaPublicaciones.agregar(publicacion);
        }
    }

    public void cargarArbol() {
        arbolPublicaciones = new ArbolABB();
        for (Publicacion publicacion : publicacions) {
            arbolPublicaciones.insertar(publicacion);
        }
    }

    public Estudiante buscarEstudiante(String correo) {
        Estudiante estudiante = null;
        for (Estudiante est : listaEstudiantes) {
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
        listaEstudiantes.agregar(estudiante);
    }

    public Moderador buscarModerador(String correo) {
        for (Moderador mod : listaModeradores) {
            if (mod.getCorreo().equals(correo)) {
                return mod;
            }
        }
        return null;
    }

    public List<Publicacion> cargarPublicaciones() {
        return listaPublicaciones.aLista();
    }
}
