package proyecto.redsocial.model.EstructurasPropias;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListaEnlazada <T> implements Iterable<T>, Serializable {
    private static final long serialVersionUID = 1L;
    private Nodo<T> cabeza;

    public ListaEnlazada() {
        this.cabeza = null;
    }

    public void agregar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo<T> actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevo);
        }
    }

    public void eliminar(T nodo){
        if (cabeza.getDato().equals(nodo)) {
            cabeza = cabeza.getSiguiente();
        } else {
            Nodo<T> actual = cabeza;
            while (actual.getSiguiente() != null) {
                if (actual.getSiguiente().getDato().equals(nodo)) {
                    actual.setSiguiente(actual.getSiguiente().getSiguiente());
                    break;
                }
                actual = actual.getSiguiente();
            }
        }
    }

    public void imprimir() {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            System.out.println(actual.getDato());
            actual = actual.getSiguiente();
        }
    }

    public List<T> aLista() {
        List<T> lista = new ArrayList<>();
        Nodo<T> actual = cabeza;
        while (actual != null) {
            lista.add(actual.getDato());
            actual = actual.getSiguiente();
        }
        return lista;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Nodo<T> actual = cabeza;
            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public T next() {
                T dato = actual.getDato();
                actual = actual.getSiguiente();
                return dato;
            }
        };
    }

    public boolean contiene(T nodo2) {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            if (actual.getDato().equals(nodo2)) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public int size() {
        int contador = 0;
        if (cabeza == null) return 0;
        Nodo<T> actual = cabeza;
        while (actual != null) {
            contador++;
            actual = actual.getSiguiente();
        }
        return contador;
    }
}
