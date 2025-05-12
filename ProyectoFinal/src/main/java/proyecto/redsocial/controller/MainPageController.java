package proyecto.redsocial.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import proyecto.redsocial.RedSocialApplication;
import proyecto.redsocial.factory.ModelFactory;
import proyecto.redsocial.model.Publicacion;
import proyecto.redsocial.model.Estudiante;

public class MainPageController {

    private Estudiante estudiante;

    private final ModelFactory modelFactory = ModelFactory.getInstance();

    @FXML
    private Label LbPublicacion;

    @FXML
    private HBox HBoxArchivo;

    @FXML
    private VBox VBoxGrupos;

    @FXML
    private VBox VBoxAmigosSugeridos;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private VBox VboxInicio;

    @FXML
    private VBox contenedorPublicaciones;

    @FXML
    private Label txtInformacion;

    @FXML
    private Label txtNombre;

    @FXML
    void OnAyuda(MouseEvent event) {

    }

    @FXML
    void OnGrupos(MouseEvent event) {

    }

    @FXML
    void Onmensajes(MouseEvent event) {

    }

    @FXML
    void onAmigos(MouseEvent event) {

    }

    @FXML
    void onInicio(MouseEvent event) {

    }

    @FXML
    void onNotificaciones(MouseEvent event) {

    }

    @FXML
    void onPublicar(MouseEvent event) {
        abrirVentanaPublicacion();
    }

    @FXML
    void onSubirArchivo(MouseEvent event) {

    }

    @FXML
    void initialize() {

    }

    public void cargarDatosVista(Estudiante estudiante) {
        this.estudiante = estudiante;
        txtNombre.setText(estudiante.getNombre());
        txtInformacion.setText(estudiante.getCorreo());
        cargarPublicaciones(estudiante);
    }

    private void cargarPublicaciones(Estudiante estudiante) {
        List<Publicacion> publicaciones = modelFactory.obtenerPublicaciones();
        for (Publicacion publicacion : publicaciones) {
            cargarEnVistaPrincipal(publicacion,estudiante);
        }
    }

    private void abrirVentanaPublicacion() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(RedSocialApplication.class.getResource("/proyecto/redsocial/fxml/publicacion-view.fxml"));
            CargaVentana(fxmlLoader);
            PublicacionController publicacionController = fxmlLoader.getController();
            publicacionController.cargarDatos(estudiante,this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void CargaVentana(FXMLLoader fxmlLoader) throws IOException {
        Parent root = fxmlLoader.load();
        Stage nuevaVentana = new Stage();
        Scene scene = new Scene(root);
        nuevaVentana.setTitle("Publicación");
        nuevaVentana.setScene(scene);
        nuevaVentana.setResizable(false);
        nuevaVentana.show();
    }

    public void cargarEnVistaPrincipal(Publicacion publicacion, Estudiante usuarioActual) {
        VBox tarjeta = new VBox();
        tarjeta.setSpacing(8);
        tarjeta.setStyle("""
    -fx-background-color: #ffffff;
    -fx-padding: 12;
    -fx-background-radius: 12;
    -fx-border-color: #dddddd;
    -fx-border-radius: 12;
    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);
    """);

        Label tema = new Label(publicacion.getTema());
        tema.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2a2a2a;");

        Label contenido = new Label(publicacion.getTexto());
        contenido.setWrapText(true);
        contenido.setStyle("-fx-font-size: 13px; -fx-text-fill: #444444;");

        Label info = new Label("Publicado por " + publicacion.getAutor().getNombre() +
                " | 📅 " + publicacion.getFechaPublicacion());
        info.setStyle("-fx-font-size: 11px; -fx-text-fill: #888888;");

        if (!publicacion.getAutor().equals(usuarioActual)) {
            Button botonValorar = new Button("Valorar");
            botonValorar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 8px; -fx-border-radius: 8px; -fx-font-weight: bold;");

            botonValorar.setCursor(Cursor.HAND);

            botonValorar.setOnAction(event -> {
                System.out.println("Boton Valoracion");
            });

            tarjeta.getChildren().add(botonValorar);
        }

        tarjeta.getChildren().addAll(tema, contenido, info);

        contenedorPublicaciones.getChildren().addFirst(tarjeta);
    }


}
