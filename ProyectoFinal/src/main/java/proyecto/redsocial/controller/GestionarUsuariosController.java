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
import proyecto.redsocial.factory.ModelFactory;
import proyecto.redsocial.model.Estudiante;
import proyecto.redsocial.model.Moderador;
import proyecto.redsocial.model.Sistema;
import proyecto.redsocial.utils.RedSocialUtils;

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

        // Cierra la ventana de gestión de usuarios
    @FXML
    void OnCerrarSesion(MouseEvent event) {
        Stage ventanaActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ventanaActual.close();
    }

    // Elimina el usuario seleccionado de la tabla y del sistema
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

    // Edita los datos del usuario seleccionado
    @FXML
    void OnEditarUsuario(ActionEvent event) {
        Estudiante seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            String nuevoNombre = txtNombreUsuario.getText().trim();
            String nuevoCorreo = txtCorreoUsuario.getText().trim();
            String nuevaContrasenia = txtContrasenaUsuario.getText().trim();

            // Validar nombre solo si se quiere cambiar
            if (!nuevoNombre.isEmpty() && nuevoNombre.length() <= 50) {
                seleccionado.setNombre(nuevoNombre);
            } else if (nuevoNombre.isEmpty()) {
                // No hacer nada, se mantiene el nombre anterior
            } else {
                mostrarAlerta("Nombre inválido. Debe tener menos de 50 caracteres.");
                return;
            }

            // Validar correo solo si se quiere cambiar
            if (!nuevoCorreo.isEmpty() && nuevoCorreo.length() <= 50) {
                // Si el correo fue cambiado, verificar que no exista otro estudiante con ese correo
                if (!nuevoCorreo.equals(seleccionado.getCorreo())) {
                    boolean correoExistente = false;
                    for (int i = 0; i < sistema.getListaEstudiantes().size(); i++) {
                        Estudiante e = sistema.getListaEstudiantes().get(i);
                        if (e.getCorreo().equalsIgnoreCase(nuevoCorreo)) {
                            correoExistente = true;
                            break;
                        }
                    }
                    if (correoExistente) {
                        mostrarAlerta("Ya existe un usuario con ese correo electrónico.");
                        return;
                    }
                    seleccionado.setCorreo(nuevoCorreo);
                }
            } else if (!nuevoCorreo.isEmpty()) {
                mostrarAlerta("Correo inválido. Debe tener menos de 50 caracteres.");
                return;
            }

            // Validar y actualizar contraseña solo si se quiere cambiar
            if (!nuevaContrasenia.isEmpty()) {
                if (nuevaContrasenia.length() > 50) {
                    mostrarAlerta("Contraseña inválida. Debe tener menos de 50 caracteres.");
                    return;
                }
                seleccionado.setContrasenia(RedSocialUtils.encriptarSHA256(nuevaContrasenia));
            }

            // Guardar el sistema para persistir los cambios
            ModelFactory.getInstance().guardarRecursosXML();

            tablaUsuarios.refresh();
            cargarEstudiantes(sistema, moderador);
            mostrarAlerta("Usuario editado correctamente.");
        } else {
            mostrarAlerta("Seleccione un usuario para editar.");
        }
    }

    // Inicializa la tabla de usuarios y su comportamiento
    @FXML
    void initialize() {
        if (sistema == null) {
            sistema = ModelFactory.getInstance().getSistema();
        }
        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtNombreUsuario.setText(newSelection.getNombre());
                txtCorreoUsuario.setText(newSelection.getCorreo());
                txtContrasenaUsuario.setText(""); // Siempre dejar vacío para forzar ingreso de nueva contraseña
            }
        });
    }

    /**
     * Carga los datos de la vista para el moderador, igual que al iniciar sesión.
     */
    public void cargarDatosVista(Moderador moderador) {
        this.moderador = moderador;
        if (sistema == null) {
            sistema = ModelFactory.getInstance().getSistema();
        }
        cargarEstudiantes(sistema, moderador);
        if (moderador != null) {
            txtNombre.setText(moderador.getNombre());
            txtInformacion.setText("Bienvenido, " + moderador.getNombre());
        }
    }

    // Carga los estudiantes en la tabla de usuarios
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

    // Muestra una alerta informativa
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}

