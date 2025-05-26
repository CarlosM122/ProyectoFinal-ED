package proyecto.redsocial.controller;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
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
import javafx.stage.Stage;
import proyecto.redsocial.RedSocialApplication;
import proyecto.redsocial.factory.ModelFactory;
import proyecto.redsocial.model.Estudiante;
import proyecto.redsocial.utils.RedSocialUtils;

public class PagePerfilController {
    private Estudiante estudiante;
    private ModelFactory modelFactory= ModelFactory.getInstance();
    private MainPageController mainPageController;
    private final File carpetaImagenesPerfil = new File("archivos_perfil");

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
        this.mainPageController= mainPageController;
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
            alerta.setTitle("Datos Actualizados Correctamente");
            alerta.setHeaderText(null);
            alerta.setContentText("La Informacion se actualizó correctamente.");
            alerta.showAndWait();
        }
        modelFactory.guardarRecursosXML();
        mainPageController.cargarDatosVista(estudiante);
        cerrarVentana();
    }

    private void cerrarVentana() {
         Stage stage = (Stage) btnGuardarCambios.getScene().getWindow();
         stage.close();
    }

    private void cargarFotoPerfilprincipal(String nombreArchivo) {
        try {
            if (nombreArchivo != null && !nombreArchivo.isBlank()) {
                File archivoImagen = new File("archivos_perfil", nombreArchivo);

                if (!archivoImagen.exists()) {
                    throw new IllegalArgumentException("No se encontró la imagen: " + archivoImagen.getAbsolutePath());
                }

                Image imagen = new Image(archivoImagen.toURI().toString());

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
            e.printStackTrace();
        }
    }



    private void cambiarFotoPerfil() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar nueva foto de perfil");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.png", "*.gif")
        );

        File archivo = fileChooser.showOpenDialog(btnCambiarFoto.getScene().getWindow());

        if (archivo != null) {
            try {
                if (!carpetaImagenesPerfil.exists()) {
                    carpetaImagenesPerfil.mkdirs();
                }

                File archivoDestino = new File(carpetaImagenesPerfil, archivo.getName());

                Files.copy(
                        archivo.toPath(),
                        archivoDestino.toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                );

                estudiante.setRutaArchivoImagen(archivoDestino.getName());

                cargarFotoPerfilprincipal(estudiante.getRutaArchivoImagen());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}