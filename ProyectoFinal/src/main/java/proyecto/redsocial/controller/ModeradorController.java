package proyecto.redsocial.controller;

import javafx.event.ActionEvent;
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
import proyecto.redsocial.RedSocialApplication;
import proyecto.redsocial.factory.ModelFactory;
import proyecto.redsocial.model.Estudiante;
import proyecto.redsocial.model.Moderador;
import proyecto.redsocial.model.Publicacion;
import proyecto.redsocial.model.Sistema;
import proyecto.redsocial.model.Valoracion;
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
            controller.cargarEstudiantes(modelFactory.getSistema(), moderador);

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
    void onVerGrafo(MouseEvent event) {
        // Visualizar el grafo de afinidad (solo ejemplo textual)
        if (sistema == null) sistema = modelFactory.getSistema();
        StringBuilder sb = new StringBuilder();
        sb.append("Grafo de afinidad (conexiones):\n");
        // Suponiendo que tienes un método para obtener todos los estudiantes y sus amigos
        for (Estudiante e : sistema.getListaEstudiantes()) {
            sb.append(e.getNombre()).append(" -> ");
            for (Estudiante amigo : e.getAmigos()) {
                sb.append(amigo.getNombre()).append(", ");
            }
            sb.append("\n");
        }
        mostrarAlerta(sb.toString());
    }

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
            sb.append((i+1)).append(". ").append(p.getTexto()).append(" (Valoraciones: ").append(p.getValoraciones().size()).append(")\n");
        }
        mostrarAlerta(sb.toString());
    }

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
            sb.append((i+1)).append(". ").append(e.getNombre()).append(" (Conexiones: ").append(e.getAmigos().size()).append(")\n");
        }
        mostrarAlerta(sb.toString());
    }

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
            sb.append("Comunidad ").append(i+1).append(": ");
            var comunidad = comunidades.get(i);
            for (int j = 0; j < comunidad.size(); j++) {
                sb.append(comunidad.get(j));
                if (j < comunidad.size() - 1) sb.append(", ");
            }
            sb.append("\n");
        }
        mostrarAlerta(sb.toString());
    }

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
            sb.append((i+1)).append(". ").append(e.getNombre()).append(" (Publicaciones: ").append(e.getContenidosPublicados().size()).append(")\n");
        }
        mostrarAlerta(sb.toString());
    }

    @FXML
    private void onPublicar(ActionEvent event) {
        // Lógica del botón publicar
    }

    /**
     * Carga las publicaciones del sistema y las muestra en la vista del moderador.
     */
    public void cargarPublicaciones() {
        if (sistema == null) {
            sistema = modelFactory.getSistema();
        }
        if (contenedorPublicaciones != null) {
            contenedorPublicaciones.getChildren().clear();
            var publicaciones = sistema.getListaPublicaciones();
            for (int i = 0; i < publicaciones.size(); i++) {
                Publicacion pub = publicaciones.get(i);
                Label label = new Label(pub.getTexto());
                contenedorPublicaciones.getChildren().add(label);
            }
        }
    }

    /**
     * Carga los datos de la vista para el moderador, igual que al iniciar sesión.
     */
    public void cargarDatosVista(Moderador moderador) {
        this.moderador = moderador;
        if (sistema == null) {
            sistema = modelFactory.getSistema();
        }
        // Cargar nombre e información del moderador
        if (moderador != null) {
            txtNombre.setText(moderador.getNombre());
            txtInformacion.setText("Bienvenido, " + moderador.getNombre());
        }
        // Aquí puedes cargar otras listas o datos que se muestran al moderador
        // Por ejemplo, publicaciones, grupos, sugerencias, etc.
        // Si tienes métodos para cargar publicaciones, puedes llamarlos aquí
        // Ejemplo:
        // cargarPublicaciones();
        // cargarGrupos();
        // cargarAmigosSugeridos();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
