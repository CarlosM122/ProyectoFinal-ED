package proyecto.redsocial.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import proyecto.redsocial.model.Moderador;

import java.io.IOException;

public class ModeradorController {

    @FXML
    private Label LbPublicacion;

    @FXML
    private VBox VBoxAmigosSugeridos;

    @FXML
    private VBox VBoxGrupos;

    @FXML
    private VBox btnCerrarSesion;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnEliminar;

    @FXML
    private VBox btnGestUsuarios;

    @FXML
    private VBox contenedorPublicaciones;

    @FXML
    private TextField txtBusqueda;

    @FXML
    private Label txtInformacion;

    @FXML
    private Label txtNombre;

    @FXML
    void OnAyuda(MouseEvent event) {

    }

    @FXML
    void OnCerrarSesion(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/proyecto/redsocial/fxml/login-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Iniciar Sesión");
            stage.show();

            // Cierra la ventana actual de forma segura
            Stage ventanaActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            ventanaActual.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    void OnEditar(ActionEvent event) {

    }

    @FXML
    void OnEliminar(ActionEvent event) {

    }

    @FXML
    void OnGestionarusuarios(MouseEvent event) {

    }

    @FXML
    void onAmigos(MouseEvent event) {

    }

    @FXML
    void onPublicar(MouseEvent event) {

    }
}

