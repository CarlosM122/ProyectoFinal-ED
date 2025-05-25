package proyecto.redsocial.controller;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import proyecto.redsocial.RedSocialApplication;
import proyecto.redsocial.model.Estudiante;

public class PagePerfilController {
    private Estudiante estudiante;


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private VBox ContenedorFotoPErfil;

    @FXML
    private ImageView ImagenViewFotoPerfil;

    @FXML
    private Button btnCambiarFoto;

    @FXML
    private Button btnGuardarCambios;

    @FXML
    private TextField txtCambiarNombre;

    @FXML
    private Label txtInformacion;

    @FXML
    private TextField txtnombreEditable;

    @FXML
    private Label txtnombreUsuario;

    @FXML
    void onCambiarFoto(ActionEvent event) {
        cambiarFotoPerfil();
    }

    @FXML
    void onGuardarCambios(ActionEvent event) {
        guardarCambios();
    }

    @FXML
    void initialize() {

    }

    public void inicializarDatos(Estudiante estudiante, MainPageController mainPageController) {
        this.estudiante = estudiante;
        txtnombreUsuario.setText(estudiante.getNombre());
        txtInformacion.setText(estudiante.getInformacion());
        cargarFotoPerfilprincipal(estudiante.getRutaArchivoImagen());
    }

    private void guardarCambios() {
        String nuevoNombre = txtCambiarNombre.getText();

        if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
            estudiante.setNombre(nuevoNombre);
            txtnombreUsuario.setText(nuevoNombre);

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Nombre actualizado");
            alerta.setHeaderText(null);
            alerta.setContentText("El nombre se actualizó correctamente.");
            alerta.showAndWait();
        } else {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Nombre inválido");
            alerta.setHeaderText(null);
            alerta.setContentText("Por favor, ingresa un nombre válido.");
            alerta.showAndWait();
        }
    }

    private void cargarFotoPerfilprincipal(String rutaArchivoImagen) {
        try {
            if (rutaArchivoImagen != null && !rutaArchivoImagen.isBlank()) {
                Image imagen = new Image(Objects.requireNonNull(getClass().getResource(rutaArchivoImagen)).toExternalForm());

                double radioPerfil = 70;
                Circle circlePerfil = new Circle(radioPerfil);
                circlePerfil.setFill(new ImagePattern(imagen));
                circlePerfil.setStroke(Color.BLACK);
                circlePerfil.setStrokeWidth(2);

                ContenedorFotoPErfil.getChildren().clear();
                ContenedorFotoPErfil.getChildren().add(circlePerfil);
            }

        } catch (Exception e) {
            System.out.println("No se pudo cargar la imagen de perfil: " + e.getMessage());
        }
    }

    private void cambiarFotoPerfil() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir Archivo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imagenes", "*.jpg", "*.png", "*.gif")
        );

        fileChooser.setInitialDirectory(new java.io.File(System.getProperty("user.home")));
        java.io.File archivo = fileChooser.showOpenDialog(btnCambiarFoto.getScene().getWindow());
        if (archivo != null) {
            try {
                java.io.File carpetaDestino = new java.io.File(String.valueOf(PagePerfilController.class.getResource("/proyecto/redsocial/imagenesFotoPerfil")));
                if (!carpetaDestino.exists()) {
                    carpetaDestino.mkdir();
                }

                java.io.File archivoDestino = new java.io.File(carpetaDestino, archivo.getName());
                java.nio.file.Files.copy(
                        archivo.toPath(),
                        archivoDestino.toPath(),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );

                estudiante.setRutaArchivoImagen(archivoDestino.getAbsolutePath());
                cargarFotoPerfilprincipal(estudiante.getRutaArchivoImagen());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}



