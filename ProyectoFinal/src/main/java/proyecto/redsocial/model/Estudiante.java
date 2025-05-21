package proyecto.redsocial.model;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
@Data
@ToString(exclude = {"gruposEstudio", "contenidosPublicados", "amigos"})


public class Estudiante {
    private int id;
    private String nombre;
    private String correo;
    private String contrasenia;
    private List<Estudiante> amigos;
    private ColaPrioridadAyuda solicitudes;
    private String rutaArchivoImagen;
    private List<String> intereses;
    private List<Publicacion> contenidosPublicados;
    private List<Valoracion> valoracions;
    private List<GrupoEstudio> gruposEstudio;
    private List<Mensaje> ListMensajes;

    public Estudiante() {
        this.ListMensajes = new ArrayList<>();
        this.amigos = new ArrayList<>();
        this.solicitudes = new ColaPrioridadAyuda();
        this.contenidosPublicados = new ArrayList<>();
        this.valoracions = new ArrayList<>();
        this.gruposEstudio = new ArrayList<>();
        this.intereses = new ArrayList<>();
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

    public boolean tieneInteres(String tema) {
        return intereses != null && intereses.contains(tema);
    }

    public void solicitarAyuda(SolicitudAyuda solicitudAyuda){
    }

    public void enviarMensaje(String mensaje,Estudiante estudiante){

    }

    public void agregarInteres(String interes){
        if(!intereses.contains(interes)){
            intereses.add(interes);
        }
    }

    public boolean tieneInteresComun(Estudiante otro) {
        for (String interes : intereses) {
            if (otro.getIntereses().contains(interes)) {
                return true;
            }
        }
        return false;
    }
}