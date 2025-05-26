package proyecto.redsocial.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import proyecto.redsocial.factory.ModelFactory;
import proyecto.redsocial.model.EstructurasPropias.ListaEnlazada;
import proyecto.redsocial.model.Estudiante;
import proyecto.redsocial.model.SolicitudAyuda;

import java.io.File;
import java.lang.annotation.Target;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SolicitudAyudaController {
    private final List<String> CBUrgencia = new ArrayList<>();
    private final ModelFactory modelFactory = ModelFactory.getInstance();
    private Estudiante estudiante;
    private MainPageController mainPageController;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> CBTemas;

    @FXML
    private VBox VBoxAmigosSugeridos;

    @FXML
    private VBox VBoxGrupos;

    @FXML
    private VBox VboxInicio;

    @FXML
    private Button btnGenerarSolicitud;

    @FXML
    private VBox contenedorSolicitudes;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private Label txtInformacion;

    @FXML
    private VBox contenedorImagenPerfil;

    @FXML
    private Label txtNombre;

    @FXML
    public void OnbtnGenerarSolicitud(ActionEvent event) {
        enviarSolicitud();
    }

    @FXML
    void onInicio(MouseEvent event) {
        cerrarVentana();
    }

    @FXML
    void initialize() {
        CBTemas.getItems().addAll(
                "Cambio de contraseña",
                "Problemas técnicos",
                "No puedo subir archivos",
                "No puedo iniciar sesión",
                "Error en publicaciones",
                "Solicitud de eliminación de cuenta",
                "Problemas con notificaciones",
                "Otros"
        );
        CBUrgencia.addAll(List.of("Baja", "Media", "Alta", "Crítica"));
    }

    public void cargarDatos(Estudiante estudiante, MainPageController mainPageController) {
        this.estudiante = estudiante;
        this.mainPageController = mainPageController;
        txtNombre.setText(estudiante.getNombre());
        txtInformacion.setText(estudiante.getInformacion());
        cargarSolicitudes();
        cargarFotoPerfil(estudiante.getRutaArchivoImagen());
    }

    private void cargarSolicitudes() {
        ListaEnlazada<SolicitudAyuda> listaSolicitudes = estudiante.getSolicitudesAyuda();
        for (SolicitudAyuda solicitudAyuda : listaSolicitudes){
            VBox tarjeta = crearTarjetaSolicitud(solicitudAyuda.getTema(),solicitudAyuda.getDescripcion(),solicitudAyuda.getUrgencia());
            contenedorSolicitudes.getChildren().add(tarjeta);
        }
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

    private void cerrarVentana() {
        Stage stage = (Stage) VboxInicio.getScene().getWindow();
        stage.close();
    }

    private void enviarSolicitud() {
        String tema = CBTemas.getValue();
        if (tema != null && txtDescripcion.getText() != null) {
            int urgencia = obtenerUrgencia(tema);
            SolicitudAyuda solicitudAyuda = new SolicitudAyuda();
            solicitudAyuda.setTema(tema);
            solicitudAyuda.setUrgencia(urgencia);
            solicitudAyuda.setEstudiante(estudiante);
            solicitudAyuda.setDescripcion(txtDescripcion.getText());
            estudiante.solicitarAyuda(solicitudAyuda);
            modelFactory.guardarSolicitud(solicitudAyuda);

            VBox tarjeta = crearTarjetaSolicitud(tema, txtDescripcion.getText(), urgencia);
            contenedorSolicitudes.getChildren().add(tarjeta);

            modelFactory.guardarRecursosXML();
        }
    }

    private VBox crearTarjetaSolicitud(String tema, String descripcion, int urgencia) {
        VBox tarjeta = new VBox(8);
        tarjeta.setStyle("""
            -fx-background-color: #f9f9f9;
            -fx-padding: 16;
            -fx-background-radius: 14;
            -fx-border-color: #d0d0d0;
            -fx-border-width: 1;
            -fx-border-radius: 14;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 6, 0.1, 0, 3);
        """);

        Label lblTema = new Label("Tema: " + tema);
        lblTema.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2a2a2a;");

        Label lblDescripcion = new Label("Descripción: " + descripcion);
        lblDescripcion.setWrapText(true);
        lblDescripcion.setStyle("-fx-font-size: 13px; -fx-text-fill: #444444;");

        Label lblUrgencia = new Label("Nivel de urgencia: " + urgencia);
        lblUrgencia.setStyle("-fx-font-size: 12px; -fx-text-fill: #ff4444;");

        tarjeta.getChildren().addAll(lblTema, lblDescripcion, lblUrgencia);
        return tarjeta;
    }

    private int obtenerUrgencia(String value) {
        return switch (value) {
            case "No puedo subir archivos", "Error en publicaciones" -> 2;
            case "Cambio de contraseña", "Problemas técnicos", "No puedo iniciar sesión", "Solicitud de eliminación de cuenta" -> 3;
            default -> 1;
        };
    }
}