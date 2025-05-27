package proyecto.redsocial.model;

import lombok.Data;
import proyecto.redsocial.model.Publicacion;
import proyecto.redsocial.model.EstructurasPropias.ListaEnlazada;

import java.io.Serializable;

@Data
public class Moderador extends Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private ListaEnlazada<Publicacion> contenidosPublicados = new ListaEnlazada<>();

    public ListaEnlazada<Publicacion> getContenidosPublicados() {
        return contenidosPublicados;
    }

    public void setContenidosPublicados(ListaEnlazada<Publicacion> contenidosPublicados) {
        this.contenidosPublicados = contenidosPublicados;
    }

    public void gestionarUsuarios() {
        // Implementar lógica
    }

    public void gestionarContenido() {
        // Implementar lógica
    }

    public void generarReporteConexiones() {
        // Implementar lógica
    }

    public void visualizarGrafoAfinidad() {
        // Implementar lógica
    }
}

