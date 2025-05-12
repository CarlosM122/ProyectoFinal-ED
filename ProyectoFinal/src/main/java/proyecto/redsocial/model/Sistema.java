package proyecto.redsocial.model;

import lombok.Data;
import proyecto.redsocial.utils.RedSocialUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class Sistema {
    private List<Estudiante> estudiantes;
    private List<Moderador> moderadores;
    private List<Publicacion> publicacions;
    private RedAfinidad redAfinidad;
    private ColaPrioridadAyuda colaPrioridadAyuda;
    private List<GrupoEstudio> gruposEstudio;
    private transient ArbolABB arbolPublicaciones;

    public Sistema() {
        this.estudiantes = new ArrayList<>();
        this.moderadores = new ArrayList<>();
        this.gruposEstudio = new ArrayList<>();
        this.publicacions = new ArrayList<>();
        this.redAfinidad = new RedAfinidad();
        this.colaPrioridadAyuda = new ColaPrioridadAyuda();
        cargarArbol();
    }

    private void cargarArbol() {
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

    public void guardarEstudiante(String nombre, String correo, String contrasenia) {
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre(nombre);
        estudiante.setCorreo(correo);
        String contraseniaEncriptada = RedSocialUtils.encriptarSHA256(contrasenia);
        estudiante.setContrasenia(contraseniaEncriptada);
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
