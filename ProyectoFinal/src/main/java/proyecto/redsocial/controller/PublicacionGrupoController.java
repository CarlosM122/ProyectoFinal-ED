package proyecto.redsocial.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import proyecto.redsocial.factory.ModelFactory;
import proyecto.redsocial.model.Estudiante;
import proyecto.redsocial.model.GrupoEstudio;
import proyecto.redsocial.model.Publicacion;

public class PublicacionGrupoController {

    private GrupoEstudioController grupoEstudioController;
    private Estudiante estudiante;
    private GrupoEstudio grupoEstudio;
    private ModelFactory modelFactory = ModelFactory.getInstance();
    private String rutaArchivoAdjunto;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> CBTemas;

    @FXML
    private HBox HBoxArchivo;

    @FXML
    private Button btnPublicar;

    @FXML
    private Label labelArchivo;

    @FXML
    private Label nombreUsuario;

    @FXML
    private TextArea txtAreaTexto;

    @FXML
    private Label txtNombreGrupo;

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
        CBTemas.getItems().addAll(List.of( "Revisión de temas vistos en clase",
                "Solucion de ejercicios y problemas",
                "Preparación para exámenes y parciales",
                "Explicación de conceptos difíciles",
                "Discusión de lecturas asignadas",
                "Comparación de apuntes entre los integrantes",
                "Elaboración de mapas conceptuales o resúmenes",
                "Resolución de guías o talleres"));
    }

    public void inicializarDatos(Estudiante estudiante, GrupoEstudioController grupoEstudioController, GrupoEstudio grupoEstudio) {
        this.grupoEstudio = grupoEstudio;
        this.grupoEstudioController = grupoEstudioController;
        this.estudiante = estudiante;
        nombreUsuario.setText(estudiante.getNombre());
        txtNombreGrupo.setText(grupoEstudio.getTema());
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

            grupoEstudio.getPublicaciones().add(publicacion);
            grupoEstudioController.cargarEnVistaPrincipal(publicacion, estudiante);
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

    private void mostrarMensaje(String titulo, String header, String contenido, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.show();
    }
}

