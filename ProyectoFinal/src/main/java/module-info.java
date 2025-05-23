module proyecto.redsocial {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.desktop;
    requires javafx.graphics;


    opens proyecto.redsocial to javafx.fxml;
    exports proyecto.redsocial;
    exports proyecto.redsocial.model;
    opens proyecto.redsocial.factory;
    exports proyecto.redsocial.factory;

    opens proyecto.redsocial.controller to javafx.fxml;
    exports proyecto.redsocial.model.EstructurasPropias;
}