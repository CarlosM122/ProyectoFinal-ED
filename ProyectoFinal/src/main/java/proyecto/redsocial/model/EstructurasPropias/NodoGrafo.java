package proyecto.redsocial.model.EstructurasPropias;

import lombok.Getter;
import lombok.Setter;
import proyecto.redsocial.model.Estudiante;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class NodoGrafo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Estudiante estudiante;
    private List<NodoGrafo> adyacentes;

    public NodoGrafo() {
    }

    public NodoGrafo(Estudiante estudiante) {
        this.estudiante = estudiante;
        this.adyacentes = new ArrayList<>();
    }
}