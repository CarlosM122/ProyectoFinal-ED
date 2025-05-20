package proyecto.redsocial.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import proyecto.redsocial.model.Estudiante;
import proyecto.redsocial.model.Sistema;

public class GestionarUsuariosController {

    @FXML
    private VBox CerrarSecion;

    @FXML
    private VBox VBoxAmigosSugeridos;

    @FXML
    private VBox VBoxGrupos;

    @FXML
    private TableView<Estudiante> tablaUsuarios;

    @FXML
    private TableColumn<Estudiante, String> colCorreo;

    @FXML
    private TableColumn<Estudiante, String> colNombre;

    @FXML
    private TableColumn<Estudiante, String> colValoracion;

    @FXML
    private TextField txtBusqueda;

    @FXML
    private Label txtInformacion;

    @FXML
    private Label txtNombre;

    public void cargarEstudiantes(Sistema sistema) {
        // Asociar columnas si no se hizo en el FXML
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colValoracion.setCellValueFactory(cellData -> {
            double promedio = cellData.getValue().getValoracions().stream()
                    .mapToInt(v -> v.getValoracion())
                    .average()
                    .orElse(0.0);
            return new javafx.beans.property.SimpleStringProperty(String.format("%.1f", promedio));
        });

        ObservableList<Estudiante> lista = FXCollections.observableArrayList(sistema.getEstudiantes());
        tablaUsuarios.setItems(lista);
    }


    @FXML
    void OnCerrarSesion(MouseEvent event) {
        try {
            // Cambia la ruta al FXML de moderador
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/proyecto/redsocial/fxml/Moderadorview.fxml"));
            Parent root = loader.load();

            // Mostrar la vista del moderador
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Panel del Moderador");
            stage.show();

            // Cierra la ventana actual
            Stage ventanaActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            ventanaActual.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

