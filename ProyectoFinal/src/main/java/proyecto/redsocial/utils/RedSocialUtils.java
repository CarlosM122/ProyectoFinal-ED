package proyecto.redsocial.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import proyecto.redsocial.RedSocialApplication;
import proyecto.redsocial.controller.MainPageController;
import proyecto.redsocial.model.ColaPrioridadAyuda;
import proyecto.redsocial.model.Estudiante;
import proyecto.redsocial.model.Moderador;
import proyecto.redsocial.model.Sistema;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class RedSocialUtils {

    public static Sistema inicializarSistema() {
        Sistema s = new Sistema();

        // Crear estudiante
        Estudiante est = new Estudiante();
        est.setNombre("July");
        est.setCorreo("July@edu.co");
        String contraseniaEncriptada = encriptarSHA256("123456");
        est.setContrasenia(contraseniaEncriptada);
        est.setRutaArchivoImagen("/proyecto/redsocial/imagenesFotoPerfil/July.png");
        s.getEstudiantes().add(est);
        s.getGrafoAfinidad().agregarEstudiante(est);

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

        s.getGestorGruposEstudio().asignarEstudiantesAGrupos(s.getEstudiantes());

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


    public static void cargarVistaPrincipal(Estudiante estudiante) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(RedSocialApplication.class.getResource("/proyecto/redsocial/fxml/mainPage-view.fxml"));
            Parent root = fxmlLoader.load();

            MainPageController mainPageController = fxmlLoader.getController();
            mainPageController.cargarDatosVista(estudiante);

            Stage nuevaVentana = new Stage();
            Scene scene = new Scene(root);
            nuevaVentana.setTitle("Nombre Web");
            nuevaVentana.setScene(scene);
            nuevaVentana.setMaximized(true);
            nuevaVentana.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void CargaVentana(FXMLLoader fxmlLoader,String titulo) throws IOException {
        Parent root = fxmlLoader.load();
        Stage nuevaVentana = new Stage();
        Scene scene = new Scene(root);
        nuevaVentana.setTitle(titulo);
        nuevaVentana.setScene(scene);
        nuevaVentana.setResizable(false);
        nuevaVentana.show();
    }
}
