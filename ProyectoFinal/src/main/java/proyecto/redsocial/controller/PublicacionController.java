package proyecto.redsocial.controller;

import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import proyecto.redsocial.factory.ModelFactory;
import proyecto.redsocial.model.Publicacion;
import proyecto.redsocial.model.Estudiante;

public class PublicacionController {

    private Estudiante estudiante;
    private final ModelFactory modelFactory = ModelFactory.getInstance();
    private MainPageController mainPageController;
    private String rutaArchivoAdjunto;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private HBox HBoxArchivo;

    @FXML
    private StackPane fotoPerfil;

    @FXML
    private Button btnPublicar;

    @FXML
    private ComboBox<String> CBTemas;

    @FXML
    private Label nombreUsuario;

    @FXML
    private Label labelArchivo;

    @FXML
    private TextArea txtAreaTexto;

    @FXML
    void onPublicar(ActionEvent event) {
        publicar();
    }

    @FXML
    void onSubirArchivo(MouseEvent event) {
        subirArchivo();
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

    public void cargarDatos(Estudiante estudiante, MainPageController mainPageController, StackPane contenedorImagenPerfilPublicacion) {
        this.mainPageController = mainPageController;
        this.estudiante = estudiante;
        nombreUsuario.setText(estudiante.getNombre());
        this.fotoPerfil.getChildren().add(contenedorImagenPerfilPublicacion);
    }

    private void subirArchivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir Archivo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Texto", "*.txt"),
                new FileChooser.ExtensionFilter("Imagenes", "*.jpg", "*.png", "*.gif"),
                new FileChooser.ExtensionFilter("Documentos", "*.doc", "*.docx", "*.pdf"),
                new FileChooser.ExtensionFilter("Videos", "*.mp4", "*.avi", "*.mov", "*.mkv")
        );
        fileChooser.setInitialDirectory(new java.io.File(System.getProperty("user.home")));
        java.io.File archivo = fileChooser.showOpenDialog(btnPublicar.getScene().getWindow());
        if (archivo != null) {
            try {
                java.io.File carpetaDestino = new java.io.File("archivos_publicaciones");
                if (!carpetaDestino.exists()) {
                    carpetaDestino.mkdir();
                }

                java.io.File archivoDestino = new java.io.File(carpetaDestino, archivo.getName());
                java.nio.file.Files.copy(
                        archivo.toPath(),
                        archivoDestino.toPath(),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );

                rutaArchivoAdjunto = archivoDestino.getAbsolutePath();
                labelArchivo.setText(archivoDestino.getName());
                mostrarMensaje("Archivo subido", null, "Archivo adjunto guardado correctamente.", Alert.AlertType.INFORMATION);

            } catch (Exception e) {
                e.printStackTrace();
                mostrarMensaje("Error", "No se pudo subir el archivo", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void publicar() {
        if(txtAreaTexto!=null && CBTemas.getValue()!=null && !Objects.equals(CBTemas.getValue(), "Selecciona un tema")) {
            String texto = txtAreaTexto.getText();
            String tema = CBTemas.getValue();

            Publicacion publicacion = new Publicacion();
            publicacion.setIdContenido(texto.hashCode());
            publicacion.setTema(tema);
            publicacion.setTexto(texto);
            publicacion.setAutor(estudiante);
            publicacion.setFechaPublicacion(LocalDate.now().toString());

            if (rutaArchivoAdjunto != null) {
                publicacion.setRutaArchivoAdjunto(rutaArchivoAdjunto);
            }

            estudiante.agregarInteres(tema);
            modelFactory.asignarAGrupoDeEstudio(estudiante, tema);
            modelFactory.guardarPublicacion(publicacion);
            mainPageController.cargarEnVistaPrincipal(publicacion, estudiante);
            mainPageController.cargarGrupos(estudiante);
            cerrarVentana();
            modelFactory.actualizarRedAfinidad(estudiante, tema);
            modelFactory.guardarRecursosXML();
        } else {
            mostrarMensaje("Error", "Datos Nulos", "Por favor rellena los campos necesarios.", Alert.AlertType.ERROR);
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
