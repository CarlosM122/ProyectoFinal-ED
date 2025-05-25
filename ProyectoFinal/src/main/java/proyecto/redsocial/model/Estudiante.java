package proyecto.redsocial.model;

import lombok.Data;
import lombok.ToString;
import proyecto.redsocial.model.EstructurasPropias.ListaEnlazada;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Data
@ToString(exclude = {"gruposEstudio", "contenidosPublicados", "amigos"})


public class Estudiante extends Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private ListaEnlazada<Estudiante> amigos;
    private String rutaArchivoImagen;
    private ListaEnlazada<String> intereses;
    private ListaEnlazada<Publicacion> contenidosPublicados;
    private ListaEnlazada<Valoracion> valoracions;
    private ListaEnlazada<GrupoEstudio> gruposEstudio;
    private ListaEnlazada<Mensaje> ListMensajes;

    public Estudiante() {
        this.ListMensajes = new ListaEnlazada<>();
        this.amigos = new ListaEnlazada<>();
        this.contenidosPublicados = new ListaEnlazada<>();
        this.valoracions = new ListaEnlazada<>();
        this.gruposEstudio = new ListaEnlazada<>();
        this.intereses = new ListaEnlazada<>();
    }

    public void publicarContenido(Publicacion publicacion){
        contenidosPublicados.agregar(publicacion);
    }

    public void valorarContenido(int valoracion, Publicacion publicacion, String comentario ){
        Valoracion v = new Valoracion();
        v.setPublicacion(publicacion);
        v.setValoracion(valoracion);
        v.setComentario(comentario);
        valoracions.agregar(v);
    }

    public boolean tieneInteres(String tema) {
        return intereses != null && intereses.contiene(tema);
    }

    public void solicitarAyuda(SolicitudAyuda solicitudAyuda){
    }

    public void enviarMensaje(String mensaje,Estudiante estudiante){

    }

    public void agregarInteres(String interes){
        if(!intereses.contiene(interes)){
            intereses.agregar(interes);
        }
    }

    public boolean tieneInteresComun(Estudiante otro) {
        for (String interes : intereses) {
            if (otro.getIntereses().contiene(interes)) {
                return true;
            }
        }
        return false;
    }
}

