package proyecto.redsocial.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import proyecto.redsocial.model.Estudiante;

public class MainPageController {


    @FXML
    private Label LbPublicacion;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private VBox VboxInicio;

    @FXML
    private Label txtInformacion;

    @FXML
    private Label txtNombre;

    @FXML
    void OnAyuda(MouseEvent event) {

    }

    @FXML
    void OnGrupos(MouseEvent event) {

    }

    @FXML
    void Onmensajes(MouseEvent event) {

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
    void onPublicar(MouseEvent event) {

    }

    @FXML
    void initialize() {
    }

    public void cargarDatosVista(Estudiante estudiante) {
        txtNombre.setText(estudiante.getNombre());
        txtInformacion.setText(estudiante.getCorreo());
    }
}
