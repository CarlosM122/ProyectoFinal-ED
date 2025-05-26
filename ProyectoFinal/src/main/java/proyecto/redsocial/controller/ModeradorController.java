package proyecto.redsocial.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import proyecto.redsocial.factory.ModelFactory;
import proyecto.redsocial.model.Estudiante;
import proyecto.redsocial.model.Moderador;
import proyecto.redsocial.model.Publicacion;
import proyecto.redsocial.model.Sistema;
import proyecto.redsocial.model.Valoracion;
import proyecto.redsocial.utils.RedSocialUtils;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModeradorController {

    private Estudiante estudiante;
    private ModelFactory modelFactory = ModelFactory.getInstance();
    private Moderador moderador;
    private Sistema sistema;

    public void setSistema(Sistema sistema) {
        this.sistema = sistema;
    }

    @FXML
    private VBox CerrarSecion;

    @FXML
    private VBox GestionarUsuarios;

    @FXML
    private Label LbPublicacion;

    @FXML
    private VBox VBoxAmigosSugeridos;

    @FXML
    private VBox VBoxGrupos;

    @FXML
    private VBox contenedorPublicaciones;

    @FXML
    private TextField txtBusqueda;

    @FXML
    private Label txtInformacion;

    @FXML
    private Label txtNombre;

    @FXML
    private TextField txtPublicacion;

    @FXML
    void OnCerrarSesion(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/proyecto/redsocial/fxml/login-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Iniciar Sesión");
            stage.show();

            // Cierra la ventana actual
            Stage ventanaActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            ventanaActual.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OnGestionarusuarios(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/proyecto/redsocial/fxml/gestionarUsuarios-view.fxml"));
            Parent root = loader.load();

            GestionarUsuariosController controller = loader.getController();
            controller.cargarEstudiantes(modelFactory.getSistema());

            Stage nuevoStage = new Stage();
            nuevoStage.setScene(new Scene(root));
            nuevoStage.setTitle("Gestión de Usuarios");
            nuevoStage.show();

            Stage ventanaActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            ventanaActual.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onPublicar(MouseEvent event) {
        String texto = txtPublicacion.getText();
        if (texto != null && !texto.trim().isEmpty()) {
            Publicacion publicacion = new Publicacion();
            publicacion.setTexto(texto);
            // Solo si el moderador puede publicar, debes cambiar el tipo de autor a Usuario o similar
            // Por ahora, esto se comentaría o necesita rediseño:
            // publicacion.setAutor(moderador);

            modelFactory.getSistema().getListaPublicaciones().agregar(publicacion);
            txtPublicacion.clear();
            cargarPublicaciones();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("Campo vacío");
            alert.setContentText("Por favor, ingrese texto para publicar.");
            alert.showAndWait();
        }
    }

    public void cargarPublicaciones() {
        contenedorPublicaciones.getChildren().clear();
        List<Publicacion> publicaciones = modelFactory.getSistema().cargarPublicaciones(); // método correcto
        for (Publicacion publicacion : publicaciones) {
            cargarEnVistaPrincipal(publicacion, null);
        }
    }


    public void cargarDatosVista(Moderador moderador) {
        this.moderador = moderador;
        txtNombre.setText(moderador.getNombre());
        txtInformacion.setText(moderador.getCorreo());
        cargarPublicaciones();
    }

    private Node crearContenido(String texto) {
        VBox contenedor = new VBox(6);

        Pattern pattern = Pattern.compile("(https?://\\S+)");
        Matcher matcher = pattern.matcher(texto);

        boolean hayEnlace = false;
        int lastEnd = 0;

        while (matcher.find()) {
            hayEnlace = true;

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
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("No se pudo abrir el enlace");
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
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

        Button botonValorar = crearBotonValorar(publicacion);
        Button botonEliminar = crearBotonEliminar(publicacion, tarjeta);

        tarjeta.getChildren().addAll(botonValorar, botonEliminar);

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
        RedSocialUtils.aplicarEstiloBotonGradiente(boton);
        boton.setCursor(Cursor.HAND);
        boton.setOnAction(event -> {
            try {
                java.awt.Desktop.getDesktop().open(new java.io.File(rutaArchivo));
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No se pudo abrir el archivo");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        });
        return boton;
    }

    private Button crearBotonValorar(Publicacion publicacion) {
        Button boton = new Button("Valorar");
        boton.setStyle("-fx-background-color: linear-gradient(to right, #f9d423, #ff4e50);\n" +
                "    -fx-background-radius: 90;\n" +
                "    -fx-padding: 6 16 6 16;\n" +
                "    -fx-text-fill: #333333;\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-font-size: 13px;");
        boton.setCursor(Cursor.HAND);
        boton.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Valorar publicación");
            dialog.setHeaderText("Ingrese una valoración (1-5):");
            dialog.setContentText("Valoración:");
            dialog.showAndWait().ifPresent(valor -> {
                try {
                    int valoracion = Integer.parseInt(valor);
                    if (valoracion < 1 || valoracion > 5) {
                        mostrarAlerta("Valoración fuera de rango", "Ingrese un valor entre 1 y 5.");
                        return;
                    }
                    Valoracion nuevaValoracion = new Valoracion();
                    nuevaValoracion.setValoracion(valoracion);
                    nuevaValoracion.setPublicacion(publicacion);
                    // Puedes agregar más lógica aquí si quieres guardar quién valoró
                    if (publicacion.getValoraciones() != null) {
                        publicacion.getValoraciones().agregar(nuevaValoracion);
                    }
                    mostrarAlerta("Valoración registrada", "¡Gracias por valorar!");
                } catch (NumberFormatException e) {
                    mostrarAlerta("Valor no válido", "Ingrese un número entero entre 1 y 5.");
                }
            });
        });
        return boton;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private Button crearBotonEliminar(Publicacion publicacion, VBox tarjeta) {
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
            contenedorPublicaciones.getChildren().remove(tarjeta);
        });
        return boton;
    }
}
