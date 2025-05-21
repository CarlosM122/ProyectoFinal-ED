package proyecto.redsocial.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import proyecto.redsocial.factory.ModelFactory;
import proyecto.redsocial.model.Estudiante;

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
        if (event.getCode() == KeyCode.ENTER){
            enviarMensaje();
        }
    }

    @FXML
    void initialize() {
        modelFactory = ModelFactory.getInstance();
    }

    public void initData(Estudiante estudiante){
        estudiante1=estudiante;
        txtnombre.setText(estudiante1.getNombre());
        txtinformacion.setText(estudiante1.getCorreo());
        cargarListaAmigos();
    };

    private void cargarListaAmigos() {
        List<Estudiante> amigos = estudiante1.getAmigos();
        for (Estudiante estudiante : amigos) {
            cargarAmigo(estudiante);
        }
    }

    private void cargarAmigo(Estudiante estudiante) {
        HBox tarjeta = crearTarjetaChatLista(estudiante);

        tarjeta.setOnMouseClicked(event -> {
            estudiante2 = estudiante;
            nombreCompañero.setText(estudiante2.getNombre());
            // Aquí cargarías el chat de este compañero
        });

        listCompañeros.getChildren().add(tarjeta);
    }

    private HBox crearTarjetaChatLista(Estudiante estudiante) {
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