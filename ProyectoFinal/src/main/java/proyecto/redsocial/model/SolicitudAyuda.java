package proyecto.redsocial.model;

import java.time.LocalDateTime;

public class SolicitudAyuda {

    private String tema;
    private String urgencia;
    private String descripcion;
    private LocalDateTime fecha;
    private Estudiante estudiante;

    public SolicitudAyuda() {
    }

    public SolicitudAyuda(String tema, String urgencia, String descripcion, LocalDateTime fecha, Estudiante estudiante) {
        this.tema = tema;
        this.urgencia = urgencia;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.estudiante = estudiante;

    }

    public String getTema() {
        return tema;
    }

    public String getUrgencia() {
        return urgencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public void setUrgencia(String urgencia) {
        this.urgencia = urgencia;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
}
