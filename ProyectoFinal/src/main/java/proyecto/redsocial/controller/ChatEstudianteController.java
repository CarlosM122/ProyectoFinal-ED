package proyecto.redsocial.controller;

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
import javafx.scene.layout.VBox;
import proyecto.redsocial.factory.ModelFactory;
import proyecto.redsocial.model.Estudiante;

public class ChatEstudianteController {

    private ModelFactory modelFactory;

    private Estudiante estudiante1;
    private Estudiante estudiante2;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private VBox boxAyuda;

    @FXML
    private VBox boxInicio;

    @FXML
    private Button btnEnviar;

    @FXML
    private VBox chatBox;

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
        cargarListaAmigos();

    }

    private void cargarListaAmigos() {
        List<Estudiante> amigos = estudiante1.getAmigos();
        for (Estudiante estudiante : amigos) {
            cargarAmigos(estudiante);
        }
    }

    private void cargarAmigos(Estudiante estudiante) {
        VBox tarjeta = crearTarjetaChat();
        Label nombre = crearLabelNombre(estudiante.getNombre());
        tarjeta.getChildren().add(nombre);
        listCompañeros.getChildren().add(tarjeta);
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

    public void cargarDatos(Estudiante estudiante) {
        this.estudiante1 = estudiante;
    }
}

