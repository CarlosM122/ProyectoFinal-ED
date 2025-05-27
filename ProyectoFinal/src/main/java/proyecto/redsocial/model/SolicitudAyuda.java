package proyecto.redsocial.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SolicitudAyuda implements Comparable<SolicitudAyuda>, Serializable {

    private static final long serialVersionUID = 1L;

    private Estudiante estudiante;
    private String tema;
    private int urgencia;
    private String descripcion;

    public SolicitudAyuda() {
    }

    public String getNombreEstudiante() {
        return estudiante != null ? estudiante.getNombre() : "";
    }

    @Override
    public int compareTo(SolicitudAyuda otra) {
        return Integer.compare(otra.urgencia, this.urgencia);
    }
}
