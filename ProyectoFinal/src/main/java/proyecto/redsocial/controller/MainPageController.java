package proyecto.redsocial.controller;

import java.io.IOException;
import java.net.URL;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import proyecto.redsocial.RedSocialApplication;
import proyecto.redsocial.factory.ModelFactory;
import proyecto.redsocial.model.GrupoEstudio;
import proyecto.redsocial.model.Publicacion;
import proyecto.redsocial.model.Estudiante;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import proyecto.redsocial.utils.RedSocialUtils;

public class MainPageController {

    private Estudiante estudiante;

    private final ModelFactory modelFactory = ModelFactory.getInstance();
    private final List<String> temas = new ArrayList<>();
    private final List<String> temasNormalizados = new ArrayList<>();

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
    private TextField txtBusqueda;

    @FXML
    private ImageView imagenPerfil;

    @FXML
    private StackPane contenedorImagenPerfil;

    @FXML
    private StackPane contenedorImagenPerfilPublicacion;

    @FXML
    private ImageView imagenPerfilPublicacion;

    @FXML
    void OnAyuda(MouseEvent event) {

    }

    @FXML
    void OnGrupos(MouseEvent event) {

    }

    @FXML
    void Onmensajes(MouseEvent event) {
        abrirVentanaMensaje();
    }

    @FXML
    void onAmigos(MouseEvent event) {

    }

    @FXML
    void onInicio(MouseEvent event) {

    }


    @FXML
    void buscarPorTema(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER){
            buscarPublicacionesPorTema();
        }
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
        temas.addAll(List.of("Deporte",
                "Matemáticas",
                "Política",
                "Ciencia",
                "Tecnología",
                "Arte",
                "Música",
                "Historia",
                "Programación",
                "Literatura"));
        for (String tema : temas) {
            temasNormalizados.add(normalizarTexto(tema));
        }
    }

    private void buscarPublicacionesPorTema() {
        String textoBusqueda = txtBusqueda.getText();

        if (textoBusqueda == null || textoBusqueda.isBlank()) {
            contenedorPublicaciones.getChildren().clear();
            cargarPublicaciones(estudiante);
            return;
        }

        String temaNormalizado = normalizarTexto(textoBusqueda);

        int indiceTema = temasNormalizados.indexOf(temaNormalizado);

        if (indiceTema == -1) {
            mostrarMensaje("Error", "Tema no encontrado", "El tema que busca no existe.", Alert.AlertType.ERROR);
            return;
        }

        String temaOriginal = temas.get(indiceTema);

        List<Publicacion> listaDePublicaciones = modelFactory.obtenerPublicacionesPorTema(temaOriginal);

        if (listaDePublicaciones.isEmpty()) {
            mostrarMensaje("Problema", "Publicaciones No Encontradas.", "No se encuentran publicaciones relacionadas con el tema.", Alert.AlertType.INFORMATION);
        } else {
            contenedorPublicaciones.getChildren().clear();
            for (Publicacion publicacion : listaDePublicaciones) {
                cargarEnVistaPrincipal(publicacion, estudiante);
            }
        }
    }

    private String normalizarTexto(String texto) {
        if (texto == null) return null;
        // Elimina tildes y caracteres diacríticos
        String textoSinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return textoSinTildes.toLowerCase();
    }

    public void cargarDatosVista(Estudiante estudiante) {
        this.estudiante = estudiante;
        txtNombre.setText(estudiante.getNombre());
        txtInformacion.setText(estudiante.getCorreo());
        cargarFotoPerfil(estudiante.getRutaArchivoImagen());
        cargarPublicaciones(estudiante);
        cargarGrupos(this.estudiante);
    }

    private void cargarFotoPerfil(String rutaArchivoImagen) {
        try {
            if (rutaArchivoImagen != null && !rutaArchivoImagen.isBlank()) {
                Image imagen = new Image(getClass().getResource(rutaArchivoImagen).toExternalForm());

                double radioPerfil = 55;
                Circle circlePerfil = new Circle(radioPerfil);
                circlePerfil.setFill(new ImagePattern(imagen));
                circlePerfil.setStroke(Color.BLACK);
                circlePerfil.setStrokeWidth(2);

                double radioPublicacion = 45;
                Circle circlePublicacion = new Circle(radioPublicacion);
                circlePublicacion.setFill(new ImagePattern(imagen));
                circlePublicacion.setStroke(Color.BLACK);
                circlePublicacion.setStrokeWidth(2);

                contenedorImagenPerfil.getChildren().clear();
                contenedorImagenPerfil.getChildren().add(circlePerfil);

                contenedorImagenPerfilPublicacion.getChildren().clear();
                contenedorImagenPerfilPublicacion.getChildren().add(circlePublicacion);
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar la imagen de perfil: " + e.getMessage());
        }
    }

    public void cargarGrupos(Estudiante estudiante) {
        VBoxGrupos.getChildren().clear();
        for (GrupoEstudio grupoEstudio:estudiante.getGruposEstudio()){
            cargarEnCampoGrupos(grupoEstudio);
        }
    }

    private void cargarEnCampoGrupos(GrupoEstudio grupoEstudio) {
        HBox tarjeta = crearTarjetaGrupo(grupoEstudio);
        VBoxGrupos.getChildren().add(tarjeta);
    }

    private HBox crearTarjetaGrupo(GrupoEstudio grupoEstudio) {
        HBox tarjeta = new HBox(12);
        tarjeta.setPadding(new Insets(12));
        tarjeta.setAlignment(Pos.CENTER_LEFT);
        tarjeta.setStyle("""
        -fx-background-color: #ffffff;
        -fx-background-radius: 10;
        -fx-border-color: #dddddd;
        -fx-border-radius: 10;
        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 4, 0, 0, 1);
        -fx-cursor: hand;
    """);

        tarjeta.setUserData(grupoEstudio);

        ImageView imagen = new ImageView(obtenerImagenPorTema(grupoEstudio.getTema()));
        imagen.setFitWidth(50);
        imagen.setFitHeight(50);
        imagen.setPreserveRatio(true);

        Label titulo = new Label(grupoEstudio.getTema());
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2a2a2a;");

        VBox contenido = new VBox(4, titulo);

        tarjeta.getChildren().addAll(imagen, contenido);

        tarjeta.setOnMouseEntered(e -> tarjeta.setStyle(tarjeta.getStyle() + "-fx-background-color: #f0f4ff;"));
        tarjeta.setOnMouseExited(e -> tarjeta.setStyle(tarjeta.getStyle().replace("-fx-background-color: #f0f4ff;", "-fx-background-color: #ffffff;")));

        tarjeta.setOnMouseClicked(e -> {
            GrupoEstudio grupoSeleccionado = (GrupoEstudio) tarjeta.getUserData();
            ingresarAGrupo(grupoSeleccionado);
        });

        return tarjeta;
    }

    private Image obtenerImagenPorTema(String tema) {
        String ruta = "/proyecto/redsocial/imagenesGrupos/trabajo-en-equipo.png";

        switch (tema.toLowerCase()) {
            case "deporte":
                ruta = "/proyecto/redsocial/imagenesGrupos/aptitud-fisica.png";
                break;
            case "matemáticas":
                ruta = "/proyecto/redsocial/imagenesGrupos/matematicas.png";
                break;
            case "política":
                ruta = "/proyecto/redsocial/imagenesGrupos/politica.png";
                break;
            case "ciencia":
                ruta = "/proyecto/redsocial/imagenesGrupos/ciencias.png";
                break;
            case "tecnología":
                ruta = "/proyecto/redsocial/imagenesGrupos/nuevas-tecnologias.png";
                break;
            case "arte":
                ruta = "/proyecto/redsocial/imagenesGrupos/arte.png";
                break;
            case "música":
                ruta = "/proyecto/redsocial/imagenesGrupos/notas-musicales.png";
                break;
            case "historia":
                ruta = "/proyecto/redsocial/imagenesGrupos/historia.png";
                break;
            case "programación":
                ruta = "/proyecto/redsocial/imagenesGrupos/codigo.png";
                break;
            case "literatura":
                ruta = "/proyecto/redsocial/imagenesGrupos/literatura.png";
                break;
        }

        return new Image(Objects.requireNonNull(getClass().getResource(ruta)).toExternalForm());
    }

    private void ingresarAGrupo(GrupoEstudio grupoSeleccionado) {
        FXMLLoader fxmlLoader = new FXMLLoader(RedSocialApplication.class.getResource("/proyecto/redsocial/fxml/grupoEstudio-view.fxml"));
        try {
            RedSocialUtils.CargaVentana(fxmlLoader,"Grupos");
            GrupoEstudioController controller = fxmlLoader.getController();
            controller.inicializar(grupoSeleccionado,this,estudiante);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void cargarPublicaciones(Estudiante estudiante) {
        List<Publicacion> publicaciones = modelFactory.obtenerPublicaciones();
        for (Publicacion publicacion : publicaciones) {
            cargarEnVistaPrincipal(publicacion, this.estudiante);
        }
    }

    private String obtenerExtensionArchivo(String ruta) {
        int lastIndex = ruta.lastIndexOf(".");
        if (lastIndex == -1) return "";
        return ruta.substring(lastIndex + 1).toLowerCase();
    }

    private void abrirVentanaPublicacion() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(RedSocialApplication.class.getResource("/proyecto/redsocial/fxml/publicacion-view.fxml"));
            RedSocialUtils.CargaVentana(fxmlLoader,"Publicar");
            PublicacionController publicacionController = fxmlLoader.getController();
            publicacionController.cargarDatos(estudiante, this,contenedorImagenPerfilPublicacion);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void abrirVentanaMensaje() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(RedSocialApplication.class.getResource("/proyecto/redsocial/fxml/chatEstudiante-view.fxml"));
            Parent root = fxmlLoader.load();
            ChatEstudianteController controller = fxmlLoader.getController();
            controller.initData(estudiante);
            Scene scene = new Scene(root);
            Stage nuevaVentana = new Stage();
            nuevaVentana.setTitle("Chat Estudiante");
            nuevaVentana.setScene(scene);
            nuevaVentana.show();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void cargarEnVistaPrincipal(Publicacion publicacion, Estudiante usuarioActual) {
        VBox tarjeta = crearTarjetaPublicacion();

        Label tema = crearLabelTema(publicacion.getTema());
        Node contenido = crearContenido(publicacion.getTexto());
        Label info = crearLabelInfo(publicacion);

        tarjeta.getChildren().addAll(tema, contenido);

        if (tieneArchivoAdjunto(publicacion)) {
            String ruta = publicacion.getRutaArchivoAdjunto();
            String extension = obtenerExtensionArchivo(ruta);

            if (extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg")) {
                try {
                    Image imagen = new Image("file:" + ruta);
                    ImageView imageView = new ImageView(imagen);
                    imageView.setFitWidth(200);
                    imageView.setPreserveRatio(true);
                    tarjeta.getChildren().add(imageView);
                } catch (Exception e) {
                    e.printStackTrace();
                    Button botonAbrirArchivo = crearBotonAbrirArchivo(ruta);
                    tarjeta.getChildren().add(botonAbrirArchivo);
                }
            }
            Button botonAbrirArchivo = crearBotonAbrirArchivo(ruta);
            tarjeta.getChildren().add(botonAbrirArchivo);
        }

        tarjeta.getChildren().add(info);

        if (usuarioActual.equals(publicacion.getAutor())) {
            Button botonEliminar = crearBotonEliminar(publicacion);
            tarjeta.getChildren().add(botonEliminar);
        }else {
            Button botonValorar = crearBotonValorar();
            tarjeta.getChildren().add(botonValorar);
        }

        contenedorPublicaciones.getChildren().addFirst(tarjeta);
    }

    private VBox crearTarjetaPublicacion() {
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

    private Label crearLabelTema(String temaTexto) {
        Label tema = new Label(temaTexto);
        tema.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2a2a2a;");
        return tema;
    }

    private Label crearLabelInfo(Publicacion publicacion) {
        String texto = "Publicado por " + publicacion.getAutor().getNombre() +
                " | 📅 " + publicacion.getFechaPublicacion();
        Label info = new Label(texto);
        info.setStyle("-fx-font-size: 11px; -fx-text-fill: #888888;");
        return info;
    }

    private boolean tieneArchivoAdjunto(Publicacion publicacion) {
        String ruta = publicacion.getRutaArchivoAdjunto();
        return ruta != null && !ruta.isEmpty();
    }

    private Button crearBotonAbrirArchivo(String rutaArchivo) {
        Button boton = new Button("Abrir archivo");
        boton.setStyle("-fx-background-color: linear-gradient(to right, #f9d423, #ff4e50);\n" +
                "    -fx-background-radius: 90;\n" +
                "    -fx-padding: 6 16 6 16;\n" +
                "    -fx-text-fill: #333333;\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-font-size: 13px;");
        boton.setCursor(Cursor.HAND);
        boton.setOnAction(event -> {
            try {
                java.awt.Desktop.getDesktop().open(new java.io.File(rutaArchivo));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return boton;
    }

    private Button crearBotonValorar() {
        Button boton = new Button("Valorar");
        boton.setStyle("-fx-background-color: linear-gradient(to right, #f9d423, #ff4e50);\n" +
                "    -fx-background-radius: 90;\n" +
                "    -fx-padding: 6 16 6 16;\n" +
                "    -fx-text-fill: #333333;\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-font-size: 13px;");
        boton.setCursor(Cursor.HAND);
        boton.setOnAction(event -> {
            System.out.println("Boton Valoracion");
        });
        return boton;
    }

    private Button crearBotonEliminar(Publicacion publicacion) {
        Button boton = new Button("Eliminar");
        boton.setStyle("-fx-background-color: linear-gradient(to right, #f9d423, #ff4e50);\n" +
                "    -fx-background-radius: 90;\n" +
                "    -fx-padding: 6 16 6 16;\n" +
                "    -fx-text-fill: #333333;\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-font-size: 13px;");
        boton.setCursor(Cursor.HAND);
        boton.setOnAction(event -> {
            modelFactory.eliminarPublicacion(publicacion);
            contenedorPublicaciones.getChildren().removeIf(child -> child instanceof VBox && ((VBox) child).getChildren().contains(boton));
        });
        return boton;
    }

    private Node crearContenido(String texto) {
        VBox contenedor = new VBox(6);

        // Regex para encontrar enlaces
        Pattern pattern = Pattern.compile("(https?://\\S+)");
        Matcher matcher = pattern.matcher(texto);

        boolean hayEnlace = false;
        int lastEnd = 0;

        while (matcher.find()) {
            hayEnlace = true;

            // Agrega texto antes del enlace
            if (matcher.start() > lastEnd) {
                String textoAntes = texto.substring(lastEnd, matcher.start()).trim();
                if (!textoAntes.isEmpty()) {
                    Label comentario = new Label(textoAntes);
                    comentario.setWrapText(true);
                    comentario.setStyle("-fx-font-size: 13px; -fx-text-fill: #444444;");
                    contenedor.getChildren().add(comentario);
                }
            }

            String url = matcher.group();
            Hyperlink link = new Hyperlink(url);
            link.setStyle("-fx-font-size: 13px;");
            link.setOnAction(e -> {
                try {
                    java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            contenedor.getChildren().add(link);

            lastEnd = matcher.end();
        }

        if (lastEnd < texto.length()) {
            String textoFinal = texto.substring(lastEnd).trim();
            if (!textoFinal.isEmpty()) {
                Label comentario = new Label(textoFinal);
                comentario.setWrapText(true);
                comentario.setStyle("-fx-font-size: 13px; -fx-text-fill: #444444;");
                contenedor.getChildren().add(comentario);
            }
        }

        if (!hayEnlace) {
            Label contenido = new Label(texto);
            contenido.setWrapText(true);
            contenido.setStyle("-fx-font-size: 13px; -fx-text-fill: #444444;");
            return contenido;
        }

        return contenedor;
    }

    private void mostrarMensaje(String titulo, String header, String contenido, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.show();
    }
}
