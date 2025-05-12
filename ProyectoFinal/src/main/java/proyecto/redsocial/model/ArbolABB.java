package proyecto.redsocial.model;

import java.util.ArrayList;
import java.util.List;

public class ArbolABB {
    private NodoABB raiz;

    public void insertar(Publicacion publicacion) {
        raiz = insertarRec(raiz, publicacion);
    }

    private NodoABB insertarRec(NodoABB actual, Publicacion publicacion) {
        if (actual == null) {
            return new NodoABB(publicacion);
        }

        int comparacion = publicacion.getTema().compareToIgnoreCase(actual.getTema());

        if (comparacion < 0) {
            actual.setIzquierdo(insertarRec(actual.getIzquierdo(), publicacion));
        } else if (comparacion > 0) {
            actual.setDerecho(insertarRec(actual.getDerecho(), publicacion));
        } else {
            actual.getPublicaciones().add(publicacion);
        }

        return actual;
    }

    public List<Publicacion> buscarPublicacionesPorTema(String tema) {
        return buscarRec(raiz, tema);
    }

    private List<Publicacion> buscarRec(NodoABB actual, String tema) {
        if (actual == null) return new ArrayList<>();

        int comparacion = tema.compareToIgnoreCase(actual.getTema());

        if (comparacion == 0) {
            return actual.getPublicaciones();
        } else if (comparacion < 0) {
            return buscarRec(actual.getIzquierdo(), tema);
        } else {
            return buscarRec(actual.getDerecho(), tema);
        }
    }

    public List<Publicacion> obtenerPublicacionesPorTema(String tema) {
        return obtenerPublicacionesRec(raiz, tema);
    }

    private List<Publicacion> obtenerPublicacionesRec(NodoABB actual, String tema) {
        if (actual == null) return new ArrayList<>();

        int comparacion = tema.compareToIgnoreCase(actual.getTema());

        if (comparacion == 0) {
            return actual.getPublicaciones();
        } else if (comparacion < 0) {
            return obtenerPublicacionesRec(actual.getIzquierdo(), tema);
        } else {
            return obtenerPublicacionesRec(actual.getDerecho(), tema);
        }
    }
}
