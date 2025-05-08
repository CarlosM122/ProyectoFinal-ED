package proyecto.redsocial.model;

import lombok.Data;

import java.util.PriorityQueue;
@Data
public class ColaPrioridadAyuda {

    private PriorityQueue<SolicitudAyuda> cola;

    public ColaPrioridadAyuda() {
        this.cola = new PriorityQueue<>();
    }

    public void  agregarSolicitud(SolicitudAyuda solicitudAyuda){
        cola.offer(solicitudAyuda);
    }

    public SolicitudAyuda atenderSolicitud(){
        return cola.poll();
    }
}
