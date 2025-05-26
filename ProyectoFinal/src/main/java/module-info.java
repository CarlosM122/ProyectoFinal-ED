module proyecto.redsocial {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.desktop;
    requires javafx.graphics;

    opens proyecto.redsocial to javafx.graphics, javafx.fxml;
    opens proyecto.redsocial.controller to javafx.fxml;

    exports proyecto.redsocial;
    exports proyecto.redsocial.model;
    exports proyecto.redsocial.model.EstructurasPropias;
    exports proyecto.redsocial.controller;
    exports proyecto.redsocial.factory;
}
