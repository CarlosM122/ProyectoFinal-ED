package proyecto.redsocial.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import proyecto.redsocial.model.Estudiante;

import java.io.File;

public class AmigosController {

    private Estudiante estudianteActual;

    private final Image imagenDefault = new Image(new File("archivos_perfil/usuario.png").toURI().toString());

    private final Label titulo = new Label("Tu Lista de Amigos");

    @FXML
    private VBox contenedorAmigos;

    public void inicializarDatos(Estudiante estudianteActual, MainPageController mainPageController) {
        this.estudianteActual = estudianteActual;

        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333;");
        contenedorAmigos.getChildren().clear();
        contenedorAmigos.getChildren().add(titulo);

        cargarAmigos();
    }

    private void cargarAmigos() {
        if (estudianteActual != null && estudianteActual.getAmigos() != null) {
            for (Estudiante amigo : estudianteActual.getAmigos().aLista()) {
                HBox tarjeta = crearTarjetaAmigo(amigo);
                contenedorAmigos.getChildren().add(tarjeta);
            }
        }
    }

    private HBox crearTarjetaAmigo(Estudiante amigo) {
        HBox tarjeta = new HBox(15);
        tarjeta.setPadding(new Insets(15));
        tarjeta.setAlignment(Pos.CENTER_LEFT);
        tarjeta.setStyle("""
            -fx-background-color: linear-gradient(to right, #fdfbfb, #ebedee);
            -fx-background-radius: 12;
            -fx-border-radius: 12;
            -fx-border-color: #dcdcdc;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 2);
        """);

        // Imagen de perfil circular
        Circle circlePerfil = new Circle(35);
        Image imagen = obtenerImagenPerfil(amigo.getRutaArchivoImagen());
        circlePerfil.setFill(new ImagePattern(imagen));
        circlePerfil.setStroke(Color.web("#ccc"));
        circlePerfil.setStrokeWidth(1.5);

        // Información textual
        VBox info = new VBox(6);
        Label nombre = new Label(amigo.getNombre());
        nombre.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #222;");

        String textoIntereses = (amigo.getIntereses() != null && !amigo.getIntereses().estaVacia())
                ? String.join(", ", amigo.getIntereses().aLista())
                : "Este usuario aún no ha agregado intereses";

        Label intereses = new Label("Intereses: " + textoIntereses);
        intereses.setStyle("-fx-font-size: 13px; -fx-text-fill: #666;");

        info.getChildren().addAll(nombre, intereses);

        Region espacio = new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);

        tarjeta.getChildren().addAll(circlePerfil, info, espacio);

        tarjeta.setOnMouseEntered(e -> tarjeta.setStyle(tarjeta.getStyle() + "-fx-cursor: hand; -fx-background-color: #f0f2f5;"));
        tarjeta.setOnMouseExited(e -> tarjeta.setStyle(tarjeta.getStyle().replace("-fx-background-color: #f0f2f5;", "-fx-background-color: linear-gradient(to right, #fdfbfb, #ebedee);")));

        tarjeta.setOnMouseClicked(event -> {
            mostrarInformacionAmigo(amigo);
        });

        return tarjeta;
    }

    private Image obtenerImagenPerfil(String rutaArchivo) {
        if (rutaArchivo != null && !rutaArchivo.isBlank()) {
            File archivo = new File("archivos_perfil", rutaArchivo);
            if (archivo.exists()) {
                return new Image(archivo.toURI().toString());
            }
        }
        return imagenDefault;
    }

    private void mostrarInformacionAmigo(Estudiante amigo) {
        StringBuilder contenido = new StringBuilder();
        contenido.append("Nombre: ").append(amigo.getNombre()).append("\n");

        if (amigo.getInformacion() != null && !amigo.getInformacion().isBlank()) {
            contenido.append("Información: ").append(amigo.getInformacion()).append("\n");
        }

        if (amigo.getIntereses() != null && !amigo.getIntereses().estaVacia()) {
            contenido.append("Intereses: ").append(String.join(", ", amigo.getIntereses().aLista())).append("\n");
        } else {
            contenido.append("Este usuario no ha agregado intereses.");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Perfil de " + amigo.getNombre());
        alert.setHeaderText(null);
        alert.setContentText(contenido.toString());
        alert.showAndWait();
    }
}
