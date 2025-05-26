package proyecto.redsocial.model;

import lombok.Data;
import proyecto.redsocial.model.EstructurasPropias.*;

import java.io.Serializable;
import java.util.*;

@Data
public class Sistema implements Serializable {

    private static final long serialVersionUID = 1L;
    private Mapa<String, ListaEnlazada<String>> conexionesAfinidad;;
    private ListaEnlazada<GrupoEstudio> listaGruposEstudio;
    private  ListaEnlazada<Estudiante> listaEstudiantes;
    private  ListaEnlazada<Moderador> listaModeradores;
    private  ListaEnlazada<Publicacion> listaPublicaciones;
    private  ArbolABB arbolPublicaciones;
    private  ColaPrioridadSolicitudes colaPrioridadSolicitudes;
    private  GrafoAfinidad redAfinidad;
    private  GestorGruposEstudio gestorGruposEstudio;

    public Sistema() {
        this.listaGruposEstudio = new ListaEnlazada<>();
        this.conexionesAfinidad = new Mapa<>();
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
        for (Mapa.Entry<String, ListaEnlazada<String>> entrada : conexionesAfinidad) {
            String correoEstudiante = entrada.getKey();
            ListaEnlazada<String> amigos = entrada.getValue();

            Estudiante estudiante = buscarEstudiante(correoEstudiante);
            NodoGrafo nodo = redAfinidad.buscarEstudiante(estudiante);

            for (int j = 0; j < amigos.size(); j++) {
                String correoAmigo = amigos.get(j);
                Estudiante amigo = buscarEstudiante(correoAmigo);
                NodoGrafo nodoAmigo = redAfinidad.buscarEstudiante(amigo);

                if (nodo != null && nodoAmigo != null && !nodo.getAdyacentes().contiene(nodoAmigo)) {
                    nodo.getAdyacentes().agregar(nodoAmigo);
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

    /**
     * Devuelve una lista de todos los estudiantes registrados en el sistema.
     */
    public List<Estudiante> getEstudiantes() {
        List<Estudiante> estudiantes = new ArrayList<>();
        for (Estudiante est : listaEstudiantes) {
            estudiantes.add(est);
        }
        return estudiantes;
    }
}
