module proyecto.redsocial {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;


    opens proyecto.redsocial to javafx.fxml;
    exports proyecto.redsocial;
}