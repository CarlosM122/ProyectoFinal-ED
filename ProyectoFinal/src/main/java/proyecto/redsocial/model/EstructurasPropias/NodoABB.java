package proyecto.redsocial.model.EstructurasPropias;

import lombok.Data;
import proyecto.redsocial.model.Publicacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Data
public class NodoABB implements Serializable {
    private static final long serialVersionUID = 1L;
    private String tema;
    private ListaEnlazada<Publicacion> publicaciones;
    private NodoABB izquierdo;
    private NodoABB derecho;

    public NodoABB(Publicacion publicacion) {
        this.tema = publicacion.getTema();
        this.publicaciones = new ListaEnlazada<>();
        this.publicaciones.agregar(publicacion);
    }
}
