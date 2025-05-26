package proyecto.redsocial.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import proyecto.redsocial.model.Estudiante;

public class AmigosController {

    private Estudiante estudianteActual;
    @FXML
    private ListView<Estudiante> listaAmigos;

    public void inicializarDatos(Estudiante estudianteActual, MainPageController mainPageController) {
        this.estudianteActual = estudianteActual;
        cargarAmigos();
    }

    private void cargarAmigos() {
        if (estudianteActual != null && estudianteActual.getAmigos() != null) {
            listaAmigos.getItems().setAll(estudianteActual.getAmigos().aLista());
            listaAmigos.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Estudiante amigo, boolean empty) {
                    super.updateItem(amigo, empty);
                    setText(empty || amigo == null ? null : amigo.getNombre());
                }
            });
        }
    }
}
