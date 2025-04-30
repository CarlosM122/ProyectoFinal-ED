package proyecto.redsocial.model;

import lombok.Data;

import java.util.List;
@Data
public class Estudiante {
    private int id;
    private String nombre;
    private String correo;
    private String contrasenia;
    private List<Estudiante> amigos;
    private ColaPrioridadAyuda solicitudes;
    private List<Contenido> contenidosPublicados;
    private List<Valoracion> valoracions;
    private List<GrupoEstudio> gruposEstudio;

    public void publicarContenido(Contenido contenido){
        contenidosPublicados.add(contenido);
    }

    public void valorarContenido(int valoracion, Contenido contenido,String comentario ){
        Valoracion v = new Valoracion();
        v.setContenido(contenido);
        v.setValoracion(valoracion);
        v.setComentario(comentario);
        valoracions.add(v);
    }

    public void solicitarAyuda(SolicitudAyuda solicitudAyuda){
    }

    public void enviarMensaje(String mensaje,Estudiante estudiante){

    }
}
