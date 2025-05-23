package proyecto.redsocial.model.EstructurasPropias;

import lombok.Getter;
import lombok.Setter;
import proyecto.redsocial.model.SolicitudAyuda;
@Getter
@Setter
public class NodoSolicitud {
    private SolicitudAyuda solicitud;
    private NodoSolicitud siguiente;

    public NodoSolicitud(SolicitudAyuda solicitud) {
        this.solicitud = solicitud;
        this.siguiente = null;
    }
}
