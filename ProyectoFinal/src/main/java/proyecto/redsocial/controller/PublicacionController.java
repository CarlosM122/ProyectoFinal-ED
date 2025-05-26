package proyecto.redsocial.controller;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import proyecto.redsocial.factory.ModelFactory;
import proyecto.redsocial.model.Publicacion;
import proyecto.redsocial.model.Estudiante;

import static proyecto.redsocial.utils.RedSocialUtils.mostrarMensaje;

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
    private VBox contenedorImagenPerfil;

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

    public void cargarDatos(Estudiante estudiante, MainPageController mainPageController) {
        this.mainPageController = mainPageController;
        this.estudiante = estudiante;
        nombreUsuario.setText(estudiante.getNombre());
        cargarFotoPerfil(estudiante.getRutaArchivoImagen());
    }

    private void cargarFotoPerfil(String nombreArchivo) {
        try {
            if (nombreArchivo != null && !nombreArchivo.isBlank()) {
                File archivoImagen = new File("archivos_perfil", nombreArchivo);

                if (!archivoImagen.exists()) {
                    throw new IllegalArgumentException("No se encontró la imagen: " + archivoImagen.getAbsolutePath());
                }

                Image imagen = new Image(archivoImagen.toURI().toString());

                double radioPerfil = 45;
                Circle circlePerfil = new Circle(radioPerfil);
                circlePerfil.setFill(new ImagePattern(imagen));
                circlePerfil.setStroke(Color.BLACK);
                circlePerfil.setStrokeWidth(2);

                contenedorImagenPerfil.getChildren().clear();
                contenedorImagenPerfil.getChildren().add(circlePerfil);
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar la imagen de perfil: " + e.getMessage());
            e.printStackTrace();
        }
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
        if (txtAreaTexto != null && CBTemas.getValue() != null && !Objects.equals(CBTemas.getValue(), "Selecciona un tema")) {
            String texto = txtAreaTexto.getText();
            String tema = CBTemas.getValue();

            Publicacion publicacion = new Publicacion();
            publicacion.setIdContenido(texto.hashCode());
            publicacion.setTema(tema);
            publicacion.setTexto(texto);
            publicacion.setAutor(estudiante);
            estudiante.getContenidosPublicados().agregar(publicacion);
            publicacion.setFechaPublicacion(LocalDate.now().toString());

            if (rutaArchivoAdjunto != null) {
                publicacion.setRutaArchivoAdjunto(rutaArchivoAdjunto);
            }

            estudiante.agregarInteres(tema);
            modelFactory.asignarAGrupoDeEstudio(estudiante, tema);
            modelFactory.guardarPublicacion(publicacion);
            mainPageController.cargarDatosVista(estudiante);
            cerrarVentana();
            modelFactory.guardarRecursosXML();
        } else {
            mostrarMensaje("Error", "Datos Nulos", "Por favor rellena los campos necesarios.", Alert.AlertType.ERROR);
        }
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnPublicar.getScene().getWindow();
        stage.close();
    }
}
