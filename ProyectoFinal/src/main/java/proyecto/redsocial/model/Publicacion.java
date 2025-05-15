package proyecto.redsocial.model;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
@Data
@ToString(exclude = "autor")
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
