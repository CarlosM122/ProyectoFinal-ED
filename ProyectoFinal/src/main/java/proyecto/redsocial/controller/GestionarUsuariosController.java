package proyecto.redsocial.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import proyecto.redsocial.model.Estudiante;
import proyecto.redsocial.model.Moderador;
import proyecto.redsocial.model.Sistema;

import java.io.IOException;

public class GestionarUsuariosController {

    private Sistema sistema;
    private proyecto.redsocial.model.Moderador moderador;


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
    private TableColumn<Estudiante, String> colContrasena; // Nueva columna

    @FXML
    private TableColumn<Estudiante, Integer> colSolicitudesAyuda;

    @FXML
    private TableColumn<Estudiante, Integer> colMensajes;

    @FXML
    private TableColumn<Estudiante, Integer> colAmigos;

    @FXML
    private TableColumn<Estudiante, Integer> colContenidos;

    @FXML
    private TableColumn<Estudiante, Integer> colValoraciones;

    @FXML
    private TableColumn<Estudiante, Integer> colGrupos;

    @FXML
    private TableColumn<Estudiante, Integer> colIntereses;

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
    private TextField txtContrasenaUsuario; // Nuevo campo

    @FXML
    void OnCerrarSesion(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/proyecto/redsocial/fxml/ModeradorView.fxml"));
            Parent root = loader.load();

            ModeradorController moderadorController = loader.getController();
            moderadorController.cargarDatosVista(moderador);


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
            tablaUsuarios.getItems().remove(seleccionado);
            mostrarAlerta("Usuario eliminado correctamente.");
        } else {
            mostrarAlerta("Seleccione un usuario para eliminar.");
        }
    }

    @FXML
    void OnEditarUsuario(ActionEvent event) {
        Estudiante seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            String nuevoNombre = txtNombreUsuario.getText().trim();
            String nuevoCorreo = txtCorreoUsuario.getText().trim();
            String nuevaContrasenia = txtContrasenaUsuario.getText().trim();

            if (!nuevoNombre.isEmpty() && nuevoNombre.length() <= 50) {
                seleccionado.setNombre(nuevoNombre);
            } else {
                mostrarAlerta("Nombre inválido. Asegúrese de que no esté vacío y tenga menos de 50 caracteres.");
                return;
            }

            if (!nuevoCorreo.isEmpty() && nuevoCorreo.length() <= 50) {
                seleccionado.setCorreo(nuevoCorreo);
            } else {
                mostrarAlerta("Correo inválido. Asegúrese de que no esté vacío y tenga menos de 50 caracteres.");
                return;
            }

            if (!nuevaContrasenia.isEmpty() && nuevaContrasenia.length() <= 50) {
                seleccionado.setContrasenia(nuevaContrasenia);
            } else {
                mostrarAlerta("Contraseña inválida. Asegúrese de que no esté vacía y tenga menos de 50 caracteres.");
                return;
            }

            tablaUsuarios.refresh();
            cargarEstudiantes(sistema, moderador);
            mostrarAlerta("Usuario editado correctamente.");
        } else {
            mostrarAlerta("Seleccione un usuario para editar.");
        }
    }

    @FXML
    void initialize() {
        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtNombreUsuario.setText(newSelection.getNombre());
                txtCorreoUsuario.setText(newSelection.getCorreo());
                txtContrasenaUsuario.setText(newSelection.getContrasenia());
            }
        });
    }

    /**
     * Carga los datos de la vista para el moderador, igual que al iniciar sesión.
     */
    public void cargarDatosVista(Moderador moderador) {
        this.moderador = moderador;
        // Si necesitas cargar datos adicionales del sistema, hazlo aquí
        // Por ejemplo, cargar la lista de estudiantes:
        if (sistema == null && moderador != null) {
            // Si tienes acceso a ModelFactory, puedes obtener el sistema aquí
            // sistema = ModelFactory.getInstance().getSistema();
        }
        cargarEstudiantes(sistema, moderador);
        // Puedes agregar aquí la carga de otros datos que se muestran al moderador
        // como nombre, información, etc.
        if (moderador != null) {
            txtNombre.setText(moderador.getNombre());
            txtInformacion.setText("Bienvenido, " + moderador.getNombre());
        }
    }

    protected void cargarEstudiantes(Sistema sistema, Moderador moderador) {
        ObservableList<Estudiante> estudiantes = FXCollections.observableArrayList();
        var lista = sistema.getListaEstudiantes();
        for (int i = 0; i < lista.size(); i++) {
            estudiantes.add(lista.get(i));
        }
        tablaUsuarios.setItems(estudiantes);
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        if (colContrasena != null) {
            colContrasena.setCellValueFactory(new PropertyValueFactory<>("contrasenia"));
        }
        if (colSolicitudesAyuda != null) {
            colSolicitudesAyuda.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getSolicitudesAyuda().size()).asObject());
        }
        if (colMensajes != null) {
            colMensajes.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getListMensajes().size()).asObject());
        }
        if (colAmigos != null) {
            colAmigos.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getAmigos().size()).asObject());
        }
        if (colContenidos != null) {
            colContenidos.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getContenidosPublicados().size()).asObject());
        }
        if (colValoraciones != null) {
            colValoraciones.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getValoracions().size()).asObject());
        }
        if (colGrupos != null) {
            colGrupos.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getGruposEstudio().size()).asObject());
        }
        if (colIntereses != null) {
            colIntereses.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getIntereses().size()).asObject());
        }
        this.moderador = moderador;
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}

