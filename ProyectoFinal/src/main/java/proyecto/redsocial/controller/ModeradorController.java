package proyecto.redsocial.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import proyecto.redsocial.factory.ModelFactory;
import proyecto.redsocial.model.Estudiante;
import proyecto.redsocial.model.Moderador;
import proyecto.redsocial.model.Publicacion;
import proyecto.redsocial.model.Sistema;
import proyecto.redsocial.utils.RedSocialUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModeradorController {

    private Estudiante estudiante;
    private ModelFactory modelFactory = ModelFactory.getInstance();
    private Moderador moderador;
    private Sistema sistema;
    private Estudiante estudianteActual;

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
    private Label txtInformacion;

    @FXML
    private Label txtNombre;

    @FXML
    private TextField txtPublicacion;

    @FXML
    private Button btnVerGrafo;
    @FXML
    private Button btnReporteValorados;
    @FXML
    private Button btnReporteConexiones;
    @FXML
    private Button btnReporteCaminos;
    @FXML
    private Button btnReporteComunidades;
    @FXML
    private Button btnReporteParticipacion;

    // Cierra la sesión del moderador y vuelve a la pantalla de login
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

    // Abre la ventana para gestionar usuarios
    @FXML
    void OnGestionarusuarios(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/proyecto/redsocial/fxml/gestionarUsuarios-view.fxml"));
            Parent root = loader.load();

            GestionarUsuariosController controller = loader.getController();
            controller.cargarEstudiantes(modelFactory.getSistema(), moderador);

            Stage nuevoStage = new Stage();
            nuevoStage.setScene(new Scene(root));
            nuevoStage.setTitle("Gestión de Usuarios");
            nuevoStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Abre la ventana para gestionar solicitudes de ayuda
    @FXML
    void OnGestionarSolicitudes(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/proyecto/redsocial/fxml/gestionarSolicitudes-view.fxml"));
            Parent root = loader.load();

            GestionarSolicitudesController controller = loader.getController();
            controller.cargarSolicitudes(modelFactory.getSistema());

            Stage nuevoStage = new Stage();
            nuevoStage.setScene(new Scene(root));
            nuevoStage.setTitle("Gestión de Solicitudes");
            nuevoStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Muestra el grafo de afinidad entre estudiantes
    @FXML
    void onVerGrafo(MouseEvent event) {
        // Visualizar el grafo de afinidad (solo ejemplo textual)
        if (sistema == null) sistema = modelFactory.getSistema();
        StringBuilder sb = new StringBuilder();
        sb.append("Grafo de afinidad (conexiones):\n");
        for (Estudiante e : sistema.getListaEstudiantes()) {
            sb.append(e.getNombre()).append(" -> ");
            for (Estudiante amigo : e.getAmigos()) {
                sb.append(amigo.getNombre()).append(", ");
            }
            sb.append("\n");
        }
        mostrarAlerta(sb.toString());
    }

    // Muestra el reporte de los contenidos más valorados
    @FXML
    void onReporteValorados(MouseEvent event) {
        // Contenidos más valorados usando ListaEnlazada propia
        if (sistema == null) sistema = modelFactory.getSistema();
        proyecto.redsocial.model.EstructurasPropias.ListaEnlazada<Publicacion> listaPropia = sistema.getListaPublicaciones();
        List<Publicacion> publicaciones = new ArrayList<>();
        for (int i = 0; i < listaPropia.size(); i++) {
            publicaciones.add(listaPropia.get(i));
        }
        publicaciones.sort((a, b) -> Integer.compare(b.getValoraciones().size(), a.getValoraciones().size()));
        StringBuilder sb = new StringBuilder();
        sb.append("Top 5 contenidos más valorados:\n");
        for (int i = 0; i < Math.min(5, publicaciones.size()); i++) {
            Publicacion p = publicaciones.get(i);
            sb.append((i + 1)).append(". ").append(p.getTexto()).append(" (Valoraciones: ").append(p.getValoraciones().size()).append(")\n");
        }
        mostrarAlerta(sb.toString());
    }

    // Muestra el reporte de los estudiantes con más conexiones
    @FXML
    void onReporteConexiones(MouseEvent event) {
        // Estudiantes con más conexiones usando ListaEnlazada propia
        if (sistema == null) sistema = modelFactory.getSistema();
        proyecto.redsocial.model.EstructurasPropias.ListaEnlazada<Estudiante> listaPropia = sistema.getListaEstudiantes();
        List<Estudiante> estudiantes = new ArrayList<>();
        for (int i = 0; i < listaPropia.size(); i++) {
            estudiantes.add(listaPropia.get(i));
        }
        estudiantes.sort((a, b) -> Integer.compare(b.getAmigos().size(), a.getAmigos().size()));
        StringBuilder sb = new StringBuilder();
        sb.append("Top 5 estudiantes con más conexiones:\n");
        for (int i = 0; i < Math.min(5, estudiantes.size()); i++) {
            Estudiante e = estudiantes.get(i);
            sb.append((i + 1)).append(". ").append(e.getNombre()).append(" (Conexiones: ").append(e.getAmigos().size()).append(")\n");
        }
        mostrarAlerta(sb.toString());
    }

    // Muestra el camino más corto entre dos estudiantes
    @FXML
    void onReporteCaminos(MouseEvent event) {
        // Caminos más cortos entre dos estudiantes usando solo estructuras propias
        if (sistema == null) sistema = modelFactory.getSistema();
        var grafo = sistema.getRedAfinidad();
        var nodos = grafo.getNodos();
        TextInputDialog dialog1 = new TextInputDialog();
        dialog1.setTitle("Camino más corto");
        dialog1.setHeaderText("Ingrese el nombre del estudiante de origen:");
        dialog1.setContentText("Nombre origen:");
        String origenNombre = dialog1.showAndWait().orElse("").trim();
        if (origenNombre.isEmpty()) return;
        TextInputDialog dialog2 = new TextInputDialog();
        dialog2.setTitle("Camino más corto");
        dialog2.setHeaderText("Ingrese el nombre del estudiante de destino:");
        dialog2.setContentText("Nombre destino:");
        String destinoNombre = dialog2.showAndWait().orElse("").trim();
        if (destinoNombre.isEmpty()) return;
        // Buscar nodos
        proyecto.redsocial.model.EstructurasPropias.NodoGrafo origen = null, destino = null;
        for (int i = 0; i < nodos.size(); i++) {
            var nodo = nodos.get(i);
            if (nodo.getEstudiante().getNombre().equalsIgnoreCase(origenNombre)) origen = nodo;
            if (nodo.getEstudiante().getNombre().equalsIgnoreCase(destinoNombre)) destino = nodo;
        }
        if (origen == null || destino == null) {
            mostrarAlerta("Uno o ambos estudiantes no existen.");
            return;
        }
        // BFS con estructuras propias
        proyecto.redsocial.model.EstructurasPropias.ListaEnlazada<proyecto.redsocial.model.EstructurasPropias.NodoGrafo> cola = new proyecto.redsocial.model.EstructurasPropias.ListaEnlazada<>();
        proyecto.redsocial.model.EstructurasPropias.ConjuntoEnlazado<proyecto.redsocial.model.Estudiante> visitados = new proyecto.redsocial.model.EstructurasPropias.ConjuntoEnlazado<>();
        proyecto.redsocial.model.EstructurasPropias.Mapa<proyecto.redsocial.model.EstructurasPropias.NodoGrafo, proyecto.redsocial.model.EstructurasPropias.NodoGrafo> predecesor = new proyecto.redsocial.model.EstructurasPropias.Mapa<>();
        cola.agregar(origen);
        visitados.agregar(origen.getEstudiante());
        boolean encontrado = false;
        while (cola.size() > 0 && !encontrado) {
            var actual = cola.get(0);
            cola.eliminar(0);
            if (actual == destino) {
                encontrado = true;
                break;
            }
            var adyacentes = actual.getAdyacentes();
            for (int i = 0; i < adyacentes.size(); i++) {
                var vecino = adyacentes.get(i);
                if (!visitados.contiene(vecino.getEstudiante())) {
                    cola.agregar(vecino);
                    visitados.agregar(vecino.getEstudiante());
                    predecesor.put(vecino, actual);
                }
            }
        }
        if (!encontrado) {
            mostrarAlerta("No existe un camino entre los estudiantes seleccionados.");
            return;
        }
        // Reconstruir camino
        proyecto.redsocial.model.EstructurasPropias.ListaEnlazada<String> camino = new proyecto.redsocial.model.EstructurasPropias.ListaEnlazada<>();
        var actual = destino;
        while (actual != null) {
            camino.insertarInicio(actual.getEstudiante().getNombre());
            actual = predecesor.get(actual);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Camino más corto: ");
        for (int i = 0; i < camino.size(); i++) {
            sb.append(camino.get(i));
            if (i < camino.size() - 1) sb.append(" -> ");
        }
        mostrarAlerta(sb.toString());
    }

    // Muestra las comunidades de estudio detectadas
    @FXML
    void onReporteComunidades(MouseEvent event) {
        // Detección de comunidades de estudio (clústeres) usando solo estructuras propias
        if (sistema == null) sistema = modelFactory.getSistema();
        var grafo = sistema.getRedAfinidad();
        var nodos = grafo.getNodos(); // ListaEnlazada propia
        proyecto.redsocial.model.EstructurasPropias.ListaEnlazada<proyecto.redsocial.model.EstructurasPropias.ListaEnlazada<String>> comunidades = new proyecto.redsocial.model.EstructurasPropias.ListaEnlazada<>();
        proyecto.redsocial.model.EstructurasPropias.ConjuntoEnlazado<proyecto.redsocial.model.Estudiante> visitados = new proyecto.redsocial.model.EstructurasPropias.ConjuntoEnlazado<>();

        for (int i = 0; i < nodos.size(); i++) {
            var nodo = nodos.get(i);
            var estudiante = nodo.getEstudiante();
            if (!visitados.contiene(estudiante)) {
                var comunidad = new proyecto.redsocial.model.EstructurasPropias.ListaEnlazada<String>();
                dfsComunidadesPropio(nodo, visitados, comunidad);
                if (comunidad.size() > 0) comunidades.agregar(comunidad);
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Comunidades de estudio detectadas:\n");
        for (int i = 0; i < comunidades.size(); i++) {
            sb.append("Comunidad ").append(i + 1).append(": ");
            var comunidad = comunidades.get(i);
            for (int j = 0; j < comunidad.size(); j++) {
                sb.append(comunidad.get(j));
                if (j < comunidad.size() - 1) sb.append(", ");
            }
            sb.append("\n");
        }
        mostrarAlerta(sb.toString());
    }

    // Algoritmo DFS para encontrar comunidades de estudio
    private void dfsComunidadesPropio(proyecto.redsocial.model.EstructurasPropias.NodoGrafo nodo, proyecto.redsocial.model.EstructurasPropias.ConjuntoEnlazado<proyecto.redsocial.model.Estudiante> visitados, proyecto.redsocial.model.EstructurasPropias.ListaEnlazada<String> comunidad) {
        var estudiante = nodo.getEstudiante();
        visitados.agregar(estudiante);
        comunidad.agregar(estudiante.getNombre());
        var adyacentes = nodo.getAdyacentes();
        for (int i = 0; i < adyacentes.size(); i++) {
            var ady = adyacentes.get(i);
            var estAdy = ady.getEstudiante();
            if (!visitados.contiene(estAdy)) {
                dfsComunidadesPropio(ady, visitados, comunidad);
            }
        }
    }

    // Muestra el reporte de participación de los estudiantes
    @FXML
    void onReporteParticipacion(MouseEvent event) {
        // Niveles de participación usando ListaEnlazada propia
        if (sistema == null) sistema = modelFactory.getSistema();
        proyecto.redsocial.model.EstructurasPropias.ListaEnlazada<Estudiante> listaPropia = sistema.getListaEstudiantes();
        List<Estudiante> estudiantes = new ArrayList<>();
        for (int i = 0; i < listaPropia.size(); i++) {
            estudiantes.add(listaPropia.get(i));
        }
        estudiantes.sort((a, b) -> Integer.compare(b.getContenidosPublicados().size(), a.getContenidosPublicados().size()));
        StringBuilder sb = new StringBuilder();
        sb.append("Top 5 estudiantes por participación (publicaciones):\n");
        for (int i = 0; i < Math.min(5, estudiantes.size()); i++) {
            Estudiante e = estudiantes.get(i);
            sb.append((i + 1)).append(". ").append(e.getNombre()).append(" (Publicaciones: ").append(e.getContenidosPublicados().size()).append(")\n");
        }
        mostrarAlerta(sb.toString());
    }

    /**
     * Carga los datos de la vista para el moderador, igual que al iniciar sesión.
     */
    public void cargarDatosVista(Moderador moderador) {
        this.moderador = moderador;
        contenedorPublicaciones.getChildren().clear();
        if (sistema == null) {
            sistema = modelFactory.getSistema();
        }
        txtNombre.setText(moderador.getNombre());
        txtInformacion.setText("Bienvenido, " + moderador.getNombre());
        cargarPublicaciones();
    }

    // Carga todas las publicaciones en la vista del moderador
    protected void cargarPublicaciones() {
        List<Publicacion> publicaciones = modelFactory.obtenerPublicaciones();
        for (Publicacion publicacion : publicaciones) {
            cargarEnVistaModerador(publicacion, this.estudianteActual);
        }
    }

    // Carga una publicación en la vista del moderador
    public void cargarEnVistaModerador(Publicacion publicacion, Estudiante usuarioActual) {
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

        Button botonEliminar = crearBotonEliminar(publicacion);
        tarjeta.getChildren().add(botonEliminar);
        // No mostrar valoración para moderador
        // VBox botonValorar = crearValoracionInteractiva(publicacion);
        // tarjeta.getChildren().add(botonValorar);

        contenedorPublicaciones.getChildren().addFirst(tarjeta);
    }

    // Crea la tarjeta visual para una publicación
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

    // Crea el botón para eliminar una publicación
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

    // Crea el botón para abrir un archivo adjunto
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

    // Obtiene la extensión de un archivo
    private String obtenerExtensionArchivo(String ruta) {
        int lastIndex = ruta.lastIndexOf(".");
        if (lastIndex == -1) return "";
        return ruta.substring(lastIndex + 1).toLowerCase();
    }

    // Verifica si una publicación tiene archivo adjunto
    private boolean tieneArchivoAdjunto(Publicacion publicacion) {
        String ruta = publicacion.getRutaArchivoAdjunto();
        return ruta != null && !ruta.isEmpty();
    }

    // Crea la etiqueta con la información de la publicación
    private Label crearLabelInfo(Publicacion publicacion) {
        String texto = "Publicado por " + publicacion.getAutor().getNombre() +
                " | 📅 " + publicacion.getFechaPublicacion();
        Label info = new Label(texto);
        info.setStyle("-fx-font-size: 11px; -fx-text-fill: #888888;");
        return info;
    }

    // Crea el contenido visual de una publicación, detectando enlaces
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

    // Crea la etiqueta del tema de la publicación
    private Label crearLabelTema(String temaTexto) {
        Label tema = new Label(temaTexto);
        tema.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2a2a2a;");
        return tema;
    }

    // Muestra una alerta con un mensaje
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

