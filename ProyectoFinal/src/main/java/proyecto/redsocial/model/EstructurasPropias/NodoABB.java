package proyecto.redsocial.model.EstructurasPropias;

import lombok.Data;
import proyecto.redsocial.model.Publicacion;

import java.util.ArrayList;
import java.util.List;
@Data
public class NodoABB {
    private String tema;
    private List<Publicacion> publicaciones;
    private NodoABB izquierdo;
    private NodoABB derecho;

    public NodoABB(Publicacion publicacion) {
        this.tema = publicacion.getTema();
        this.publicaciones = new ArrayList<>();
        this.publicaciones.add(publicacion);
    }
}
