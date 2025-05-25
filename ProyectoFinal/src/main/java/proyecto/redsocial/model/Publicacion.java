package proyecto.redsocial.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString(exclude = "autor")
public class Publicacion implements Serializable {
    private static final long serialVersionUID = 1L;
    private int idContenido;
    private String tema;
    private String texto;
    private Usuario autor;
    private List<Valoracion> valoraciones;
    private String fechaPublicacion;
    private String rutaArchivoAdjunto;

    public Publicacion() {
    }

    public double calcularPromedioValoracion() {
        return 0; // Puedes implementarlo si tienes valoraciones
    }
}
