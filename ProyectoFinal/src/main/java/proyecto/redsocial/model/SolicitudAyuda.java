package proyecto.redsocial.model;

import lombok.Data;

@Data
public class SolicitudAyuda implements Comparable<SolicitudAyuda> {
    private String tema;
    private Estudiante estudiante;
    private String urgencia;
    private String descripcion;

    @Override
    public int compareTo(SolicitudAyuda o) {
        return this.urgencia.compareTo(o.getUrgencia());
    }
}
