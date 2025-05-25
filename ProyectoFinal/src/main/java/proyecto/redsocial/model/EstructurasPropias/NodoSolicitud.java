package proyecto.redsocial.model.EstructurasPropias;

import lombok.Getter;
import lombok.Setter;
import proyecto.redsocial.model.SolicitudAyuda;

import java.io.Serializable;

@Getter
@Setter
public class NodoSolicitud implements Serializable {
    private static final long serialVersionUID = 1L;
    private SolicitudAyuda solicitud;
    private NodoSolicitud siguiente;

    public NodoSolicitud(SolicitudAyuda solicitud) {
        this.solicitud = solicitud;
        this.siguiente = null;
    }
}
