package proyecto.redsocial.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import proyecto.redsocial.factory.ModelFactory;
import proyecto.redsocial.model.Estudiante;
import proyecto.redsocial.model.SolicitudAyuda;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SolicitudAyudaController {
    private final List<String> CBUrgencia = new ArrayList<>();
    private final ModelFactory modelFactory = ModelFactory.getInstance();
    private Estudiante estudiante;
    private MainPageController mainPageController;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> CBTemas;

    @FXML
    private VBox VBoxAmigosSugeridos;

    @FXML
    private VBox VBoxGrupos;

    @FXML
    private VBox VboxInicio;

    @FXML
    private Button btnGenerarSolicitud;

    @FXML
    private VBox contenedorSolicitudes;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private Label txtInformacion;

    @FXML
    private Label txtNombre;

    @FXML
    void OnAyuda(MouseEvent event) {

    }

    @FXML
    void Onmensajes(MouseEvent event) {

    }

    @FXML
    public void OnbtnGenerarSolicitud(MouseEvent event) {
        enviarSolicitud();
    }

    @FXML
    void onAmigos(MouseEvent event) {

    }

    @FXML
    void onInicio(MouseEvent event) {

    }

    @FXML
    void onNotificaciones(MouseEvent event) {

    }

    @FXML
    void initialize() {
        CBTemas.getItems().addAll(
                "Cambio de contraseña",
                "Problemas técnicos",
                "No puedo subir archivos",
                "No puedo iniciar sesión",
                "Error en publicaciones",
                "Solicitud de eliminación de cuenta",
                "Problemas con notificaciones",
                "Otros"
        );
        CBUrgencia.addAll(List.of("Baja", "Media", "Alta", "Crítica"));
    }

    public void cargarDatos(Estudiante estudiante, MainPageController mainPageController) {
        this.estudiante = estudiante;
        this.mainPageController = mainPageController;
        txtNombre.setText(estudiante.getNombre());
    }

    private void enviarSolicitud() {
        String tema = CBTemas.getValue();
        if (tema != null && txtDescripcion.getText() != null) {
            int urgencia = obtenerUrgencia(CBTemas.getValue());
            SolicitudAyuda solicitudAyuda = new SolicitudAyuda();
            solicitudAyuda.setTema(tema);
            solicitudAyuda.setUrgencia(urgencia);
            solicitudAyuda.setEstudiante(estudiante);
            modelFactory.guardarSolicitud(solicitudAyuda);
            Node descripcion = crearTarjetaContenido(txtDescripcion.getText() + "\n" + urgencia);
            Label titulo = crearTarjetaTema(tema);
            VBox tarjeta = crearTarjetaSolicitud();
            tarjeta.getChildren().addAll(titulo, descripcion);
            contenedorSolicitudes.getChildren().add(tarjeta);
            modelFactory.guardarRecursosXML();
        }
    }

    private Label crearTarjetaTema(String temaTexto) {
        Label tema = new Label(temaTexto);
        tema.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2a2a2a;");
        return tema;
    }

    private Node crearTarjetaContenido(String texto) {
        Label contenido = new Label(texto);
        contenido.setWrapText(true);
        contenido.setStyle("-fx-font-size: 13px; -fx-text-fill: #444444;");
        return contenido;
    }

    private int obtenerUrgencia(String value) {
        switch (value) {
            case "No puedo subir archivos":
            case "Error en publicaciones":
                return 2;

            case "Cambio de contraseña":
            case "Problemas técnicos":
            case "No puedo iniciar sesión":
            case "Solicitud de eliminación de cuenta":
                return 3;

            default:
                return 1;
        }
    }

    private void mostrarMensaje(String titulo, String header, String contenido, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.show();
    }

    private VBox crearTarjetaSolicitud() {
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
}
