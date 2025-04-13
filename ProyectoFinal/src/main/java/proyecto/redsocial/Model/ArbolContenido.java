package proyecto.redsocial.Model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Data
public class ArbolContenido {
    private TreeMap<String,Contenido> map = new TreeMap<>();

    public void insertarContenido(Contenido contenido) {
        map.put(contenido.getAutor(),contenido);
    }
    public Contenido buscarPorTema(String tema) {
        return null;
    }
    public Contenido buscarPorAutor(String autor) {
        return null;
    }

    public List<Contenido> inOrden() {
        return new ArrayList<>(map.values());
    }

}
