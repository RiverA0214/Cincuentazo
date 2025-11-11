module edu.univalle.cincuentazo {
    requires javafx.controls;
    requires javafx.fxml;

    exports edu.univalle.cincuentazo;
    exports edu.univalle.cincuentazo.controller;
    exports edu.univalle.cincuentazo.model;

    opens edu.univalle.cincuentazo.controller to javafx.fxml;
}
