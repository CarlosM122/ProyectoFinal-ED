package proyecto.redsocial.model.EstructurasPropias;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Nodo <T>{
    private T dato;
    private Nodo<T> siguiente;

    public Nodo() {
    }

    public Nodo(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }
}
