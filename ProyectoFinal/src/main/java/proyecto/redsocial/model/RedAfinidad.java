package proyecto.redsocial.model;

import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
public class RedAfinidad {
    private List<Estudiante> nodos;
    private Map<Estudiante, List<Estudiante>> conexiones;

    public void agregarConexion(Estudiante estudianteA, Estudiante estudianteB){}
    public List<Estudiante> eliminarConexion(Estudiante estudiante){
        return null;
    }
    public List<Estudiante> rutaMasCorta(Estudiante estudianteA, Estudiante estudianteB){
        return null;
    }
    public List<List<Estudiante>> detectarComunidades(){
        return null;
    }
}
