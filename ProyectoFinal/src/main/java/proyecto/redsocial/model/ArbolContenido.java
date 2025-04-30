package proyecto.redsocial.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Data
public class ArbolContenido {
    private TreeMap<String,Contenido> raiz = new TreeMap<>();

    public void insertarContenido(Contenido contenido) {
        raiz.put(contenido.getAutor(),contenido);
    }
    public Contenido buscarPorTema(String tema) {
        return null;
    }
    public Contenido buscarPorAutor(String autor) {
        return null;
    }

    public List<Contenido> inOrden() {
        return new ArrayList<>(raiz.values());
    }

}
