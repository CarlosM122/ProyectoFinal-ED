package proyecto.redsocial.model;

import lombok.Data;
import proyecto.redsocial.model.EstructurasPropias.*;

import java.io.Serializable;
import java.util.*;

@Data
public class Sistema implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, List<String>> conexionesAfinidad;

    private ListaEnlazada<GrupoEstudio> listaGruposEstudio;

    /** Lista enlazada auxiliar de estudiantes. Se reconstruye desde la lista simple al cargar. */
    private  ListaEnlazada<Estudiante> listaEstudiantes;

    /** Lista enlazada auxiliar de moderadores. */
    private  ListaEnlazada<Moderador> listaModeradores;

    /** Lista enlazada auxiliar de publicaciones. */
    private  ListaEnlazada<Publicacion> listaPublicaciones;

    /** Árbol binario de publicaciones ordenadas. Se reconstruye al cargar. */
    private  ArbolABB arbolPublicaciones;

    /** Cola de prioridad para solicitudes de ayuda. */
    private  ColaPrioridadSolicitudes colaPrioridadSolicitudes;

    /** Grafo de afinidad entre estudiantes. Se reconstruye usando conexionesAfinidad. */
    private  GrafoAfinidad redAfinidad;

    /** Manejador de grupos de estudio. */
    private  GestorGruposEstudio gestorGruposEstudio;

    // --- CONSTRUCTOR ---

    public Sistema() {
        this.listaGruposEstudio = new ListaEnlazada<>();
        this.conexionesAfinidad = new HashMap<>();
        this.listaEstudiantes = new ListaEnlazada<>();
        this.listaModeradores = new ListaEnlazada<>();
        this.listaPublicaciones = new ListaEnlazada<>();
        this.colaPrioridadSolicitudes = new ColaPrioridadSolicitudes();
        this.redAfinidad = new GrafoAfinidad();
        this.gestorGruposEstudio = new GestorGruposEstudio();
    }

    // --- MÉTODOS DE INICIALIZACIÓN Y RECONSTRUCCIÓN ---

    /**
     * Inicializa todas las estructuras del sistema a partir de los datos serializados.
     * Este método se debe llamar luego de cargar el objeto desde un archivo.
     */
    public void inicializarSistema() {
        reconstruirConexionesDesdeMapa();
        cargarArbol();
        for (Estudiante estudiante : listaEstudiantes) {
            redAfinidad.agregarEstudiante(estudiante);
        }
    }
    /**
     * Reconstruye las conexiones del grafo de afinidad a partir del mapa serializado.
     */
    public void reconstruirConexionesDesdeMapa() {
        for (Map.Entry<String, List<String>> entrada : conexionesAfinidad.entrySet()) {
            String correoEstudiante = entrada.getKey();
            Estudiante estudiante = buscarEstudiante(correoEstudiante);
            NodoGrafo nodo = redAfinidad.buscarEstudiante(estudiante);

            for (String correoAmigo : entrada.getValue()) {
                Estudiante amigo = buscarEstudiante(correoAmigo);
                NodoGrafo nodoAmigo = redAfinidad.buscarEstudiante(amigo);

                if (nodo != null && nodoAmigo != null && !nodo.getAdyacentes().contains(nodoAmigo)) {
                    nodo.getAdyacentes().add(nodoAmigo);
                }
            }
        }
    }

    /**
     * Reconstruye el árbol de publicaciones ordenadas.
     */
    public void cargarArbol() {
        arbolPublicaciones = new ArbolABB();
        for (Publicacion publicacion : listaPublicaciones) {
            arbolPublicaciones.insertar(publicacion);
        }
    }

    // --- MÉTODOS DE BÚSQUEDA Y UTILIDAD ---

    /**
     * Busca un estudiante por su correo.
     */
    public Estudiante buscarEstudiante(String correo) {
        for (Estudiante est : listaEstudiantes) {
            if (est.getCorreo().equals(correo)) {
                return est;
            }
        }
        return null;
    }

    /**
     * Guarda un estudiante nuevo en el sistema.
     */
    public void guardarEstudiante(Estudiante estudiante) {
        estudiante.setId(listaEstudiantes.size() + 1);
        listaEstudiantes.agregar(estudiante);
        listaEstudiantes.agregar(estudiante);
    }

    /**
     * Busca un moderador por su correo.
     */
    public Moderador buscarModerador(String correo) {
        for (Moderador mod : listaModeradores) {
            if (mod.getCorreo().equals(correo)) {
                return mod;
            }
        }
        return null;
    }

    /**
     * Retorna las publicaciones cargadas.
     */
    public List<Publicacion> cargarPublicaciones() {
        return listaPublicaciones.aLista();
    }
}
