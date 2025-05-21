package proyecto.redsocial.controller;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import proyecto.redsocial.factory.ModelFactory;
import proyecto.redsocial.model.Estudiante;
import proyecto.redsocial.model.GrupoEstudio;

public class GrupoEstudioController {

    private final ModelFactory modelFactory = ModelFactory.getInstance();
    private MainPageController mainPageController;

    @FXML
    private TextArea txtAreaChat;

    @FXML
    private TextField txtMensaje;

    @FXML
    private Button btnEnviar;

    @FXML
    private ListView<String> listaMiembros;

    private GrupoEstudio grupoEstudio;

    public void inicializar(GrupoEstudio grupo, MainPageController mainPageController) {
        this.mainPageController = mainPageController;
        this.grupoEstudio = grupo;
        cargarMiembros();
    }

    private void cargarMiembros() {
        listaMiembros.getItems().clear();
        for (Estudiante e : grupoEstudio.getMiembros()) {
            listaMiembros.getItems().add(e.getNombre());
        }
    }

    @FXML
    private void enviarMensaje() {
        String mensaje = txtMensaje.getText().trim();
        if (!mensaje.isEmpty()) {
            txtAreaChat.appendText("Tú: " + mensaje + "\n");
            txtMensaje.clear();
        }
    }
}
