package proyecto.redsocial.model.EstructurasPropias;

import lombok.Data;
import proyecto.redsocial.model.Estudiante;

import java.util.ArrayList;
import java.util.List;
@Data
public class NodoGrafo {
    private Estudiante estudiante;
    private List<NodoGrafo> adyacentes;

    public NodoGrafo(Estudiante estudiante) {
        this.estudiante = estudiante;
        this.adyacentes = new ArrayList<>();
    }

    public void agregarAdyacente(NodoGrafo nodo){
        if(!adyacentes.contains(nodo)){
            adyacentes.add(nodo);
        }
    }

}
