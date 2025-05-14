package proyecto.redsocial.factory;

import proyecto.redsocial.model.*;
import proyecto.redsocial.utils.Persistencia;
import proyecto.redsocial.utils.RedSocialUtils;

import java.util.List;

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
    }

    private void guardarRecursosXML() {
        Persistencia.guardarRecursosXML(sistema);
    }

    private void inicializarDatosBase() {
        sistema = RedSocialUtils.inicializarSistema();
    }

    public boolean registrarUsuario(String nombre, String correo, String contrasenia) {
        boolean registrado = false;
        Estudiante estudiante = sistema.buscarEstudiante(correo);
        if (estudiante == null) {
            sistema.guardarEstudiante(nombre, correo, contrasenia);
            guardarRecursosXML();
            registrado = true;
        }
        return registrado;
    }

    public void guardarSolicitud(SolicitudAyuda solicitudAyuda) {
        sistema.getColaPrioridadAyuda().agregarSolicitud(solicitudAyuda);
        guardarRecursosXML();
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
        guardarRecursosXML();
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
}
