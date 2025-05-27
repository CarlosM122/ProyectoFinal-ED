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

    public void insertarInicio(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.setSiguiente(cabeza);
        cabeza = nuevo;
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

    public T get(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Índice negativo: " + index);
        }
        Nodo<T> actual = cabeza;
        int contador = 0;
        while (actual != null) {
            if (contador == index) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
            contador++;
        }
        throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
    }

    public void eliminar(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Índice negativo: " + index);
        }
        if (cabeza == null) {
            throw new IndexOutOfBoundsException("Lista vacía");
        }
        if (index == 0) {
            cabeza = cabeza.getSiguiente();
            return;
        }
        Nodo<T> actual = cabeza;
        int contador = 0;
        while (actual.getSiguiente() != null) {
            if (contador == index - 1) {
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                return;
            }
            actual = actual.getSiguiente();
            contador++;
        }
        throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
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

    public boolean estaVacia(){
        return cabeza==null;
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

    public void addAll(List<T> list) {
        for (T elemento : list) {
            this.agregar(elemento);
        }
    }

    public void reemplazarEn(int indice, T elemento) {
        if (indice < 0 || indice >= size()) {
            throw new IndexOutOfBoundsException("Índice fuera de rango");
        }

        Nodo<T> actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.getSiguiente();
        }

        actual.setDato(elemento);
    }

}
