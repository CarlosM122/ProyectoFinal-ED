package proyecto.redsocial.model.EstructurasPropias;

import proyecto.redsocial.model.Estudiante;

public class GrafoAfinidad {
    private ListaEnlazada<NodoGrafo> nodos;

    public GrafoAfinidad() {
        nodos = new ListaEnlazada<>();
    }

    public void agregarEstudiante(Estudiante estudiante) {
        nodos.add(new NodoGrafo(estudiante));
    }

    public void conectarEstudiantes(Estudiante estudiante1, Estudiante estudiante2) {
        NodoGrafo nodo1 = buscarEstudiante(estudiante1);
        NodoGrafo nodo2 = buscarEstudiante(estudiante2);
        if (nodo1 != null && nodo2 != null && !nodo1.getAdyacentes().contains(nodo2)) {
            nodo1.getAdyacentes().add(nodo2);
            nodo2.getAdyacentes().add(nodo1);
        }
    }

    private NodoGrafo buscarEstudiante(Estudiante estudiante2) {
        for (NodoGrafo nodo : nodos) {
            if (nodo.getEstudiante().equals(estudiante2)) {
                return nodo;
            }
        }
        return null;
    }

    public ListaEnlazada<Estudiante> amigosRecomendados(Estudiante estudiante) {
        NodoGrafo nodo = buscarEstudiante(estudiante);
        if (nodo == null || nodo.getAdyacentes() == null) return null;

        ListaEnlazada<Estudiante> recomendaciones = new ListaEnlazada<>();
        for (NodoGrafo amigo : nodo.getAdyacentes()) {
            for (NodoGrafo amigoDeAmigo : amigo.getAdyacentes()) {
                if (!recomendaciones.contiene(amigoDeAmigo.getEstudiante()) &&
                        !amigoDeAmigo.getEstudiante().equals(estudiante) &&
                        !amigoDeAmigo.getAdyacentes().contains(nodo)) {
                    recomendaciones.add(amigoDeAmigo.getEstudiante());
                }
            }
        }
        return recomendaciones;
    }
}