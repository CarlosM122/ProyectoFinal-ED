package proyecto.redsocial.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolicitudAyuda implements Comparable<SolicitudAyuda> {

    private Estudiante estudiante;
    private String tema;
    private int urgencia;

    public SolicitudAyuda() {
    }

    @Override
    public int compareTo(SolicitudAyuda otra) {
        return Integer.compare(otra.urgencia, this.urgencia);
    }
}
