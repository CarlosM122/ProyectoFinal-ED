package proyecto.redsocial.controller;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import proyecto.redsocial.factory.ModelFactory;
import proyecto.redsocial.model.Estudiante;
import proyecto.redsocial.model.Mensaje;
import proyecto.redsocial.utils.RedSocialUtils;

public class ChatEstudianteController {

    private ModelFactory modelFactory;

    private MainPageController mainPageController;

    private Estudiante estudiante1;

    private Estudiante estudiante2;

    @FXML
    private VBox boxAyuda;

    @FXML
    private VBox boxInicio;

    @FXML
    private Button btnEnviar;

    @FXML
    private VBox chatBox;

    @FXML
    private Label infoCompañero;

    @FXML
    private VBox listCompañeros;

    @FXML
    private Label nombreCompañero;

    @FXML
    private TextField txtBuscar;

    @FXML
    private VBox contenedorImagenPerfil;

    @FXML
    private HBox contenerdorImagenPerfilCompañero;

    @FXML
    private TextField txtMensaje;

    @FXML
    private Label txtinformacion;

    @FXML
    private Label txtnombre;

    @FXML
    void OnEnviar(ActionEvent event) {
        enviarMensaje();
    }

    @FXML
    void enviar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            enviarMensaje();
        }
    }

    @FXML
    void onAyuda(MouseEvent event) {
        mainPageController.OnAyuda(event);
    }

    @FXML
    void onInicio(MouseEvent event) {
        Stage stage = (Stage) btnEnviar.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onBuscar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            buscarAmigo();
        }
    }

    @FXML
    void initialize() {
        modelFactory = ModelFactory.getInstance();
    }

    public void cargarDatos(Estudiante estudiante, MainPageController mainPageController) {
        this.mainPageController = mainPageController;
        estudiante1 = estudiante;
        txtnombre.setText(estudiante1.getNombre());
        txtinformacion.setText(estudiante1.getInformacion());
        cargarFotoPerfilPrincipal(estudiante1.getRutaArchivoImagen());
        cargarListaAmigos();
    }

    ;

    private void cargarListaAmigos() {
        List<Estudiante> amigos = estudiante1.getAmigos().aLista();
        for (Estudiante estudiante : amigos) {
            cargarAmigo(estudiante);
        }
    }

    private void cargarAmigo(Estudiante estudiante) {
        HBox tarjeta = RedSocialUtils.crearTarjetaEstudiante(estudiante);

        tarjeta.setUserData(estudiante);
        tarjeta.setOnMouseClicked(event -> {
            contenerdorImagenPerfilCompañero.getChildren().clear();
            Estudiante estudianteCompañero = (Estudiante) tarjeta.getUserData();
            estudiante2 = estudianteCompañero;
            nombreCompañero.setText(estudiante2.getNombre());
            cargarFotoPerfilCompañero(estudianteCompañero.getRutaArchivoImagen());
            infoCompañero.setText(estudiante2.getInformacion());
            List<Mensaje> mensajes = modelFactory.obtenerMensajes(estudiante,estudianteCompañero);
            cargarMensajes(mensajes);
            txtMensaje.requestFocus();
            event.consume();
        });

        listCompañeros.getChildren().add(tarjeta);
    }

    private void cargarFotoPerfilCompañero(String nombreArchivo) {
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

                contenerdorImagenPerfilCompañero.getChildren().clear();
                contenerdorImagenPerfilCompañero.getChildren().add(circlePerfil);
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar la imagen de perfil: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarMensajes(List<Mensaje> mensajes) {
        chatBox.getChildren().clear();
        for (Mensaje mensaje : mensajes) {
            VBox tarjeta = crearTarjetaChat();
            Label nombre = crearLabelNombre(mensaje.getEmisor().getNombre());
            Node contenidoMensaje = crearMensaje(mensaje.getMensaje());
            tarjeta.getChildren().addAll(nombre, contenidoMensaje);
            chatBox.getChildren().add(tarjeta);
        }
    }

    private void cargarFotoPerfilPrincipal(String nombreArchivo) {
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

    private void enviarMensaje() {
        String mensaje = txtMensaje.getText().trim();
        if (!mensaje.isEmpty()) {
            Mensaje mensaje1 = new Mensaje();
            mensaje1.setEmisor(estudiante1);
            mensaje1.setReceptor(estudiante2);
            mensaje1.setMensaje(mensaje);
            estudiante1.getListMensajes().agregar(mensaje1);
            estudiante2.getListMensajes().agregar(mensaje1);
            VBox tarjeta = crearTarjetaChat();
            Label nombre = crearLabelNombre(estudiante1.getNombre());
            Node contenidoMensaje = crearMensaje(mensaje);
            tarjeta.getChildren().addAll(nombre, contenidoMensaje);
            chatBox.getChildren().add(tarjeta);
            txtMensaje.clear();
            modelFactory.guardarRecursosXML();
        }
    }

    private void buscarAmigo() {
        listCompañeros.getChildren().clear();
        if (txtBuscar.getText().trim().isEmpty()) {
            cargarListaAmigos();
        }
        String nombreCompañero = txtBuscar.getText().trim();
        for (Estudiante estudiante : estudiante1.getAmigos()) {
            if (estudiante.getNombre().equals(nombreCompañero)){
                cargarAmigo(estudiante);
            }
        }
    }

    private VBox crearTarjetaChat() {
        VBox tarjeta = new VBox(8);
        tarjeta.setStyle("""
                    -fx-background-color: #ffffff;
                    -fx-padding: 12;
                    -fx-background-radius: 12;
                    -fx-border-color: #dddddd;
                    -fx-border-radius: 12;
                    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);
                """);
        return tarjeta;
    }

    private Node crearMensaje(String texto) {
        Label contenido = new Label(texto);
        contenido.setWrapText(true);
        contenido.setStyle("-fx-font-size: 13px; -fx-text-fill: #444444;");
        return contenido;
    }

    private Label crearLabelNombre(String nombre) {
        Label tema = new Label(nombre);
        tema.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2a2a2a;");
        return tema;
    }
}