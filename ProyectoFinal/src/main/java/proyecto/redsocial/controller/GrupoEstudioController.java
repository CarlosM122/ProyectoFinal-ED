package proyecto.redsocial.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import proyecto.redsocial.RedSocialApplication;
import proyecto.redsocial.model.Estudiante;
import proyecto.redsocial.model.GrupoEstudio;
import proyecto.redsocial.model.Publicacion;
import proyecto.redsocial.utils.RedSocialUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GrupoEstudioController {

    private Estudiante estudiante;
    private GrupoEstudio grupoEstudio;
    private MainPageController mainPageController;
    private List<Estudiante> miembros = new ArrayList<>();
    private List<Publicacion> publicacions = new ArrayList<>();

    @FXML
    private Label LbPublicacion;

    @FXML
    private VBox VBoxAmigosSugeridos;

    @FXML
    private VBox VBoxGrupos;

    @FXML
    private VBox VboxInicio;

    @FXML
    private VBox contenedorPublicaciones;

    @FXML
    private Label txtInformacion;

    @FXML
    private Label txtNombre;

    @FXML
    private Label nombreGrupo;

    @FXML
    void OnAyuda(MouseEvent event) {

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
    void onPublicar(MouseEvent event) {
        cargarVentanaPublicacion();
    }

    private void cargarVentanaPublicacion() {
        FXMLLoader fxmlLoader = new FXMLLoader(RedSocialApplication.class.getResource("/proyecto/redsocial/fxml/publicacionGrupoEstudio-view.fxml"));
        try {
            RedSocialUtils.CargaVentana(fxmlLoader,"Publicar");
            PublicacionGrupoController controller = fxmlLoader.getController();
            controller.inicializarDatos(estudiante,this,grupoEstudio);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void inicializar(GrupoEstudio grupoSeleccionado, MainPageController mainPageController, Estudiante estudiante) {
        this.estudiante = estudiante;
        this.grupoEstudio = grupoSeleccionado;
        this.mainPageController = mainPageController;
        this.miembros = grupoSeleccionado.getMiembros();
        this.publicacions = grupoSeleccionado.getPublicaciones();
        txtNombre.setText(estudiante.getNombre());
        txtInformacion.setText(estudiante.getCorreo());
        nombreGrupo.setText(grupoSeleccionado.getTema());
        cargarGrupos(estudiante);
        cargarMiembros();
    }

    public void cargarGrupos(Estudiante estudiante) {
        VBoxGrupos.getChildren().clear();
        for (GrupoEstudio grupoEstudio : estudiante.getGruposEstudio()) {
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

    public Image obtenerImagenPorTema(String tema) {
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
        if (grupoSeleccionado.getTema().equals(grupoEstudio.getTema())) return;
        FXMLLoader fxmlLoader = new FXMLLoader(RedSocialApplication.class.getResource("/proyecto/redsocial/fxml/grupoEstudio-view.fxml"));
        try {
            RedSocialUtils.CargaVentana(fxmlLoader,"Grupos");
            GrupoEstudioController controller = fxmlLoader.getController();
            controller.inicializar(grupoSeleccionado,mainPageController,estudiante);
            cerrarVentana();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void cerrarVentana() {
        Stage stage = (Stage) VboxInicio.getScene().getWindow();
        stage.close();
    }

    private void cargarMiembros(){
        for(Estudiante estudiante: miembros){
            cargarMiembro(estudiante);
        }
    }

    private void cargarMiembro(Estudiante estudiante) {
        HBox tarjeta = crearTarjetaMiembrosLista(estudiante);
        VBoxAmigosSugeridos.getChildren().add(tarjeta);
    }

    private HBox crearTarjetaMiembrosLista(Estudiante estudiante) {
        HBox tarjeta = new HBox(10);
        tarjeta.setAlignment(Pos.CENTER_LEFT);
        tarjeta.setPadding(new Insets(8));
        tarjeta.setStyle("""
        -fx-background-color: #f4f4f8;
        -fx-background-radius: 10;
        -fx-cursor: hand;
    """);

        // Crear avatar circular (placeholder)
        Circle avatar = new Circle(20, Color.web("#6a8caf"));
        // Si tienes imagen, usar ImageView así:
        // ImageView avatar = new ImageView(new Image("ruta/al/avatar.png"));
        // avatar.setFitWidth(40);
        // avatar.setFitHeight(40);
        // avatar.setClip(new Circle(20, 20, 20));

        // Nombre
        Label nombre = new Label(estudiante.getNombre());
        nombre.setFont(Font.font("System", FontWeight.BOLD, 14));
        nombre.setTextFill(Color.web("#333333"));

        // Estado o info adicional (puedes cambiar texto)
        Label estado = new Label("Activo ahora");
        estado.setFont(Font.font("System", 12));
        estado.setTextFill(Color.web("#777777"));

        VBox textos = new VBox(4, nombre, estado);

        tarjeta.getChildren().addAll(avatar, textos);

        // Estilo hover para mejor experiencia
        tarjeta.setOnMouseEntered(e -> tarjeta.setStyle("""
        -fx-background-color: #e0e5f2;
        -fx-background-radius: 10;
        -fx-cursor: hand;
    """));
        tarjeta.setOnMouseExited(e -> tarjeta.setStyle("""
        -fx-background-color: #f4f4f8;
        -fx-background-radius: 10;
        -fx-cursor: hand;
    """));

        return tarjeta;
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

    private String obtenerExtensionArchivo(String ruta) {
        int lastIndex = ruta.lastIndexOf(".");
        if (lastIndex == -1) return "";
        return ruta.substring(lastIndex + 1).toLowerCase();
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
            grupoEstudio.getPublicaciones().remove(publicacion);
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
}

