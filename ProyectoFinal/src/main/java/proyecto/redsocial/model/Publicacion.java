package proyecto.redsocial.model;

import lombok.Data;
import lombok.ToString;
import proyecto.redsocial.model.EstructurasPropias.ListaEnlazada;

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
    private ListaEnlazada<Valoracion> valoraciones;
    private String fechaPublicacion;
    private String rutaArchivoAdjunto;

    public Publicacion() {
    }

    public ListaEnlazada<Valoracion> getValoraciones() {
        if (valoraciones == null) {
            valoraciones = new ListaEnlazada<>();
        }
        return valoraciones;
    }

    public double calcularPromedioValoracion() {
        int total = 0;
        int cantidad = 0;
        for (var v : valoraciones) {
            total += v.getValoracion();
            cantidad++;
        }
        return cantidad > 0 ? ((double) total / cantidad) : 0.0;
    }
}
