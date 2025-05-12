package proyecto.redsocial.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class Estudiante {
    private int id;
    private String nombre;
    private String correo;
    private String contrasenia;
    private List<Estudiante> amigos;
    private ColaPrioridadAyuda solicitudes;
    private List<Publicacion> contenidosPublicados;
    private List<Valoracion> valoracions;
    private List<GrupoEstudio> gruposEstudio;

    public Estudiante() {
        this.amigos = new ArrayList<>();
        this.solicitudes = new ColaPrioridadAyuda();
        this.contenidosPublicados = new ArrayList<>();
        this.valoracions = new ArrayList<>();
        this.gruposEstudio = new ArrayList<>();
    }

    public void publicarContenido(Publicacion publicacion){
        contenidosPublicados.add(publicacion);
    }

    public void valorarContenido(int valoracion, Publicacion publicacion, String comentario ){
        Valoracion v = new Valoracion();
        v.setPublicacion(publicacion);
        v.setValoracion(valoracion);
        v.setComentario(comentario);
        valoracions.add(v);
    }

    public void solicitarAyuda(SolicitudAyuda solicitudAyuda){
    }

    public void enviarMensaje(String mensaje,Estudiante estudiante){

    }
}
