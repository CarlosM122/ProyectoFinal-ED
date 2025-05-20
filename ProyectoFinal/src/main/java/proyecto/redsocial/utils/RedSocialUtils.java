package proyecto.redsocial.utils;

import proyecto.redsocial.model.ColaPrioridadAyuda;
import proyecto.redsocial.model.Estudiante;
import proyecto.redsocial.model.Moderador;
import proyecto.redsocial.model.Sistema;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RedSocialUtils {

    public static Sistema inicializarSistema() {
        Sistema s = new Sistema();

        // Crear estudiante
        Estudiante est = new Estudiante();
        est.setNombre("July");
        est.setCorreo("July@edu.co");
        String contraseniaEncriptada = encriptarSHA256("123456");
        est.setContrasenia(contraseniaEncriptada);
        s.getEstudiantes().add(est);

        Estudiante est1 = new Estudiante();
        est1.setNombre("AlejoElAmorDeMaria");
        est1.setCorreo("Alejo@edu.co");
        String contrasenia = encriptarSHA256("123456");
        est1.setContrasenia(contrasenia);
        s.getEstudiantes().add(est1);

        est.getAmigos().add(est1);
        est1.getAmigos().add(est);

        // Crear moderador
        Moderador mod = new Moderador();
        mod.setNombre("Admin");
        mod.setCorreo("admin@edu.co");
        String passModerador = "admin123";
        String passEncriptadaMod = encriptarSHA256(passModerador);
        mod.setContrasenia(passEncriptadaMod);
        s.getModeradores().add(mod);

        return s;
    }


    public static String encriptarSHA256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b)); // Convertir a hexadecimal
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al encriptar la contraseña", e);
        }
    }
}
