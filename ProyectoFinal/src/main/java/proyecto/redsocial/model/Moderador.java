package proyecto.redsocial.model;

import lombok.Data;
import proyecto.redsocial.model.Publicacion;
import proyecto.redsocial.model.EstructurasPropias.ListaEnlazada;

import java.io.Serializable;

@Data
public class Moderador extends Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private ListaEnlazada<Publicacion> contenidosPublicados = new ListaEnlazada<>();

    // Devuelve la lista de contenidos publicados por el moderador
    public ListaEnlazada<Publicacion> getContenidosPublicados() {
        return contenidosPublicados;
    }

    // Permite establecer la lista de contenidos publicados
    public void setContenidosPublicados(ListaEnlazada<Publicacion> contenidosPublicados) {
        this.contenidosPublicados = contenidosPublicados;
    }

    // Método para gestionar usuarios (lógica a implementar)
    public void gestionarUsuarios() {
        // Implementar lógica
    }

    // Método para gestionar contenido (lógica a implementar)
    public void gestionarContenido() {
        // Implementar lógica
    }

    // Método para generar un reporte de conexiones (lógica a implementar)
    public void generarReporteConexiones() {
        // Implementar lógica
    }

    // Método para visualizar el grafo de afinidad (lógica a implementar)
    public void visualizarGrafoAfinidad() {
        // Implementar lógica
    }
}

