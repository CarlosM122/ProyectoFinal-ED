package proyecto.redsocial.controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import proyecto.redsocial.model.Moderador;

public class ModeradorController {

    @FXML
    private Text lblBienvenida;

    private Moderador moderador;

    public void setModerador(Moderador moderador) {
        this.moderador = moderador;
        lblBienvenida.setText("Bienvenido, " + moderador.getNombre());
    }

    @FXML
    private void gestionarUsuarios() {
        // Lógica para gestionar usuarios
        System.out.println("Funcionalidad en desarrollo: gestionar usuarios");
    }

    @FXML
    private void revisarPublicaciones() {
        // Lógica para revisar publicaciones
        System.out.println("Funcionalidad en desarrollo: revisar publicaciones");
    }

    @FXML
    private void cerrarSesion() {
        // Lógica para cerrar sesión
        // Puedes cerrar la ventana y volver al login si deseas
        System.out.println("Sesión cerrada.");
    }
}
