package proyecto.redsocial.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import proyecto.redsocial.RedSocialApplication;
import proyecto.redsocial.factory.ModelFactory;
import proyecto.redsocial.model.Estudiante;
import proyecto.redsocial.model.Moderador;
import proyecto.redsocial.utils.RedSocialUtils;

import java.io.IOException;

public class LoginController {

    private ModelFactory modelFactory;

    @FXML
    private Button btnIngreso;

    @FXML
    private Button btnRegistro;

    @FXML
    private PasswordField txtContrasenia;

    @FXML
    private TextField txtUsuario;

    @FXML
    void onIngresar(ActionEvent event) {
        ingresar();
    }

    @FXML
    void onRegistrar(ActionEvent event) {
        cargarVistaRegistro();
        cerrarVentanaLogin();
    }

    @FXML
    void initialize() {
        modelFactory = ModelFactory.getInstance();
    }

    private void ingresar() {
        if (validarDatos()) {
            String contrasenia = txtContrasenia.getText();
            String correo = txtUsuario.getText();

            if (modelFactory.verificarCredenciales(correo, contrasenia)) {
                Object usuario = modelFactory.obtnerUsuario(correo);

                if (usuario instanceof Moderador) {
                    cargarVistaModerador((Moderador) usuario);
                } else if (usuario instanceof Estudiante) {
                    RedSocialUtils.cargarVistaPrincipal((Estudiante) usuario);
                }

                cerrarVentanaLogin();
            } else {
                mostrarMensaje("Error", "Error Inicio De Sesion", "Usuario o contraseña incorrectos.", Alert.AlertType.ERROR);
            }

        } else {
            mostrarMensaje("Error", "Datos Nulos", "Hay un campo nulo.", Alert.AlertType.ERROR);
        }
    }

    private void cargarVistaModerador(Moderador moderador) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/proyecto/redsocial/fxml/ModeradorView.fxml"));
            Parent root = loader.load();

            ModeradorController moderadorController = loader.getController();
            moderadorController.cargarDatosVista(moderador);


            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Panel de Administrador");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarVistaRegistro() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(RedSocialApplication.class.getResource("/proyecto/redsocial/fxml/registro-view.fxml"));
            CargaVentana(fxmlLoader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validarDatos() {
        return !txtContrasenia.getText().isEmpty() && !txtUsuario.getText().isEmpty();
    }

    private void mostrarMensaje(String titulo, String header, String contenido, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.show();
    }

    private void CargaVentana(FXMLLoader fxmlLoader) throws IOException {
        Parent root = fxmlLoader.load();
        Stage nuevaVentana = new Stage();
        Scene scene = new Scene(root);
        nuevaVentana.setTitle("Nombre Web");
        nuevaVentana.setScene(scene);
        nuevaVentana.setMaximized(true);
        nuevaVentana.show();
    }

    private void cerrarVentanaLogin() {
        Stage stage = (Stage) btnRegistro.getScene().getWindow();
        stage.close();
    }

}
