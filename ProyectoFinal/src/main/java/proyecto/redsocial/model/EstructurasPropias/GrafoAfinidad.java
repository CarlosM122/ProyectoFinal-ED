package proyecto.redsocial.model.EstructurasPropias;

import proyecto.redsocial.model.Estudiante;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GrafoAfinidad implements Serializable {
    private static final long serialVersionUID = 1L;
    private ListaEnlazada<NodoGrafo> nodos;

    public GrafoAfinidad() {
        nodos = new ListaEnlazada<>();
    }

    public void agregarEstudiante(Estudiante estudiante) {
        nodos.agregar(new NodoGrafo(estudiante));
    }

    public void conectarEstudiantes(Estudiante estudiante1, Estudiante estudiante2, Map<String, List<String>> conexionesAfinidad) {
        NodoGrafo nodo1 = buscarEstudiante(estudiante1);
        NodoGrafo nodo2 = buscarEstudiante(estudiante2);

        if (nodo1 != null && nodo2 != null && !nodo1.getAdyacentes().contains(nodo2)) {
            nodo1.getAdyacentes().add(nodo2);
            nodo2.getAdyacentes().add(nodo1);

            // Guardar en el mapa las conexiones
            guardarConexion(conexionesAfinidad, estudiante1.getCorreo(), estudiante2.getCorreo());
            guardarConexion(conexionesAfinidad, estudiante2.getCorreo(), estudiante1.getCorreo());
        }
    }

    private void guardarConexion(Map<String, List<String>> mapa, String origen, String destino) {
        mapa.putIfAbsent(origen, new ArrayList<>());
        List<String> adyacentes = mapa.get(origen);
        if (!adyacentes.contains(destino)) {
            adyacentes.add(destino);
        }
    }

    public NodoGrafo buscarEstudiante(Estudiante estudiante2) {
        for (NodoGrafo nodo : nodos) {
            if (nodo.getEstudiante().equals(estudiante2)) {
                return nodo;
            }
        }
        return null;
    }

    public ListaEnlazada<Estudiante> amigosRecomendados(Estudiante estudiante) {
        NodoGrafo nodo = buscarEstudiante(estudiante);
        if (nodo == null || nodo.getAdyacentes() == null) return new ListaEnlazada<>();

        ListaEnlazada<Estudiante> recomendaciones = new ListaEnlazada<>();
        for (NodoGrafo amigo : nodo.getAdyacentes()) {
            for (NodoGrafo amigoDeAmigo : amigo.getAdyacentes()) {
                if (!recomendaciones.contiene(amigoDeAmigo.getEstudiante()) &&
                        !amigoDeAmigo.getEstudiante().equals(estudiante) &&
                        !amigoDeAmigo.getAdyacentes().contains(nodo)) {
                    recomendaciones.agregar(amigoDeAmigo.getEstudiante());
                }
            }
        }
        return recomendaciones;
    }
}