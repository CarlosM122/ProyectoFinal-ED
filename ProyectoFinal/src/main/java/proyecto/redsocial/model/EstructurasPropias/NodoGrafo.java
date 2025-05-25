package proyecto.redsocial.model.EstructurasPropias;

import lombok.Getter;
import lombok.Setter;
import proyecto.redsocial.model.Estudiante;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class NodoGrafo {
    private Estudiante estudiante;
    private List<NodoGrafo> adyacentes;

    public NodoGrafo(Estudiante estudiante) {
        this.estudiante = estudiante;
        this.adyacentes = new ArrayList<>();
    }
}