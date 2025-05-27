package proyecto.redsocial.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import proyecto.redsocial.model.Sistema;
import proyecto.redsocial.model.SolicitudAyuda;
import proyecto.redsocial.model.EstructurasPropias.ColaPrioridadSolicitudes;

public class GestionarSolicitudesController {
    @FXML
    private TableView<SolicitudAyuda> tablaSolicitudes;
    @FXML
    private TableColumn<SolicitudAyuda, String> colEstudiante;
    @FXML
    private TableColumn<SolicitudAyuda, String> colDescripcion;
    @FXML
    private TableColumn<SolicitudAyuda, Integer> colUrgencia;
    @FXML
    private TableColumn<SolicitudAyuda, Void> colAccion;

    private ColaPrioridadSolicitudes cola;
    private ObservableList<SolicitudAyuda> solicitudes = FXCollections.observableArrayList();

    // Inicializa la tabla de solicitudes y su comportamiento
    @FXML
    private void initialize() {
        tablaSolicitudes.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tablaSolicitudes.setRowFactory(tv -> {
            TableRow<SolicitudAyuda> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    SolicitudAyuda solicitud = row.getItem();
                    eliminarDeCola(solicitud);
                    solicitudes.remove(solicitud);
                }
            });
            return row;
        });
    }

    // Carga las solicitudes de ayuda en la tabla desde el sistema
    public void cargarSolicitudes(Sistema sistema) {
        this.cola = sistema.getColaPrioridadSolicitudes();
        solicitudes.clear();
        ColaPrioridadSolicitudes copia = new ColaPrioridadSolicitudes();
        while (!cola.estaVacia()) {
            SolicitudAyuda s = cola.desencolar();
            solicitudes.add(s);
            copia.insertar(s);
        }
        while (!copia.estaVacia()) {
            cola.insertar(copia.desencolar());
        }
        tablaSolicitudes.setItems(solicitudes);
        colEstudiante.setCellValueFactory(new PropertyValueFactory<>("nombreEstudiante"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colUrgencia.setCellValueFactory(new PropertyValueFactory<>("urgencia"));
        colAccion.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Atender");
            {
                btn.setOnAction(event -> {
                    SolicitudAyuda solicitud = getTableView().getItems().get(getIndex());
                    solicitudes.remove(solicitud);
                    eliminarDeCola(solicitud);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
    }

    // Elimina una solicitud de la cola y de la lista del estudiante
    private void eliminarDeCola(SolicitudAyuda solicitud) {
        ColaPrioridadSolicitudes nuevaCola = new ColaPrioridadSolicitudes();
        while (!cola.estaVacia()) {
            SolicitudAyuda s = cola.desencolar();
            if (!s.equals(solicitud)) {
                nuevaCola.insertar(s);
            }
        }
        while (!nuevaCola.estaVacia()) {
            cola.insertar(nuevaCola.desencolar());
        }
        if (solicitud.getEstudiante() != null && solicitud.getEstudiante().getSolicitudesAyuda() != null) {
            solicitud.getEstudiante().getSolicitudesAyuda().eliminar(solicitud);
        }
        proyecto.redsocial.factory.ModelFactory.getInstance().guardarRecursosXML();
    }

    // Cierra la ventana de gestión de solicitudes
    @FXML
    private void onCerrar(ActionEvent event) {
        Stage stage = (Stage) tablaSolicitudes.getScene().getWindow();
        stage.close();
    }
}

