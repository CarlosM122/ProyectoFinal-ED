package proyecto.redsocial.controller;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.List;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import proyecto.redsocial.factory.ModelFactory;
import proyecto.redsocial.model.Estudiante;
import proyecto.redsocial.utils.RedSocialUtils;

public class ChatEstudianteController {

    private ModelFactory modelFactory;

    private Estudiante estudiante1;
    private Estudiante estudiante2;

    @FXML
    private VBox boxAyuda;

    @FXML
    private VBox boxInicio;

    @FXML
    private Button btnEnviar;

    @FXML
    private VBox chatBox;

    @FXML
    private Label infoCompañero;

    @FXML
    private VBox listCompañeros;

    @FXML
    private Label nombreCompañero;

    @FXML
    private TextField txtBuscar;

    @FXML
    private HBox contenerdorImagenPerfil;

    @FXML
    private TextField txtMensaje;

    @FXML
    private Label txtinformacion;

    @FXML
    private Label txtnombre;

    @FXML
    void OnEnviar(ActionEvent event) {
        enviarMensaje();
    }

    @FXML
    void enviar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            enviarMensaje();
        }
    }

    @FXML
    void initialize() {
        modelFactory = ModelFactory.getInstance();
    }

    public void initData(Estudiante estudiante) {
        estudiante1 = estudiante;
        txtnombre.setText(estudiante1.getNombre());
        txtinformacion.setText(estudiante1.getCorreo());
        cargarListaAmigos();
    }

    ;

    private void cargarListaAmigos() {
        List<Estudiante> amigos = estudiante1.getAmigos();
        for (Estudiante estudiante : amigos) {
            cargarAmigo(estudiante);
        }
    }

    private void cargarAmigo(Estudiante estudiante) {
        HBox tarjeta = RedSocialUtils.crearTarjetaEstudiante(estudiante);

        tarjeta.setUserData(estudiante);
        tarjeta.setOnMouseClicked(event -> {
            Estudiante estudianteCompañero = (Estudiante) tarjeta.getUserData();
            estudiante2 = estudianteCompañero;
            nombreCompañero.setText(estudiante2.getNombre());
            cargarFotoPerfil(estudianteCompañero.getRutaArchivoImagen());
            infoCompañero.setText(estudiante2.getCorreo());
            // Aquí cargarías el chat de este compañero
        });

        listCompañeros.getChildren().add(tarjeta);
    }

    private void cargarFotoPerfil(String rutaArchivoImagen) {
        try {
            if (rutaArchivoImagen != null && !rutaArchivoImagen.isBlank()) {
                Image imagen = new Image(Objects.requireNonNull(getClass().getResource(rutaArchivoImagen)).toExternalForm());

                double radioPerfil = 45;
                Circle circlePerfil = new Circle(radioPerfil);
                circlePerfil.setFill(new ImagePattern(imagen));
                circlePerfil.setStroke(Color.BLACK);
                circlePerfil.setStrokeWidth(2);

                contenerdorImagenPerfil.getChildren().clear();
                contenerdorImagenPerfil.getChildren().add(circlePerfil);
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar la imagen de perfil: " + e.getMessage());
        }
    }

    private void enviarMensaje() {
        String mensaje = txtMensaje.getText().trim();
        if (!mensaje.isEmpty()) {
            VBox tarjeta = crearTarjetaChat();
            Label nombre = crearLabelNombre(estudiante1.getNombre());
            Node contenidoMensaje = crearMensaje(mensaje);
            tarjeta.getChildren().addAll(nombre, contenidoMensaje);
            chatBox.getChildren().add(tarjeta);
            txtMensaje.clear();
        }
    }

    private VBox crearTarjetaChat() {
        VBox tarjeta = new VBox(8);
        tarjeta.setStyle("""
                    -fx-background-color: #ffffff;
                    -fx-padding: 12;
                    -fx-background-radius: 12;
                    -fx-border-color: #dddddd;
                    -fx-border-radius: 12;
                    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);
                """);
        return tarjeta;
    }

    private Node crearMensaje(String texto) {
        Label contenido = new Label(texto);
        contenido.setWrapText(true);
        contenido.setStyle("-fx-font-size: 13px; -fx-text-fill: #444444;");
        return contenido;
    }

    private Label crearLabelNombre(String nombre) {
        Label tema = new Label(nombre);
        tema.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2a2a2a;");
        return tema;
    }
}