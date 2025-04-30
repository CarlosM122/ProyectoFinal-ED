package proyecto.redsocial.factory;

import proyecto.redsocial.model.Estudiante;
import proyecto.redsocial.model.Sistema;
import proyecto.redsocial.utils.Persistencia;
import proyecto.redsocial.utils.RedSocialUtils;

public class ModelFactory {
    private Sistema sistema;

    public boolean registrarUsuario(String nombre, String correo, String contrasenia) {
        boolean registrado = false;
        Estudiante estudiante = sistema.buscarEstudiante(correo);
        if (estudiante == null) {
            sistema.guardarEstudiante(nombre,correo,contrasenia);
            guardarRecursosXML();
            registrado = true;
        }
        return registrado;
    }

    private static class SingletonHolder {
        private final static ModelFactory eINSTANCE = new ModelFactory();
    }

    public static ModelFactory getInstance() {
        return SingletonHolder.eINSTANCE;
    }

    private ModelFactory() {
        sistema = new Sistema();
//        inicializarDatosBase();
//        guardarRecursosXML();
        cargarRecursosXML();
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

    public boolean verificarCredenciales(String correo, String contrasenia) {
        boolean respuesta = false;
        Estudiante estudiante = sistema.buscarEstudiante(correo);
        if (estudiante == null) return false;
        String contraseniaEncriptada = RedSocialUtils.encriptarSHA256(contrasenia) ;
        if (estudiante.getContrasenia().equals(contraseniaEncriptada)) {
            respuesta = true;
        }
        return respuesta;
    }
}
