package proyecto.redsocial.utils;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import proyecto.redsocial.RedSocialApplication;
import proyecto.redsocial.controller.MainPageController;
import proyecto.redsocial.model.Estudiante;
import proyecto.redsocial.model.Moderador;
import proyecto.redsocial.model.Sistema;

import java.io.IOException;
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
        est.setRutaArchivoImagen("/proyecto/redsocial/imagenesFotoPerfil/July.png");
        s.getListaEstudiantes().agregar(est);

        Estudiante est1 = new Estudiante();
        est1.setNombre("AlejoElAmorDeMaria");
        est1.setCorreo("Alejo@edu.co");
        String contrasenia = encriptarSHA256("123456");
        est1.setContrasenia(contrasenia);
        s.getListaEstudiantes().agregar(est1);

        est.getAmigos().add(est1);
        est1.getAmigos().add(est);


        Estudiante est2 = new Estudiante();
        est2.setNombre("Samuel");
        est2.setCorreo("Samuel@edu.co");
        est2.setContrasenia(encriptarSHA256("samuel123"));
        est2.setRutaArchivoImagen("/proyecto/redsocial/imagenesFotoPerfil/Samu.jpg");
        s.getListaEstudiantes().agregar(est2);

        est1.getAmigos().add(est2);
        est2.getAmigos().add(est1);


        // Crear moderador
        Moderador mod = new Moderador();
        mod.setNombre("Admin");
        mod.setCorreo("admin@edu.co");
        String passModerador = "admin123";
        String passEncriptadaMod = encriptarSHA256(passModerador);
        mod.setContrasenia(passEncriptadaMod);
        s.getListaModeradores().agregar(mod);

        s.getGestorGruposEstudio().asignarEstudiantesAGrupos(s.getListaEstudiantes().aLista());
        s.inicializarSistema();
        s.getRedAfinidad().conectarEstudiantes(est, est1,s.getConexionesAfinidad());
        s.getRedAfinidad().conectarEstudiantes(est1, est2, s.getConexionesAfinidad());
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

    public static HBox crearTarjetaEstudiante(Estudiante estudiante) {
        HBox tarjeta = new HBox(10);
        tarjeta.setAlignment(Pos.CENTER_LEFT);
        tarjeta.setPadding(new Insets(8));
        tarjeta.setStyle("""
                    -fx-background-color: #f4f4f8;
                    -fx-background-radius: 10;
                    -fx-cursor: hand;
                """);

        // Crear avatar circular (placeholder)
        Circle avatar = new Circle(20, Color.web("#6a8caf"));
        // Si tienes imagen, usar ImageView así:
        // ImageView avatar = new ImageView(new Image("ruta/al/avatar.png"));
        // avatar.setFitWidth(40);
        // avatar.setFitHeight(40);
        // avatar.setClip(new Circle(20, 20, 20));

        // Nombre
        Label nombre = new Label(estudiante.getNombre());
        nombre.setFont(Font.font("System", FontWeight.BOLD, 14));
        nombre.setTextFill(Color.web("#333333"));

        // Estado o info adicional (puedes cambiar texto)
        Label estado = new Label("Activo ahora");
        estado.setFont(Font.font("System", 12));
        estado.setTextFill(Color.web("#777777"));

        VBox textos = new VBox(4, nombre, estado);

        tarjeta.getChildren().addAll(avatar, textos);

        // Estilo hover para mejor experiencia
        tarjeta.setOnMouseEntered(e -> tarjeta.setStyle("""
                    -fx-background-color: #e0e5f2;
                    -fx-background-radius: 10;
                    -fx-cursor: hand;
                """));
        tarjeta.setOnMouseExited(e -> tarjeta.setStyle("""
                    -fx-background-color: #f4f4f8;
                    -fx-background-radius: 10;
                    -fx-cursor: hand;
                """));

        return tarjeta;
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
