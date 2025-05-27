package proyecto.redsocial.factory;

import lombok.Getter;
import proyecto.redsocial.model.*;
import proyecto.redsocial.model.EstructurasPropias.ListaEnlazada;
import proyecto.redsocial.utils.Persistencia;
import proyecto.redsocial.utils.RedSocialUtils;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Getter
public class ModelFactory implements Serializable {
    private static final long serialVersionUID = 1L;
    private Sistema sistema;

    private ModelFactory() {
        if (Persistencia.existeArchivoXML()) {
            cargarRecursosXML();
        } else {
            inicializarDatosBase();
            guardarRecursosXML();
        }
    }

    public static ModelFactory getInstance() {
        return SingletonHolder.eINSTANCE;
    }

    private void cargarRecursosXML() {
        sistema = Persistencia.cargarRecursosXML();
        sistema.inicializarSistema();
    }

    public void guardarRecursosXML() {
        Persistencia.guardarRecursosXML(sistema);
    }

    private void inicializarDatosBase() {
        sistema = RedSocialUtils.inicializarSistema();
    }

    public boolean registrarUsuario(String nombre, String correo, String contrasenia, String informacion) {
        boolean registrado = false;
        Estudiante estudiante = sistema.buscarEstudiante(correo);
        if (estudiante == null) {
            Estudiante nuevoEstudiante = new Estudiante();
            nuevoEstudiante.setNombre(nombre);
            nuevoEstudiante.setCorreo(correo);
            nuevoEstudiante.setInformacion(informacion);
            nuevoEstudiante.setContrasenia(proyecto.redsocial.utils.RedSocialUtils.encriptarSHA256(contrasenia));
            sistema.guardarEstudiante(nuevoEstudiante);
            guardarRecursosXML();
            registrado = true;
        }
        return registrado;
    }

    public void guardarSolicitud(SolicitudAyuda solicitudAyuda) {
        sistema.getColaPrioridadSolicitudes().insertar(solicitudAyuda);

    }

    public Object obtnerUsuario(String correo) {
        Estudiante est = sistema.buscarEstudiante(correo);
        if (est != null) return est;

        Moderador mod = sistema.buscarModerador(correo);
        return mod;
    }

    public void guardarPublicacion(Publicacion publicacion) {
        sistema.getListaPublicaciones().agregar(publicacion);
        sistema.getArbolPublicaciones().insertar(publicacion);
        if (publicacion.getAutor() instanceof Estudiante estudiante) {
            estudiante.publicarContenido(publicacion);
        }
    }

    public boolean verificarCredenciales(String correo, String contrasenia) {
        String contraEncriptada = RedSocialUtils.encriptarSHA256(contrasenia);

        Estudiante est = sistema.buscarEstudiante(correo);
        if (est != null && est.getContrasenia().equals(contraEncriptada)) {
            return true;
        }

        Moderador mod = sistema.buscarModerador(correo);
        return mod != null && mod.getContrasenia().equals(contraEncriptada);
    }

    public List<Publicacion> obtenerPublicaciones() {
        return sistema.cargarPublicaciones();
    }

    public List<Publicacion> obtenerPublicacionesPorTema(String tema) {
        return sistema.getArbolPublicaciones().buscarPublicacionesPorTema(tema);
    }

    public void eliminarPublicacion(Publicacion publicacion) {
        sistema.getListaPublicaciones().eliminar(publicacion);
        if (publicacion.getRutaArchivoAdjunto() != null) {
            Path path = Paths.get(publicacion.getRutaArchivoAdjunto());
            try {
                Files.delete(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        guardarRecursosXML();
    }

    public void asignarAGrupoDeEstudio(Estudiante estudiante, String tema) {
        sistema.getGestorGruposEstudio().agregarEstudianteAGrupo(estudiante, tema, this);
    }

    public void agregarGrupo(GrupoEstudio grupoEstudio) {
        sistema.getListaGruposEstudio().agregar(grupoEstudio);
        sistema.getGestorGruposEstudio().agregarGrupos(sistema.getListaGruposEstudio());
    }

    public ListaEnlazada<Estudiante> obtenerAmigosRecomendados(Estudiante estudiante) {
        return sistema.getRedAfinidad().amigosRecomendados(estudiante,sistema);
    }

    public void agregarAmigo(Estudiante estudiante, Estudiante estudianteAgregar) {
        sistema.getRedAfinidad().conectarEstudiantes(estudiante, estudianteAgregar, sistema.getConexionesAfinidad());
        guardarRecursosXML();
    }

    public List<Mensaje> obtenerMensajes(Estudiante estudiante, Estudiante estudianteCompañero) {
        ListaEnlazada<Mensaje> mensajeList = new ListaEnlazada<>();
        ListaEnlazada<Mensaje> mensajesTotales = estudiante.getListMensajes();
        for (Mensaje mensaje: mensajesTotales){
            if (mensaje.getEmisor().equals(estudianteCompañero)||mensaje.getReceptor().equals(estudianteCompañero)){
                mensajeList.agregar(mensaje);
            }
        }
        return mensajeList.aLista();
    }

    public List<Publicacion> obtenerPublicacionesPorNombre(String textoBusqueda) {
        return sistema.buscarPublicacionPornNombre(textoBusqueda);
    }

    private static class SingletonHolder {
        private final static ModelFactory eINSTANCE = new ModelFactory();
    }
}
