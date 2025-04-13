package proyecto.redsocial.Model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
@Data
public class Contenido {
    private String idContenido;
    private String titulo;
    private String tema;
    private String autor;
    private List<Valoracion> valoraciones;
    private LocalDate fechaPublicacion;

    public double calcularPromedioValoracion(){
        return 0;
    }
}
