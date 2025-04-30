package proyecto.redsocial.model;

import lombok.Data;
import proyecto.redsocial.utils.RedSocialUtils;

import java.util.ArrayList;
import java.util.List;
@Data
public class Sistema {
    private List<Estudiante> estudiantes;
    private List<Moderador> moderadores;
    private ArbolContenido contenidos;
    private RedAfinidad redAfinidad;
    private ColaPrioridadAyuda colaPrioridadAyuda;
    private List<GrupoEstudio> gruposEstudio;

    public Sistema() {
        this.estudiantes = new ArrayList<>();
        this.moderadores = new ArrayList<>();
        this.gruposEstudio = new ArrayList<>();
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
}
