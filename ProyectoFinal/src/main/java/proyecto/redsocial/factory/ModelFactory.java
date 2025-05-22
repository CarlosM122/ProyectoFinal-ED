package proyecto.redsocial.factory;

import lombok.Getter;
import proyecto.redsocial.model.*;
import proyecto.redsocial.utils.Persistencia;
import proyecto.redsocial.utils.RedSocialUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ModelFactory {
    private Sistema sistema;

    private static class SingletonHolder {
        private final static ModelFactory eINSTANCE = new ModelFactory();
    }

    public static ModelFactory getInstance() {
        return SingletonHolder.eINSTANCE;
    }

    private ModelFactory() {
        if (Persistencia.existeArchivoXML()) {
            cargarRecursosXML();
        } else {
            inicializarDatosBase();
            guardarRecursosXML();
        }
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

    public boolean registrarUsuario(String nombre, String correo, String contrasenia) {
        boolean registrado = false;
        Estudiante estudiante = sistema.buscarEstudiante(correo);
        if (estudiante == null) {
            Estudiante nuevoEstudiante = new Estudiante();
            nuevoEstudiante.setNombre(nombre);
            nuevoEstudiante.setCorreo(correo);
            nuevoEstudiante.setContrasenia(RedSocialUtils.encriptarSHA256(contrasenia));
            sistema.guardarEstudiante(nuevoEstudiante);
            sistema.getGrafoAfinidad().agregarEstudiante(nuevoEstudiante);
            guardarRecursosXML();
            registrado = true;
        }
        return registrado;
    }

    public void guardarSolicitud(SolicitudAyuda solicitudAyuda) {
        sistema.getColaPrioridadAyuda().agregarSolicitud(solicitudAyuda);
    }

    public Object obtnerUsuario(String correo) {
        Estudiante est = sistema.buscarEstudiante(correo);
        if (est != null) return est;

        Moderador mod = sistema.buscarModerador(correo);
        return mod;
    }

    public void guardarPublicacion(Publicacion publicacion) {
        sistema.getPublicacions().add(publicacion);
        sistema.getArbolPublicaciones().insertar(publicacion);
        Estudiante estudiante = publicacion.getAutor();
        estudiante.publicarContenido(publicacion);
    }

    public boolean verificarCredenciales(String correo, String contrasenia) {
        String contraEncriptada = RedSocialUtils.encriptarSHA256(contrasenia);

        Estudiante est = sistema.buscarEstudiante(correo);
        if (est != null && est.getContrasenia().equals(contraEncriptada)) {
            return true;
        }

        Moderador mod = sistema.buscarModerador(correo);
        if (mod != null && mod.getContrasenia().equals(contraEncriptada)) {
            return true;
        }

        return false;
    }

    public List<Publicacion> obtenerPublicaciones() {
        return sistema.cargarPublicaciones();
    }

    public List<Publicacion> obtenerPublicacionesPorTema(String tema) {
        return sistema.getArbolPublicaciones().buscarPublicacionesPorTema(tema);
    }

    public void eliminarPublicacion(Publicacion publicacion) {
        sistema.getPublicacions().remove(publicacion);
        if (publicacion.getRutaArchivoAdjunto()!=null){
            Path path = Paths.get(publicacion.getRutaArchivoAdjunto());
            try {
                Files.delete(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        guardarRecursosXML();
    }

    public void actualizarRedAfinidad(Estudiante estudianteActual, String tema) {
        for (Estudiante otro : sistema.getEstudiantes()) {
            if (!otro.equals(estudianteActual) && otro.getIntereses().contains(tema)) {
                sistema.getGrafoAfinidad().agregarRelacion(estudianteActual, otro);
                return;
            }
        }
    }

    public void asignarAGrupoDeEstudio(Estudiante estudiante, String tema) {
        sistema.getGestorGruposEstudio().agregarEstudianteAGrupo(estudiante, tema,this);
    }

    public void agregarGrupo(GrupoEstudio grupoEstudio) {
        sistema.getGruposEstudio().add(grupoEstudio);
        sistema.getGestorGruposEstudio().agregarGrupos(sistema.getGruposEstudio());
    }
}
