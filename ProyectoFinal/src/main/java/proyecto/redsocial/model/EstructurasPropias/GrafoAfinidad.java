package proyecto.redsocial.model.EstructurasPropias;

import proyecto.redsocial.model.Estudiante;

import java.util.ArrayList;
import java.util.List;

public class GrafoAfinidad {
    private List<NodoGrafo> nodos;

    public GrafoAfinidad() {
        this.nodos = new ArrayList<>();
    }

    public void agregarEstudiante(Estudiante estudiante) {
        if (buscarNodo(estudiante) == null) {
            nodos.add(new NodoGrafo(estudiante));
        }
    }

    public void conectarEstudiantesPorInteres() {
        for(NodoGrafo nodoA : nodos){
            for (NodoGrafo nodoB : nodos){
                if(!nodoA.equals(nodoB) && nodoA.getEstudiante().tieneInteresComun(nodoB.getEstudiante())
                        && !nodoA.getAdyacentes().contains(nodoB)){
                    nodoA.agregarAdyacente(nodoB);
                    nodoB.agregarAdyacente(nodoA);
                }
            }
        }
    }

    public void agregarRelacion(Estudiante e1, Estudiante e2) {
        NodoGrafo nodo1 = buscarOCrearNodo(e1);
        NodoGrafo nodo2 = buscarOCrearNodo(e2);

        if (!nodo1.getAdyacentes().contains(nodo2)) {
            nodo1.getAdyacentes().add(nodo2);
        }
        if (!nodo2.getAdyacentes().contains(nodo1)) {
            nodo2.getAdyacentes().add(nodo1);
        }
    }

    private NodoGrafo buscarOCrearNodo(Estudiante estudiante) {
        for (NodoGrafo nodo : nodos) {
            if (nodo.getEstudiante().equals(estudiante)) {
                return nodo;
            }
        }
        NodoGrafo nuevo = new NodoGrafo(estudiante);
        nodos.add(nuevo);
        return nuevo;
    }

    public List<Estudiante> obtenerEstudiantesConectados(Estudiante estudiante) {
        NodoGrafo nodo = buscarNodo(estudiante);
        List<Estudiante> estudiantes = new ArrayList<>();
        if (nodo != null) {
            for (NodoGrafo nodoAdyacente : nodo.getAdyacentes()) {
                estudiantes.add(nodoAdyacente.getEstudiante());
            }
        }
        return estudiantes;
    }

    private NodoGrafo buscarNodo(Estudiante estudiante) {
        for (NodoGrafo nodo : nodos) {
            if (nodo.getEstudiante().equals(estudiante)) {
                return nodo;
            }
        }
        return null;
    }
}
