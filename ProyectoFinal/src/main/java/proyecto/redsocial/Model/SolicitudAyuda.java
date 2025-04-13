package proyecto.redsocial.Model;

import lombok.Data;

@Data
public class SolicitudAyuda implements Comparable<SolicitudAyuda> {
    private String tema;
    private Estudiante estudiante;
    private int urgencia;

    @Override
    public int compareTo(SolicitudAyuda o) {
        return this.urgencia - o.urgencia;
    }
}
