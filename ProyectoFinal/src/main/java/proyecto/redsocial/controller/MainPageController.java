package proyecto.redsocial.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import proyecto.redsocial.RedSocialApplication;
import proyecto.redsocial.factory.ModelFactory;
import proyecto.redsocial.model.EstructurasPropias.ListaEnlazada;
import proyecto.redsocial.model.Estudiante;
import proyecto.redsocial.model.GrupoEstudio;
import proyecto.redsocial.model.Publicacion;
import proyecto.redsocial.model.Valoracion;
import proyecto.redsocial.utils.RedSocialUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static proyecto.redsocial.utils.RedSocialUtils.mostrarMensaje;

public class MainPageController {

    private final ModelFactory modelFactory = ModelFactory.getInstance();
    private final List<String> temas = new ArrayList<>();
    private final List<String> temasNormalizados = new ArrayList<>();
    private Estudiante estudianteActual;
    private final File carpetaImagenesPerfil = new File("archivos_perfil");
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
        cargarVistaAyuda();
    }

    @FXML
    void Onmensajes(MouseEvent event) {
        abrirVentanaMensaje();
    }

    @FXML
    void onAmigos(MouseEvent event) {
        cargarVistaAmigosTest();
    }

    @FXML
    void buscarPorTema(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            buscarPublicacionesPorTema();
        }
    }

    @FXML
    void onPublicar(MouseEvent event) {
        abrirVentanaPublicacion();
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

    public void cargarDatosVista(Estudiante estudiante) {
        contenedorPublicaciones.getChildren().clear();
        VBoxGrupos.getChildren().clear();
        VBoxAmigosSugeridos.getChildren().clear();
        this.estudianteActual = estudiante;
        txtNombre.setText(estudiante.getNombre());
        txtInformacion.setText(estudiante.getInformacion());
        cargarFotoPerfilprincipal(estudiante.getRutaArchivoImagen());
        cargarPublicaciones();
        cargarGrupos(this.estudianteActual);
        cargarAmigosSugeridos();
    }

    private void buscarPublicacionesPorTema() {
        String textoBusqueda = txtBusqueda.getText();
        if (textoBusqueda == null || textoBusqueda.isBlank()) {
            contenedorPublicaciones.getChildren().clear();
            cargarPublicaciones();
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
                cargarEnVistaPrincipal(publicacion, estudianteActual);
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

    private void cargarAmigosSugeridos() {
        VBoxAmigosSugeridos.getChildren().clear();
        ListaEnlazada<Estudiante> amigosSugeridos = modelFactory.obtenerAmigosRecomendados(estudianteActual);
        for (Estudiante amigoSugerido : amigosSugeridos) {
            cargarEnAmigosSugueridos(amigoSugerido);
        }
    }

    private void cargarEnAmigosSugueridos(Estudiante amigoSugerido) {
        HBox tarjeta = RedSocialUtils.crearTarjetaEstudiante(amigoSugerido);
        VBoxAmigosSugeridos.getChildren().add(tarjeta);
    }

    private HBox crearTarjetaSugerido(Estudiante compañero) {
        HBox tarjeta = new HBox(10);
        tarjeta.setAlignment(Pos.CENTER_LEFT);
        tarjeta.setPadding(new Insets(10));
        tarjeta.setStyle("""
                    -fx-background-color: #eaf1fb;
                    -fx-background-radius: 10;
                    -fx-border-color: #cddbf0;
                    -fx-border-radius: 10;
                    -fx-cursor: hand;
                """);

        Circle avatar = new Circle(20, Color.web("#6a8caf"));

        Label nombre = new Label(compañero.getNombre());
        nombre.setFont(Font.font("System", FontWeight.BOLD, 14));
        nombre.setTextFill(Color.web("#2a2a2a"));

        Label sugerido = new Label("Sugerido");
        sugerido.setFont(Font.font("System", FontPosture.ITALIC, 12));
        sugerido.setTextFill(Color.web("#5075a8"));

        VBox textos = new VBox(4, nombre, sugerido);

        Region espacio = new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);

        tarjeta.setUserData(compañero);
        Button botonAgregar = new Button("Agregar");
        botonAgregar.setStyle("""
                    -fx-background-color: #5075a8;
                    -fx-text-fill: white;
                    -fx-background-radius: 5;
                    -fx-font-size: 12px;
                """);
        botonAgregar.setOnAction(e -> {
            Estudiante estudianteAgregar = (Estudiante) tarjeta.getUserData();

            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setTitle("Agregar Compañero");
            alerta.setHeaderText(null);
            alerta.setContentText("¿Desea agregar a " + estudianteAgregar.getNombre() + "?");

            Optional<ButtonType> resultado = alerta.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                agregarEstudiante(estudianteAgregar, estudianteActual);
                cargarAmigosSugeridos();
                modelFactory.guardarRecursosXML();
            }
        });

        tarjeta.getChildren().addAll(avatar, textos, espacio, botonAgregar);

        return tarjeta;
    }

    private void agregarEstudiante(Estudiante estudianteAgregar, Estudiante estudiante) {
        modelFactory.agregarAmigo(estudiante, estudianteAgregar);
    }

    public void cargarGrupos(Estudiante estudiante) {
        VBoxGrupos.getChildren().clear();
        for (GrupoEstudio grupoEstudio : estudiante.getGruposEstudio()) {
            cargarEnCampoGrupos(grupoEstudio);
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
                contenedorImagenPerfilPublicacion.getChildren().clear();
                contenedorImagenPerfilPublicacion.getChildren().add(circlePublicacion);

                contenedorImagenPerfil.setOnMouseClicked(e -> {
                    cargarVistaPerfil();
                });
                contenedorImagenPerfilPublicacion.setOnMouseClicked(e -> {
                    cargarVistaPerfil();
                });
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar la imagen de perfil: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarVistaPerfil() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/proyecto/redsocial/fxml/pagePerfil-viw.fxml"));
        try {
            RedSocialUtils.CargaVentana(fxmlLoader, "Edición De Perfil");
            PagePerfilController pagePerfilController = fxmlLoader.getController();
            pagePerfilController.inicializarDatos(estudianteActual, this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void cargarVistaAmigosTest() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/proyecto/redsocial/fxml/amigos-view.fxml"));
        try {
            RedSocialUtils.CargaVentana(fxmlLoader, "Listado De Amigos");
            AmigosController pagePerfilController = fxmlLoader.getController();
            pagePerfilController.inicializarDatos(estudianteActual, this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void cargarVistaAyuda() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/proyecto/redsocial/fxml/solicitudAyuda-view.fxml"));
        try {
            RedSocialUtils.CargaVentana(fxmlLoader,"Solicitar Ayuda");
            SolicitudAyudaController controller = fxmlLoader.getController();
            controller.cargarDatos(estudianteActual,this);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            RedSocialUtils.CargaVentana(fxmlLoader, "Grupos");
            GrupoEstudioController controller = fxmlLoader.getController();
            controller.inicializar(grupoSeleccionado, this, estudianteActual);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void cargarPublicaciones() {
        List<Publicacion> publicaciones = modelFactory.obtenerPublicaciones();
        for (Publicacion publicacion : publicaciones) {
            cargarEnVistaPrincipal(publicacion, this.estudianteActual);
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
            RedSocialUtils.CargaVentana(fxmlLoader, "Publicar");
            PublicacionController publicacionController = fxmlLoader.getController();
            publicacionController.cargarDatos(estudianteActual, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void abrirVentanaMensaje() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(RedSocialApplication.class.getResource("/proyecto/redsocial/fxml/chatEstudiante-view.fxml"));
            Parent root = fxmlLoader.load();
            ChatEstudianteController controller = fxmlLoader.getController();
            controller.cargarDatos(estudianteActual,this);
            Scene scene = new Scene(root);
            Stage nuevaVentana = new Stage();
            nuevaVentana.setTitle("Chat Estudiante");
            nuevaVentana.setScene(scene);
            nuevaVentana.show();
        } catch (IOException e) {
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
        } else {
            VBox botonValorar = crearValoracionInteractiva(publicacion);
            tarjeta.getChildren().add(botonValorar);
        }

        contenedorPublicaciones.getChildren().addFirst(tarjeta);
    }

    private VBox crearValoracionInteractiva(Publicacion publicacion) {
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
            nuevaValoracion.setEstudiante(estudianteActual);

            ListaEnlazada<Valoracion> listaValoraciones = publicacion.getValoraciones();
            boolean reemplazada = false;

            for (int i = 0; i < listaValoraciones.size(); i++) {
                Valoracion existente = listaValoraciones.get(i);
                if (existente.getEstudiante().equals(estudianteActual)) {
                    listaValoraciones.reemplazarEn(i, nuevaValoracion);
                    reemplazada = true;
                    break;
                }
            }

            if (!reemplazada) {
                listaValoraciones.agregar(nuevaValoracion);
            }

            estudianteActual.valorarContenido(valor, publicacion, comentario);
            comentarioArea.clear();
            mostrarMensaje("Valoracion","Valoracion Guardada","Su valoracion fue correctamente cargada", Alert.AlertType.INFORMATION);
            modelFactory.guardarRecursosXML();
        });

        contenedor.getChildren().addAll(label, botones, comentarioArea, enviarValoracion);
        return contenedor;
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
}

