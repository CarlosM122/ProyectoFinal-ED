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

import javafx.scene.control.Button;
import javafx.event.ActionEvent;

import java.io.IOException;

public class GestionarUsuariosController {

    private Sistema sistema;
    private proyecto.redsocial.model.Moderador moderador;

    public void setSistema(Sistema sistema) {
        this.sistema = sistema;
        cargarEstudiantes(sistema);
    }

    public void setModerador(proyecto.redsocial.model.Moderador moderador) {
        this.moderador = moderador;
    }

    @FXML
    private VBox CerrarSecion;

    @FXML
    private VBox VBoxAmigosSugeridos;

    @FXML
    private VBox VBoxGrupos;

    @FXML
    private Button btnEditarUsuario;

    @FXML
    private Button btnEliminarUsuario;

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
    private TextField txtCorreoUsuario;

    @FXML
    private Label txtInformacion;

    @FXML
    private Label txtNombre;

    @FXML
    private TextField txtNombreUsuario;

    @FXML
    private TextField txtValoracionUsuario;

    @FXML
    void OnCerrarSesion(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/proyecto/redsocial/fxml/ModeradorView.fxml"));
            Parent root = loader.load();

            ModeradorController moderadorController = loader.getController();
            moderadorController.setSistema(sistema);
            if (moderador != null) {
                moderadorController.cargarDatosVista(moderador);
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Panel de Administrador");
            stage.show();

            Stage ventanaActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            ventanaActual.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    void OnEliminarUsuario(ActionEvent event) {
        Estudiante seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            sistema.getListaEstudiantes().eliminar(seleccionado);
            cargarEstudiantes(sistema);
            txtInformacion.setText("Usuario eliminado correctamente.");
        } else {
            txtInformacion.setText("Seleccione un usuario para eliminar.");
        }
    }

    @FXML
    void OnEditarUsuario(ActionEvent event) {
        Estudiante seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            String nuevoNombre = txtNombreUsuario.getText();
            if (!nuevoNombre.isEmpty()) {
                seleccionado.setNombre(nuevoNombre);
            }
            cargarEstudiantes(sistema);
            txtInformacion.setText("Usuario editado correctamente.");
        } else {
            txtInformacion.setText("Seleccione un usuario para editar.");
        }
    }

    @FXML
    public void initialize() {
        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtNombreUsuario.setText(newSelection.getNombre());
                txtCorreoUsuario.setText(newSelection.getCorreo());

                // Calcular promedio de valoraciones
                double suma = 0;
                int cantidad = 0;
                for (var v : newSelection.getValoracions()) {
                    suma += v.getValoracion();
                    cantidad++;
                }
                double promedio = cantidad > 0 ? (suma / cantidad) : 0.0;
                txtValoracionUsuario.setText(String.format("%.1f", promedio));
            }
        });
    }

    public void cargarEstudiantes(Sistema sistema) {
        // Asociar columnas si no se hizo en el FXML
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colValoracion.setCellValueFactory(cellData -> {
            // Calcular promedio manualmente porque ListaEnlazada no tiene stream()
            double suma = 0;
            int cantidad = 0;
            for (var v : cellData.getValue().getValoracions()) {
                suma += v.getValoracion();
                cantidad++;
            }
            double promedio = cantidad > 0 ? (suma / cantidad) : 0.0;
            return new javafx.beans.property.SimpleStringProperty(String.format("%.1f", promedio));
        });

        ObservableList<Estudiante> lista = FXCollections.observableArrayList(sistema.getEstudiantes());
        tablaUsuarios.setItems(lista);
    }


}

