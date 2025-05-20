package proyecto.redsocial.controller;

import java.io.IOException;
import java.net.URL;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    void OnAyuda(MouseEvent event) {

    }

    @FXML
    void OnGrupos(MouseEvent event) {

    }

    @FXML
    void Onmensajes(MouseEvent event) {
        cargarVistaMensajes();
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

    private void cargarVistaMensajes() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(RedSocialApplication.class.getResource("/proyecto/redsocial/css/chatEstudiante-view.fxml"));
            CargaVentana(fxmlLoader,"Chat");
            ChatEstudianteController chatEstudianteController = fxmlLoader.getController();
            chatEstudianteController.cargarDatos(estudiante);
        } catch (IOException e) {
            e.printStackTrace();
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
        cargarPublicaciones(estudiante);
    }

    private void cargarPublicaciones(Estudiante estudiante) {
        List<Publicacion> publicaciones = modelFactory.obtenerPublicaciones();
        for (Publicacion publicacion : publicaciones) {
            cargarEnVistaPrincipal(publicacion, estudiante);
        }
    }

    private void abrirVentanaPublicacion() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(RedSocialApplication.class.getResource("/proyecto/redsocial/fxml/publicacion-view.fxml"));
            CargaVentana(fxmlLoader,"Publicaciones");
            PublicacionController publicacionController = fxmlLoader.getController();
            publicacionController.cargarDatos(estudiante,this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void CargaVentana(FXMLLoader fxmlLoader,String titulo) throws IOException {
        Parent root = fxmlLoader.load();
        Stage nuevaVentana = new Stage();
        Scene scene = new Scene(root);
        nuevaVentana.setTitle(titulo);
        nuevaVentana.setScene(scene);
        nuevaVentana.setResizable(false);
        nuevaVentana.show();
    }

    public void cargarEnVistaPrincipal(Publicacion publicacion, Estudiante usuarioActual) {
        VBox tarjeta = crearTarjetaPublicacion();

        Label tema = crearLabelTema(publicacion.getTema());
        Node contenido = crearContenido(publicacion.getTexto());
        Label info = crearLabelInfo(publicacion);

        tarjeta.getChildren().addAll(tema, contenido, info);

        if (tieneArchivoAdjunto(publicacion)) {
            Button botonAbrirArchivo = crearBotonAbrirArchivo(publicacion.getRutaArchivoAdjunto());
            tarjeta.getChildren().add(botonAbrirArchivo);
        }

        if (!publicacion.getAutor().equals(usuarioActual)) {
            Button botonValorar = crearBotonValorar();
            tarjeta.getChildren().add(botonValorar);
        } else {
            Button botonEliminar = crearBotonEliminar(publicacion);
            tarjeta.getChildren().add(botonEliminar);
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

            // Crea el enlace clickeable
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

        // Agrega texto restante después del último enlace
        if (lastEnd < texto.length()) {
            String textoFinal = texto.substring(lastEnd).trim();
            if (!textoFinal.isEmpty()) {
                Label comentario = new Label(textoFinal);
                comentario.setWrapText(true);
                comentario.setStyle("-fx-font-size: 13px; -fx-text-fill: #444444;");
                contenedor.getChildren().add(comentario);
            }
        }

        // Si no hay enlaces, retorna solo el texto completo
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
