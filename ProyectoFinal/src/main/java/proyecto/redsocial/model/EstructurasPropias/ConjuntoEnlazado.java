package proyecto.redsocial.model.EstructurasPropias;

import java.io.Serializable;

public class ConjuntoEnlazado<T> implements Serializable {
    private ListaEnlazada<T> elementos;

    public ConjuntoEnlazado() {
        elementos = new ListaEnlazada<>();
    }

    public void agregar(T elemento) {
        if (!contiene(elemento)) {
            elementos.agregar(elemento);
        }
    }

    public boolean contiene(T elemento) {
        for (int i = 0; i < elementos.size(); i++) {
            if (elementos.get(i).equals(elemento)) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return elementos.size();
    }

    public T get(int index) {
        return elementos.get(index);
    }

    public ListaEnlazada<T> getElementos() {
        return elementos;
    }
}

