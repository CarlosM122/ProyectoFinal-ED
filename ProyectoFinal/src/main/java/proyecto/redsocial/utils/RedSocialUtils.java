package proyecto.redsocial.utils;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

import java.io.File;
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
        est.setInformacion("Universidad Del Quindio\nFacultad de Ingenieria\nIngenieria de Sistemas");
        String contraseniaEncriptada = encriptarSHA256("123456");
        est.setContrasenia(contraseniaEncriptada);
        est.setRutaArchivoImagen("July.png");
        s.getListaEstudiantes().agregar(est);

        Estudiante est1 = new Estudiante();
        est1.setNombre("AlejoElAmorDeMaria");
        est1.setCorreo("Alejo@edu.co");
        est1.setInformacion("Universidad Del Quindio\nFacultad de Ingenieria\nIngenieria de Sistemas");
        String contrasenia = encriptarSHA256("123456");
        est1.setContrasenia(contrasenia);
        s.getListaEstudiantes().agregar(est1);

        Estudiante est2 = new Estudiante();
        est2.setNombre("Samuel");
        est2.setCorreo("Samuel@edu.co");
        est2.setContrasenia(encriptarSHA256("samuel123"));
        est2.setInformacion("Universidad Del Quindio\nFacultad de Ingenieria\nIngenieria de Sistemas");
        est2.setRutaArchivoImagen("Samu.jpg");
        s.getListaEstudiantes().agregar(est2);

        Estudiante est3 = new Estudiante();
        est3.setNombre("Pablo");
        est3.setCorreo("Pablo@edu.co");
        est3.setContrasenia(encriptarSHA256("pablo123"));
        est3.setInformacion("Universidad Del Quindio\nFacultad de Ingenieria\nIngenieria de Sistemas");
        est3.setRutaArchivoImagen("Pablo.jpg");
        s.getListaEstudiantes().agregar(est3);

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
        s.getRedAfinidad().conectarEstudiantes(est, est1, s.getConexionesAfinidad());
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

        // Cargar imagen de perfil
        Image imagen;
        if (estudiante.getRutaArchivoImagen() != null && !estudiante.getRutaArchivoImagen().isBlank()) {
            File archivo = new File("archivos_perfil", estudiante.getRutaArchivoImagen());
            if (archivo.exists()) {
                imagen = new Image(archivo.toURI().toString());
            } else {
                imagen = new Image(new File("archivos_perfil/usuario.png").toURI().toString());
            }
        } else {
            imagen = new Image(new File("archivos_perfil/usuario.png").toURI().toString());
        }

        // Crear avatar circular con imagen
        ImageView avatarView = new ImageView(imagen);
        avatarView.setFitWidth(40);
        avatarView.setFitHeight(40);
        Circle clip = new Circle(20, 20, 20);
        avatarView.setClip(clip);

        // Nombre
        Label nombre = new Label(estudiante.getNombre());
        nombre.setFont(Font.font("System", FontWeight.BOLD, 14));
        nombre.setTextFill(Color.web("#333333"));

        // Estado o info adicional
        Label estado = new Label("Activo ahora");
        estado.setFont(Font.font("System", 12));
        estado.setTextFill(Color.web("#777777"));

        VBox textos = new VBox(4, nombre, estado);

        tarjeta.getChildren().addAll(avatarView, textos);

        // Estilo hover
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

    public static void mostrarMensaje(String titulo, String header, String contenido, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.show();
    }

    public static void aplicarEstiloBotonGradiente(Button boton) {
        boton.setStyle("""
                    -fx-background-color: linear-gradient(to right, #f9d423, #ff4e50);
                    -fx-background-radius: 90;
                    -fx-padding: 6 16 6 16;
                    -fx-text-fill: #333333;
                    -fx-font-weight: bold;
                    -fx-font-size: 13px;
                """);
        boton.setCursor(Cursor.HAND);
    }


    public static void cargarVistaPrincipal(Estudiante estudiante) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(RedSocialApplication.class.getResource("/proyecto/redsocial/fxml/mainPage-view.fxml"));
            Parent root = fxmlLoader.load();

            MainPageController mainPageController = fxmlLoader.getController();
            mainPageController.cargarDatosVista(estudiante);

            Stage nuevaVentana = new Stage();
            Scene scene = new Scene(root);
            nuevaVentana.setTitle("MindWave");
            nuevaVentana.setScene(scene);
            nuevaVentana.setMaximized(true);
            nuevaVentana.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void CargaVentana(FXMLLoader fxmlLoader, String titulo) throws IOException {
        Parent root = fxmlLoader.load();
        Stage nuevaVentana = new Stage();
        Scene scene = new Scene(root);
        nuevaVentana.setTitle(titulo);
        nuevaVentana.setScene(scene);
        nuevaVentana.setResizable(false);
        nuevaVentana.show();
    }
}
