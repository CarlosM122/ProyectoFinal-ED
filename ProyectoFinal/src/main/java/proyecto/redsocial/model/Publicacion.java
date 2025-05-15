package proyecto.redsocial.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
@Data
public class Publicacion {
    private int idContenido;
    private String tema;
    private String texto;
    private Estudiante autor;
    private List<Valoracion> valoraciones;
    private String fechaPublicacion;
    private String rutaArchivoAdjunto;

    public double calcularPromedioValoracion(){
        return 0;
    }
}
