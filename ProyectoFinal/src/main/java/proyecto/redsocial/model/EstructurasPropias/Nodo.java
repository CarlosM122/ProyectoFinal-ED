package proyecto.redsocial.model.EstructurasPropias;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class Nodo <T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private T dato;
    private Nodo<T> siguiente;

    public Nodo() {
    }

    public Nodo(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }
}
