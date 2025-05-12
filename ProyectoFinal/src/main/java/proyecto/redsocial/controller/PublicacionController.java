package proyecto.redsocial.controller;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import proyecto.redsocial.factory.ModelFactory;
import proyecto.redsocial.model.Publicacion;
import proyecto.redsocial.model.Estudiante;

public class PublicacionController {

    private Estudiante estudiante;
    private final ModelFactory modelFactory = ModelFactory.getInstance();
    private MainPageController mainPageController;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private HBox HBoxArchivo;

    @FXML
    private Button btnPublicar;

    @FXML
    private ComboBox<String> CBTemas;

    @FXML
    private Label nombreUsuario;

    @FXML
    private TextArea txtAreaTexto;

    @FXML
    void onPublicar(ActionEvent event) {
        publicar();
    }

    @FXML
    void onSubirArchivo(MouseEvent event) {

    }

    @FXML
    void initialize() {
        inicializarComboBox();
    }

    private void inicializarComboBox() {
        CBTemas.getItems().addAll(
                "Deporte",
                "Matemáticas",
                "Política",
                "Ciencia",
                "Tecnología",
                "Arte",
                "Música",
                "Historia",
                "Programación",
                "Literatura"
        );
        CBTemas.setValue("Selecciona un tema");
    }

    public void cargarDatos(Estudiante estudiante, MainPageController mainPageController) {
        this.mainPageController = mainPageController;
        this.estudiante = estudiante;
        nombreUsuario.setText(estudiante.getNombre());
    }

    private void publicar() {
        if(txtAreaTexto!=null&&CBTemas.getValue()!=null&& !Objects.equals(CBTemas.getValue(), "Selecciona un tema")){
            String texto = txtAreaTexto.getText();
            String tema = CBTemas.getValue();
            Publicacion publicacion = new Publicacion();
            publicacion.setIdContenido(texto.hashCode());
            publicacion.setTema(tema);
            publicacion.setTexto(texto);
            publicacion.setAutor(estudiante);
            publicacion.setFechaPublicacion(LocalDate.now().toString());
            modelFactory.guardarPublicacion(publicacion);
            mainPageController.cargarEnVistaPrincipal(publicacion,estudiante);
            cerrarVentana();
        }else {
            mostrarMensaje("Error","Datos Nulos","Porfavor rellena los campos necesarios.",Alert.AlertType.ERROR);
        }
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnPublicar.getScene().getWindow();
        stage.close();
    }

    private void mostrarMensaje(String titulo, String header, String contenido, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.show();
    }
}
