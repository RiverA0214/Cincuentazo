package edu.univalle.cincuentazo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import edu.univalle.cincuentazo.view.StartStage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        new StartStage();
    }


    public static void main(String[] args) {
        launch();
    }
}
