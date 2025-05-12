package proyecto.redsocial.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import proyecto.redsocial.factory.ModelFactory;

public class RegistroController {

    private ModelFactory modelFactory;



    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnRegistro;

    @FXML
    private PasswordField txtContrasenia;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtNombre;

    @FXML
    void onRegistrarse(ActionEvent event) {
        crearRegistro();
    }

    @FXML
    void initialize() {
        modelFactory = ModelFactory.getInstance();
    }

    private void crearRegistro() {
        if(validarDatos()){
            String correo = txtCorreo.getText();
            String nombre = txtNombre.getText();
            String contrasenia = txtContrasenia.getText().trim();
            if (modelFactory.registrarUsuario(nombre,correo,contrasenia)){
                mostrarMensaje("Registro","Registro Exitoso","Su registro fue exitoso.",Alert.AlertType.INFORMATION);
                //ABRIR PAGINA PRINCIPAL
            }else {
                mostrarMensaje("Error","Error al registrar","Error al reguistrarse, el correo ya existe.",Alert.AlertType.ERROR);
            }
        }else {
            mostrarMensaje("Error","Datos Nulos","Hay un campo nulo.", Alert.AlertType.ERROR);
        }
    }

    private boolean validarDatos() {
        return !txtNombre.getText().isEmpty() || !txtCorreo.getText().isEmpty() || !txtContrasenia.getText().isEmpty();
    }

    private void mostrarMensaje(String titulo, String header, String contenido, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.show();
    }

}
