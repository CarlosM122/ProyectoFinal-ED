package proyecto.redsocial.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import proyecto.redsocial.factory.ModelFactory;
import proyecto.redsocial.model.SolicitudAyuda;

public class SolicitudAyudaView {
    private ModelFactory modelFactory = ModelFactory.getInstance();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnEnviarSolicitud;
    
    @FXML
    private TextArea txtDescripcion;
    private final Map<String, String> urgenciasPorTema = new HashMap<>();

    @FXML
    private ComboBox<String> cbTemasAyuda;


    @FXML
    void onEnviarSolicitud(ActionEvent event) {
        enviarSolicitud();
    }

    private void enviarSolicitud() {
        String tema = cbTemasAyuda.getValue();
        String descripcion = txtDescripcion.getText().trim();

        if (tema == null || descripcion.isEmpty()) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Campos incompletos");
            alerta.setHeaderText(null);
            alerta.setContentText("Por favor selecciona un tema y escribe la descripción del problema.");
            alerta.showAndWait();
            return;
        }

        String urgencia = urgenciasPorTema.getOrDefault(tema, "Baja");
        SolicitudAyuda solicitudAyuda=new SolicitudAyuda();
        solicitudAyuda.setTema(tema);
        solicitudAyuda.setUrgencia(urgencia);
        solicitudAyuda.setDescripcion(descripcion);


        System.out.println("Tema: " + tema);
        System.out.println("Descripción: " + descripcion);
        System.out.println("Urgencia: " + urgencia);

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Solicitud enviada");
        alerta.setHeaderText(null);
        alerta.setContentText("Tu solicitud ha sido enviada con éxito.\n"
                + "Urgencia asignada: " + urgencia);
        alerta.showAndWait();

        cbTemasAyuda.getSelectionModel().clearSelection();
        txtDescripcion.clear();
        modelFactory.guardarSolicitud(solicitudAyuda);
    }

    @FXML
    void initialize() {
        inicializarComboBox();
    }

    private void inicializarComboBox() {
        cbTemasAyuda.getItems().addAll(
                "Error al acceder a un grupo",
                "Cambio de contraseña",
                "Problemas técnicos"

        );
        urgenciasPorTema.put("Inicio de sesión", "Alta");
        urgenciasPorTema.put("Registro de usuario", "Media");
        urgenciasPorTema.put("Cambio de contraseña", "Media");
        urgenciasPorTema.put("Problemas técnicos", "Alta");

    }

}

