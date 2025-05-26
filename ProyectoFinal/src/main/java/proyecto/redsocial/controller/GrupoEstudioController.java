package proyecto.redsocial.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import proyecto.redsocial.RedSocialApplication;
import proyecto.redsocial.model.EstructurasPropias.ListaEnlazada;
import proyecto.redsocial.model.Estudiante;
import proyecto.redsocial.model.GrupoEstudio;
import proyecto.redsocial.model.Publicacion;
import proyecto.redsocial.model.Valoracion;
import proyecto.redsocial.utils.RedSocialUtils;

import java.io.File;
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
    private List<Publicacion> publicaciones = new ArrayList<>();

    @FXML
    private Label LbPublicacion;

    @FXML
    private VBox VBoxAmigosSugeridos;

    @FXML
    private VBox VBoxGrupos;

    @FXML
    private VBox VboxInicio;

    @FXML
    private VBox contenedorImagenPerfil;

    @FXML
    private VBox contenedorImagenPublicacion;

    @FXML
    private VBox contenedorPublicaciones;

    @FXML
    private Label txtInformacion;

    @FXML
    private VBox VboxInfoGrupo;

    @FXML
    private Label txtNombre;

    @FXML
    void OnAyuda(MouseEvent event) {
        mainPageController.OnAyuda(event);
    }

    @FXML
    void Onmensajes(MouseEvent event) {
        mainPageController.Onmensajes(event);
    }

    @FXML
    void onAmigos(MouseEvent event) {
        mainPageController.onAmigos(event);
    }

    @FXML
    void onInicio(MouseEvent event) {
        Stage stage = (Stage) VboxInicio.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onPublicar(MouseEvent event) {
        cargarVentanaPublicacion();
    }

    private void cargarVentanaPublicacion() {
        FXMLLoader fxmlLoader = new FXMLLoader(RedSocialApplication.class.getResource("/proyecto/redsocial/fxml/publicacionGrupoEstudio-view.fxml"));
        try {
            RedSocialUtils.CargaVentana(fxmlLoader, "Publicar");
            PublicacionGrupoController controller = fxmlLoader.getController();
            controller.inicializarDatos(estudiante, this, grupoEstudio);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void inicializar(GrupoEstudio grupoSeleccionado, MainPageController mainPageController, Estudiante estudiante) {
        this.estudiante = estudiante;
        this.grupoEstudio = grupoSeleccionado;
        this.mainPageController = mainPageController;
        this.miembros = grupoSeleccionado.getMiembros().aLista();
        this.publicaciones = grupoSeleccionado.getPublicaciones().aLista();
        txtNombre.setText(estudiante.getNombre());
        txtInformacion.setText(estudiante.getCorreo());
        VboxInfoGrupo.getChildren().clear();
        HBox tarjeta = crearTarjetaGrupoInfo(grupoEstudio);
        VboxInfoGrupo.getChildren().add(tarjeta);
        cargarFotoPerfilprincipal(estudiante.getRutaArchivoImagen());
        cargarGrupos(estudiante);
        cargarPublicaionesGrupo();
        cargarMiembros();
    }

    private void cargarPublicaionesGrupo() {
        contenedorPublicaciones.getChildren().clear();
        for (Publicacion publicacion : grupoEstudio.getPublicaciones()) {
            cargarEnVistaPrincipal(publicacion, estudiante);
        }
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

                double radioPerfilpublicacion = 45;
                Circle circlePublicacion = new Circle(radioPerfilpublicacion);
                circlePublicacion.setFill(new ImagePattern(imagen));
                circlePublicacion.setStroke(Color.BLACK);
                circlePublicacion.setStrokeWidth(2);

                contenedorImagenPerfil.getChildren().clear();
                contenedorImagenPerfil.getChildren().add(circlePerfil);
                contenedorImagenPublicacion.getChildren().clear();
                contenedorImagenPublicacion.getChildren().add(circlePublicacion);
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar la imagen de perfil: " + e.getMessage());
            e.printStackTrace();
        }
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

    private HBox crearTarjetaGrupoInfo(GrupoEstudio grupoEstudio) {
        HBox tarjeta = new HBox(20);
        tarjeta.setPadding(new Insets(12));
        tarjeta.setAlignment(Pos.CENTER);
        tarjeta.setStyle("""
                -fx-background-color: #efefef;
                -fx-background-radius: 10;
                -fx-border-radius: 10;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 4, 0, 0, 1);
                """);

        ImageView imagen = new ImageView(obtenerImagenPorTema(grupoEstudio.getTema()));
        imagen.setFitWidth(50);
        imagen.setFitHeight(50);
        imagen.setPreserveRatio(true);

        Label titulo = new Label(grupoEstudio.getTema());
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2a2a2a;");

        VBox contenido = new VBox(4, titulo);

        tarjeta.getChildren().addAll(imagen, contenido);
        return tarjeta;
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
            RedSocialUtils.CargaVentana(fxmlLoader, "Grupos");
            GrupoEstudioController controller = fxmlLoader.getController();
            controller.inicializar(grupoSeleccionado, mainPageController, estudiante);
            cerrarVentana();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void cerrarVentana() {
        Stage stage = (Stage) VboxInicio.getScene().getWindow();
        stage.close();
    }

    private void cargarMiembros() {
        for (Estudiante estudiante : miembros) {
            cargarMiembro(estudiante);
        }
    }

    private void cargarMiembro(Estudiante estudiante) {
        HBox tarjeta = RedSocialUtils.crearTarjetaEstudiante(estudiante);
        VBoxAmigosSugeridos.getChildren().add(tarjeta);
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
        } else {
            VBox botonValorar = crearValoracionInteractiva(publicacion,estudiante);
            tarjeta.getChildren().add(botonValorar);
        }

        contenedorPublicaciones.getChildren().addFirst(tarjeta);
    }

    private VBox crearValoracionInteractiva(Publicacion publicacion, Estudiante usuarioActual) {
        VBox contenedor = new VBox(8);
        contenedor.setAlignment(Pos.CENTER_LEFT);

        Label label = new Label("Valorar publicación:");
        label.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

        HBox botones = new HBox(10);
        botones.setAlignment(Pos.CENTER_LEFT);

        ToggleGroup grupoValoracion = new ToggleGroup();
        List<ToggleButton> botonesLista = new ArrayList<>();

        String estiloNormal =
                "-fx-background-color: linear-gradient(to right, #f9d423, #ff4e50);" +
                        "-fx-background-radius: 90;" +
                        "-fx-padding: 6 16 6 16;" +
                        "-fx-text-fill: #333333;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 13px;";

        String estiloSeleccionado = estiloNormal +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 6, 0.0, 0, 1);" +
                "-fx-border-color: #ff4e50;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 90;";

        for (int i = 1; i <= 3; i++) {
            final int valor = i;
            ToggleButton boton = new ToggleButton("★".repeat(i));
            boton.setToggleGroup(grupoValoracion);
            boton.setUserData(valor);
            boton.setCursor(Cursor.HAND);
            boton.setStyle(estiloNormal);

            boton.setOnAction(e -> {
                for (ToggleButton b : botonesLista) {
                    b.setStyle(estiloNormal);
                }
                boton.setStyle(estiloSeleccionado);
            });

            botonesLista.add(boton);
            botones.getChildren().add(boton);
        }

        TextArea comentarioArea = new TextArea();
        comentarioArea.setPromptText("Deja un comentario (opcional)");
        comentarioArea.setPrefRowCount(2);
        comentarioArea.setWrapText(true);
        comentarioArea.setStyle("-fx-font-size: 12px;");

        Button enviarValoracion = new Button("Enviar valoración");
        enviarValoracion.setStyle(estiloNormal);
        enviarValoracion.setCursor(Cursor.HAND);

        enviarValoracion.setOnAction(e -> {
            Toggle selectedToggle = grupoValoracion.getSelectedToggle();
            if (selectedToggle == null) {
                System.out.println("Debe seleccionar una valoración.");
                return;
            }

            int valor = (int) selectedToggle.getUserData();
            String comentario = comentarioArea.getText().trim();

            Valoracion nuevaValoracion = new Valoracion();
            nuevaValoracion.setValoracion(valor);
            nuevaValoracion.setComentario(comentario);
            nuevaValoracion.setPublicacion(publicacion);
            nuevaValoracion.setEstudiante(estudiante);

            ListaEnlazada<Valoracion> listaValoraciones = publicacion.getValoraciones();
            boolean reemplazada = false;

            for (int i = 0; i < listaValoraciones.size(); i++) {
                Valoracion existente = listaValoraciones.get(i);
                if (existente.getEstudiante().equals(estudiante)) {
                    listaValoraciones.reemplazarEn(i, nuevaValoracion);
                    reemplazada = true;
                    break;
                }
            }

            if (!reemplazada) {
                listaValoraciones.agregar(nuevaValoracion);
            }

            estudiante.valorarContenido(valor, publicacion, comentario);
            comentarioArea.clear();
        });

        contenedor.getChildren().addAll(label, botones, comentarioArea, enviarValoracion);
        return contenedor;
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
        RedSocialUtils.aplicarEstiloBotonGradiente(boton);
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

    private Button crearBotonEliminar(Publicacion publicacion) {
        Button boton = new Button("Eliminar");
        RedSocialUtils.aplicarEstiloBotonGradiente(boton);
        boton.setCursor(Cursor.HAND);
        boton.setOnAction(event -> {
            grupoEstudio.getPublicaciones().eliminar(publicacion);
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